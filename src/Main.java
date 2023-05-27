import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a password you want to test:");
        String enteredPassword = sc.nextLine();

        String filePath = "src/passwords.txt"; // Path to the text file with passwords
        String[] commonPasswords = readPasswordsFromFile(filePath);
        AlphabetPrinter.printCombinations(4, 10, commonPasswords, enteredPassword); // This is the password length, most passwords are over 6 characters in length, but I used 4 just as a test.
    }

    private static String[] readPasswordsFromFile(String filePath) {

        List<String> passwords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String password = line.trim();
                passwords.add(password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords.toArray(new String[0]);
    }

    static class AlphabetPrinter {
        private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        public static void printCombinations(int startLength, int maxLength, String[] commonPasswords, String targetPassword) {
            long startTime = System.currentTimeMillis();

            // Print passwords from the file
            boolean targetFound = false;
            int count = 1;
            for (String password : commonPasswords) {
                if (password.length() >= startLength && password.length() <= maxLength) {
                    //System.out.println("Found common password from file: " + password);
                    if (password.equals(targetPassword)) {
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        System.out.println("Password reached: " + password);
                        System.out.println("Time taken: " + elapsedTime + " milliseconds");
                        System.out.println("Your password is number " + count + " amongst the most common passwords in the world");
                        targetFound = true;
                        break;
                    }
                    count++;
                }
            }

            // Generate combinations if target password not found
            if (!targetFound) {
                for (int length = startLength; length <= maxLength; length++) {
                    generateCombinations("", length, startTime, targetPassword, count);
                }
            }
        }

        private static void generateCombinations(String combination, int length, long startTime, String targetPassword, int count) {
            if (combination.length() == length) {
                //System.out.println("Current combination: " + combination);
                if (combination.equals(targetPassword)) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    System.out.println("Password reached: " + combination);
                    System.out.println("It took " + count + " of attempts to break your password");
                    System.out.println("Time taken: " + elapsedTime + " milliseconds");
                    System.exit(0);
                }
            }

            if (combination.length() < length) {
                for (int i = 0; i < CHARS.length(); i++) {
                    String newCombination = combination + CHARS.charAt(i);
                    generateCombinations(newCombination, length, startTime, targetPassword, count);
                }
            }
        }
    }
}