import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Start");


        String filePath = "src/passwords.txt"; // Path to the text file with passwords
        String[] commonPasswords = readPasswordsFromFile(filePath);
        AlphabetPrinter.printCombinations(4, 10, commonPasswords, "glamour");
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
            for (String password : commonPasswords) {
                if (password.length() >= startLength && password.length() <= maxLength) {
                    System.out.println(password);
                    if (password.equals(targetPassword)) {
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        System.out.println("Password found in common password file.\nPassword: " + password);
                        System.out.println("Time taken to find the password: " + elapsedTime + " milliseconds");
                        targetFound = true;
                        break;
                    }
                }
            }

            // Generate combinations if target password not found
            if (!targetFound) {
                generateCombinations("", startLength, maxLength, startTime, targetPassword);
            }
        }

        private static void generateCombinations(String combination, int length, int maxLength, long startTime, String targetPassword) {

            if (combination.length() == length) {
                System.out.println("Current combination: " + combination);
                return;
            }

            for (int i = 0; i < CHARS.length(); i++) {
                String newCombination = combination + CHARS.charAt(i);
                generateCombinations(newCombination, length, maxLength, startTime, targetPassword);
            }

            if (length < maxLength) {
                generateCombinations(combination, length + 1, maxLength, startTime, targetPassword);
            }
        }


        private static boolean containsPassword(String combination, String[] commonPasswords) {
            for (String password : commonPasswords) {
                if (password.equals(combination)) {
                    return true;
                }
            }
            return false;
        }
    }
}