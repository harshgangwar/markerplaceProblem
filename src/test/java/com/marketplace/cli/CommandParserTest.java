
package com.marketplace.cli;

import java.util.List;

public class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    public void testSimpleCommand() {

        List<String> tokens = parser.parse("REGISTER harsh");

        assertEquals(2, tokens.size());
        assertEquals("REGISTER", tokens.get(0));
        assertEquals("harsh", tokens.get(1));
    }

    public void testQuotedArguments() {

        List<String> tokens = parser.parse(
                "CREATE_LISTING harsh 'Phone model 8' 'Brand new item' 1000 Electronics"
        );

        assertEquals(6, tokens.size());
        assertEquals("Phone model 8", tokens.get(2));
        assertEquals("Brand new item", tokens.get(3));
    }

    public void testMultipleSpaces() {

        List<String> tokens = parser.parse(
                "REGISTER     harsh"
        );

        assertEquals(2, tokens.size());
    }

    public void testUnclosedQuote() {

        boolean exceptionThrown = false;

        try {
            parser.parse("CREATE_LISTING harsh 'Phone model");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual
            );
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}
