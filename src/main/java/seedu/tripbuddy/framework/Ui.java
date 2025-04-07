package seedu.tripbuddy.framework;

import java.util.Scanner;

/**
 * Handles user interaction, including printing messages and getting user input.
 * This class manages all input/output operations for the program.
 */
public class Ui {

    private static Ui instance = null;

    private static final String LINE =
            "____________________________________________________________";
    private static final String START_MESSAGE =
            "Welcome to TripBuddy! Type `tutorial` for a list of available commands.";
    private static final String END_MESSAGE = "Your TripBuddy session has ended. Bye!";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Private constructor for singleton pattern.
     */
    private Ui() {}

    /**
     * Returns the singleton instance of {@code Ui}.
     * If the instance does not exist, it is created.
     */
    public static Ui getInstance() {
        if (instance == null) {
            instance = new Ui();
        }
        return instance;
    }

    /**
     * Prints a line separator for better readability.
     */
    public void printLineSeparator() {
        System.out.println(LINE);
    }

    /**
     * Prints a start message when the program begins.
     */
    public void printStartMessage() {
        printLineSeparator();
        System.out.println(START_MESSAGE);
        printLineSeparator();
    }

    /**
     * Retrieves user input from the console.
     * If there is no next line (e.g., in text UI testing), returns "quit".
     *
     * @return The command entered by the user, with leading and trailing whitespace removed.
     */
    public String getUserInput() {
        // Force quit if text-ui-testing does not end with a "quit" line input
        if (!scanner.hasNextLine()) {
            return "quit";
        }
        return scanner.nextLine().strip();
    }

    /**
     * Prints a message indicating that the program is exiting.
     */
    public void printEndMessage() {
        printLineSeparator();
        System.out.println(END_MESSAGE);
        printLineSeparator();
    }

    /**
     * Prints a custom message wrapped with line separators.
     *
     * @param message The message to display.
     */
    public void printMessage(String message) {
        printLineSeparator();
        System.out.println(message);
        printLineSeparator();
    }
}
