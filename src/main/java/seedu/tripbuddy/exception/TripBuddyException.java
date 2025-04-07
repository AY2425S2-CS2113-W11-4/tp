package seedu.tripbuddy.exception;

/**
 * Base class for all exceptions specific to the TripBuddy application.
 * Serves as the root of the custom exception hierarchy.
 */
public abstract class TripBuddyException extends Exception {

    /**
     * Constructs a {@code TripBuddyException} with no detail message.
     */
    public TripBuddyException() {
        super();
    }

    /**
     * Constructs a {@code TripBuddyException} with the specified detail message.
     *
     * @param message A descriptive message explaining the exception.
     */
    public TripBuddyException(String message) {
        super(message);
    }
}
