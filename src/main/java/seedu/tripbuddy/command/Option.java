package seedu.tripbuddy.command;

/**
 * Represents an option-value pair from a command input.
 * The {@code opt} excludes the leading dash {@code '-'}.
 *
 * For example, in the command {@code add-expense lunch -a 10},
 * the option would be {@code a} and the value would be {@code 10}
 *
 * @param opt the option name without the leading dash (e.g., {@code "a"})
 * @param val the value associated with the option
 */
public record Option(String opt, String val) {

    /**
     * Returns a string representation of this option for debugging and display.
     * The format is {@code -opt: val}, or just the value if no option name is set.
     *
     * @return formatted option string
     */
    @Override
    public String toString() {
        if (opt.isEmpty()) {
            return val;
        }
        if (val.isEmpty()) {
            return "-" + opt;
        }
        return "-" + opt + ": " + val;
    }
}
