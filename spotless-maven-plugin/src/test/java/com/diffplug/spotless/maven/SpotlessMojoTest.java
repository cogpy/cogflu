package com.diffplug.spotless.maven;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic unit test for SpotlessMojo
 */
public class SpotlessMojoTest {

    @Test
    public void testMojoExists() {
        // Basic test to ensure the Mojo class can be instantiated
        SpotlessMojo mojo = new SpotlessMojo();
        assertNotNull(mojo);
    }
}