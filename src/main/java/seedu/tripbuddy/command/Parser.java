package seedu.tripbuddy.command;

import seedu.tripbuddy.exception.InvalidKeywordException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible for parsing raw command-line input into structured {@link Command} objects.
 * Converts the user's string input into a keyword and its associated options.
 */
public class Parser {

    private final Logger logger;

    /**
     * Constructs a parser instance with the given logger.
     *
     * @param logger the logger to be used during parsing
     */
    public Parser(Logger logger) {
        this.logger = logger;
    }


    /**
     * Determines whether the given string represents an option (starts with {@code -}).
     *
     * @param s the string to check
     * @return true if the string is a valid option, false otherwise
     */
    private boolean isOpt(String s) {
        return s.length() > 1 && s.startsWith("-");
    }

    /**
     * Parses a full command input line into a {@link Command} object.
     * Extracts the keyword and its associated options, logging the process.
     *
     * @param cmdInput the raw user input string
     * @return a parsed {@link Command} object
     * @throws InvalidKeywordException if the command keyword is unrecognized
     */
    public Command parseCommand(String cmdInput) throws InvalidKeywordException {
        logger.log(Level.INFO, "Start parsing: \"" + cmdInput + '"');

        String[] tokens = cmdInput.strip().replace("\t", " ").split(" ");
        Command cmd = null;

        String keywordString = tokens[0];
        for (Keyword i : Keyword.values()) {
            if (keywordString.equals(i.toString())) {
                cmd = new Command(i);
                break;
            }
        }

        // Not a legit keyword
        if (cmd == null) {
            throw new InvalidKeywordException(keywordString);
        }

        int numTokens = tokens.length;
        if (numTokens == 1) {
            return cmd;
        }

        String opt = "";
        StringBuilder val = new StringBuilder();
        for (int i = 1; i < numTokens; ++i) {
            if (!isOpt(tokens[i])) {
                val.append(tokens[i]).append(' ');
                continue;
            }
            // Has extra tokens before the first opt-val pair
            if (i > 1) {
                Option newOpt = new Option(opt, val.toString().strip());
                cmd.addOption(newOpt);
                logger.log(Level.INFO, "New option: \"" + newOpt + '"');
            }
            opt = tokens[i].substring(1);
            val = new StringBuilder();
        }

        Option newOpt = new Option(opt, val.toString().strip());
        cmd.addOption(newOpt);
        logger.log(Level.INFO, "New option: \"" + newOpt + '"');

        logger.log(Level.INFO, "End parsing: \"" + cmd + '"');
        return cmd;
    }
}
