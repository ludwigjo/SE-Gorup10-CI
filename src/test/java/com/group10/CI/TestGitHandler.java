package com.group10.CI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test for GitHander class.
 * The GitHandler is responsible for cloning and checking out a repository
 * using a provided url and branch name.
 */
public class TestGitHandler {
    /**
     * Checks if the path of the repository is correct.
     */
    @Test
    @DisplayName("Positive test for GitHandler branch path")
    public void testGitHandlerPath() {
        GitHandler gh = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "main");
        assert gh.getRepoPath().equals("temp/SE-Gorup10-CI");
        gh.deleteClonedRepo(new File("temp"));
    }
    
    /**
     * Checks that the repository is cloned.
     */
    @Test
    @DisplayName("Positive test for GitHandler clone")
    public void testGitHandlerClone() {
        GitHandler gh = new GitHandler();
        gh.setRepoPath("temp/SE-Gorup10-CI");  
        assertEquals(true, gh.cloneRepo("https://github.com/ludwigjo/SE-Gorup10-CI", "main"));
        gh.deleteClonedRepo(new File("temp"));
    }

}
