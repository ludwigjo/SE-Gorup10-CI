package com.group10.CI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import netscape.javascript.JSObject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.*;
/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        response.getWriter().println("CI job in progress...");

        // if server receives a webhook
        if (request.getMethod() == "POST") {
            try {
                Build build = handlePostRequest(request);
                if(build.equals(null)) return;
                String html = "<html><title></title><body>"
                        + "<h1> Commit sha: " + build.getPrId() + "<h1>"
                        + "<p> Build status: " + build.getBuildStatus() + "<p>"
                        + "<p> Test status: " + build.getTestStatus() + "<p>"
                        + "</body></html>";
                response.getWriter().println("CI job done");
                response.getWriter().write(html);
                response.getWriter().flush();
            } catch (InterruptedException e) {
                System.out.println("Error when handling the post request: " + e.getMessage());
                return;
            }
        }


    }

    private Build handlePostRequest(HttpServletRequest request) throws IOException, InterruptedException {
        JSONObject body = getBody(request);
        if(body.equals(null)) return null;
        System.out.println("JSON BODY:\n" + body);

        String[] ref = body.getString("ref").split("/");
        // since split is on / we want the last two parts
        String branch = String.join("", Arrays.copyOfRange(ref, (ref.length - 2), (ref.length - 1)));
        String repoUrl = body.getJSONObject("repository").getString("url");
        String commitSha = body.getString("after");

        // instantiate new build object and notification handler
        Build build = new Build(commitSha, "", Status.PENDING, Status.PENDING, repoUrl);
        NotificationHandler notifier = new NotificationHandler();

        // clone
        System.out.println("Cloning branch: " + branch + " from url: " + repoUrl);
        GitHandler git = new GitHandler();
        boolean hasCloned = git.cloneRepo(repoUrl, branch);
        if(!hasCloned) return null;  //unable to clone

        // compile
        System.out.println("Compiling project.");
        CompileHandler compileHandler = new CompileHandler(commitSha, repoUrl);
        compileHandler.compile();

        // if unable to execute compilation command, we do not want to send a notification
        if(compileHandler.getStatus() == Status.ERROR) return null;

        build.setBuildStatus(compileHandler.getStatus());

        // test
        System.out.println("Testing project.");
        TestHandler testHandler = new TestHandler(commitSha, repoUrl);
        testHandler.test();

        // if unable to execute test command, we do not want to send a notification
        if(testHandler.getStatus() == Status.ERROR) return null;

        build.setTestStatus(testHandler.getStatus());
        System.out.println("Build and testing complete.");

        notifier.notifyGitHub(build);

        if(!notifier.getSuccessfulDelivery()) System.out.println("Unable to notify Github.");
        return build;
    }

    private JSONObject getBody(HttpServletRequest request){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader br = request.getReader();
            String line;
            while ((line = br.readLine()) != null) stringBuilder.append(line);
            return new JSONObject(stringBuilder);
        } catch (IOException e) {
            System.out.println("ERROR");
            System.out.println("Unable to get body from request: " + e.getMessage());
            System.out.println("Returning...");
            return null;
        }
    }

    // used to start the CI server in command line
    // takes 0 or 1 arguments.
    // usage: java ContnuousIntegrationServer.java [PORT_NUMBER]
    // mvn usage: mvn exec:java -D"exec.mainClass"="com.group10.CI.ContinuousIntegrationServer" -Dexec.args="<PORT_NUMBER>"
    public static void main(String[] args) throws Exception
    {
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