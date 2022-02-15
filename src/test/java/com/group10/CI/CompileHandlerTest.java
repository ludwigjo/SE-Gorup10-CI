package com.group10.CI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the CompileHandler class
 * Tests for setters and getters are omitted since they are
 * only responsible for setting and getting, no additional
 * operations are made.
 * */
public class CompileHandlerTest {

    /**
     * Test if able to compile the project.
     *
     * Uses GitHandler to clone this project which is stored in a temp directory.
     * The directory is removed on tear down.
     *
     * Asserts that status is set to SUCCESS,and that the compilation information
     * contains BUILD SUCCESS (always outputted when using mvn compile and the
     * build is successful).
     */
    @Test
    @DisplayName("Compile success")
    public void testCompileSuccess() throws Exception {
        // set up
        String mockCommitHash = "9e580b9635fae304a09e6e0dd401910b377a7e51";
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "main");
        CompileHandler ch = new CompileHandler(mockCommitHash, gitHandler.getRepoPath());

        // action
        ch.compile();

        // assert
        assertEquals(true, ch.getInformation().contains("BUILD SUCCESS"));
        assertEquals(Status.SUCCESS, ch.getStatus(), "Status expected to be SUCCESS if successful compile.");

        // tear down
        gitHandler.deleteClonedRepo(new File("temp"));
    }


    /**
     * Test compiling the branch test/build-fail, which contains code with syntax error, thus
     * the build should not be successful.
     *
     * It uses GitHandler to clone this project which is stored in a temp directory.
     * The directory is removed on tear down.
     *
     * Asserts that status is set to FAILURE,
     * and that the compilation information contains BUILD FAILURE (always outputted
     * when using mvn compile and the build is unsuccessful).
     */
    @Test
    @DisplayName("Compile failure")
    public void testCompileFailure() throws Exception {
        // set up
        String mockCommitHash = "9e580b9635fae304a09e6e0dd401910b377a7e51";
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "test/build-fail");
        CompileHandler ch = new CompileHandler(mockCommitHash, gitHandler.getRepoPath());

        // action
        ch.compile();

        // assert
        assertEquals(true, ch.getInformation().contains("BUILD FAILURE"));
        assertEquals(Status.FAILURE, ch.getStatus(), "Status expected to be FAILURE if unsuccessful compile.");

        // tear down
        gitHandler.deleteClonedRepo(new File("temp"));
    }
}
