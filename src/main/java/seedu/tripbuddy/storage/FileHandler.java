package seedu.tripbuddy.storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {

    private static Logger LOGGER = null;

    public FileHandler(Logger logger) {
        LOGGER = logger;
    }

    public JSONObject readJsonObject(String path) throws FileNotFoundException, JSONException {
        File file = new File(path);
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
        }
        return new JSONObject(content.toString());
    }

    /**
     * Write json string into a file. The file will be created if not exists.
     * @return absolute path of the file.
     */
    public String writeJsonObject(String path, JSONObject data) throws IOException {
        File file = new File(path);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        LOGGER.log(Level.INFO, "folder init done");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data.toString(4));
        }
        return file.getAbsolutePath();
    }
}
