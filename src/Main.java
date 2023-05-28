import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a password you want to test:");
        String enteredPassword = sc.nextLine();
        System.out.println("Guessing password, this may take some time so please be patient...");

        String filePath = "src/passwords.txt"; // Path to the text file with passwords
        //String[] commonPasswords = readPasswordsFromFile(filePath);
        /*
        I asked chatgpt for suggestions to improve the code, and it suggested using HashSet. This shouldn't improve the brute-force speed, but it would most likely improve the
        speed it takes to find the password in the txt file. However, since the file only has 10k passwords it only takes a few milliseconds for my computer to find them so
        this does not offer a significant advantage for me. It may be different if the txt file was much larger, but since I do not have a larger sample size as of right now
        it is not possible to test the new (and hopefully improved) efficiency of this code. But I will leave it in since it maybe an overall improvement if you were to have
        a larger sample size of common passwords.
        */
        HashSet<String> commonPasswords = readPasswordsFromFile(filePath);
        AlphabetPrinter.printCombinations(6, 10, commonPasswords, enteredPassword); // This is the password length, most passwords are over 6 characters in length, but I used 4 just as a test.
    }

    private static HashSet<String> readPasswordsFromFile(String filePath) {

        HashSet<String> passwords = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String password = line.trim();
                passwords.add(password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords;
    }

    static class AlphabetPrinter {
        private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        private static final int UPDATE_INTERVAL_MS = 10000; // 10 seconds

        private static boolean targetFound;

        public static void printCombinations(int startLength, int maxLength, HashSet<String> commonPasswords, String targetPassword) {

            long startTime = System.currentTimeMillis();

            // Start the timer for periodic message to let the user know the program is still working.
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (targetFound) {
                        timer.cancel();
                    } else {
                        System.out.println("Still working, please wait...");
                    }
                }
            }, UPDATE_INTERVAL_MS, UPDATE_INTERVAL_MS);

            int count = 1;
            for (String password : commonPasswords) {
                if (password.length() >= startLength && password.length() <= maxLength) {
                    //System.out.println("Found common password from file: " + password);
                    if (password.equals(targetPassword)) {
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        System.out.println("Password reached: " + password);
                        System.out.println("Time taken: " + elapsedTime + " milliseconds.");
                        if (count == 1) {
                            System.out.println("Congratulations, you have the most common password in the world...this is not good, please change it...now.");
                        } else if (count > 1) {
                            System.out.println("Your password is number " + count + " amongst the most common passwords in the world.");
                        }
                        targetFound = true;
                        break;
                    }
                    count++;
                }
            }

            // Generate combinations if target password was not found
            if (!targetFound) {
                for (int length = startLength; length <= maxLength; length++) {
                    generateCombinations("", length, startTime, targetPassword, count);
                }
            }
        }

        private static void generateCombinations(String combination, int length, long startTime, String targetPassword, int count) {

            if (combination.length() == length) {
                // System.out.println("Current combination: " + combination);
                if (combination.equals(targetPassword)) {
                    double endTime = System.currentTimeMillis();
                    double elapsedTime = endTime - startTime;
                    System.out.println("Password reached: " + combination);
                    System.out.println("It took " + count + " attempts to break your password");

                    // Calculate time taken to guess a password
                    double milliseconds = elapsedTime % 1000;
                    double seconds = Math.floor((elapsedTime / 1000) % 60);
                    double minutes = Math.floor((elapsedTime / (1000 * 60)) % 60);
                    double hours = Math.floor((elapsedTime / (1000 * 60 * 60)) % 24);
                    double days = Math.floor(elapsedTime / (1000 * 60 * 60 * 24));

                    // Construct the "time taken" message
                    StringBuilder timeTaken = new StringBuilder("Time taken: ");
                    if (days > 0) {
                        timeTaken.append((int) days).append(" day");
                        if (days > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (hours > 0) {
                        timeTaken.append((int) hours).append(" hour");
                        if (hours > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (minutes > 0) {
                        timeTaken.append((int) minutes).append(" minute");
                        if (minutes > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (seconds > 0) {
                        timeTaken.append((int) seconds).append(" second");
                        if (seconds > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    timeTaken.append((int) milliseconds).append(" millisecond");

                    System.out.println(timeTaken);
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