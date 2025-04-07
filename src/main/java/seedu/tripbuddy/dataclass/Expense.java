package seedu.tripbuddy.dataclass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.json.JSONException;
import org.json.JSONObject;
import seedu.tripbuddy.framework.ExpenseManager;

/**
 * Represents an individual travel expense.
 * Contains details such as name, amount, category, and timestamp.
 */
public class Expense {

    /** Formatter for consistent date-time representation across the app. */
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String name;
    private double amount;
    private String category;
    private LocalDateTime dateTime;

    /**
     * Constructs an expense with a name and amount. Timestamp is set to current time.
     *
     * @param name    the name of the expense
     * @param amount  the monetary amount spent
     */
    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.category = null;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Constructs an expense with name, amount, and category. Timestamp is set to current time.
     *
     * @param name      the name of the expense
     * @param amount    the monetary amount spent
     * @param category  the category assigned to the expense
     */
    public Expense(String name, double amount, String category) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Constructs an expense with full data including a date-time string.
     * Used when loading from saved files.
     *
     * @param name         the name of the expense
     * @param amount       the monetary amount spent
     * @param category     the category assigned to the expense
     * @param dateTimeStr  the date-time string to parse
     *
     * @throws DateTimeParseException if the date-time string is in an invalid format
     */
    public Expense(String name, double amount, String category, String dateTimeStr) throws DateTimeParseException {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.dateTime = LocalDateTime.parse(dateTimeStr, FORMATTER);
    }

    /**
     * Returns the name of the expense.
     *
     * @return the expense name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the expense.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the amount of the expense.
     *
     * @return the monetary amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Updates the amount of the expense.
     *
     * @param amount the new amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the category of the expense.
     *
     * @return the category string, or null if not set
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the date-time as a formatted string.
     *
     * @return the formatted timestamp string
     */
    public String getDateTimeString() {
        return dateTime.format(FORMATTER);
    }

    /**
     * Updates the category of the expense.
     *
     * @param category the new category to assign
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the timestamp of when the expense was recorded.
     *
     * @return the timestamp as a {@link LocalDateTime}
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Updates the timestamp of the expense.
     *
     * @param dateTime the new timestamp to assign
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the amount formatted in the given currency.
     *
     * @param amount   the raw amount
     * @param currency the currency to format with
     * @return formatted amount string
     */
    public String getFormattedAmount(double amount, Currency currency) {
        return currency.getFormattedAmount(amount);
    }

    /**
     * Returns a human-readable string of the expense including currency and date.
     */
    @Override
    public String toString() {
        String dateTimeStr = dateTime.format(FORMATTER);
        Currency baseCurrency = ExpenseManager.getInstance().getBaseCurrency();
        if (category == null) {
            return "name: " + name + ", amount: " +
                    getFormattedAmount(amount, baseCurrency) +
                    ", date: " + dateTimeStr;
        }
        return "name: " + name + ", amount: " + getFormattedAmount(amount, baseCurrency)
                + ", category: " + category + ", date: " + dateTimeStr;
    }

    /**
     * Serializes the expense object to a JSON representation.
     *
     * @return the JSON object representing this expense
     */
    public JSONObject toJSON() {
        JSONObject ret = new JSONObject();
        ret.put("name", name);
        ret.put("amount", (int)(amount * 100 + .5) / 100.);
        ret.put("category", category);
        ret.put("dateTime", getDateTimeString());
        return ret;
    }

    /**
     * Deserializes an expense from a {@link JSONObject}.
     * Used when reading from saved files.
     *
     * @param json the JSON object containing the expense data
     * @return the constructed {@code Expense}
     * @throws JSONException if any required field is missing or date format is invalid
     */
    public static Expense fromJSON(JSONObject json) throws JSONException {
        String name = json.getString("name");
        double amount = json.getDouble("amount");
        String category = json.optString("category", null); // returns null if not present
        String dateTimeStr = json.getString("dateTime");

        // Disallow empty names, remove invalid category if exists
        if (category != null && category.isEmpty()) {
            category = null;
        }
        try {
            return new Expense(name, amount, category, dateTimeStr);
        } catch (DateTimeParseException e) {
            throw new JSONException(e.getParsedString() +
                    ": Invalid date/time format! Please use yyyy-MM-dd HH:mm:ss");
        }
    }
}
