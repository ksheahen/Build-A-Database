import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Create the database file (if it doesn't exist)
        try {
            File database = new File("data.db");
            if (database.createNewFile()) {
                System.out.println("File created: " + database.getName());
            } else {
                System.out.println("Database file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occured while creating DB file: " + e);
        }

        Scanner scanner = new Scanner(System.in);

        // Command loop to get continued user input until EXIT
        while (true) {
            System.out.print("Build-A-DB % ");
            String input = scanner.nextLine();
            input = input.toUpperCase();

            if (input.equals("EXIT")) {
                System.out.println("Exiting the program...");
                break;
            }

            String[] arguments = input.split(" ", 3);
            if (arguments.length == 0) {
                continue;
            }

            if (arguments[0].equals("SET") && arguments.length == 3) {
                Set(arguments[1], arguments[2]);
            } else if (arguments[0].equals("GET") && arguments.length == 2) {
                Get(arguments[1]);
            } else {
                System.out.println("Command not found: " + input);
            }
        }

        scanner.close();
    }

    // SET Command
    public static void Set(String key, String value) {
        System.out.println("Test Set");
    }

    // GET Command
    public static void Get(String key) {
        System.out.println("Test Get");
    }

}
