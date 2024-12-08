package procurementordertrackingsystem.entities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.roles.Administrators;
import procurementordertrackingsystem.roles.InventoryManager;
import procurementordertrackingsystem.roles.FinanceManager;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;

public class LoginPage {
    // Method to get the username and password from the user and perform the login check
    public void login() {
        CRUDOntoFile crud = new CRUDOntoFile(); // Directly initializing CRUDOntoFile
        DataFilePaths dataFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data"); // Directly initializing DataFilePaths
        File userFile = dataFilePaths.getUserFile(); // Get the user file path
        int attemptCount = 0;
        final int maxAttempts = 3;
        
        Scanner scanner = new Scanner(System.in);
        while(attemptCount<maxAttempts){
        // Prompt the user for username and password
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Attempt to authenticate the user
        user loggedInUser = authenticateUser(username, password, crud, userFile);

        if (loggedInUser != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + loggedInUser.getName() + " (" + loggedInUser.getRole() + ")");
            handleUserRole(loggedInUser);
            return;
        } else {
            attemptCount++;
            if (attemptCount < maxAttempts) {
            System.out.println("Login failed. Please check your username and password. Attempts left: " 
                               + (maxAttempts - attemptCount));
           }else {
            System.out.println("Login failed. Maximum attempts reached. Exiting the system.");
        }

        
            }
        }
        scanner.close();
    }
    // Method to authenticate the user by comparing username and password with the file
    private user authenticateUser(String username, String password, CRUDOntoFile crud, File userFile) {
        try {
            // Read all user data from the file
            List<String> lines = crud.readFromAFile(userFile);

            // Check each user in the file
            for (String line : lines) {
                String[] userDetails = line.split(","); // Split each line by comma
                if (userDetails.length == 6) {
                    String storedUsername = userDetails[3]; // Username is at index 3
                    String storedPassword = userDetails[5]; // Password is at index 5

                    // Check if credentials match
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        // Create and return a User object if credentials match
                        user user = new user();
                        user.SetUserID(userDetails[0]);
                        user.setName(userDetails[1]);
                        user.SetRole(userDetails[2]);
                        user.setUsername(storedUsername);
                        user.setEmail(userDetails[4]);
                        user.setPassword(storedPassword);
                        return user; // Successful login
                    }
                }
            }
            // Return null if no match is found
            return null;

        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
            return null;
        }
    }
    private void handleUserRole(user loggedInUser) {
        String role = loggedInUser.getRole();
        
        // Check the user's role and perform corresponding actions
        switch (role) {
            case "Administrators":
                System.out.println("You have administrative access.");
                Administrators admin = new Administrators ();
                admin.displayMenu();
                break;
            case "Finance Manager":
                System.out.println("You have finance access.");
                FinanceManager fm = new FinanceManager();
                fm.displayMenu();
                break;
            case "Inventory Manager":
                System.out.println("You have inventory management access.");
                InventoryManager.menu();
                break;
            case "Sales Manager":
                System.out.println("You have sales manager access.");
                // Add sales manager-related tasks here
                break;
            case "Purchase Manager":
                System.out.println("You have purchase manager access.");
                // Add purchase manager-related tasks here
                break;
            default:
                System.out.println("Unknown role: " + role);
                break;
        }
    }
}
