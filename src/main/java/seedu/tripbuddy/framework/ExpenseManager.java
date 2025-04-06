package seedu.tripbuddy.framework;

import org.json.JSONException;
import org.json.JSONObject;
import seedu.tripbuddy.command.Command;
import seedu.tripbuddy.dataclass.Currency;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Has CRUD access to all user data.
 */
public class ExpenseManager {

    public static final int DEFAULT_BUDGET = 1000;

    private static ExpenseManager instance = null;

    private Currency baseCurrency;
    private double budget;
    private double totalExpense;
    private final HashSet<String> categories = new HashSet<>();
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final HashSet<String> expenseNames = new HashSet<>();

    private ExpenseManager(double budget) {
        assert budget > 0 : "Budget must be positive";
        this.budget = budget;
        this.totalExpense = 0;
        this.baseCurrency = Currency.SGD;
        clearExpensesAndCategories();
    }

    /**
     * Gets a singleton instance and sets the budget to {@code DEFAULT_BUDGET}.
     */
    public static ExpenseManager getInstance() {
        if (instance == null) {
            instance = new ExpenseManager(DEFAULT_BUDGET);
        }
        return instance;
    }

    /**
     * Gets a singleton instance and sets the budget to the given value.
     */
    public static ExpenseManager getInstance(double budget) {
        if (instance == null) {
            instance = new ExpenseManager(budget);
        }
        instance.setBudget(budget);
        return instance;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency newBaseCurrency) {
        baseCurrency = newBaseCurrency;
        Currency.setBaseCurrency(baseCurrency);
    }

    public double getBudget() {
        return budget;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getRemainingBudget() {
        return budget - totalExpense;
    }

    public List<String> getCategories() {
        return categories.stream().toList();
    }

    public List<Expense> getExpenses() {
        return List.copyOf(expenses);
    }


    /**
     * Truncates all existing expenses and categories without modifying budget
     * and base currency.
     */
    public void clearExpensesAndCategories() {
        expenses.clear();
        categories.clear();
        expenseNames.clear();
        totalExpense = 0;
    }

    public void setBudget(double budget) {
        assert budget > 0 : "Budget must be positive";
        this.budget = budget;
    }

    /**
     * Adds a new category name into {@code categories} if not exists.
     */
    public void createCategory(String categoryName) throws InvalidArgumentException {
        if (categoryName.isEmpty()) {
            throw new InvalidArgumentException("", "Category name should not be empty.");
        }
        if (categories.contains(categoryName)) {
            throw new InvalidArgumentException(categoryName, "Category name already exists.");
        }
        categories.add(categoryName);
    }

    /**
     * Adds a new {@link Expense} without category.
     */
    public void addExpense(String name, double amount) throws InvalidArgumentException {
        assert amount > 0 : "Amount must be positive";
        if (name.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        if (expenseNames.contains(name)) {
            throw new InvalidArgumentException(name, "Expense name already exists.");
        }
        Expense expense = new Expense(name, amount);
        expenses.add(expense);
        expenseNames.add(name);
        totalExpense += amount;
    }

    /**
     * Adds a new {@link Expense} with a specific category.
     * <ul>
     *     <li>A new category will be created if not exists.
     * </ul>
     */
    public void addExpense(String name, double amount, String categoryName) throws InvalidArgumentException {
        assert amount > 0 : "Amount must be positive";
        if (name.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        if (expenseNames.contains(name)) {
            throw new InvalidArgumentException(name, "Expense name already exists.");
        }
        if (!categories.contains(categoryName)) {
            createCategory(categoryName);
        }
        Expense expense = new Expense(name, amount, categoryName);
        expenses.add(expense);
        expenseNames.add(name);
        totalExpense += amount;
    }

    public void addExpense(JSONObject expObj) throws JSONException {
        Expense expense = Expense.fromJSON(expObj);
        String name = expense.getName();
        if (name.isEmpty()) {
            throw new JSONException("Expense name should not be empty.");
        }
        if (expenseNames.contains(name)) {
            throw new JSONException("Expense \"" + name + "\" already exists. Skipping");
        }

        double amount = expense.getAmount();
        if (amount <= 0) {
            throw new JSONException('"' + name + "\": Expense amount should be more than 0.");
        }
        if (amount > Command.MAX_INPUT_VAL) {
            throw new JSONException('"' + name + "\": Expense amount should be no more than " +
                    Command.MAX_INPUT_VAL);
        }

        String categoryName = expense.getCategory();
        if (categoryName != null) {
            categories.add(categoryName);
        }

        expenses.add(expense);
        expenseNames.add(name);
        totalExpense += amount;
    }

    public Expense getExpense(int id) throws InvalidArgumentException {
        if (id < 0 || id >= expenses.size()) {
            throw new InvalidArgumentException(Integer.toString(id), "id index out of bound");
        }
        return expenses.get(id);
    }

    public void deleteExpense(String expenseName) throws InvalidArgumentException {
        for (Expense expense : expenses) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                expenses.remove(expense);
                expenseNames.remove(expenseName);
                totalExpense -= expense.getAmount();
                return;
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense name not found.");
    }

    public List<Expense> getExpensesByCategory(String category) throws InvalidArgumentException {
        if (!categories.contains(category)) {
            throw new InvalidArgumentException(category, "Category name not found.");
        }

        ArrayList<Expense> ret = new ArrayList<>();
        for (Expense expense : expenses) {
            if (category.equals(expense.getCategory())) {
                ret.add(expense);
            }
        }
        return ret;
    }

    public void setExpenseCategory(String expenseName, String category) throws InvalidArgumentException {
        if (expenseName.isEmpty()) {
            throw new JSONException("Expense name should not be empty.");
        }
        for (Expense expense : expenses) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                expense.setCategory(category);
                categories.add(category);
                return;
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense name not found.");
    }

    public Expense getMaxExpense() throws InvalidArgumentException {
        if (expenses.isEmpty()) {
            throw new InvalidArgumentException("No expenses available");
        }
        Expense maxExpense = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() > maxExpense.getAmount()) {
                maxExpense = expense;
            }
        }
        return maxExpense;
    }

    public Expense getMinExpense() throws InvalidArgumentException {
        if (expenses.isEmpty()) {
            throw new InvalidArgumentException("No expenses available");
        }
        Expense minExpense = expenses.get(0);
        for (Expense expense : expenses) {
            if (expense.getAmount() < minExpense.getAmount()) {
                minExpense = expense;
            }
        }
        return minExpense;
    }

    public List<Expense> getExpensesByDateRange(LocalDateTime start, LocalDateTime end) {
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            LocalDateTime expenseDateTime = expense.getDateTime();
            if ((expenseDateTime.isEqual(start) || expenseDateTime.isAfter(start))
                    && (expenseDateTime.isEqual(end) || expenseDateTime.isBefore(end))) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    public void setCategories(Set<String> loadedCategories) {
        categories.clear();
        categories.addAll(loadedCategories);
    }
  
    public List<Expense> getExpensesBySearchword(String searchword) {
        ArrayList<Expense> matchingExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getName().toLowerCase().contains(searchword.toLowerCase())) {
                matchingExpenses.add(expense);
            }
        }
        return matchingExpenses;
    }
}
