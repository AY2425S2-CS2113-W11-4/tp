package seedu.tripbuddy.command;

import seedu.tripbuddy.exception.InvalidArgumentException;
import seedu.tripbuddy.exception.MissingOptionException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a parsed command from the user's input.
 * Stores the command {@link Keyword} and a list of associated {@link Option}s.
 * Also allows retrieval of option values for command execution.
 */
public class Command {

    /**
     * Maximum allowed numeric input value.
     * Values above this threshold are considered invalid.
     */
    public static final double MAX_INPUT_VAL = 1e6;

    private final Keyword keyword;
    private final ArrayList<Option> optList;
    private final HashMap<String, String> optMap;

    /**
     * Constructs a Command with the specified keyword.
     *
     * @param keyword the main keyword that defines this command
     */
    public Command(Keyword keyword) {
        this.keyword = keyword;
        this.optList = new ArrayList<>();
        this.optMap = new HashMap<>();
    }

    /**
     * Returns the string representation of the command,
     * including the keyword and its options.
     *
     * @return a formatted command string
     */
    @Override
    public String toString() {
        if (optList.isEmpty()) {
            return keyword.toString();
        }

        StringBuilder builder = new StringBuilder();
        for (Option opt : optList) {
            builder.append(' ').append(opt);
        }
        return keyword.toString() + builder;
    }

    /**
     * Returns the keyword of the command.
     *
     * @return the command keyword
     */
    public Keyword getKeyword() {
        return keyword;
    }

    /**
     * Returns the list of options associated with this command.
     *
     * @return a list of options
     */
    public ArrayList<Option> getOptList() {
        return optList;
    }

    /**
     * Adds an option to the command and stores its value.
     *
     * @param opt the option to add
     */
    public void addOption(Option opt) {
        optList.add(opt);
        optMap.put(opt.opt(), opt.val());
    }

    /**
     * Checks whether a specific option exists in the command.
     *
     * @param opt the option flag to check (e.g., "-a")
     * @return true if the option is present, false otherwise
     */
    public boolean hasOpt(String opt) {
        return optMap.containsKey(opt);
    }

    /**
     * Retrieves the value of the specified option.
     *
     * @param opt the option flag to retrieve
     * @return the string value of the option
     * @throws MissingOptionException if the option is not found
     */
    public String getOpt(String opt) throws MissingOptionException {
        if (!optMap.containsKey(opt)) {
            throw new MissingOptionException(opt);
        }
        return optMap.get(opt);
    }

    /**
     * Returns the total number of options provided with the command.
     *
     * @return the count of options
     */
    public int getOptCount() {
        return optList.size();
    }

    /**
     * Parses the value of the given option into a double.
     * Performs range validation.
     *
     * @param opt the option flag whose value should be parsed
     * @return the parsed double value
     * @throws MissingOptionException if the option is not present
     * @throws InvalidArgumentException if the value is not a valid number,
     *                                  non-positive, or exceeds {@code MAX_INPUT_VAL}
     */
    public Double parseDouble(String opt) throws MissingOptionException, InvalidArgumentException {
        String val = getOpt(opt);
        try {
            double ret = Double.parseDouble(val);
            if (ret <= 0) {
                throw new InvalidArgumentException(val, "Value should be more than 0.");
            }
            if (ret > MAX_INPUT_VAL) {
                throw new InvalidArgumentException(val,
                        "Value should be no more than " + MAX_INPUT_VAL);
            }
            return ret;
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException(val, "Not a number.");
        }
    }
}
