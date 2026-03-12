import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file operations for the database.
 */
public class DatabaseManager {
    private final String filename;

    /**
     * Constructs a DatabaseManager for the specified file.
     * @param filename the name of the file created by the user.
     */
    public DatabaseManager(String filename) {
        this.filename = filename;
    }

    /**
     * Appends a SET command to the database file.
     * @param key The key to update or add to the database.
     * @param value The value to associate with the key to add to the database.
     * @throws IOException if writing to the database fails.
     */
    public void appendSetCommand(String key, String value) throws IOException{
        try (BufferedWriter output = new BufferedWriter(new FileWriter(filename, true))) {
            output.write("SET " + key + " " + value);
            output.newLine();
        } catch (IOException e) {
            throw new IOException("IO error when writing to the database: " + filename, e);
        }
    }

    /**
     * Reads all commands from the database file.
     * @return List of command lines from the database.
     * @throws IOException if reading from the database fails.
     */
    public List<String> readAllCommands() throws IOException {
        List<String> commands = new ArrayList<>();
        try (BufferedReader db = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = db.readLine()) != null) {
                commands.add(line);
            }
        } catch (IOException e) {
            throw new IOException("IO error when reading the database: " + filename, e);
        }
        return commands;
    }

    /**
     * Checks if the database file exists and is accessible.
     * @return true if the file exists and is accessible, otherwise false.
     */
    public boolean exists() {
        try {
            return new File(filename).exists();
        } catch (SecurityException e) {
            return false;
        }
    }
}