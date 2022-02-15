package com.group10.CI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Arrays;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.*;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setContentType("text/plain;charset=UTF-8");

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        PrintWriter out = response.getWriter();

        System.out.println(target);

        // out.println("Job starting.");
        Build build;
        // if server receives a webhook
        if (request.getMethod() == "POST") {
            // all "out.println" in this if-statement will not print since this handles
            // the POST request.
            // The "out.println" must be when handling the GET request
            System.out.println("### POST REQUEST FROM WEBHOOK RECEIVED ###");
            try {

                build = handlePostRequest(request);
                if (build.equals(null))
                    return;

                /* TODO: Add information to history object */
                /*
                 * String html = "Commit sha: " + build.getPrId() + " | Build status: " +
                 * build.getBuildStatus() + " | Test status: " + build.getTestStatus();
                 * System.out.println("HTML: " + html);
                 * out.println(html);
                 */

            } catch (InterruptedException e) {
                System.out.println("Error when handling the post request: " + e.getMessage());
                return;
            }
        } else if (request.getMethod() == "GET") {
            String resp = handleGetRequest(request, target);

            out.write(resp);
        }
        // remember to flush after when finished writing!
        out.flush();
    }

    private String handleGetRequest(HttpServletRequest request, String target) {
        // Trim following and preceding slashes of the target uri
        target = target.replaceFirst("^/", "").replace("/$", "");

        String[] targetParts = target.split("/");
        // Match which function we want to reach
        if (target.equals("history")) {
            // list items in history directory to a html document
            File historyDir = new File("history");
            return HtmlFormater.formatHtmlHrefList("All repos", historyDir.list(), "history/");
        } else if (target.matches("history/[A-Za-z0-9_.-]+")) {
            // list items in repo history directory to a html document
            File dir = new File(target);
            return HtmlFormater.formatHtmlHrefList("Build history for " + targetParts[1], dir.list(),
                    targetParts[1] + "/");
        } else if (target.matches("history/[A-Za-z0-9_.-]+/[A-Za-z0-9]+")) {
            // fetch items from file and place in a html document, if they exist
            HistoryHandler hh = new HistoryHandler(targetParts[1]);
            try {
                return HtmlFormater.formatHtml("Build " + targetParts[2], hh.getHistory(targetParts[2]));
            } catch (FileNotFoundException e) {
                // TODO: handle exception
                return HtmlFormater.formatHtml("No history found", "There is no history with this ID in this repo");
            }
        } else {
            // if nothing is matched it doesn't exist
            return HtmlFormater.formatHtml("404 Not Found", "No content here");
        }
    }

    private Build handlePostRequest(HttpServletRequest request) throws IOException, InterruptedException {
        JSONObject body = getBody(request);
        if (body.equals(new JSONObject("{}")))
            return null;

        // Fetch the relevant info from POST request
        String branch = body.getJSONObject("pull_request").getJSONObject("head").getString("ref");
        String repoUrl = body.getJSONObject("repository").getString("html_url");
        String commitSha = body.getJSONObject("pull_request").getJSONObject("head").getString("sha");
        String gitUrl = body.getJSONObject("pull_request").getJSONObject("head").getJSONObject("repo")
                .getString("full_name");

        System.out.println("Handle post request: \nCommit Sha: " + commitSha + " | Branch: " + branch + " | Git url: "
                + gitUrl + " | Temp dir: " + repoUrl);

        // clone
        // System.out.println("Cloning branch " + branch + " from url " + gitUrl);
        GitHandler git = new GitHandler();
        boolean hasCloned = git.cloneRepo(repoUrl, branch);
        if (!hasCloned)
            return null; // unable to clone

        // instantiate new build object and notification handler
        Build build = new Build(commitSha, "", Status.PENDING, Status.PENDING, gitUrl);
        NotificationHandler notifier = new NotificationHandler();

        // compile
        System.out.println("Compiling project.");
        CompileHandler compileHandler = new CompileHandler(commitSha, git.getRepoPath());
        compileHandler.compile();

        // if unable to execute compilation command, we do not want to send a
        // notification
        if (compileHandler.getStatus() == Status.ERROR)
            return null;

        build.setBuildStatus(compileHandler.getStatus());

        // test
        System.out.println("Testing project.");
        TestHandler testHandler = new TestHandler(commitSha, git.getRepoPath());
        testHandler.test();

        // remove cloned repo
        git.deleteClonedRepo(new File("temp"));

        // if unable to execute test command, we do not want to send a notification
        if (testHandler.getStatus() == Status.ERROR)
            return null;

        build.setTestStatus(testHandler.getStatus());
        System.out.println("Build and testing complete.");

        notifier.notifyGitHub(build);

        if (!notifier.getSuccessfulDelivery())
            System.out.println("Unable to notify Github.");
        return build;
    }

    private JSONObject getBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader br = request.getReader();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return new JSONObject(stringBuilder.toString());
        } catch (IOException e) {
            System.out.println("ERROR");
            System.out.println("Unable to get body from request: " + e.getMessage());
            System.out.println("Returning...");
            return new JSONObject("{}");
        }
    }

    // used to start the CI server in command line
    // takes 0 or 1 arguments.
    // usage: java ContnuousIntegrationServer.java [PORT_NUMBER]
    // mvn usage: mvn exec:java
    // -D"exec.mainClass"="com.group10.CI.ContinuousIntegrationServer"
    // -Dexec.args="<PORT_NUMBER>"
    public static void main(String[] args) throws Exception {
        int default_port_number = 8080;
        int port_number;

        if (args.length == 0) {
            port_number = default_port_number;
        } else if (args.length == 1) {
            try {
                port_number = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Given port number couldn't be parsed into an integer.");
                System.out.println("Returning...");
                return;
            }
        } else {
            System.out.println("Too many args provide 0 or 1 integer argument");
            System.out.println("Usage: java Cont...Server.java [PORT_NUMBER]");
            System.out.println("Returning...");
            return;
        }

        Server server = new Server(port_number);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
