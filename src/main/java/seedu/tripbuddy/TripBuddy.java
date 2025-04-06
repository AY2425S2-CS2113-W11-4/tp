package seedu.tripbuddy;

import seedu.tripbuddy.exception.DataLoadingException;
import seedu.tripbuddy.exception.ExceptionHandler;
import seedu.tripbuddy.framework.InputHandler;
import seedu.tripbuddy.framework.ExpenseManager;
import seedu.tripbuddy.framework.Ui;
import seedu.tripbuddy.storage.DataHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TripBuddy {

    private static final String LOG_PATH = "log.txt";
    private static final String FILE_PATH = "tripbuddy_data.json";

    private static Logger logger;

    /**
     * Directs logging to a file
     */
    private static void initLogging() {
        logger = Logger.getLogger("TripBuddy");
        logger.setUseParentHandlers(false);
        try {
            FileHandler fh = new FileHandler(LOG_PATH);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    /**
     * Runs the main program loop, loads all data, and handles user input.
     */
    public static void run() {
        initLogging();
        Ui ui = new Ui();
        ExpenseManager expenseManager;

        try {
            expenseManager = DataHandler.loadData(FILE_PATH, ui);
            ui.printMessage("Loading expense data from " + FILE_PATH);
        } catch (FileNotFoundException e) {
            ui.printMessage(ExceptionHandler.handleFileNotFoundException(e));
            expenseManager = new ExpenseManager();
        } catch (DataLoadingException e) {
            ui.printMessage(e.getMessage());
            expenseManager = new ExpenseManager();
        }
        InputHandler inputHandler = new InputHandler(logger, expenseManager);
        ui.printStartMessage();
        while (true) {
            String userInput = ui.getUserInput();
            if (userInput.isEmpty()) {
                continue;
            }

            if (inputHandler.isQuitCommand(userInput)) {
                try {
                    DataHandler.saveData(FILE_PATH, expenseManager);
                    ui.printMessage("Saved data to " + FILE_PATH);
                } catch (IOException e) {
                    ExceptionHandler.handleException(e);
                }
                ui.printEndMessage();
                return;
            }
            ui.printMessage(inputHandler.handleUserInput(userInput));

        }
    }

    public static void main(String[] args) {
        TripBuddy.run();
    }
}
