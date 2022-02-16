package com.group10.CI;

import java.io.*;

/**
 * Class responsible for compiling a commit.
 * */
public class CompileHandler extends BuildHandler {

    /**
     * Inherit from super class:
     * protected String commitHash;
     * protected Status status;
     * protected String information;
     * protected String repoPath;
     */
    private final String[] COMPILE_COMMAND = new String[]{"mvn", "compile"};

    /**
     * Default constructor.
     *
     * @param commitHash the hash of the commit that is being compiled.
     * @param repoPath   the path to the cloned repo with the commit to be compiled.
     */
    public CompileHandler(String commitHash, String repoPath) {
        super(commitHash, repoPath);
    }

    /**
     * Empty constructor
     */
    public CompileHandler() {
        super();
    }

    /**
     * Method for compiling the commit.
     * Runs mvn compile on the commit,
     * if the commit compiles without errors the method sets the status to SUCCESS,
     * if the compilation failed the status is set to FAILURE,
     * if the method is unable to complete the compilation for some reason, the status is set to ERROR.
     * If the compilation is a success or failure, the compilationInformation is set,
     * if an error occurred it is null. The status can be retrieved with the getStatus() method
     * and the compilationInformation can be retrieved with getCompilationInformation().
     */
    public void compile() {
        try {
            runCommand(COMPILE_COMMAND);
        } catch (IOException | InterruptedException e) {
            System.out.println("--- ERROR ---\n" + e.getMessage());
        }
    }

}

