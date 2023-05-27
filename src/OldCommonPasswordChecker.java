/*

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CommonPasswordChecker {
    private final String filePath;

    public CommonPasswordChecker(String filePath) {

        this.filePath = filePath;
    }

    public String[] getPasswords() {

        List<String> passwords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] passwordArray = line.split("\n");
                Collections.addAll(passwords, passwordArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords.toArray(new String[0]);
    }
}

 */



/*
class CommonPasswordList {
    public String[] getPasswords() {
        return new String[]{
                "password", "123456", "123456789", "12345678", "12345", "1234567", "1234567",
                // Add more common passwords here...
                "letmein", "qwerty", "admin", "welcome", "monkey", "password1", "superman"
        };
    }
}

 */