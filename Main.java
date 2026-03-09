import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // mr world wide
    static KeyValue[] index = new KeyValue[1000];
    static int indexSize = 0;

    public static void main(String[] args) {

        // Calls the updateDB() method, which rebuilds the in-memory index
        updateDB();

        // Calls the userInput() method, which loops for user input
        userInput();
    }

    // SET: Creates a key value pair (append-only)
    public static void set(String key, String value) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter("data.db", true))) {
            output.write("SET " + key + " " + value);
            output.newLine();
            System.out.println("Debug: Writing to file...");
            output.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
        updateIndex(key, value);
    }

    // GET: Retrieves the value associated with the given key
    public static String get(String key) throws Exception {
        // System.out.println("Test Get");
        for (int i = indexSize - 1; i >= 0; i--) {
            if (index[i].getKey().equals(key)) {
                System.out.println(index[i].getValue());
                return index[i].getValue();
            }
        }
        System.out.println("Error: Key-Value Pair does not exist.");
        return null;
    }

    // Update in-memory index (key-value pairs)
    public static void updateIndex(String key, String value) {
        for (int i = indexSize - 1; i >= 0; i--) {
            if (index[i].getKey().equals(key)) {
                index[i].setValue(value);
                return;
            }
        }
        index[indexSize++] = new KeyValue(key, value);
    }

    // Asks user for input until the program is imploded
    public static void userInput() {
        Scanner scanner = new Scanner(System.in);

        // Command loop to get continued user input until EXIT
        while (true) {
            System.out.print("Build-A-DB % ");
            String input = scanner.nextLine();
            // input = input.toUpperCase();

            if (input.equals("EXIT")) {
                System.out.println("Exiting the program...");
                break;
            }

            // Parse the command arguments
            // SET has 2 arguments - key, value
            // GET has 1 argument - key
            String[] arguments = input.split(" ", 3);
            if (arguments.length == 0) {
                continue;
            }

            if (arguments[0].equals("SET") && arguments.length == 3) {
                set(arguments[1], arguments[2]);
            } else if (arguments[0].equals("GET") && arguments.length == 2) {
                try {
                    get(arguments[1]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Command not found: " + input);
            }
        }

        scanner.close();
    }

    // Rebuilds the in-memory index and initializes data.db if not already created
    public static void updateDB() {

        File database = new File("data.db");
        if (!database.exists()) return;

        indexSize = 0;

        try (BufferedReader db = new BufferedReader(new FileReader("data.db"))) {
            String input;
            while ((input = db.readLine()) != null) {
                String[] arguments = input.split(" ", 3);

                if (arguments.length == 3 && arguments[0].equals("SET")) {
                    updateIndex(arguments[1], arguments[2]);
                }
            }
            db.close();
        } catch (IOException e) {
            System.out.println("An error occured while creating DB file: " + e);
        }

        
    }

}
