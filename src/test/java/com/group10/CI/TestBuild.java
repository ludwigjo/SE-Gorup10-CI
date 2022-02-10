package com.group10.CI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the Build class taking care of the build information.
 */
public class TestBuild {
    /**
     * Checks of the prId is correctly returned.
     */
    @Test
    @DisplayName("Test for correct prId")
    public void testPrId() {
        Build b = new Build();
        b.setPrId("12345");
        assertEquals("12345", b.getPrId());
    }

    /**
     * Checks of the email is correctly returned.
     */
    @Test
    @DisplayName("Test for correct email")
    public void testEmail() {
        Build b = new Build();
        b.setEmail("hello@github.com");
        assertEquals("hello@github.com", b.getEmail());
    }
}
