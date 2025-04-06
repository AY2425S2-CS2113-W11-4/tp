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
import seedu.tripbuddy.framework.Ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHandler {

    private static final Logger LOGGER = Logger.getLogger("TripBuddy");

    public static double round2Digits(double x) {
        assert x > 0 && x <= Command.MAX_INPUT_VAL;
        return (int) (x * 100 + .5) / 100.;
    }

    public static String saveData(String path, ExpenseManager expenseManager) throws IOException {
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

        String absPath = FileHandler.writeJsonObject(path, root);
        return "Saved data to file:\n\t" + absPath;
    }

    /**
     * Loads the ExpenseManager data from a file and shows progress messages via the provided UI.
     *
     * @param path The path to the JSON file.
     * @param ui   The UI instance to show messages.
     * @return A new ExpenseManager instance populated with the data.
     * @throws FileNotFoundException If the file cannot be found.
     * @throws JSONException         If there is an error parsing the JSON.
     * @throws DataLoadingException  If required fields are missing or invalid.
     */
    public static ExpenseManager loadData(String path, Ui ui)
            throws FileNotFoundException, DataLoadingException {

        JSONObject root = FileHandler.readJsonObject(path);
        ExpenseManager expenseManager;

        try {
            double budget = root.getDouble("budget");
            if (budget <= 0 || budget > Command.MAX_INPUT_VAL) {
                throw new DataLoadingException(
                        "Budget value invalid or out of range. Using default budget instead.");
            }
            expenseManager = new ExpenseManager(budget);

        } catch (JSONException e) {
            throw new DataLoadingException("Missing or invalid budget information.");
        }

        String currencyName;
        try {
            currencyName = root.getString("currency");
            Currency currency = Currency.valueOf(currencyName);
            expenseManager.setBaseCurrency(currency);

        } catch (JSONException e) {
            ui.printMessage("Currency information missing.");
        } catch (IllegalArgumentException e) {
            currencyName = root.optString("currency", "UNKNOWN");
            ui.printMessage("Unrecognized currency: " + currencyName + ". Using SGD instead.");
        }

        JSONArray categoriesArr = root.getJSONArray("categories");
        for (int i = 0; i < categoriesArr.length(); i++) {
            String category = categoriesArr.optString(i, null);
            if (category == null) {
                continue;
            }
            try {
                expenseManager.createCategory(category);
            } catch (InvalidArgumentException e) {
                ui.printMessage("Category \"" + category + "\" already exists. Skipping.");
            }
        }

        JSONArray expensesArr = root.getJSONArray("expenses");
        for (int i = 0; i < expensesArr.length(); i++) {
            try {
                JSONObject expObj = expensesArr.getJSONObject(i);
                expenseManager.addExpense(expObj);
            } catch (JSONException e) {
                ui.printMessage("Failed to parse expense at index " + i);
            }
        }

        return expenseManager;
    }
}
