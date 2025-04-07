package seedu.tripbuddy.exception;

/**
 * Thrown when a command keyword is not recognized.
 * This typically occurs when the user inputs an unknown or unsupported command.
 */
public class InvalidKeywordException extends InvalidCommandException {

    private final String keyword;

    /**
     * Constructs an {@code InvalidKeywordException} with the unrecognized keyword.
     *
     * @param keyword The invalid keyword that caused the exception.
     */
    public InvalidKeywordException(String keyword) {
        super();
        this.keyword = keyword;
    }

    /**
     * Returns the invalid keyword that triggered this exception.
     *
     * @return The unrecognized keyword.
     */
    public String getKeyword() {
        return keyword;
    }
}
