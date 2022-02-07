package com.group10.CI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test for GitHander class.
 */
public class TestGitHandler {
    
    /**
     * The GitHandler is responsible for cloning and checking out a repository
     * using a provided url and branch name.
     */
    @Test
    @DisplayName("Positive test for GitHandler")
    public void testGitHandler() {
        GitHandler gitHandler = new GitHandler("https://github.com/ludwigjo/SE-Gorup10-CI", "main");
        // TODO: Test that a folder is created for the repository and that it has content.
    }

}
