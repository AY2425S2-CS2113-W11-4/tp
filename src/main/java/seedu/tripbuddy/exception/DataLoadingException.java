package seedu.tripbuddy.exception;

/**
 * Represents an exception that occurs during data loading.
 * This exception is thrown when the application fails to parse or load saved data.
 */
public class DataLoadingException extends TripBuddyException {

    /**
     * Constructs a new {@code DataLoadingException} with the specified detail message.
     *
     * @param message Description of the error that occurred while loading data.
     */
    public DataLoadingException(String message) {
        super(message);
    }

}
