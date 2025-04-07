package seedu.tripbuddy.dataclass;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.tripbuddy.framework.ExpenseManager;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExpenseTest {

    @BeforeEach
    void initExpenseManager() {
        ExpenseManager.getInstance().clearExpensesAndCategories();
    }

    @Test
    void testConstructorWithoutCategory() {
        String name = "Coffee";
        double amount = 3.50;
        Expense expense = new Expense(name, amount);

        assertEquals(name, expense.getName());
        assertEquals(amount, expense.getAmount(), 0.0001);
        assertNull(expense.getCategory());
        // Since the date is set to now, verify it is within a reasonable time range.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expenseTime = expense.getDateTime();
        assertTrue(expenseTime.isBefore(now.plusSeconds(1)) && expenseTime.isAfter(now.minusSeconds(1)));
    }

    @Test
    void testConstructorWithCategory() {
        String name = "Lunch";
        double amount = 10.0;
        String category = "Food";
        Expense expense = new Expense(name, amount, category);

        assertEquals(name, expense.getName());
        assertEquals(amount, expense.getAmount(), 0.0001);
        assertEquals(category, expense.getCategory());
        assertNotNull(expense.getDateTime());
    }

    @Test
    void testConstructorWithDateTimeString() {
        String name = "Dinner";
        double amount = 20.0;
        String category = "Food";
        String dateTimeStr = "2025-04-05 19:30:00";
        Expense expense = new Expense(name, amount, category, dateTimeStr);

        assertEquals(name, expense.getName());
        assertEquals(amount, expense.getAmount(), 0.0001);
        assertEquals(category, expense.getCategory());
        // The date/time string should match exactly.
        assertEquals(dateTimeStr, expense.getDateTimeString());
    }

    @Test
    void testToStringWithoutCategory() {
        String name = "Snack";
        double amount = 5.25;
        // Create an Expense without a category.
        Expense expense = new Expense(name, amount);
        // For predictable output, override the dateTime.
        String testDateTime = "2025-04-05 15:00:00";
        LocalDateTime dateTime = LocalDateTime.parse(testDateTime, Expense.FORMATTER);
        expense.setDateTime(dateTime);

        String expected = "name: " + name + ", amount: " + String.format("%.2f", amount) +
                " SGD, date: " + testDateTime;
        assertEquals(expected, expense.toString());
    }

    @Test
    void testToStringWithCategory() {
        String name = "Dinner";
        double amount = 20.0;
        String category = "Food";
        // Create an Expense with a category.
        Expense expense = new Expense(name, amount, category);
        String testDateTime = "2025-04-05 20:00:00";
        LocalDateTime dateTime = LocalDateTime.parse(testDateTime, Expense.FORMATTER);
        expense.setDateTime(dateTime);

        String expected = "name: " + name + ", amount: " + String.format("%.2f", amount)
                + " SGD, category: " + category + ", date: " + testDateTime;
        assertEquals(expected, expense.toString());
    }

    @Test
    void testSettersAndGetters() {
        Expense expense = new Expense("Test", 0);
        expense.setName("New Name");
        expense.setAmount(100.0);
        expense.setCategory("TestCategory");
        String testDateTime = "2025-04-05 21:00:00";
        LocalDateTime dateTime = LocalDateTime.parse(testDateTime, Expense.FORMATTER);
        expense.setDateTime(dateTime);

        assertEquals("New Name", expense.getName());
        assertEquals(100.0, expense.getAmount(), 0.0001);
        assertEquals("TestCategory", expense.getCategory());
        assertEquals(testDateTime, expense.getDateTimeString());
    }

    @Test
    void testFromJSONWithCategory() {
        String name = "Bus Ticket";
        double amount = 2.50;
        String category = "Transport";
        String dateTimeStr = "2025-04-05 08:30:00";

        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("amount", amount);
        json.put("category", category);
        json.put("dateTime", dateTimeStr);

        Expense expense = assertDoesNotThrow(() -> Expense.fromJSON(json));
        assertEquals(name, expense.getName());
        assertEquals(amount, expense.getAmount(), 0.0001);
        assertEquals(category, expense.getCategory());
        assertEquals(dateTimeStr, expense.getDateTimeString());
    }

    @Test
    void testFromJSONWithoutCategory() {
        String name = "Train Ticket";
        double amount = 15.75;
        String dateTimeStr = "2025-04-05 09:15:00";

        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("amount", amount);
        json.put("dateTime", dateTimeStr);
        // Note: "category" is omitted so it should default to null.

        Expense expense = assertDoesNotThrow(() -> Expense.fromJSON(json));
        assertEquals(name, expense.getName());
        assertEquals(amount, expense.getAmount(), 0.0001);
        assertNull(expense.getCategory());
        assertEquals(dateTimeStr, expense.getDateTimeString());
    }

    @Test
    public void setDateTime_validDateTime_success() {
        Expense expense = new Expense("flight", 300.00);
        LocalDateTime newTime = LocalDateTime.of(2024, 3, 30, 15, 0);
        expense.setDateTime(newTime);
        assertEquals(newTime, expense.getDateTime());
    }
}
