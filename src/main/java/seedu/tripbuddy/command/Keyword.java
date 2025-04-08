package seedu.tripbuddy.command;

/**
 * Represents the valid keywords (commands) that the user can input in TripBuddy.
 * Each enum constant maps to a CLI command and has a user-friendly string representation.
 */
public enum Keyword {

    /** Displays the tutorial with all available features. */
    TUTORIAL,

    /** Sets a total budget for the trip. */
    SET_BUDGET,

    /** Views the current budget and remaining balance. */
    VIEW_BUDGET,

    /** Creates a new category for organizing expenses. */
    CREATE_CATEGORY,

    /** Assigns or updates a category to an existing expense. */
    SET_CATEGORY,

    /** Adds a new expense entry. */
    ADD_EXPENSE,

    /** Deletes a specific expense from the list. */
    DELETE_EXPENSE,

    /** Lists all recorded expenses, optionally filtered by category. */
    LIST_EXPENSE,

    /** Returns the expense with the highest amount. */
    MAX_EXPENSE,

    /** Returns the expense with the lowest amount. */
    MIN_EXPENSE,

    /** Filters expenses that fall within a date-time range. */
    FILTER_DATE,

    /** Displays exchange rates relative to the base currency. */
    VIEW_CURRENCY,

    /** Searches for expenses that match a keyword. */
    SEARCH,

    /** Displays all user-defined categories. */
    VIEW_CATEGORIES,

    /** Changes the base currency used for conversions. */
    SET_BASE_CURRENCY,

    /** Updates the timestamp of an existing expense. */
    SET_TIME,

    /** Clears all expenses and categories from memory. */
    CLEAR,

    /** Deletes an empty category. **/
    DELETE_CATEGORY,

    /** Clears the category of an expense. **/
    CLEAR_CATEGORY,

    /** Edits the amount of an expense. **/
    EDIT_AMOUNT;

    /**
     * Returns the command keyword in its user-facing string form.
     *
     * @return the CLI representation of the command
     */
    @Override
    public String toString() {
        return switch (this) {
        case TUTORIAL -> "tutorial";
        case SET_BUDGET -> "set-budget";
        case VIEW_BUDGET -> "view-budget";
        case CREATE_CATEGORY -> "create-category";
        case SET_CATEGORY -> "set-category";
        case ADD_EXPENSE -> "add-expense";
        case DELETE_EXPENSE -> "delete-expense";
        case LIST_EXPENSE -> "list-expense";
        case MAX_EXPENSE -> "max-expense";
        case MIN_EXPENSE -> "min-expense";
        case FILTER_DATE -> "filter-date";
        case VIEW_CURRENCY -> "view-currency";
        case SEARCH -> "search";
        case VIEW_CATEGORIES -> "view-categories";
        case SET_BASE_CURRENCY -> "set-base-currency";
        case SET_TIME -> "set-time";
        case CLEAR -> "clear";
        case DELETE_CATEGORY -> "delete-category";
        case CLEAR_CATEGORY -> "clear-category";
        case EDIT_AMOUNT -> "edit-amount";
        };
    }
}
