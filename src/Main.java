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

        /*
        I asked chatgpt for suggestions to improve the code, and it suggested using HashSet. This shouldn't improve the brute-force speed, but it would most likely improve the
        speed it takes to find the password in the txt file. However, since the file only has 10k passwords it only takes a few milliseconds for my computer to find them so
        this does not offer a significant advantage for me. It may be different if the txt file was much larger, but since I do not have a larger sample size as of right now
        it is not possible to test the new (and hopefully improved) efficiency of this code. But I will leave it in since it maybe an overall improvement if you were to have
        a larger sample size of common passwords.
        */
        //String[] commonPasswords = readPasswordsFromFile(filePath);
        //HashSet<String> commonPasswords = readPasswordsFromFile(filePath);

        // Easy way of adding multiple file paths if you want to add more in the future.
        String[] filePaths = {
                "src/dictionary.txt",   // Path to the dictionary file
                "src/passwords.txt"     // Path to the passwords file
        };

        ArrayList<String> commonPasswords = new ArrayList<>(); // The count was wrong since HashSet does not preserve the order, now the count works properly.
        HashSet<String> dictionaryPasswords = new HashSet<>();

        for (String filePath : filePaths) {
            if (filePath.endsWith("dictionary.txt")) {
                dictionaryPasswords.addAll(readPasswordsFromFile(filePath));
            } else if (filePath.endsWith("passwords.txt")) {
                commonPasswords.addAll(readPasswordsFromFile(filePath));
            }
        }

        AlphabetPrinter.printCombinations(6, 10, commonPasswords, dictionaryPasswords, enteredPassword);
    }

    private static ArrayList<String> readPasswordsFromFile(String filePath) {
        ArrayList<String> passwords = new ArrayList<>();

        // Read the common passwords from the file
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

        public static void printCombinations(int startLength, int maxLength, ArrayList<String> commonPasswords, HashSet<String> dictionaryPasswords, String targetPassword) {

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

            int countCommonPasswords = 1;
            boolean foundInPasswordsTxt = false;

            for (String password : commonPasswords) {
                // Overlooked that HashSet is not in order so changed commonPasswords back to ArrayList and use HashSet for dictionary. If not the count is off.
                    if (password.equals(targetPassword)) {
                        foundInPasswordsTxt = true;
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        System.out.println("\nPassword reached: " + password);
                        System.out.println("Time taken: " + elapsedTime + " milliseconds.");
                        break;
                    }
                countCommonPasswords++;
            }

            if (foundInPasswordsTxt) {
                if (countCommonPasswords == 1) {
                    System.out.println("Congratulations, you have the most common password in the world...this is not good, please change it...now.");
                } else {
                    System.out.println("Your password is number " + countCommonPasswords + " amongst the most common passwords in the world.");
                }
                System.exit(0);
            } else {
                // If password was not found in passwords.txt file it will then check the dictionary.txt file.
                for (String password : dictionaryPasswords) {
                        if (password.equals(targetPassword)) {
                            long endTime = System.currentTimeMillis();
                            long elapsedTime = endTime - startTime;
                            System.out.println("\nPassword reached: " + password);
                            System.out.println("Time taken: " + elapsedTime + " milliseconds.");
                            System.out.println("Your password was found in the dictionary.");
                            System.exit(0);
                            targetFound = true;
                            break;
                        }
                    }


            // Generate combinations if target password was not found
            if (!targetFound) {
                System.out.println("Password not found in common password or dictionary files, now attempting to generate password.");
                generateCombinations("", startLength, maxLength, startTime, targetPassword, 1);
            }
        }
    }

        private static int generateCombinations(String combination, int length, int maxLength, long startTime, String targetPassword, int count) {
            if (combination.length() == length) {
                 // System.out.println(combination);
                if (combination.equals(targetPassword)) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    System.out.println("\nPassword reached: " + combination);
                    System.out.println("It took " + count + " attempts to break your password");

                    // Calculate time taken to guess a password
                    long milliseconds = elapsedTime % 1000;
                    long seconds = (elapsedTime / 1000) % 60;
                    long minutes = (elapsedTime / (1000 * 60)) % 60;
                    long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                    long days = elapsedTime / (1000 * 60 * 60 * 24);

                    // Construct the "time taken" message
                    StringBuilder timeTaken = new StringBuilder("Time taken: ");
                    if (days > 0) {
                        timeTaken.append(days).append(" day");
                        if (days > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (hours > 0) {
                        timeTaken.append(hours).append(" hour");
                        if (hours > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (minutes > 0) {
                        timeTaken.append(minutes).append(" minute");
                        if (minutes > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    if (seconds > 0) {
                        timeTaken.append(seconds).append(" second");
                        if (seconds > 1) {
                            timeTaken.append("s");
                        }
                        timeTaken.append(" & ");
                    }
                    timeTaken.append(milliseconds).append(" millisecond");

                    System.out.println(timeTaken);
                    System.exit(0);
                }
            }

            if (combination.length() < maxLength) {
                for (int i = 0; i < CHARS.length(); i++) {
                    String newCombination = combination + CHARS.charAt(i);
                    count = generateCombinations(newCombination, length, maxLength, startTime, targetPassword, count);
                }
            }
            // Increment count only when combination length matches the target length
            if (combination.length() == length) {
                count++;
            }
            return count;
        }
    }
}