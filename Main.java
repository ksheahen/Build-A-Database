import java.util.logging.*;

/**
 * Main class for Build A Database.
 */
public class Main {

    // Logger for logging important errors and events.
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Initializes the UserInput handler and starts the command loop.
     * Loads the database and prepares for user commands.
     */
    public static void main(String[] args) {
        try {
            UserInput program = new UserInput("data.db");
            program.run();
        } catch (DatabaseException e) {
            logger.warning("Error running program: " + e.getMessage());
        }

    }
}
