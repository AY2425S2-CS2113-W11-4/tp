package seedu.tripbuddy.framework;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.dataclass.Currency;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.InvalidArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CommandHandlerTest {

    private static final Logger LOGGER = Logger.getLogger(CommandHandlerTest.class.getName());
    private static final int DEFAULT_BUDGET = 2333;
    private CommandHandler commandHandler;
    private ExpenseManager expenseManager;

    @BeforeEach
    void setUp() {
        expenseManager = ExpenseManager.getInstance(DEFAULT_BUDGET);
        expenseManager.clearExpensesAndCategories();
        commandHandler = new CommandHandler();

    }


    @Test
    void addExpense_a1() throws InvalidArgumentException {
        String message = commandHandler.handleAddExpense("a", 1);
        assertEquals("Expense a added successfully.\n" +
                        "Your remaining budget is $" + String.format("%.2f", DEFAULT_BUDGET - 1.0) + " SGD.",
                message);
    }

    @Test
    void addExpense_negativeAmount_expectInvalidArgumentException() {
        // Assuming negative amounts trigger an assertion failure in the new design.
        assertThrows(AssertionError.class,
                () -> commandHandler.handleAddExpense("a", -1));
    }

    @Test
    void setBudget_set135() throws InvalidArgumentException {
        String message = commandHandler.handleSetBudget(135);
        assertEquals("Your budget has been set to 135.00 SGD.", message);
    }

    @Test
    void handleViewBudgetTest_positiveRemaining() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(100);
        expenseManager.addExpense("item1", 40);
        String message = commandHandler.handleViewBudget();
        assertTrue(message.contains("remaining budget of 60.00 SGD."));
    }

    @Test
    void handleViewBudgetTest_exceededBudget() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(100);
        expenseManager.addExpense("item1", 150);
        String message = commandHandler.handleViewBudget();
        LOGGER.log(Level.INFO, message);
        assertTrue(message.contains("exceeded your budget by 50"));
    }

    @Test
    void handleCreateCategoryTest() throws InvalidArgumentException {
        String message = commandHandler.handleCreateCategory("food");
        assertEquals("Successfully created category: food.", message);
        assertTrue(expenseManager.getCategories().contains("food"));
    }

    @Test
    void handleSetCategoryTest() throws InvalidArgumentException {
        expenseManager.addExpense("meal", 50);
        String message = commandHandler.handleSetCategory("meal", "dining");
        assertEquals("Successfully set category for meal to dining.", message);
        assertEquals("dining", expenseManager.getExpense(0).getCategory());
    }

    @Test
    void handleDeleteExpenseTest() throws InvalidArgumentException {
        expenseManager.addExpense("deleteTest", 20);
        String message = commandHandler.handleDeleteExpense("deleteTest");
        assertTrue(message.contains("Expense deleteTest deleted successfully."));
        assertEquals(0, expenseManager.getExpenses().size());
    }

    @Test
    void handleMaxExpenseTest() throws InvalidArgumentException {
        expenseManager.addExpense("a", 10);
        expenseManager.addExpense("b", 20);
        Expense expense = new Expense("b", 20);
        String expected = "Maximum expense: " + expense;
        String actual = commandHandler.handleMaxExpense();
        assertEquals(expected, actual);
    }

    @Test
    void handleMinExpenseTest() throws InvalidArgumentException {
        expenseManager.addExpense("a", 10);
        expenseManager.addExpense("b", 20);
        Expense expense = new Expense("a", 10);
        String expected = "Minimum expense: " + expense;
        String actual = commandHandler.handleMinExpense();
        assertEquals(expected, actual);
    }

    @Test
    void handleMaxExpense_noExpenses_throwsException() {
        assertThrows(InvalidArgumentException.class, commandHandler::handleMaxExpense);
    }

    @Test
    void handleMinExpense_noExpenses_throwsException() {
        assertThrows(InvalidArgumentException.class, commandHandler::handleMinExpense);
    }

    @Test
    void handleListExpense_totalAmountSpent() throws InvalidArgumentException {
        expenseManager.addExpense("a", 50);
        expenseManager.addExpense("b", 100);
        Expense expense1 = new Expense("a", 50);
        Expense expense2 = new Expense("b", 100);
        String expected = "Here is a list of your past expenses: " +
                "\n - " + expense1 +
                "\n - " + expense2 +
                "\nTotal amount spent: 150.00 SGD.";
        String actual = commandHandler.handleListExpense(null);
        assertEquals(expected, actual);
    }

    @Test
    void handleSearchExpense_matchingExpenses() throws InvalidArgumentException {
        expenseManager.addExpense("lunch", 20);
        expenseManager.addExpense("dinner", 40);
        expenseManager.addExpense("lunch-buffet", 30);
        expenseManager.addExpense("transport", 15);
        Expense expense1 = new Expense("lunch", 20);
        Expense expense2 = new Expense("lunch-buffet", 30);
        String expected = "Expenses that matched your search word 'lunch':" +
                "\n - " + expense1 +
                "\n - " + expense2;
        String actual = commandHandler.handleSearch("lunch");
        assertEquals(expected, actual);
    }

    @Test
    void handleSearchExpense_noMatchingExpenses() throws InvalidArgumentException {
        expenseManager.addExpense("lunch", 20);
        expenseManager.addExpense("dinner", 40);
        expenseManager.addExpense("transport", 15);
        String expected = "There are no expenses that matched your search word: shopping.";
        String actual = commandHandler.handleSearch("shopping");
        assertEquals(expected, actual);
    }

    @Test
    void handleSearchExpense_emptyExpenseList() {
        String expected = "There are no expenses that matched your search word: shirt.";
        String actual = commandHandler.handleSearch("shirt");
        assertEquals(expected, actual);
    }

    @Test
    public void getCategories_returnCorrectList() throws InvalidArgumentException {
        expenseManager.addExpense("lunch", 20, "food");
        expenseManager.addExpense("dinner", 40, "food");
        expenseManager.addExpense("grab", 15, "transport");
        List<String> categories = expenseManager.getCategories();
        assertEquals(2, categories.size());
        assertTrue(categories.contains("food"));
        assertTrue(categories.contains("transport"));
    }

    @Test
    public void getCategories_emptySet_returnsEmptyList() {
        List<String> categories = expenseManager.getCategories();
        assertEquals(0, categories.size());
    }

    @Test
    public void clear_returnsEmptyList() throws InvalidArgumentException {
        expenseManager.addExpense("greek-meal", 20, "food");
        expenseManager.addExpense("mediterranean-meal", 30, "food");
        expenseManager.addExpense("grab", 15, "transport");
        commandHandler.handleClearAll();
        List<String> categories = expenseManager.getCategories();
        List<Expense> expenses = expenseManager.getExpenses();
        assertEquals(0, categories.size());
        assertEquals(0, expenses.size());
    }

    @Test
    public void viewCurrency_returnsEmptyList() {
        assertNotEquals(null, commandHandler.handleViewCurrency());
    }

    @Test
    public void setBaseCurrency_correct() throws InvalidArgumentException {
        try {
            commandHandler.handleSetBaseCurrency("USD");
        } catch (InvalidArgumentException e) {
            fail();
        }
        Currency baseCurrency = expenseManager.getBaseCurrency();
        assertEquals(Currency.USD.getRate(), baseCurrency.getRate());
        assertNotEquals(Currency.SGD.getRate(), baseCurrency.getRate());
    }

    @Test
    public void setBaseCurrency_incorrect() {
        assertThrows(InvalidArgumentException.class, () -> commandHandler.handleSetBaseCurrency("XXX"));
    }

    @Test
    public void testSetTime_validExpense_success() throws InvalidArgumentException {
        String name = "test-dinner";
        double amount = 25.0;
        expenseManager.addExpense(name, amount);
        String newTimestampStr = "2024-03-15 19:30:00";
        LocalDateTime expectedTime = LocalDateTime.parse(newTimestampStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String result = commandHandler.handleSetTime(name, newTimestampStr);
        Expense updatedExpense = expenseManager.getExpense(0);
        assertEquals(expectedTime, updatedExpense.getDateTime());
        assertTrue(result.contains("Updated timestamp for"),
                "The result message should contain 'Updated timestamp for'");
    }

    @Test
    public void testSetTime_nonexistentExpense_throwsException() {
        String name = "ghost-expense";
        String timestampStr = "2024-01-01 10:00:00";
        InvalidArgumentException thrown = assertThrows(InvalidArgumentException.class, () ->
                commandHandler.handleSetTime(name, timestampStr));
        assertEquals("Expense name not found.", thrown.getMessage(),
                "The exception message should be 'Expense name not found.'");
    }

    @Test
    public void testSetTime_invalidTimestampFormat_throwsException() throws InvalidArgumentException {
        String name = "test-breakfast";
        expenseManager.addExpense(name, 10.0);
        String invalidTime = "15th March, 2024";
        assertThrows(DateTimeParseException.class, () ->
                commandHandler.handleSetTime(name, invalidTime));
    }
}
