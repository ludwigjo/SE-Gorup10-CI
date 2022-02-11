package com.group10.CI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class for managing writing and fetching info from files
 * 
 * @author Bror Sebastian Sjovald
 */
public class HistoryHandler {

    protected final String historyFolderPath = "/history/";
    protected String repoHistoryFolder;

    /**
     * A constructor which initializes where history is being saved, by adding
     * the repoName the server can save several build histories in different
     * folders.
     * 
     * @param repoName The name of the repository tested
     */
    public HistoryHandler(String repoName) {
        this.repoHistoryFolder = historyFolderPath + repoName + "/";
    }

    /**
     * saveHistory writes the CI status message to a file located at
     * repoHistoryFolder/commitId.txt. The message needs to be encoded as a string.
     * 
     * @param commitId The commit id related to the build info
     * @param message  Status of the build
     * @throws IOException When there is an error writing to the file
     */
    public void saveHistory(String commitId, String message) throws IOException {
        FileWriter historyFileWriter = new FileWriter(repoHistoryFolder + commitId + ".txt");

        historyFileWriter.write(message);

        historyFileWriter.close();
    }

    /**
     * Method getHistory fetches the content of the file located at
     * repoHistoryFolder/commitId.txt and returns it as a string.
     * 
     * @param commitId The commit id related to the build info
     * @return Returns the build info of given commit id
     * @throws FileNotFoundException When there is no build info file
     */
    public String getHistory(String commitId) throws FileNotFoundException {
        File historyFile = new File(repoHistoryFolder + commitId + ".txt");

        Scanner s = new Scanner(historyFile);
        StringBuilder sb = new StringBuilder();

        while (s.hasNextLine()) {
            sb.append(s.nextLine());
        }

        s.close();

        return sb.toString();
    }

}
