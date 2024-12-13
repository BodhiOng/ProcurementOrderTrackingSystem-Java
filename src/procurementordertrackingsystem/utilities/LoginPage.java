package procurementordertrackingsystem.utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.roles.Administrators;
import procurementordertrackingsystem.roles.InventoryManager;
import procurementordertrackingsystem.roles.FinanceManager;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.entities.User;
import procurementordertrackingsystem.roles.PurchaseManager;
import procurementordertrackingsystem.roles.SalesManager;

public class LoginPage extends User { // Extending User class

    // Method to get the username and password from the user and perform the login check
    public void login() throws IOException {
        CRUDOntoFile crud = new CRUDOntoFile();
        DataFilePaths dataFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        File userFile = dataFilePaths.getUserFile();
        int attemptCount = 0;
        final int maxAttempts = 3;

        Scanner scanner = new Scanner(System.in);
        while (attemptCount < maxAttempts) {
            // Prompt the user for username and password
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Attempt to authenticate the user
            User loggedInUser = authenticateUser(username, password, crud, userFile);

            if (loggedInUser != null) {
                // Successful login
                setUserID(loggedInUser.getUserID()); // Set inherited attributes
                setName(loggedInUser.getName());
                setRole(loggedInUser.getRole());
                setUsername(loggedInUser.getUsername());
                setEmail(loggedInUser.getEmail());
                setPassword(loggedInUser.getPassword());

                System.out.println("Login successful!");
                System.out.println("Welcome, " + getName() + " (" + getRole() + ")");
                handleUserRole(loggedInUser); // Role-specific functionality
                return;
            } else {
                attemptCount++;
                if (attemptCount < maxAttempts) {
                    System.out.println("Login failed. Please check your username and password. Attempts left: "
                            + (maxAttempts - attemptCount));
                } else {
                    System.out.println("Login failed. Maximum attempts reached. Exiting the system.");
                }
            }
        }
        scanner.close();
    }

    // Method to authenticate the user by comparing username and password with the file
    private User authenticateUser(String username, String password, CRUDOntoFile crud, File userFile) {
        try {
            List<String> lines = crud.readFromAFile(userFile);

            for (String line : lines) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 6) {
                    String storedUsername = userDetails[3];
                    String storedPassword = userDetails[5];

                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        User user = new User(); // Create a User object for successful authentication
                        user.setUserID(userDetails[0]);
                        user.setName(userDetails[1]);
                        user.setRole(userDetails[2]);
                        user.setUsername(storedUsername);
                        user.setEmail(userDetails[4]);
                        user.setPassword(storedPassword);
                        return user;
                    }
                }
            }
            return null;

        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
            return null;
        }
    }

    // Handle role-based functionality
    private void handleUserRole(User loggedInUser) throws IOException {
        String role = loggedInUser.getRole();

        switch (role) {
            case "Administrators":
                System.out.println("You have administrative access.");
                Administrators admin = new Administrators();
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
                SalesManager sm = new SalesManager();
                sm.DisplayMenu();
                break;
            case "Purchase Manager":
                System.out.println("You have purchase manager access.");
                PurchaseManager pm = new PurchaseManager();
                pm.displayMenu();
                break;
            default:
                System.out.println("Unknown role: " + role);
                break;
        }
    }
}
