package com.group10.CI;

import java.util.Base64;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Handling the notiflication sending notiflications to github and Email
 */
public class NotificationHandler {
    private Boolean successfulDelivery = false;
    private String gitUser = "";
    private String gitToken = "";

    //Constructor creating a notilfication
    public NotificationHandler(){
        Dotenv dotenv = Dotenv.configure().directory(".").ignoreIfMissing().load();
        this.gitUser = dotenv.get("GITHUB_USER");
        this.gitToken = dotenv.get("GITHUB_TOKEN");
        Map<String, String> env = System.getenv();
        if(this.gitToken.length() < 5) {
            System.out.println("Github credentials not found on file, trying with system variables");
            this.gitUser = env.get("GITHUB_USER");
            this.gitUser = env.get("GITHUB_TOKEN");
            if (this.gitUser.length() == 0 || this.gitToken.length() == 0) {
                System.out.println("Github credentials user not found as system environment variables");
            }
        }

        //notifyGitHub(build);
    }

    /**
     * Sending notifications on status to GitHub using the post API
     * @param
     */
    public void notifyGitHub(Build build){
        String repo = build.getRepo(); // Ex. ludwigjo/SE-Gorup10-CI
        //The name witout the user before /
        String repoName = repo.substring(repo.indexOf("/") + 1);
        String prId = build.getPrId();
        String status = build.getBuildStatus().toString().toLowerCase(); //Not sure if we only should send the general status.
        String postURL = "https://api.github.com/repos/" + repo  + "/statuses/" + prId;
        String postBody;
        if(status == "pending"){
            postBody = "{\"state\":\"" + status + "\",\"description\":\"Build status pending\",\"context\":\"Custom CI Server \"}";
        }
        else {
            postBody = "{\"state\":\"" + status + "\",\"target_url\":\"http://3b82-18-222-141-151.ngrok.io/history/" + repoName + "/" + prId + "\",\"description\":\"Build status " + status + " \",\"context\":\"Custom CI Server\"}";
        }

        //System.out.println("REPO: " + repo + " PR ID: " + prId + " POST URL: "+ postURL + " POST BODY: " + postBody);
        //Sending the post request
        try{
            String authParams = gitUser + ":" + gitToken;
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(postURL);
            StringEntity entity = new StringEntity(postBody);
            httpPost.setEntity(entity);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(authParams.getBytes()));
            //Sending the request
            HttpResponse response = httpclient.execute(httpPost);
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 201){
                System.out.println("Successfully sent the notification to GitHub");
                this.successfulDelivery = true;
            }
            else{
                System.out.println("Failed to send the notification to GitHub");
                this.successfulDelivery = false;
            }
        }
        catch(Exception e){
            System.err.println("Failure in sending the post request" + e);
            this.successfulDelivery = false;
            e.printStackTrace();
        }

    }

    /**
     * Get method for the successfulDelivery
     * @return successfulDelivery
     */
    public Boolean getSuccessfulDelivery() {
        return successfulDelivery;
    }

    /**
     * Sending notifications on status to Email using the sendEmail API
     * @param status
     */
    public void notifyEmail(String status){
    }

    
}
