package seedu.tripbuddy.storage;

import java.io.IOException;

public class StorageManager {
    private final JsonStorage jsonStorage;

    public StorageManager() {
        jsonStorage = new JsonStorage();
    }

    public boolean loadData() {
        try {
            jsonStorage.loadData();
            return true;
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return false;
        }
    }

    public void saveData() {
        try {
            jsonStorage.saveData();
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}
