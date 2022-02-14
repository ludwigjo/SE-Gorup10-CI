package com.group10.CI;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for testing a commit.
 * */
public class TestHandler extends CompileHandler{
    /**
     *inherent from super class:
     * protected String commitHash;
     * protected Status status;
     * protected String compilationInformation;
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
    public void test() throws IOException, InterruptedException {
        setStatus(Status.PENDING);

        File directory = new File(this.repoPath);
        ProcessBuilder processBuilder = new ProcessBuilder(TEST_COMMAND);
        processBuilder.directory(directory);

        Process process;
        boolean failureFound;
        try {
            process = processBuilder.start();
            //System.out.println("Compilation started ...");

            joinOutput(convertInputStreamToString(process.getInputStream()));

            process.waitFor();

            if(process.exitValue() == 0) setStatus(Status.SUCCESS);
            else setStatus(Status.FAILURE);

            process.destroy();

            setCompilationInformation(this.jointOutput);
        } catch (IOException | InterruptedException e) {
            System.out.println("\n ---- ERROR ---- \n" + e.getMessage());
            setStatus(Status.ERROR);
            setCompilationInformation(null);
        }
        //System.out.println("Testing finished with status: " + getStatus());
    }
}
