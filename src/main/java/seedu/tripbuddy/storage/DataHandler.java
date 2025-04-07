package seedu.tripbuddy.storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import seedu.tripbuddy.command.Command;
import seedu.tripbuddy.dataclass.Currency;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.DataLoadingException;
import seedu.tripbuddy.exception.InvalidArgumentException;
import seedu.tripbuddy.framework.ExpenseManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHandler {

    private static DataHandler instance = null;
    private static final Logger LOGGER = Logger.getLogger("TripBuddy");
    private final FileHandler fileHandler;

    private DataHandler() {
        fileHandler = FileHandler.getInstance();
    }

    private static double round2Digits(double x) {
        assert x > 0 && x <= Command.MAX_INPUT_VAL;
        return (int) (x * 100 + .5) / 100.;
    }

    /**
     * Gets a singleton instance of {@link DataHandler}.
     */
    public static DataHandler getInstance() {
        if (instance == null) {
            instance = new DataHandler();
        }
        return instance;
    }

    /**
     * Saves the current {@link ExpenseManager} info into a json file.
     * @return The message for display
     */
    public String saveData(String path, ExpenseManager expenseManager) throws IOException {
        JSONObject root = new JSONObject();
        root.put("currency", expenseManager.getBaseCurrency().toString());
        root.put("budget", round2Digits(expenseManager.getBudget()));

        LOGGER.log(Level.INFO, "budget converted");

        JSONArray categoriesArr = new JSONArray();
        for (String category : expenseManager.getCategories()) {
            categoriesArr.put(category);
        }
        root.put("categories", categoriesArr);

        LOGGER.log(Level.INFO, "categories converted");

        JSONArray expensesArr = new JSONArray();
        for (Expense expense : expenseManager.getExpenses()) {
            JSONObject expObj = expense.toJSON();
            expensesArr.put(expObj);
            LOGGER.log(Level.INFO, "expense converted: " + expObj);
        }
        root.put("expenses", expensesArr);

        LOGGER.log(Level.INFO, "expenses converted");

        String absPath = fileHandler.writeJsonObject(path, root);
        return "Saved data to file:\n\t" + absPath;
    }

    /**
     * Loads the ExpenseManager data from a file and returns a status message for display.
     *
     * @param path The path to the JSON file.
     * @return A message including all error info.
     * @throws FileNotFoundException If the file cannot be found.
     * @throws JSONException         If there is an error parsing the JSON.
     * @throws DataLoadingException  If required fields are missing or invalid.
     */
    public String loadData(String path)
            throws FileNotFoundException, DataLoadingException {

        JSONObject root;
        try {
            root = fileHandler.readJsonObject(path);
        } catch (JSONException e) {
            throw new DataLoadingException("Failed to load your json save due syntax errors:\n\t" + e.getMessage());
        }
        ExpenseManager expenseManager = ExpenseManager.getInstance();

        // Save all messages to be displayed
        StringBuilder invalidJsonMessage = new StringBuilder();

        try {
            double budget = root.getDouble("budget");
            if (budget <= 0 || budget > Command.MAX_INPUT_VAL) {
                throw new DataLoadingException(
                        "Budget value invalid or out of range. Using default budget instead.");
            }
            expenseManager.setBudget(budget);
        } catch (JSONException e) {
            invalidJsonMessage.append("Budget information missing. Using default budget instead.\n");
        }

        String currencyName;
        try {
            currencyName = root.getString("currency");
            Currency currency = Currency.valueOf(currencyName);
            expenseManager.setBaseCurrency(currency);
        } catch (JSONException e) {
            invalidJsonMessage.append("Currency information missing. Using SGD instead.\n");
        } catch (IllegalArgumentException e) {
            currencyName = root.optString("currency", "UNKNOWN");
            invalidJsonMessage.append("Unrecognized currency: ")
                    .append(currencyName).append(". Using SGD instead.\n");
        }

        try {
            JSONArray categoriesArr = root.getJSONArray("categories");
            for (int i = 0; i < categoriesArr.length(); i++) {
                String category = categoriesArr.optString(i, null);
                if (category == null) {
                    continue;
                }
                try {
                    expenseManager.createCategory(category);
                } catch (InvalidArgumentException e) {
                    String message = e.getMessage();
                    invalidJsonMessage.append("Category \"").append(category).append("\": ")
                            .append(message).append("\n");
                }
            }
        } catch (JSONException e) {
            invalidJsonMessage.append("Categories information missing. Will create categories along with expenses.\n");
        }

        try {
            JSONArray expensesArr = root.getJSONArray("expenses");
            for (int i = 0; i < expensesArr.length(); i++) {
                try {
                    JSONObject expObj = expensesArr.getJSONObject(i);
                    expenseManager.addExpense(expObj);
                } catch (JSONException e) {
                    String message = e.getMessage();
                    invalidJsonMessage.append("Failed to parse expense at index ").append(i).append(". Skipping:\n\t")
                            .append(message).append("\n");
                }
            }
        } catch (JSONException e) {
            invalidJsonMessage.append("Expenses information missing.\n");
        }

        return invalidJsonMessage.toString();
    }
}
