package seedu.tripbuddy.exception;

import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;

/**
 * Handles formatting of exception messages for display to the user.
 * Provides user-friendly messages for different types of exceptions encountered in the program.
 */
public class ExceptionHandler {

    /**
     * Handles invalid keyword exceptions.
     *
     * @param e The exception containing the invalid keyword.
     * @return A user-friendly message describing the error.
     */
    public static String handleInvalidKeywordException(InvalidKeywordException e) {
        String keyword = e.getKeyword();
        return "Invalid command keyword: \"" + keyword +
                "\". Please review the user guide for more information.";
    }

    /**
     * Handles invalid argument exceptions.
     *
     * @param e The exception containing the invalid argument and optional message.
     * @return A user-friendly message describing the error.
     */
    public static String handleInvalidArgumentException(InvalidArgumentException e) {
        String argument = e.getArgument();
        String message = e.getMessage();
        if (message != null) {
            return "Invalid command argument: \"" + argument +
                    "\": " + message;
        }
        return "Invalid command argument: \"" + argument +
                "\". Please review the user guide for more information.";
    }

    /**
     * Handles missing option exceptions.
     *
     * @param e The exception containing the missing option label.
     * @return A user-friendly message describing the missing option.
     */
    public static String handleMissingOptionException(MissingOptionException e) {
        String missingOpt = e.getMissingOpt();
        if (e.getMissingOpt().isEmpty()) {
            return "Oh no. Cannot not find the main argument.";

        }
        return "Oh no. Cannot not find option label: -" + missingOpt;
    }

    /**
     * Handles date/time parsing exceptions.
     *
     * @param e The exception that occurred while parsing a date/time string.
     * @return A message indicating the correct format and what went wrong.
     */
    public static String handleDateTimeParseException(DateTimeParseException e) {
        return "Invalid date/time format! Please use yyyy-MM-dd HH:mm:ss\n\t" + e.getParsedString();
    }

    /**
     * Handles file not found exceptions during data loading.
     *
     * @param e The exception indicating the file could not be found.
     * @return A message indicating that no save file exists.
     */
    public static String handleFileNotFoundException(FileNotFoundException e) {
        return "No save file detected.";
    }

    /**
     * Handles any generic exception.
     *
     * @param e The exception to handle.
     * @return The message contained in the exception.
     */
    public static String handleException(Exception e) {
        return e.getMessage();
    }
}
