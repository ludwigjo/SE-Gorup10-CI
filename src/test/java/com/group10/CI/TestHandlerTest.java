package com.group10.CI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the TestHandler class
 * Tests for setters and getters are omitted since they are
 * only responsible for setting and getting, no additional
 * operations are made.
 * */
public class TestHandlerTest {

    /**
     * Test if able to test the project.
     *
     * Uses GitHandler to clone this project which is stored in a temp directory.
     * The directory is removed on tear down.
     *
     * Asserts that status is set to SUCCESS,and that the compilation information
     * contains BUILD SUCCESS (always outputted when using mvn compile and the
     * build is successful).
     */
    
    @Test
    @DisplayName("Test success")
    public void testTestSuccess() throws Exception {
        // set up
        String mockCommitHash = "9e580b9635fae304a09e6e0dd401910b377a7e51";
        GitHandler gitHandler = new GitHandler("https://github.com/darkway-s/SE-Group10", "main");
        CompileHandler ch = new CompileHandler(mockCommitHash, gitHandler.getRepoPath());
        TestHandler th = new TestHandler(mockCommitHash, gitHandler.getRepoPath());

        // action
        ch.compile();
        th.test();

        // assert
        assertEquals(Status.SUCCESS, ch.getStatus(), "Status expected to be SUCCESS if successful test.");
        // tear down
        gitHandler.deleteClonedRepo(new File("temp"));
    }

    /**
     * Test the branch identify the test failure, which contains unpassed unit tests, thus
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
    @DisplayName("Test failure")
    public void testTestFailure() throws Exception {
        // set up
        String mockCommitHash = "4c9130f81a2d52176d56ef6a75e4b1070692a498";
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "test/test-fail");
        CompileHandler ch = new CompileHandler(mockCommitHash, gitHandler.getRepoPath());
        TestHandler th = new TestHandler(mockCommitHash, gitHandler.getRepoPath());

        // action
        ch.compile();
        th.test();

        // assert
        assertEquals(Status.FAILURE, th.getStatus(), "Status expected to be FAILURE if unsuccessful test.");

        // tear down
        gitHandler.deleteClonedRepo(new File("temp"));
    }
}
