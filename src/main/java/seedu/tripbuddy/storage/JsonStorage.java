package seedu.tripbuddy.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import seedu.tripbuddy.dataclass.Expense;
import seedu.tripbuddy.framework.ExpenseManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonStorage {

    private static final String FILE_PATH;
    static {
        Path path = Paths.get(System.getProperty("user.home"), "tripbuddy", "tripbuddy.json");
        FILE_PATH = path.toString();
    }

    private final Gson gson;

    public JsonStorage() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private static class TripBuddyData {
        double budget;
        double totalExpense;
        List<String> categories;
        List<Expense> expenses;
    }

    public void saveData() throws IOException {
        TripBuddyData data = new TripBuddyData();
        data.budget = ExpenseManager.getBudget();
        data.totalExpense = ExpenseManager.getTotalExpense();
        data.categories = ExpenseManager.getCategories();
        data.expenses = ExpenseManager.getExpenses();

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        }
    }

    public void loadData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            TripBuddyData data = gson.fromJson(reader, TripBuddyData.class);
            if (data != null) {
                ExpenseManager.initExpenseManager(data.budget);
                if (data.expenses != null) {
                    for (Expense expense : data.expenses) {
                        if (expense.getCategory() == null) {
                            ExpenseManager.addExpense(expense.getName(), expense.getAmount());
                        } else {
                            ExpenseManager.addExpense(expense.getName(), expense.getAmount(), expense.getCategory());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Error loading data from JSON", e);
        }
    }
}
