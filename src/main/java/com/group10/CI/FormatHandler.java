package com.group10.CI;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer.Form;
import java.util.Arrays;

public class FormatHandler extends BuildHandler {

    /**
     * Inherit from super class:
     * protected String commitHash;
     * protected Status status;
     * protected String information;
     * protected String repoPath;
     */
    private final String[] COMPILE_COMMAND = new String[] { "aha" };

    /**
     * Default constructor.
     *
     * @param commitHash the hash of the commit that is being compiled.
     * @param repoPath   the path to the cloned repo with the commit to be compiled.
     */
    public FormatHandler(String commitHash, String repoPath) {
        super(commitHash, repoPath);
    }

    /**
     * Empty constructor
     */
    public FormatHandler() {
        super();
    }

    /**
     * Uses ANSI HTML adapter (aha) to format std output to html.
     * 
     * @param content The std output that will be formatted
     */
    public void format(String content) {
        setStatus(Status.PENDING);

        File directory = new File(this.repoPath);
        ProcessBuilder processBuilder = new ProcessBuilder(COMPILE_COMMAND);
        processBuilder.directory(directory);

        Process process;
        try {
            process = processBuilder.start();
            System.out.println("Running command: " + Arrays.toString(COMPILE_COMMAND));

            OutputStream os = process.getOutputStream();

            os.write(content.getBytes());
            os.flush();
            os.close();

            joinOutput(convertInputStreamToString(process.getInputStream()));

            process.waitFor();

            if (process.exitValue() == 0)
                setStatus(Status.SUCCESS);
            else
                setStatus(Status.FAILURE);

            process.destroy();

            setInformation(this.jointOutput);
        } catch (IOException | InterruptedException e) {
            System.out.println("\n ---- ERROR ---- \n" + e.getMessage());
            setStatus(Status.ERROR);
            setInformation(null);
        }
    }
}
