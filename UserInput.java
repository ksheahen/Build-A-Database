import java.io.IOException;
import java.util.*;
import java.util.logging.*;

public class UserInput {

    private static final Logger logger = Logger.getLogger(UserInput.class.getName());
    private List<KeyValue> index = new ArrayList<>();
    private DatabaseManager databaseHandler;

    public UserInput(String filename) throws DatabaseException {
        this.databaseHandler = new DatabaseManager(filename);
        rebuildIndexFromDatabase();
    }

    /**
     * Handles user input commands in a loop.
     * Continously reads commands from the user until "EXIT" is entered.
     * Valid commands are "SET <key> <value>" and "GET <key".
     * 
     * @throws DatabaseException if a database operation fails.
     */
    public void run() throws DatabaseException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String commandLine = scanner.nextLine();
            if (commandLine.equals("EXIT")) {
                logger.info("Exiting the program...");
                break;
            }

            if (commandLine.isEmpty()) {
                logger.warning("Empty command received. Please enter a valid command.");
                continue;
            }

            String[] arguments = commandLine.split("\\s+");
            String command = arguments[0];
            try {
                switch (command) {
                    case "SET":
                        processSetCommand(arguments);
                        break;
                    case "GET":
                        processGetCommand(arguments);
                        break;
                    default:
                        logger.warning(
                                "Command not found. The valid commands are `SET <key> <value>`, `GET <key>`, or `EXIT`. Input: "
                                        + commandLine);            
                }
            } catch (IllegalArgumentException e) {
                logger.warning(e.getMessage());
            }

        }

        scanner.close();
    }

    /**
     * Processes the SET command and the arguments entered by the user.
     * 
     * @param arguments The arguments entered in the command line by the user.
     * @throws DatabaseException if writing to the database fails.
     */
    private void processSetCommand(String[] arguments) throws DatabaseException {
        if (arguments.length > 3) {
            throw new IllegalArgumentException("SET command: requires 2 arguments.");
        }
        if (arguments.length == 3) {
            String key = arguments[1];
            String value = arguments[2];
            try {
                setKeyValuePair(key, value);
            } catch (DatabaseException e) {
                logger.severe("SET command failed: " + e.getMessage());
            }

        } else {
            logger.warning("SET command requires a key and a value. Input: " + Arrays.toString(arguments));
        }
    }

    /**
     * Processes the GET command and the arguments entered by the user.
     * 
     * @param arguments The arguments entered in the command line by the user.
     * @throws DatabaseException if getting a key-value pair from the database
     *                           fails.
     */
    private void processGetCommand(String[] arguments) throws DatabaseException {
        if (arguments.length > 2) {
            throw new IllegalArgumentException("GET command: requires exactly 1 argument.");
        }
        if (arguments.length == 2) {
            String key = arguments[1];
            Optional<String> result = getKeyValuePair(key);
            // If the key is found, print the value.
            if (result.isPresent()) {
                System.out.println(result.get());
            } else {
                logger.info("GET command: Key not found.");
            }
        } else {
            logger.warning("GET command requires one key. Input: " + Arrays.toString(arguments));
        }
    }

    /**
     * Adds a key-value pair to the database and updates the in-memory index.
     * 
     * @param key   The key to add to the database.
     * @param value The value to associate with the key to add to the database.
     * @throws DatabaseException if writing to the database fails.
     */
    private void setKeyValuePair(String key, String value) throws DatabaseException {
        try {
            databaseHandler.appendSetCommand(key, value);
        } catch (IOException e) {
            throw new DatabaseException("IO error during setKeyValuePair", e);
        }
        updateIndex(key, value);
    }

    /**
     * Retrieves the value for a given key if it is present.
     * 
     * @param key The key to look up in the database.
     * @return If the key is found, returns the associated value, or empty
     *         otherwise.
     */
    private Optional<String> getKeyValuePair(String key) {
        for (int i = index.size() - 1; i >= 0; i--) {
            // If the key matches, return its value.
            if (index.get(i).getKey().equals(key)) {
                return Optional.of(index.get(i).getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * Updates the in-memory index with the given key-value pair.
     * 
     * @param key   The key to update or add to the database.
     * @param value The value to associate with the key to add to the database.
     */
    private void updateIndex(String key, String value) {
        for (int i = index.size() - 1; i >= 0; i--) {
            // If the key matches an existing key, update its value.
            if (index.get(i).getKey().equals(key)) {
                index.set(i, new KeyValue(key, value));
                return;
            }
        }
        // If the key was not found, add the new key-value pair.
        index.add(new KeyValue(key, value));
    }

    /**
     * Rebuilds the in-memory index from the database file.
     * @throws IOException if an error occurs when updating the database.
     */
    private void rebuildIndexFromDatabase() throws DatabaseException {
        if (!databaseHandler.exists())
            return;

        try {
            List<String> commands = databaseHandler.readAllCommands();
            // Loops through each command and splits them into arguments.
            for (String commandLine : commands) {
                String[] arguments = commandLine.split(" ", 3);
                if (arguments.length == 3 && arguments[0].equals("SET")) {
                    updateIndex(arguments[1], arguments[2]);
                }
            }
        } catch (IOException e) {
            throw new DatabaseException("IO error during rebuildIndexFromDatabase", e);
        }

    }
}