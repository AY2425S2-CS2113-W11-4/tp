package seedu.tripbuddy.storage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.framework.ExpenseManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
class DataHandlerTest {

    private static final int DEFAULT_BUDGET = 2333;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;


    @BeforeEach
    void setUp() {
        // Capture system output so we can verify Ui messages.
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Reset ExpenseManager state before each test.
        ExpenseManager.initExpenseManager(DEFAULT_BUDGET);
        ExpenseManager.setCategories(new HashSet<>());
    }

    @AfterEach
    void tearDown() {
        // Restore System.out after tests.
        System.setOut(originalOut);
    }

    @Test
    void round2DigitsTest_floor() {
        assertEquals(11.11, DataHandler.round2Digits(11.111), 1e-6);
        assertEquals(11.11, DataHandler.round2Digits(11.1119), 1e-6);
    }

    @Test
    void round2DigitsTest_ceil() {
        assertEquals(11.12, DataHandler.round2Digits(11.118), 1e-6);
    }

    @Test
    void testSaveData() throws IOException {
        // Set up ExpenseManager state.
        double budget = 1000.0;
        ExpenseManager.initExpenseManager(budget);
        // For testing, assume no expenses and no categories.
        ExpenseManager.setCategories(new HashSet<>());
        // ExpenseManager.getTotalExpense() should be 0 in this state.

        // Create a temporary file to save JSON data.
        File tempFile = File.createTempFile("testSaveData", ".json");
        tempFile.deleteOnExit();
        String path = tempFile.getAbsolutePath();

        // Call DataHandler.saveData.
        DataHandler.saveData(path);

        // Verify that Ui printed the expected message.
        String output = outContent.toString();
        assertTrue(output.contains("Saved data to file:"), "Output should mention saved data.");

        // Read the file content to verify JSON structure.
        String jsonContent = new String(Files.readAllBytes(tempFile.toPath()));
        JSONObject root = new JSONObject(jsonContent);

        // Check that the JSON contains the correct budget.
        assertEquals(budget, root.getDouble("budget"), 0.0001);
        // Categories array should be empty.
        JSONArray categoriesArr = root.getJSONArray("categories");
        assertEquals(0, categoriesArr.length());
        // Expenses array should be empty.
        JSONArray expensesArr = root.getJSONArray("expenses");
        assertEquals(0, expensesArr.length());
    }

    @Test
    void testLoadData() throws IOException {
        // Create a JSON object that mimics the saved data structure.
        JSONObject root = new JSONObject();
        double budget = 2000.0;
        root.put("budget", budget);
        double totalExpense = 150.0;
        root.put("totalExpense", totalExpense);

        // Prepare categories.
        JSONArray categoriesArr = new JSONArray();
        categoriesArr.put("Food");
        categoriesArr.put("Transport");
        root.put("categories", categoriesArr);

        // Prepare expenses.
        JSONArray expensesArr = new JSONArray();
        JSONObject expenseObj = new JSONObject();
        expenseObj.put("name", "Lunch");
        expenseObj.put("amount", 12.5);
        expenseObj.put("category", "Food");
        // Use a fixed date/time string for reproducibility.
        String dateTimeStr = "2025-04-05 12:00:00";
        expenseObj.put("dateTime", dateTimeStr);
        expensesArr.put(expenseObj);
        root.put("expenses", expensesArr);

        // Write the JSON to a temporary file.
        File tempFile = File.createTempFile("testLoadData", ".json");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), root.toString().getBytes());
        String path = tempFile.getAbsolutePath();

        // Clear the ExpenseManager state before loading.
        ExpenseManager.initExpenseManager(DEFAULT_BUDGET);
        ExpenseManager.setCategories(new HashSet<>());
        outContent.reset();

        // Call DataHandler.loadData.
        DataHandler.loadData(path);

        // After loading, verify that ExpenseManager has updated its state.
        assertEquals(budget, ExpenseManager.getBudget(), 0.0001);

        // Verify categories.
        List<String> categories = ExpenseManager.getCategories();
        assertTrue(categories.contains("Food"), "Categories should include 'Food'");
        assertTrue(categories.contains("Transport"), "Categories should include 'Transport'");

        // Verify expenses.
        List<Expense> expenses = ExpenseManager.getExpenses();
        assertEquals(1, expenses.size(), "There should be one expense loaded.");
        Expense loadedExpense = expenses.get(0);
        assertEquals("Lunch", loadedExpense.getName());
        assertEquals(12.5, loadedExpense.getAmount(), 0.0001);
        assertEquals("Food", loadedExpense.getCategory());
        assertEquals(dateTimeStr, loadedExpense.getDateTimeString());

        // Verify Ui output for loadData.
        String output = outContent.toString();
        assertTrue(output.contains("Loaded data from file:"), "Output should mention loaded data.");
    }
}
