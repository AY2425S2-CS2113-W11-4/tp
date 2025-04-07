package seedu.tripbuddy.exception;

/**
 * Thrown when a command cannot be correctly parsed or interpreted.
 * This is the superclass for all specific command-related parsing exceptions.
 */
public abstract class InvalidCommandException extends TripBuddyException {

    /**
     * Constructs an {@code InvalidCommandException} with no detail message.
     */
    public InvalidCommandException() {
        super();
    }

    /**
     * Constructs an {@code InvalidCommandException} with the specified detail message.
     *
     * @param message Description of the parsing or command error.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
