package com.group10.CI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for HistoryHandler includes reading and writing to history files
 * located at a history folder.
 * 
 * Requires:
 * <li>- A history folder at project root</li>
 * 
 * @author Bror Sebastian Sjovald
 */
public class HistoryHandlerTest {

    private static HistoryHandler hh;
    private String testMessage = "I'm having an old friend for dinner";
    private String testCommitId = "testingishere";

    /**
     * Initializes the HistoryHandler to focus on the folder <b>history/testing/</b>
     */
    @BeforeAll
    public static void setUp() {
        hh = new HistoryHandler("testing");
    }

    /**
     * The testing folder is initialized to at the start of each test contain
     * a single file called testExists.txt with the content of testMessage.
     * 
     * @throws IOException
     */
    @BeforeEach
    public void resetTestFolder() throws IOException {
        FileUtils.cleanDirectory(new File("history/testing"));

        FileWriter fw = new FileWriter(new File("history/testing/fileExists"));
        fw.write(testMessage);
        fw.close();
    }

    /**
     * After testing is done, the <b>history/testing/</b> folder is cleaned by
     * removing all files in it
     * 
     * @throws IOException
     */
    @AfterAll
    public static void cleanUp() throws IOException {
        FileUtils.cleanDirectory(new File("history/testing"));
    }

    /**
     * When saving the build history a new file should be created
     * as <b>FileWriter</b> is doing the writing no assertions is performed on the
     * contents. Only tests if a file is created when it does not exist in
     * beforehand.
     */
    @Test
    @DisplayName("Tests saving info to a new file")
    public void saveHistoryTestFileDoesNotExist() {
        try {
            hh.saveHistory(testCommitId, testMessage);
            File newHistory = new File("history/testing/" + testCommitId);
            assertEquals(false, newHistory.createNewFile(), "The expected written file shouldn't be new");
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * When saving the build history an already existing file the contents should be
     * modified.
     * Only tests if a file has been modified when it exists in beforehand.
     */
    @Test
    @DisplayName("Tests saving info to already existing file")
    public void saveHistoryTestFileAlreadyExists() {
        File existingHistory = new File("history/testing/testExists");
        long timeBeforeModification = existingHistory.lastModified();
        try {
            hh.saveHistory("testExists", testMessage);
            assertNotEquals(timeBeforeModification, existingHistory.lastModified(),
                    "The file should have been modified");
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * If a build file doesn't exist, a <b>FileNotFoundException</b> should be
     * thrown
     */
    @Test
    @DisplayName("Tests fetching non-available information")
    public void getHistoryFileDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> {
            hh.getHistory(testCommitId);
        }, "Should throw exception when the file doesn't exist");
    }

    /**
     * When fetching info from a build file, the content should match with the
     * written content
     */
    @Test
    @DisplayName("Tests fetching available info")
    public void getHistoryFileAlreadyExists() {
        try {
            String message = hh.getHistory("fileExists");
            assertEquals(testMessage, message, "Should get the right content from file");
        } catch (FileNotFoundException e) {
            fail();
        }
    }
}
