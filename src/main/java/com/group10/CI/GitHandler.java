package com.group10.CI;

import java.io.File;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;

/**
 * GitHandler class responsible for cloning and checking out a repository
 * The jgit library by eclipse is used to clone and check out a repository
 */
public class GitHandler {

    private String repoPath;

    // Main method to test the GitHandler class
    public static void main(String[] args) {
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "main");
        //deleteClonedRepo(new File("temp"));
    }

    // Default constructor
    public GitHandler() {
    }

    // Constructor taking the url of the repository as well as the branch name to
    // check out
    public GitHandler(String url, String branch) {
        this.repoPath = "temp/" + url.substring(url.lastIndexOf('/') + 1);
        Boolean res = cloneRepo(url, branch);
        System.out.println(res);

    }

    /**
     * Method to clone the repository
     * 
     * @param url the url of the repository
     */
    public Boolean cloneRepo(String url, String branch) {
        try {
            // Create file path for the temp repository
            Git.cloneRepository().setURI(url).setDirectory(new File(repoPath))
                    .setBranchesToClone(Arrays.asList("refs/heads/" + branch))
                    .setBranch("refs/heads/" + branch)
                    .call();
            System.out.println("Repository cloned");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get method for the repoPath
     * 
     * @return the repoPath
     */
    public String getRepoPath() {
        return repoPath;
    }

    /**
     * Set method for the repoPath. Used for testing
     * 
     * @param repoPath the repoPath to set
     */
    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    /**
     * Method to delete the cloned repository recursively
     * 
     * @param directory the directory
     */
    public static Boolean deleteClonedRepo(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteClonedRepo(file);
            }
        }
        return directory.delete();
    }

}
