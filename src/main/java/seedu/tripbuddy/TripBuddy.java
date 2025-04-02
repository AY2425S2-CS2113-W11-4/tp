package seedu.tripbuddy;

import seedu.tripbuddy.command.Parser;
import seedu.tripbuddy.framework.ExpenseManager;
import seedu.tripbuddy.framework.Ui;
import seedu.tripbuddy.storage.StorageManager;

public class TripBuddy {

    private static final int DEFAULT_BUDGET = 1000;
    private final StorageManager storageManager;

    public TripBuddy() {
        storageManager = new StorageManager();
        // Let StorageManager handle I/O. If loading fails, initialize with the default budget.
        if (!storageManager.loadData()) {
            ExpenseManager.initExpenseManager(DEFAULT_BUDGET);
        }
    }

    /**
     * Runs the main program loop, handling user input and executing commands.
     */
    public static void run() {
        ExpenseManager.initExpenseManager(DEFAULT_BUDGET);
        Ui.printStartMessage();
        while (true) {
            String userInput = Ui.getUserInput();

            if (userInput.isEmpty()) {
                continue;
            }

            if (Parser.isQuitCommand(userInput)) {
                Ui.printEndMessage();
                return;
            }

            Parser.handleUserInput(userInput);
        }
    }

    public static void main(String[] args) {
        TripBuddy.run();
    }
}
