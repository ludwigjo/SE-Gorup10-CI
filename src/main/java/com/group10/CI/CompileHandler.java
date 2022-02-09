package com.group10.CI;

import java.io.*;

/**
 * Class responsible for compiling a commit.
 * */
public class CompileHandler {

    private final String[] COMPILE_COMMAND = new String[]{"mvn", "compile"};
    private String commitHash;
    private Status status;
    private String compilationInformation;
    private String repoPath;

    /**
     * Default constructor.
     * @param commitHash    the hash of the commit that is being compiled.
     * @param repoPath      the path to the cloned repo with the commit to be compiled.
     * */
    public CompileHandler(String commitHash, String repoPath){
        this.commitHash = commitHash;
        this.repoPath = repoPath;
    }

    /**
     * Empty constructor
     * */
    public  CompileHandler() { }

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
    public void compile() throws IOException, InterruptedException {
        setStatus(Status.PENDING);

        File directory = new File(this.repoPath);
        ProcessBuilder processBuilder = new ProcessBuilder(COMPILE_COMMAND);
        processBuilder.directory(directory);

        Process process;
        try {
            System.out.println("Compilation started ...");
            process = processBuilder.start();
            process.waitFor();

            if (process.exitValue() == 0) setStatus(Status.SUCCESS);
            else setStatus(Status.FAILURE);

            setCompilationInformation(convertInputStreamToString(process.getInputStream()));
        } catch (IOException | InterruptedException e) {
            System.out.println("\n ---- ERROR ---- \n" + e.getMessage());
            setStatus(Status.ERROR);
            setCompilationInformation(null);
        }
        System.out.println("Compilation finished with status: " + getStatus());
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
    private String convertInputStreamToString(InputStream is) throws IOException {
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
     * Sets the status
     * @param s     the status
     * */
    private void setStatus(Status s){
        this.status = s;
    }

    /**
     * Sets the information about the compilation
     * @param s     the information
     * */
    private void setCompilationInformation(String s)  {
        this.compilationInformation = s;
    }

    /**
     * Gets the status of the compilation
     * */
    public Status getStatus(){
        return this.status;
    }

    /**
     * Gets the information of the compilation
     * */
    public String getCompilationInformation(){
        return this.compilationInformation;
    }

    /**
     * Gets the hash of the commit that is compiled
     * */
    public String getCommitHash(){
        return this.commitHash;
    }
}
