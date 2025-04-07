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

/**
 * The main entry point of the TripBuddy application.
 * Handles application startup, data loading, logging setup, and user input loop.
 */
public class TripBuddy {

    private static final String LOG_PATH = "log.txt";
    private static final String FILE_PATH = "tripbuddy_data.json";

    private static Logger logger;
    private static final Ui ui = Ui.getInstance();

    /**
     * Initializes logging to a file called log.txt.
     * Logs are written using a simple formatter.
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
            ui.printMessage(ExceptionHandler.handleException(e));
        }
    }

    /**
     * Starts the TripBuddy application.
     * Loads data, prints the welcome message, and processes user commands until quit is entered.
     */
    public static void run() {
        initLogging();
        DataHandler dataHandler = DataHandler.getInstance();
        try {
            String message = dataHandler.loadData(FILE_PATH);
            ui.printMessage(message + "Loaded expense data from " + FILE_PATH);
        } catch (FileNotFoundException e) {
            ui.printMessage(ExceptionHandler.handleFileNotFoundException(e));
        } catch (DataLoadingException e) {
            ui.printMessage(ExceptionHandler.handleException(e));
        }
        ExpenseManager expenseManager = ExpenseManager.getInstance();
        InputHandler inputHandler = InputHandler.getInstance(logger);
        ui.printStartMessage();
        while (true) {
            String userInput = ui.getUserInput();
            if (userInput.isEmpty()) {
                continue;
            }

            if (inputHandler.isQuitCommand(userInput)) {
                try {
                    String message = dataHandler.saveData(FILE_PATH, expenseManager);
                    ui.printMessage(message);
                } catch (IOException e) {
                    ui.printMessage(ExceptionHandler.handleException(e));
                }
                ui.printEndMessage();
                return;
            }
            ui.printMessage(inputHandler.handleUserInput(userInput));
        }
    }

    /**
     * The main method that launches the application.
     */
    public static void main(String[] args) {
        TripBuddy.run();
    }
}
