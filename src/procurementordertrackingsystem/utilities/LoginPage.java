package procurementordertrackingsystem.utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.roles.Administrators;
import procurementordertrackingsystem.roles.InventoryManager;
import procurementordertrackingsystem.roles.FinanceManager;
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
            // Prompt the user for username
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            // Prompt the user for password
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
                Administrators admin = new Administrators();
                admin.displayMenu();
                break;
            case "Finance Manager":
                FinanceManager fm = new FinanceManager();
                fm.displayMenu(role);
                break;
            case "Inventory Manager":
                InventoryManager.menu(role);
                break;
            case "Sales Manager":
                SalesManager sm = new SalesManager();
                sm.DisplayMenu(role);
                break;
            case "Purchase Manager":
                PurchaseManager pm = new PurchaseManager();
                pm.displayMenu(role);
                break;
            default:
                System.out.println("Unknown role: " + role);
                break;
        }
    }
}
