package com.group10.CI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Notification handler
 */
public class NotificationHandlerTest {
    
    /**
     * Testing that the build status is set correctly to github.
     * The notification is corectly sent to github wen a 201 code is returned. This is a true return.
     */
    @Test
    @DisplayName("Positive test for build status")
    public void testBuildStatus() {
        Build build = new Build("78682ea6f5917499797e1a78827a2e41556b44c8", "hello@email.com", Status.SUCCESS, Status.FAILURE, "ludwigjo/SE-Gorup10-CI");
        NotificationHandler nh = new NotificationHandler();
        nh.notifyGitHub(build);
        assertEquals(true, nh.getSuccessfulDelivery());

    }

    /**
     * Testing that the status code is false when the notification is not sent to github.
     * The test repo does not exist, thus the delivery is false.
     */
    @Test
    @DisplayName("Negative test for build status")
    public void testBuildStatusFail() {
        Build build = new Build("78682ea6f5917499797e1a78827a2e41556b44c8",  "hello@email.com", Status.SUCCESS, Status.FAILURE, "ludwigjo/SE-Gorup10-CIFail");
        NotificationHandler nh = new NotificationHandler();
        nh.notifyGitHub(build);
        assertEquals(false, nh.getSuccessfulDelivery());
    }

    /**
     * Testing that the pending status is set correctly to github.
     * We should receive a 201 code when the notification is sent to github.
     */
    @Test
    @DisplayName("Positive test for pending status")
    public void testPendingStatus() {
        Build build = new Build("78682ea6f5917499797e1a78827a2e41556b44c8", "hello@email.com", Status.PENDING, Status.FAILURE, "ludwigjo/SE-Gorup10-CI");
        NotificationHandler nh = new NotificationHandler();
        nh.notifyGitHub(build);
        assertEquals(true, nh.getSuccessfulDelivery());
    }
}
