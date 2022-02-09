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
 * Test for the CompileHandler class
 * Tests for setters and getters are omitted since they are
 * only responsible for setting and getting, no additional
 * operations are made.
 * */
public class CompileHandlerTest {

    /**
     * Test if able to compile the project.
     * Uses GitHandler to clone this project which is stored in a temp directory.
     * The directory is removed on tear down. Asserts that status is set to SUCCESS,
     * and that the compilation information contains BUILD SUCCESS (always outputted
     * when using mvn compile and the build is successful).
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
        assertEquals(true, ch.getCompilationInformation().contains("BUILD SUCCESS"));
        assertEquals(Status.SUCCESS, ch.getStatus(), "Status expected to be SUCCESS if successful compile.");

        // tear down
        gitHandler.deleteClonedRepo(new File("temp"));
    }

    /**
     * Test if able to convert InputStream to String.
     * The test is constructed by converting a String to an InputStream
     * via the ByteArrayInputStream class. The using the convertInputStreamToString
     * to convert it back to a String. If that String is the same as the initial string
     * the test will pass.
     *
     * Since the method is private the Method class is used.
     * */
    @Test
    @DisplayName("Convert from InputStream to String.")
    public void testConvertInputStreamToString() throws Exception {
        // set up
        String initialString = "This is converted to an\n InputStream";
        InputStream is = new ByteArrayInputStream(initialString.getBytes());

        // action
        Method method = CompileHandler.class.getDeclaredMethod("convertInputStreamToString", InputStream.class);
        method.setAccessible(true);
        String result = (String) method.invoke(new CompileHandler(), is);

        // assert
        assertEquals(initialString, result, "Initial string and result should be equal");
    }

}
