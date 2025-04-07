package seedu.tripbuddy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileHandlerTest {

    @TempDir
    Path tempDir;

    @Test
    public void testWriteAndReadJsonObject() throws IOException {
        FileHandler fileHandler = FileHandler.getInstance();
        // Create a temporary file path.
        Path filePath = tempDir.resolve("test.json");
        String path = filePath.toString();

        // Create a sample JSONObject.
        JSONObject jsonToWrite = new JSONObject();
        jsonToWrite.put("name", "Test");
        jsonToWrite.put("value", 123);

        // Write the JSONObject to file.
        String absolutePath = fileHandler.writeJsonObject(path, jsonToWrite);

        // Verify that the file exists.
        File file = new File(absolutePath);
        assertTrue(file.exists(), "File should exist after writing.");

        // Read the JSONObject from the file.
        JSONObject jsonRead = fileHandler.readJsonObject(path);

        // Verify that the read JSON contains the expected values.
        assertEquals("Test", jsonRead.getString("name"), "The name should match.");
        assertEquals(123, jsonRead.getInt("value"), "The value should match.");
    }

    @Test
    public void testReadNonExistentFile() {
        // Define a file path that does not exist.
        String nonExistentPath = tempDir.resolve("nonexistent.json").toString();

        // Expect FileNotFoundException when trying to read a file that doesn't exist.
        assertThrows(FileNotFoundException.class, () -> {
            FileHandler.getInstance().readJsonObject(nonExistentPath);
        });
    }

    @Test
    public void testWriteCreatesDirectories() throws IOException {
        // Create a file path in a nested directory that doesn't exist.
        Path nestedDir = tempDir.resolve("subdir1/subdir2");
        Path filePath = nestedDir.resolve("test.json");
        String path = filePath.toString();

        // Create a sample JSONObject.
        JSONObject jsonToWrite = new JSONObject();
        jsonToWrite.put("key", "value");

        // Write the JSONObject to file.
        String absolutePath = FileHandler.getInstance().writeJsonObject(path, jsonToWrite);

        // Verify that the file now exists (and thus that the parent directories were created).
        File file = new File(absolutePath);
        assertTrue(file.exists(), "File should exist and directories should be created.");
    }
}
