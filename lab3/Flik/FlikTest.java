import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FlikTest {

    /** Performs a few arbitrary tests to see if the product method is correct */

    @Test 
    public void testFlik() {
        int upperBoundary = 128, lowerBoundary = -128;
        assertTrue(Flik.isSameNumber(lowerBoundary, lowerBoundary));
        assertTrue(Flik.isSameNumber(upperBoundary, upperBoundary));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {        
        jh61b.junit.TestRunner.runTests("all", FlikTest.class);
    }
}
