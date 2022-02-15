package com.group10.CI;

import java.io.*;
import java.util.Arrays;

public class BuildHandler {
    protected String commitHash;
    protected Status status;
    protected String information;
    protected String repoPath;
    protected String jointOutput;

    /**
     * Default constructor.
     * @param commitHash    the hash of the commit that is being compiled.
     * @param repoPath      the path to the cloned repo with the commit to be compiled.
     * */
    public BuildHandler(String commitHash, String repoPath){
        this.commitHash = commitHash;
        this.repoPath = repoPath;
        this.jointOutput = "";
    }
    /**
     * Empty constructor
     * */
    public BuildHandler() { }

    /**
     * Method for compiling the commit.
     * Runs mvn compile on the commit,
     *      if the commit compiles without errors the method sets the status to SUCCESS,
     *      if the compilation failed the status is set to FAILURE,
     *      if the method is unable to complete the compilation for some reason, the status is set to ERROR.
     * If the compilation is a success or failure, the compilationInformation is set,
     * if an error occurred it is null. The status can be retrieved with the getStatus() method
     * and the compilationInformation can be retrieved with getCompilationInformation().
     * */
    public void runCommand(String[] command) throws IOException, InterruptedException {
        setStatus(Status.PENDING);

        File directory = new File(this.repoPath);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(directory);

        Process process;
        try {
            process = processBuilder.start();
            System.out.println("Running command: " + Arrays.toString(command));

            joinOutput(convertInputStreamToString(process.getInputStream()));

            process.waitFor();

            if (process.exitValue() == 0) setStatus(Status.SUCCESS);
            else setStatus(Status.FAILURE);

            process.destroy();

            setInformation(this.jointOutput);
        } catch (IOException | InterruptedException e) {
            System.out.println("\n ---- ERROR ---- \n" + e.getMessage());
            setStatus(Status.ERROR);
            setInformation(null);
        }
    }


    /**
     * Utility method.
     * Converts an InputStream to a String. Each line in the InputStream is
     * on a new line in the String. Sets the status to ERROR if unable to
     * convert the InputStream to a String.
     *
     * @param is    the InputStream
     * @return      a String of the converted InputStream
     * */
    protected String convertInputStreamToString(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);    // remove last /n
        } catch (IOException e) {
            System.out.println("\n ---- ERROR ---- \n" + e.getMessage());
            setStatus(Status.ERROR);
            return null;
        }

        return stringBuilder.toString();
    }

    /**
     * Utility method.
     * Joins a string to a string.
     * @param s     the string to add
     * */
    protected void joinOutput(String s){
        this.jointOutput += s;
    }

    /**
     * Sets the status
     * @param s     the status
     * */
    protected void setStatus(Status s){
        this.status = s;
    }

    /**
     * Sets the information about the build
     * @param s     the information
     * */
    protected void setInformation(String s)  {
        this.information = s;
    }

    /**
     * Gets the status of the build
     * */
    public Status getStatus(){
        return this.status;
    }

    /**
     * Gets the information of the build
     * */
    public String getInformation(){
        return this.information;
    }

    /**
     * Gets the hash of the commit that is being built
     * */
    public String getCommitHash(){
        return this.commitHash;
    }
}
