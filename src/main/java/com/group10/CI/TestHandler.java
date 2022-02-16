package com.group10.CI;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for testing a commit.
 * */
public class TestHandler extends BuildHandler {
    /**
     * Inherit from super class:
     * protected String commitHash;
     * protected Status status;
     * protected String information;
     * protected String repoPath;
     */
    private final String[] TEST_COMMAND = new String[]{"mvn", "test"};
    

    /**
     * Default constructor.
     * @param commitHash    the hash of the commit that is being compiled.
     * @param repoPath      the path to the cloned repo with the commit to be compiled.
     * */
    public TestHandler(String commitHash, String repoPath){
        super(commitHash, repoPath);
    }

    /**
     * Method for testing the commit.
     * Runs mvn test on the commit,
     *      if the commit tests without errors the method sets the status to SUCCESS,
     *      if the test failed the status is set to FAILURE,
     *      if the test program aborted for some reason, the status is set to ERROR.
     * If the testing is a success or failure, the compilationInformation is set,
     * if an error occurred it is null. The status can be retrieved with the getStatus() method
     * and the compilationInformation can be retrieved with getCompilationInformation().
     * */
    public void test() {
        try {
            runCommand(TEST_COMMAND);
        } catch (IOException | InterruptedException e) {
            System.out.println("--- ERROR ---\n" + e.getMessage());
        }
    }
}
