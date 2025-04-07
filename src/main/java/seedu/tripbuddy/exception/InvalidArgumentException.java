package seedu.tripbuddy.exception;

/**
 * Represents an exception thrown when a command argument is invalid.
 * This could be due to missing values, invalid formatting, or unsupported inputs.
 */
public class InvalidArgumentException extends InvalidCommandException {

    private final String argument;

    /**
     * Constructs an {@code InvalidArgumentException} with the given argument.
     *
     * @param argument The argument that caused the exception.
     */
    public InvalidArgumentException(String argument) {
        super();
        this.argument = argument;
    }

    /**
     * Constructs an {@code InvalidArgumentException} with a custom message and argument.
     *
     * @param argument The argument that caused the exception.
     * @param message  A descriptive message about why the argument is invalid.
     */
    public InvalidArgumentException(String argument, String message) {
        super(message);
        this.argument = argument;
    }

    /**
     * Returns the argument that caused the exception.
     *
     * @return The invalid argument.
     */
    public String getArgument() {
        return argument;
    }
}
