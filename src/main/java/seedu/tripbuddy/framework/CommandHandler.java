package seedu.tripbuddy.framework;

import seedu.tripbuddy.dataclass.Currency;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.exception.InvalidArgumentException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;

/**
 * Handles user commands and returns result messages.
 * Coordinates actions with the {@link ExpenseManager}.
 */
public class CommandHandler {

    private static CommandHandler instance = null;
    private final ExpenseManager expenseManager;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private CommandHandler() {
        this.expenseManager = ExpenseManager.getInstance();
    }

    /**
     * Returns the singleton instance of {@code CommandHandler}.
     */
    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    /**
     * Returns a full tutorial message listing all commands.
     */
    public String handleTutorial() {
        return """
                Welcome to the tutorial of TripBuddy!
                
                Format Guidelines:
                - Square brackets [] indicate optional elements.
                - AMOUNT must be a positive number.
                
                Here are the commands you can use:
                
                tutorial
                        - Displays a message explaining features.
                set-base-currency CURRENCY
                        - It sets the new currency.
                        - By default, the base currency is SGD.
                view-currency
                        - Displays the actual rates between currencies with the base currency.
                set-budget AMOUNT
                        - Set your total trip budget.
                        - Default budget is 1000 in base currency.
                view-budget
                        - Check your remaining budget.
                add-expense EXPENSE_NAME -a AMOUNT [-c CATEGORY]
                        - Add a new expense.
                delete-expense EXPENSE_NAME
                        - Remove an expense by name.
                list-expense [CATEGORY]
                        - Show all expenses, or expenses under a category if CATEGORY is given.
                        - Calculate sum of recorded expenses.
                search SEARCHWORD
                        - Displays expenses that include the given search word.
                max-expense
                        - Display an expense with the highest amount.
                min-expense
                        - Display an expense with the lowest amount.
                filter-date -f yyyy-MM-dd HH:mm:ss -t yyyy-MM-dd HH:mm:ss
                        - Get all expenses within date range, inclusive.
                create-category CATEGORY
                        - Create a new expense category.
                set-category EXPENSE_NAME -c CATEGORY
                        - Assign an expense to a category.
                view-categories
                        - Displays all categories.
                set-time EXPENSE_NAME -t yyyy-MM-dd HH:mm:ss
                        - Updates the timestamp for an existing expense.
                clear
                        - Clears all past expenses and categories.
                quit
                        - Exits the program.
                
                Enjoy tracking your expenses with TripBuddy!""";
    }

    /**
     * Displays the user's budget, total spent, and remaining amount.
     */
    public String handleViewBudget() {
        double budget = expenseManager.getBudget();
        double totalExpense = expenseManager.getTotalExpense();
        double remainingBudget = budget - totalExpense;
        Currency baseCurrency = expenseManager.getBaseCurrency();
        if (remainingBudget > 0) {
            return "The original budget you set was " + baseCurrency.getFormattedAmount(budget) +
                    ".\nSo far, you have spent " + baseCurrency.getFormattedAmount(totalExpense) +
                    ".\nThis leaves you with a remaining budget of " +
                    baseCurrency.getFormattedAmount(remainingBudget) + ".";
        }
        return "The original budget you set was " + baseCurrency.getFormattedAmount(budget) +
                ".\nSo far, you have spent " + baseCurrency.getFormattedAmount(totalExpense) +
                ".\nUh oh! You have exceeded your budget by " +
                baseCurrency.getFormattedAmount(-remainingBudget) +
                ".\nConsider adjusting your budget to get back on track.";
    }

    /**
     * Sets a new budget amount.
     *
     * @param budget the budget value to set
     * @return confirmation message
     */
    public String handleSetBudget(double budget) throws InvalidArgumentException {
        assert budget > 0;
        expenseManager.setBudget(budget);
        Currency baseCurrency = expenseManager.getBaseCurrency();
        return "Your budget has been set to " + baseCurrency.getFormattedAmount(budget) + ".";
    }

    /**
     * Creates a new category.
     *
     * @param category the category name
     * @return confirmation message
     */
    public String handleCreateCategory(String category) throws InvalidArgumentException {
        expenseManager.createCategory(category);
        return "Successfully created category: " + category + ".";
    }

    /**
     * Assigns a category to an existing expense.
     *
     * @param expenseName name of the expense
     * @param category    category to assign
     * @return confirmation message
     */
    public String handleSetCategory(String expenseName, String category) throws InvalidArgumentException {
        expenseManager.setExpenseCategory(expenseName, category);
        return "Successfully set category for " + expenseName + " to " + category + ".";
    }

