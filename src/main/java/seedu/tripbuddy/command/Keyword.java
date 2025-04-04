package seedu.tripbuddy.command;


public enum Keyword {

    TUTORIAL,
    SET_BUDGET,
    VIEW_BUDGET,
    CREATE_CATEGORY,
    SET_CATEGORY,
    ADD_EXPENSE,
    DELETE_EXPENSE,
    LIST_EXPENSE,
    MAX_EXPENSE,
    MIN_EXPENSE,
    FILTER_DATE,
    VIEW_CURRENCY,
    SEARCH,
    VIEW_CATEGORIES,
    SET_BASE_CURRENCY,
    CLEAR;

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
        case CLEAR -> "clear";
        };
    }
}
