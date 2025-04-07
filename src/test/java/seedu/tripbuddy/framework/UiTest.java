package seedu.tripbuddy.framework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UiTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;
    private Ui ui; // Instance of Ui

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output.
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Instantiate Ui
        ui = Ui.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out and System.in.
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testPrintLineSeparator() {
        outContent.reset();
        ui.printLineSeparator();
        String expected = "____________________________________________________________" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testPrintStartMessage() {
        outContent.reset();
        ui.printStartMessage();
        String expected = "____________________________________________________________" + System.lineSeparator() +
                "Welcome to TripBuddy! Type `tutorial` for a list of available commands." + System.lineSeparator() +
                "____________________________________________________________" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testPrintEndMessage() {
        outContent.reset();
        ui.printEndMessage();
        String expected = "____________________________________________________________" + System.lineSeparator() +
                "Your TripBuddy session has ended. Bye!" + System.lineSeparator() +
                "____________________________________________________________" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testPrintMessage() {
        outContent.reset();
        String message = "Test message";
        ui.printMessage(message);
        String expected = "____________________________________________________________" + System.lineSeparator() +
                message + System.lineSeparator() +
                "____________________________________________________________" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }
}
