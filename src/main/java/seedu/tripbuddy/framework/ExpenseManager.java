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


    /**
     * Private constructor for singleton pattern. Initializes with a given budget.
     *
     * @param budget Initial budget to set.
     */
    private ExpenseManager(double budget) {
        assert budget > 0 : "Budget must be positive";
        this.budget = budget;
        this.totalExpense = 0;
        this.baseCurrency = Currency.SGD;
        clearExpensesAndCategories();
    }

    /**
     * Gets a singleton instance and sets the budget to {@code DEFAULT_BUDGET}.
     *
     * @return The singleton instance of {@code ExpenseManager}.
     */
    public static ExpenseManager getInstance() {
        if (instance == null) {
            instance = new ExpenseManager(DEFAULT_BUDGET);
        }
        return instance;
    }

    /**
     * Gets a singleton instance and sets the budget to the given value.
     *
     * @param budget The budget to initialize with.
     * @return The singleton instance of {@code ExpenseManager}.
     */
    public static ExpenseManager getInstance(double budget) {
        if (instance == null) {
            instance = new ExpenseManager(budget);
        }
        instance.setBudget(budget);
        return instance;
    }

    /**
     * Gets the current base currency.
     *
     * @return The base {@link Currency}.
     */
    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * Sets the base currency.
     *
     * @param newBaseCurrency The new base {@link Currency}.
     */
    public void setBaseCurrency(Currency newBaseCurrency) {
        baseCurrency = newBaseCurrency;
        Currency.setBaseCurrency(baseCurrency);
    }

    /**
     * Gets the current budget.
     *
     * @return The current budget amount.
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Gets the total amount spent.
     *
     * @return Total expense amount.
     */
    public double getTotalExpense() {
        return totalExpense;
    }

    /**
     * Sets the total amount spent.
     *
     * @param totalExpense New total expense value.
     */
    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    /**
     * Gets the remaining budget.
     *
     * @return Budget remaining after subtracting total expenses.
     */
    public double getRemainingBudget() {
        return budget - totalExpense;
    }

    /**
     * Gets the list of category names.
     *
     * @return A list of categories.
     */
    public List<String> getCategories() {
        return categories.stream().toList();
    }

    /**
     * Gets a list of all recorded expenses.
     *
     * @return A copy of the expense list.
     */
    public List<Expense> getExpenses() {
        return List.copyOf(expenses);
    }

    /**
     * Clears all expenses and categories while retaining budget and currency.
     */
    public void clearExpensesAndCategories() {
        expenses.clear();
        categories.clear();
        totalExpense = 0;
    }

    /**
     * Sets the user-defined budget.
     *
     * @param budget The budget amount to set.
     */
    public void setBudget(double budget) {
        assert budget > 0 : "Budget must be a positive integer";
        this.budget = budget;
    }

    /**
     * Adds a new category.
     *
     * @param categoryName Name of the category to add.
     * @throws InvalidArgumentException If the name is empty or already exists.
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
     * Adds a new expense without a category.
     *
     * @param name   Expense name.
     * @param amount Expense amount.
     * @throws InvalidArgumentException If name is invalid or duplicate, or amount is invalid.
     */
    public void addExpense(String name, double amount) throws InvalidArgumentException {
        assert amount > 0 : "Amount must be positive";
        if (name.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }

        // check whether the expense name is already used
        for (Expense expense : expenses) {
            if (expense.getName().equals(name)) {
                throw new InvalidArgumentException(name, "Expense name already exists.");
            }
        }
        Expense expense = new Expense(name, amount);
        expenses.add(expense);
        totalExpense += amount;
    }

    /**
     * Adds a new expense with a category.
     *
     * @param name         Expense name.
     * @param amount       Expense amount.
     * @param categoryName Expense category.
     * @throws InvalidArgumentException If name or amount is invalid.
     */
    public void addExpense(String name, double amount, String categoryName) throws InvalidArgumentException {
        assert amount > 0 : "Amount must be positive";
        if (name.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        // check whether the expense name is already used
        for (Expense expense : expenses) {
            if (expense.getName().equals(name)) {
                throw new InvalidArgumentException(name, "Expense name already exists.");
            }
        }
        if (!categories.contains(categoryName)) {
            createCategory(categoryName);
        }
        Expense expense = new Expense(name, amount, categoryName);
        expenses.add(expense);
        totalExpense += amount;
    }

    /**
     * Adds a new expense from a JSON object.
     *
     * @param expObj JSON object representing an expense.
     * @throws JSONException If any data is invalid or duplicate.
     */
    public void addExpense(JSONObject expObj) throws JSONException {
        Expense expense = Expense.fromJSON(expObj);
        String name = expense.getName();
        if (name.isEmpty()) {
            throw new JSONException("Expense name should not be empty.");
        }
        for (Expense exp : expenses) {
            if (exp.getName().equals(name)) {
                throw new JSONException("Expense name already exists.");
            }
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
        totalExpense += amount;
    }

    /**
     * Retrieves an expense by its index.
     *
     * @param id Index of the expense.
     * @return The expense at the given index.
     * @throws InvalidArgumentException If index is out of bounds.
     */
    public Expense getExpense(int id) throws InvalidArgumentException {
        if (id < 0 || id >= expenses.size()) {
            throw new InvalidArgumentException(Integer.toString(id), "id index out of bound");
        }
        return expenses.get(id);
    }

    /**
     * Deletes an expense by name.
     *
     * @param expenseName Name of the expense to delete.
     * @throws InvalidArgumentException If expense name does not exist.
     */
    public void deleteExpense(String expenseName) throws InvalidArgumentException {
        for (Expense expense : expenses) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                expenses.remove(expense);
                totalExpense -= expense.getAmount();
                return;
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense name not found.");
    }

    /**
     * Gets expenses under a specific category.
     *
     * @param category Category name.
     * @return List of expenses in that category.
     * @throws InvalidArgumentException If category doesn't exist.
     */
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

    /**
     * Assigns a category to an expense.
     *
     * @param expenseName Expense name.
     * @param category    Category to assign.
     * @throws InvalidArgumentException If the expense doesn't exist.
     */
    public void setExpenseCategory(String expenseName, String category) throws InvalidArgumentException {
        if (expenseName.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        if (category.isEmpty()) {
            throw new InvalidArgumentException("", "Category name should not be empty.");
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

    /**
     * Gets the expense with the maximum amount.
     *
     * @return The expense with the highest amount.
     * @throws InvalidArgumentException If there are no expenses.
     */
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

    /**
     * Gets the expense with the minimum amount.
     *
     * @return The expense with the lowest amount.
     * @throws InvalidArgumentException If there are no expenses.
     */
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

    /**
     * Filters expenses within the given date range.
     *
     * @param start Start datetime.
     * @param end   End datetime.
     * @return List of expenses within the range.
     */
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

    /**
     * Replaces the current categories with a loaded set.
     *
     * @param loadedCategories Categories to load into memory.
     */
    public void setCategories(Set<String> loadedCategories) {
        categories.clear();
        categories.addAll(loadedCategories);
    }

    /**
     * Retrieves expenses whose names contain the given search word.
     *
     * @param searchword The keyword to search in expense names.
     * @return List of matching expenses.
     */
    public List<Expense> getExpensesBySearchword(String searchword) {
        ArrayList<Expense> matchingExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getName().toLowerCase().contains(searchword.toLowerCase())) {
                matchingExpenses.add(expense);
            }
        }
        return matchingExpenses;
    }

    /**
     * Deletes a category from the list of categories if it exists and has no associated expenses.
     *
     * @param category The name of the category to delete.
     * @return {@code true} if the category was successfully deleted (i.e., it existed and had no associated expenses),
     *         {@code false} if the category has existing expenses and could not be deleted.
     * @throws InvalidArgumentException if the category name is empty or does not exist in the current category list.
     */
    public boolean deleteCategory(String category) throws InvalidArgumentException {
        if (category.isEmpty()) {
            throw new InvalidArgumentException("", "Category name should not be empty.");
        }
        if (!categories.contains(category)) {
            throw new InvalidArgumentException(category, "Category with name `" + category + "` does not exist.");
        }
        List<Expense> expenses = getExpensesByCategory(category);
        if (expenses.isEmpty()) {
            categories.remove(category);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the category of an expense, so the expense no longer belongs to any category.
     *
     * @param expenseName The name of the expense to clear the category of.
     * @return The name of the old category that the expense was just cleared of. Empty string if expense did not
     *          previously belong to any category.
     * @throws InvalidArgumentException if the category name is empty or does not exist in the current category list.
     */
    public String clearCategory(String expenseName) throws InvalidArgumentException {
        if (expenseName.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        for (Expense expense : expenses) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                boolean belongsToCategory = expense.belongsToCategory();
                if (belongsToCategory) {
                    String oldCategory = expense.getCategory();
                    expense.clearCategory();
                    return oldCategory;
                } else {
                    return "";
                }
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense with name `" + expenseName + "` does not exist.");
    }

    /**
     * Edits the amount associated with an expense, overriding the previous amount.
     *
     * @param expenseName The name of the expense to edit.
     * @param amount The new amount to set for the expense. Must be a non-negative value.
     * @throws InvalidArgumentException if the expense name is empty, the amount is negative,
     *         or no expense with the given name exists.
     */
    public void editExpenseAmount(String expenseName, double amount) throws InvalidArgumentException {
        if (expenseName.isEmpty()) {
            throw new InvalidArgumentException("", "Expense name should not be empty.");
        }
        if (amount <= 0) {
            throw new InvalidArgumentException("", "Amount should be a positive integer.");
        }
        for (Expense expense : expenses) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                double oldTotalExpense = getTotalExpense();
                double oldExpenseAmount = expense.getAmount();
                expense.setAmount(amount);
                setTotalExpense(oldTotalExpense + (amount - oldExpenseAmount));
                return;
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense with name `" + expenseName + "` does not exist.");
    }
}
