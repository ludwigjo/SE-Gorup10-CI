package com.group10.CI;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;

/**
 * GitHandler class responsible for cloning and checking out a repository
 * The jgit library by eclipse is used to clone and check out a repository
 */
public class GitHandler {

    private String repoPath;

    //Main method to test the GitHandler class
    public static void main(String[] args) {
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "main");
    }

    //Constructor taking the url of the repository as well as the branch name to check out
    public GitHandler(String url, String branch) {
        this.repoPath = "temp/" + url.substring(url.lastIndexOf('/') + 1);
        cloneRepo(url, branch);
        
    }

    /**
     * Method to clone the repository
     * @param url the url of the repository
     */
    public void cloneRepo(String url, String branch) {
        try {
            //Create file path for the temp repository
            Git.cloneRepository().setURI(url).setDirectory(new File(repoPath))
            .setBranchesToClone(Arrays.asList("refs/heads/"+branch))
            .setBranch("refs/heads/"+branch)
            .call();
            System.out.println("Repository cloned");
        } catch (Exception e) {
            e.printStackTrace();
         }
    }

    /**
     * Get method for the repoPath
     * @return the repoPath
     */
    public String getRepoPath() {
        return repoPath;
    }

}