    /**
     * Deletes an expense by name.
     *
     * @param expenseName name of the expense to delete
     * @return confirmation message
     */
    public String handleDeleteExpense(String expenseName) throws InvalidArgumentException {
        expenseManager.deleteExpense(expenseName);
        return "Expense " + expenseName + " deleted successfully.\nYour remaining budget is " +
                expenseManager.getBaseCurrency().getFormattedAmount(expenseManager.getRemainingBudget()) + ".";
    }

    /**
     * Adds a new expense with a given name, amount, and category or currency.
     * If the category matches a supported currency code, the amount is converted accordingly.
     *
     * @param expenseName the name of the expense to add
     * @param amount      the amount of the expense
     * @param category    the category or currency code
     * @return a message confirming the addition and updated budget status
     * @throws InvalidArgumentException if the input is invalid
     */
    public String handleAddExpense(String expenseName, double amount, String category)
            throws InvalidArgumentException {
        assert amount > 0;
        try {
            /* the value entered is a currency */
            Currency currency = Currency.valueOf(category);
            expenseManager.addExpense(expenseName, currency.convert(amount));
        } catch (IllegalArgumentException e) {
            /* the value entered is a category */
            expenseManager.addExpense(expenseName, amount, category);
        }

        double remainingBudget = expenseManager.getRemainingBudget();
        Currency baseCurrency = expenseManager.getBaseCurrency();
        if (remainingBudget >= 0) {
            return "Expense " + expenseName + " added successfully to category " + category + ".\n" +
                    "Your remaining budget is " + baseCurrency.getFormattedAmount(remainingBudget) + ".";
        } else {
            double amountOfDebt = remainingBudget * -1;
            return "Expense " + expenseName + " added successfully to category " + category + ".\n" +
                    "Uh oh! You've exceeded your budget.\n" +
                    "You are now in debt by " +
                    baseCurrency.getFormattedAmount(amountOfDebt) + ". Time to rein it in!\n" +
                    "Consider adjusting your budget to get back on track!";
        }
    }

    /**
     * Adds a new expense with a given name and amount.
     *
     * @param expenseName the name of the expense to add
     * @param amount      the amount of the expense
     * @return a message confirming the addition and updated budget status
     * @throws InvalidArgumentException if the input is invalid
     */
    public String handleAddExpense(String expenseName, double amount) throws InvalidArgumentException {
        assert amount > 0;
        expenseManager.addExpense(expenseName, amount);
        double remainingBudget = expenseManager.getRemainingBudget();
        Currency baseCurrency = expenseManager.getBaseCurrency();
        if (remainingBudget >= 0) {
            return "Expense " + expenseName + " added successfully.\n" +
                    "Your remaining budget is $" +
                    baseCurrency.getFormattedAmount(remainingBudget) + ".";
        }
        return "Expense " + expenseName + " added successfully.\n" +
                "Uh oh! You've exceeded your budget.\n" +
                "You are now in debt by $" + baseCurrency.getFormattedAmount(-remainingBudget) +
                ". Time to rein it in!\nConsider adjusting your budget to get back on track!";
    }

    /**
     * Lists all expenses or filters by category.
     *
     * @param category the category to filter by (null to list all expenses)
     * @return a formatted list of expenses and total amount spent
     * @throws InvalidArgumentException if an error occurs retrieving expenses
     */
    public String handleListExpense(String category) throws InvalidArgumentException {
        List<Expense> expenses = (category == null? expenseManager.getExpenses() :
                expenseManager.getExpensesByCategory(category));
        StringBuilder expensesString = new StringBuilder();
        double totalAmount = 0;
        for (Expense expense : expenses) {
            expensesString.append("\n - ").append(expense.toString());
            totalAmount += expense.getAmount();
        }
        expensesString.append("\nTotal amount spent: ")
                .append(expenseManager.getBaseCurrency().getFormattedAmount(totalAmount)).append(".");
        return expenses.isEmpty() ? "There are no expenses." : "Here is a list of your past expenses: "
                + expensesString;
    }

    /**
     * Retrieves the expense with the maximum amount.
     *
     * @return a message containing the maximum expense details
     * @throws InvalidArgumentException if no expenses are recorded
     */
    public String handleMaxExpense() throws InvalidArgumentException {
        Expense maxExpense = expenseManager.getMaxExpense();
        return "Maximum expense: " + maxExpense.toString();
    }

    /**
     * Retrieves the expense with the minimum amount.
     *
     * @return a message containing the minimum expense details
     * @throws InvalidArgumentException if no expenses are recorded
     */
    public String handleMinExpense() throws InvalidArgumentException {
        Expense minExpense = expenseManager.getMinExpense();
        return "Minimum expense: " + minExpense.toString();
    }

