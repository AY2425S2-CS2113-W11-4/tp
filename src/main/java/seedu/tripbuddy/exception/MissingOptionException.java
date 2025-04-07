package seedu.tripbuddy.exception;

/**
 * Thrown when a required command option or argument is missing.
 * This typically occurs when a flag or parameter expected by a command is not provided.
 */
public class MissingOptionException extends InvalidCommandException {

    private final String missingOpt;

    /**
     * Constructs a {@code MissingOptionException} with the missing option label.
     *
     * @param missingOpt The option that was expected but not provided.
     */
    public MissingOptionException(String missingOpt) {
        super();
        this.missingOpt = missingOpt;
    }

    /**
     * Returns the missing option label that triggered this exception.
     *
     * @return The name of the missing option.
     */
    public String getMissingOpt() {
        return missingOpt;
    }
}
