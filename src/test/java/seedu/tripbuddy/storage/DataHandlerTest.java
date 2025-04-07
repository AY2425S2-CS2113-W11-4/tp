package seedu.tripbuddy.storage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.dataclass.Currency;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.DataLoadingException;
import seedu.tripbuddy.framework.ExpenseManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

class DataHandlerTest {

    @BeforeEach
    void initExpenseManager() {
        ExpenseManager expenseManager = ExpenseManager.getInstance();
        expenseManager.clearExpensesAndCategories();
    }

    @Test
    void testLoadDataValid() throws IOException, DataLoadingException {
        // Create a JSON object with valid data.
        JSONObject root = new JSONObject();
        double budget = 1500.0;
        root.put("budget", budget);
        root.put("currency", "SGD");

        JSONArray categoriesArr = new JSONArray();
        categoriesArr.put("Food");
        categoriesArr.put("Transport");
        root.put("categories", categoriesArr);

        JSONArray expensesArr = new JSONArray();
        JSONObject expense1 = new JSONObject();
        expense1.put("name", "Lunch");
        expense1.put("amount", 12.5);
        expense1.put("category", "Food");
        expense1.put("dateTime", "2025-04-05 12:00:00");
        expensesArr.put(expense1);
        root.put("expenses", expensesArr);

        // Write JSON to a temporary file.
        File tempFile = File.createTempFile("testLoadDataValid", ".json");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), root.toString().getBytes());

        // Load the data.
        DataHandler.getInstance().loadData(tempFile.getAbsolutePath());
        ExpenseManager expenseManager = ExpenseManager.getInstance();

        // Verify the loaded state.
        assertEquals(budget, expenseManager.getBudget(), 0.0001);
        assertEquals(Currency.SGD, expenseManager.getBaseCurrency());
        List<String> categories = expenseManager.getCategories();
        assertTrue(categories.contains("Food"), "Categories should contain 'Food'");
        assertTrue(categories.contains("Transport"), "Categories should contain 'Transport'");

        List<Expense> expenses = expenseManager.getExpenses();
        assertEquals(1, expenses.size(), "There should be one expense loaded.");
        Expense loadedExpense = expenses.get(0);
        assertEquals("Lunch", loadedExpense.getName());
        assertEquals(12.5, loadedExpense.getAmount(), 0.0001);
        assertEquals("Food", loadedExpense.getCategory());
        assertEquals("2025-04-05 12:00:00", loadedExpense.getDateTimeString());
    }

    @Test
    void testLoadDataInvalidBudget() throws IOException {
        // Create JSON with an invalid (negative) budget.
        JSONObject root = new JSONObject();
        root.put("budget", -100.0);
        root.put("currency", "SGD");
        root.put("categories", new JSONArray());
        root.put("expenses", new JSONArray());

        File tempFile = File.createTempFile("testLoadDataInvalidBudget", ".json");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), root.toString().getBytes());

        // Expect a DataLoadingException due to an invalid budget.
        assertThrows(DataLoadingException.class, () -> DataHandler.getInstance().loadData(tempFile.getAbsolutePath()));
    }

    @Test
    void testLoadDataMissingCurrency_expectNoThrows() throws IOException {
        // Create JSON missing the currency field.
        JSONObject root = new JSONObject();
        root.put("budget", 1500.0);
        // Currency is missing here.
        root.put("categories", new JSONArray());
        root.put("expenses", new JSONArray());

        File tempFile = File.createTempFile("testLoadDataMissingCurrency", ".json");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), root.toString().getBytes());

        // Expect handled exception due to missing currency information.
        assertAll(() -> DataHandler.getInstance().loadData(tempFile.getAbsolutePath()));
    }
}