    /**
     * Filters and lists all expenses between two specified date-time values.
     *
     * @param startStr the start date-time string in format yyyy-MM-dd HH:mm:ss
     * @param endStr   the end date-time string in format yyyy-MM-dd HH:mm:ss
     * @return a formatted message listing filtered expenses or an appropriate message if none found
     * @throws DateTimeParseException      if either start or end date-time strings are invalid
     * @throws InvalidArgumentException    if filtering fails due to internal logic constraints
     */
    public String handleFilterExpenseByDateRange(String startStr, String endStr)
            throws DateTimeParseException, InvalidArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);

        List<Expense> filteredExpenses = expenseManager.getExpensesByDateRange(start, end);
        if (filteredExpenses.isEmpty()) {
            return "No expenses found between " + startStr + " and " + endStr + ".";
        } else {
            StringBuilder sb = new StringBuilder("Expenses between " + startStr + " and " + endStr + ":");
            for (Expense expense : filteredExpenses) {
                sb.append("\n - ").append(expense.toString());
            }
            return sb.toString();
        }
    }

    /**
     * Displays the current exchange rates of all currencies against the base currency.
     *
     * @return A string listing the exchange rate of each currency compared to the base currency.
     */
    public String handleViewCurrency() {
        StringBuilder message = new StringBuilder("The current exchange rate against base currency is: \n");
        Currency baseCurrency = expenseManager.getBaseCurrency();
        for (Currency currency : Currency.values()) {
            if (currency != baseCurrency) {
                message.append("Rate of ")
                        .append(baseCurrency.toString())
                        .append(" and ")
                        .append(currency.toString())
                        .append(" is ")
                        .append(String.format("%.2f", currency.getRate()))
                        .append("\n");
            }
        }
        return message.toString();
    }

    /**
     * Searches for expenses that match the provided search word.
     *
     * @param searchWord The word to search for in expense names.
     * @return A string listing all matched expenses or a message if no matches are found.
     */
    public String handleSearch(String searchWord) {
        List<Expense> expenses = expenseManager.getExpensesBySearchword(searchWord);
        if (expenses.isEmpty()) {
            return "There are no expenses that matched your search word: " + searchWord + ".";
        } else {
            StringBuilder sb = new StringBuilder("Expenses that matched your search word '" + searchWord + "':");
            for (Expense expense : expenses) {
                sb.append("\n - ").append(expense.toString());
            }
            return sb.toString();
        }
    }

    /**
     * Retrieves and displays all user-defined expense categories.
     *
     * @return A string listing all categories or a message if none exist.
     */
    public String handleViewCategories() {
        List<String> categories = expenseManager.getCategories();
        if (categories.isEmpty()) {
            return "There are no categories.";
        } else {
            StringBuilder sb = new StringBuilder("Here is the category list:");
            for (String category : categories) {
                sb.append("\n - ").append(category);
            }
            return sb.toString();
        }
    }

    /**
     * Clears all recorded expenses and categories from the expense manager.
     *
     * @return A confirmation message indicating that all data has been cleared.
     */
    public String handleClearAll() {
        expenseManager.clearExpensesAndCategories();
        return "All past expenses and categories have been cleared.";
    }

    /**
     * Sets a new base currency and converts all relevant data (budget, total spent, expenses) accordingly.
     *
     * @param baseCurrency The new base currency as a string (e.g., "USD", "SGD").
     * @return A confirmation message displaying the new base currency.
     * @throws InvalidArgumentException If the provided currency is invalid.
     */
    public String handleSetBaseCurrency(String baseCurrency) throws InvalidArgumentException {
        Currency newBase;
        try {
            newBase = Currency.valueOf(baseCurrency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException(baseCurrency, "Base currency is not a valid currency.");
        }

        // change budget
        double currentBudget = expenseManager.getBudget();
        expenseManager.setBudget(newBase.convert(currentBudget));

        // change total spent
        double currentTotalSpent = expenseManager.getTotalExpense();
        expenseManager.setTotalExpense(newBase.convert(currentTotalSpent));

        // change all expenses
        for (Expense expense : expenseManager.getExpenses()) {
            expense.setAmount(newBase.convert(expense.getAmount()));
        }

        expenseManager.setBaseCurrency(newBase);

        return "Current base is: " + newBase;
    }

    /**
     * Updates the timestamp of a specific expense by name.
     *
     * @param expenseName The name of the expense to update.
     * @param timestampStr The new timestamp in the format "yyyy-MM-dd HH:mm:ss".
     * @return A confirmation message if the update was successful.
     * @throws InvalidArgumentException If the expense name is not found.
     */
    public String handleSetTime(String expenseName, String timestampStr)
            throws DateTimeParseException, InvalidArgumentException {
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (Expense expense : expenseManager.getExpenses()) {
            if (expense.getName().equalsIgnoreCase(expenseName)) {
                expense.setDateTime(timestamp);
                return "Updated timestamp for \"" + expenseName + "\" to " + timestampStr + ".";
            }
        }
        throw new InvalidArgumentException(expenseName, "Expense name not found.");
    }
}
