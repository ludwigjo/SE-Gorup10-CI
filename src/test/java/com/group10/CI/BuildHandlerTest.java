package com.group10.CI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildHandlerTest {
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
        Method method = BuildHandler.class.getDeclaredMethod("convertInputStreamToString", InputStream.class);
        method.setAccessible(true);
        String result = (String) method.invoke(new BuildHandler(), is);

        // assert
        assertEquals(initialString, result, "Initial string and result should be equal");
    }
}
