package seedu.tripbuddy.framework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.InvalidArgumentException;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

class ExpenseManagerTest {

    @BeforeEach
    void initExpenseManager() {
        ExpenseManager expenseManager = ExpenseManager.getInstance();
        expenseManager.clearExpensesAndCategories();
    }

    @Test
    void initBudgetTest() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        assertEquals(2333, expenseManager.getBudget());
    }

    @Test
    void setBudgetTest() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        expenseManager.setBudget(233);
        assertEquals(233, expenseManager.getBudget());
    }

    @Test
    void createCategoryTest_sameName_expectInvalidArgumentException() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        assertAll(
                () -> expenseManager.createCategory("a"),
                () -> expenseManager.createCategory("b")
        );
        assertThrows(InvalidArgumentException.class, () -> expenseManager.createCategory("a"));
    }

    @Test
    void addExpenseTest_sameNameNoCategory_expectInvalidArgumentException() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        assertAll(
                () -> expenseManager.addExpense("a", 1),
                () -> expenseManager.addExpense("b", 1)
        );
        assertThrows(InvalidArgumentException.class, () -> expenseManager.addExpense("a", 1));
        assertThrows(InvalidArgumentException.class,
                () -> expenseManager.addExpense("a", 1, "b"));
    }

    @Test
    void addExpenseTest_sameNameHasCategory_expectInvalidArgumentException() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        assertAll(
                () -> expenseManager.addExpense("a", 1, "test"),
                () -> expenseManager.addExpense("b", 1)
        );
        assertThrows(InvalidArgumentException.class, () -> expenseManager.addExpense("a", 1));
        assertThrows(InvalidArgumentException.class,
                () -> expenseManager.addExpense("a", 1, "b"));
    }

    @Test
    void addDeleteExpenseTest_add3Delete1() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        expenseManager.addExpense("a", 1);
        expenseManager.addExpense("b", 2);
        expenseManager.deleteExpense("a");
        expenseManager.addExpense("c", 3);

        List<Expense> expenses = expenseManager.getExpenses();
        // Debug print (optional)
        for (Expense expense : expenses) {
            System.err.println(expense);
        }
        assertEquals(2, expenses.size());
    }

    @Test
    void addExpenseTest_categoryNotExists() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(2333);
        assertArrayEquals(new String[]{}, expenseManager.getCategories().toArray());

        expenseManager.addExpense("lunch", 100, "food");
        System.err.println(Arrays.toString(expenseManager.getCategories().toArray()));
        assertArrayEquals(new String[]{"food"}, expenseManager.getCategories().toArray(), "1");

        expenseManager.addExpense("lol", 233);
        System.err.println(Arrays.toString(expenseManager.getCategories().toArray()));
        assertArrayEquals(new String[]{"food"}, expenseManager.getCategories().toArray(), "2");

        expenseManager.addExpense("a", 10, "sth");
        System.err.println(Arrays.toString(expenseManager.getCategories().toArray()));
        assertEquals(2, expenseManager.getCategories().size(), "3");

        for (Expense expense : expenseManager.getExpenses()) {
            System.err.println(expense);
        }
        assertEquals(3, expenseManager.getExpenses().size(), "4");
    }

    @Test
    void setExpenseCategoryTest() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(200);
        expenseManager.addExpense("testExpense", 50);
        expenseManager.setExpenseCategory("testExpense", "utilities");
        Expense expense = expenseManager.getExpense(0);
        assertEquals("utilities", expense.getCategory());
    }

    @Test
    void getMaxExpenseTest() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);
        expenseManager.addExpense("expense1", 100);
        expenseManager.addExpense("expense2", 300);
        expenseManager.addExpense("expense3", 200);

        Expense maxExpense = expenseManager.getMaxExpense();
        assertEquals("expense2", maxExpense.getName());
        assertEquals(300.00, maxExpense.getAmount(), 0.001);
    }

    @Test
    void getMinExpenseTest() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);
        expenseManager.addExpense("expense1", 100);
        expenseManager.addExpense("expense2", 300);
        expenseManager.addExpense("expense3", 200);

        Expense minExpense = expenseManager.getMinExpense();
        assertEquals("expense1", minExpense.getName());
        assertEquals(100.00, minExpense.getAmount(), 0.001);
    }

    @Test
    void getMaxExpense_emptyExpenses_throwsException() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);
        assertThrows(InvalidArgumentException.class, expenseManager::getMaxExpense);
    }

    @Test
    void getMinExpense_emptyExpenses_throwsException() {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);
        assertThrows(InvalidArgumentException.class, expenseManager::getMinExpense);
    }

    @Test
    void getExpensesByDateRangeTest() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);

        expenseManager.addExpense("expense1", 100);
        expenseManager.addExpense("expense2", 200);
        expenseManager.addExpense("expense3", 300);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        expenseManager.getExpenses().get(0).setDateTime(LocalDateTime.parse("2025-04-01 10:00:00", formatter));
        expenseManager.getExpenses().get(1).setDateTime(LocalDateTime.parse("2025-04-03 15:30:00", formatter));
        expenseManager.getExpenses().get(2).setDateTime(LocalDateTime.parse("2025-04-05 20:00:00", formatter));

        LocalDateTime start = LocalDateTime.parse("2025-04-01 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2025-04-04 23:59:59", formatter);

        List<Expense> filteredExpenses = expenseManager.getExpensesByDateRange(start, end);

        assertEquals(2, filteredExpenses.size(), "Should return 2 expenses within the date range");

        boolean containsExp1 = filteredExpenses.stream().anyMatch(exp -> exp.getName().equals("expense1"));
        boolean containsExp2 = filteredExpenses.stream().anyMatch(exp -> exp.getName().equals("expense2"));
        boolean containsExp3 = filteredExpenses.stream().anyMatch(exp -> exp.getName().equals("expense3"));

        assertTrue(containsExp1, "Filtered expenses should contain expense1");
        assertTrue(containsExp2, "Filtered expenses should contain expense2");
        assertFalse(containsExp3, "Filtered expenses should not contain expense3");
    }

    @Test
    void getExpensesByDateRangeEmptyResultTest() throws InvalidArgumentException {
        ExpenseManager expenseManager = ExpenseManager.getInstance(1000);

        expenseManager.addExpense("expenseA", 100);
        expenseManager.addExpense("expenseB", 200);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        expenseManager.getExpenses().get(0).setDateTime(LocalDateTime.parse("2025-03-25 09:00:00", formatter));
        expenseManager.getExpenses().get(1).setDateTime(LocalDateTime.parse("2025-03-30 18:00:00", formatter));

        LocalDateTime start = LocalDateTime.parse("2025-04-01 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2025-04-05 23:59:59", formatter);

        List<Expense> filteredExpenses = expenseManager.getExpensesByDateRange(start, end);

        assertEquals(0, filteredExpenses.size(), "Should return 0 expenses since none are within the date range");
    }
}
