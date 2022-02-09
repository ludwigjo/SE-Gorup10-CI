package com.group10.CI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

/**
 * Handling the notiflication sending notiflications to github and Email
 */
public class NotificationHandler {

    //Constructor creating a notilfication
    public NotificationHandler(String status, String SHA){

    }

    /**
     * Sending notifications on status to GitHub using the post API
     * @param status
     */
    public void notifyGitHub(String status, String SHA){
        String postURL = "https://api.github.com/repos/SE-Gorup10-CI/statuses/" + SHA;
        String postBody = "{\"state\":\"" + status + "\"}";

        //Sending the post request
        try{
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(postURL);
            StringEntity entity = new StringEntity(postBody);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
    
            //Sending the request
            HttpResponse response = httpclient.execute(httpPost);
        }
        catch(Exception e){
            System.err.println("Failure in sending the post request" + e);
            e.printStackTrace();
        }

    }

    /**
     * Sending notifications on status to Email using the sendEmail API
     * @param status
     */
    public void notifyEmail(String status){
    }

    
}
