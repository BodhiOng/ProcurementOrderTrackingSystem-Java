package procurementordertrackingsystem.roles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import procurementordertrackingsystem.utilities.LoginPage;
import procurementordertrackingsystem.entities.User;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.IDGenerator;

public class Administrators implements IDGenerator {

    DataFilePaths filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();

    public void displayMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String menu = """
                1. Manage Users
                2. Login as Finance Manager
                3. Login as Purchase Manager
                4. Login as Inventory Manager
                5. Login as Sales Manager
                6. Logout
                7. Exit
            """;

        // Main menu loop
        while (true) {
            System.out.println(menu);
            System.out.print("Please select an option (1-7): ");
            int choice = -1;

            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1:
                    manageUserMenu(scanner);
                    break;
                case 2:
                    loginAsFinanceManager();
                    break;
                case 3:
                    loginAsPurchaseManager();
                    break;
                case 4:
                    loginAsInventoryManager();
                    break;
                case 5:
                    loginAsSalesManager();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    new LoginPage().login();
                    break;
                case 7:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please select between 1 and 7.");
            }
        }
    }

    @Override
    public String generateID() {
        int maxID = 0;
        DataFilePaths userFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        File userFile = userFilePaths.getUserFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                String userID = userDetails[0];
                if (userID.startsWith("U")) {
                    try {
                        int currentID = Integer.parseInt(userID.substring(1));
                        if (currentID > maxID) {
                            maxID = currentID;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format: " + userID);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }

        int newID = maxID + 1;
        return String.format("U%04d", newID);
    }

    private void manageUserMenu(Scanner scanner) throws IOException {
        String manageUsersMenu = """
                Manage Users
                1. Add New User
                2. Edit User
                3. Delete User
                4. Back to Main Menu
            """;

        while (true) {
            System.out.println(manageUsersMenu);
            System.out.print("Please select an option (1-4): ");
            int subChoice = -1;

            try {
                String input = scanner.nextLine();
                subChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                continue;
            }

            switch (subChoice) {
                case 1:
                    addNewUser(scanner);
                    break;
                case 2:
                    editUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please select between 1 and 4.");
            }
        }
    }

    private void addNewUser(Scanner scanner) throws IOException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.println("Select role:");
        System.out.println("1. Sales Manager");
        System.out.println("2. Purchase Manager");
        System.out.println("3. Finance Manager");
        System.out.println("4. Inventory Manager");
        System.out.println("5. Administrators");

        String role = getValidRole(scanner);

        
        String username = getValidUsername(scanner);

        String email = getValidEmail(scanner);
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String userID = generateID();

        String newUserLine = String.format("%s,%s,%s,%s,%s,%s", userID, name, role, username, email, password);
        File userfile = filePaths.getUserFile();

        try {
            crudOntoFile.createToFile(userfile, newUserLine);
            System.out.println("New user added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding new user: " + e.getMessage());
        }
    }

    private String getValidRole(Scanner scanner) {
        String role = "";
        boolean validRole = false;
        while (!validRole) {
            System.out.print("Enter the number corresponding to the role: ");
            String roleChoice = scanner.nextLine();

            switch (roleChoice) {
                case "1":
                    role = "Sales Manager";
                    validRole = true;
                    break;
                case "2":
                    role = "Purchase Manager";
                    validRole = true;
                    break;
                case "3":
                    role = "Finance Manager";
                    validRole = true;
                    break;
                case "4":
                    role = "Inventory Manager";
                    validRole = true;
                    break;
                case "5":
                    role = "Administrators";
                    validRole = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a valid role.");
                    break;
            }
        }
        return role;
    }
    
        private String getValidEmail(Scanner scanner) {
        String email;
        DataFilePaths userFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        File userFile = userFilePaths.getUserFile();
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();

            // Validate email format
            if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                System.out.println("Invalid email format. Please try again.");
                continue;
            }

            // Check if the email already exists
            if (isEmailOrUsernameExists(userFile, email,null)) {
                System.out.println("This email is already registered. Please use a different email.");
            } else {
                break; // Exit loop if email is valid and does not exist
            }
        }
        return email;
    }

    private String getValidUsername(Scanner scanner) {
        String username;
        DataFilePaths userFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        File userFile = userFilePaths.getUserFile();
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine().trim();

            // Check if the email already exists
            if (isEmailOrUsernameExists(userFile, null ,username)) {
                System.out.println("This username is already registered. Please use a different username.");
            } else {
                break; 
            }
        }
        return username;
    }

    private boolean isEmailOrUsernameExists(File userFile, String email, String username) {
        CRUDOntoFile fileHandler = new CRUDOntoFile(); // Instantiate the CRUDOntoFile class
        try {
            List<String> lines = fileHandler.readFromAFile(userFile); // Reuse the method from CRUDOntoFile
            for (String line : lines) {
                String[] userDetails = line.split(",");
                // Ensure the line has sufficient details to avoid ArrayIndexOutOfBoundsException
                if (userDetails.length > 4) {
                    String fileEmail = userDetails[4].trim();
                    String fileUsername = userDetails[3].trim(); // Assuming username is stored in the first column

                    // Check if either email or username matches
                    if (fileEmail.equalsIgnoreCase(email) || fileUsername.equalsIgnoreCase(username)) {
                        return true; // Email or username found
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false; // Neither email nor username found
    }

    private void editUser(Scanner scanner) throws IOException {
        DataFilePaths userFile = new DataFilePaths("src/procurementordertrackingsystem/data");
        File file = userFile.getUserFile();

        List<String> users = Files.readAllLines(file.toPath());

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        displayUsers();

        System.out.print("Enter the User ID of the user to edit: ");
        String userID = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            String[] userDetails = users.get(i).split(",");
            if (userDetails[0].equals(userID)) {
                found = true;

                System.out.println("Which field would you like to edit?");
                System.out.println("1. Name");
                System.out.println("2. Role");
                System.out.println("3. Username");
                System.out.println("4. Email");
                System.out.println("5. Cancel");
                System.out.print("Enter what you wish to edit: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            userDetails[1] = newName;
                        }
                        break;
                    case 2:
                        String newRole = getValidRole(scanner);
                        userDetails[2] = newRole;
                        break;
                    case 3:
                        System.out.print("Enter new username: ");
                        String newUsername = getValidUsername(scanner);
                        if (!newUsername.isEmpty()) {
                            userDetails[3] = newUsername;
                        }
                        break;
                    case 4:
                        System.out.print("Enter new email: ");
                        String newEmail = getValidEmail(scanner);
                        if (!newEmail.isEmpty()) {
                            userDetails[4] = newEmail;
                        }
                        break;
                    case 5:
                        System.out.println("Cancelling edit operation.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }

                String updatedUser = String.join(",", userDetails);
                users.set(i, updatedUser);

                Files.write(file.toPath(), users);

                System.out.println("User updated successfully.");
                return;
            }
        }

        if (!found) {
            System.out.println("User ID not found.");
        }
    }

    private void deleteUser(Scanner scanner) throws IOException {
        DataFilePaths userFile = new DataFilePaths("src/procurementordertrackingsystem/data");
        File file = userFile.getUserFile();

        List<String> users = Files.readAllLines(file.toPath());

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        displayUsers();

        System.out.print("Enter the User ID of the user to delete: ");
        String userID = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            String[] userDetails = users.get(i).split(",");
            if (userDetails[0].equals(userID)) {
                found = true;
                users.remove(i);

                Files.write(file.toPath(), users);

                System.out.println("User deleted successfully.");
                return;
            }
        }

        if (!found) {
            System.out.println("User ID not found.");
        }
    }

    // Placeholder for login methods
    private void loginAsFinanceManager() {
        /* Implementation */ }

    private void loginAsPurchaseManager() {
        /* Implementation */ }

    private void loginAsInventoryManager() {
        /* Implementation */ }

    private void loginAsSalesManager() {
        /* Implementation */ }

    private void displayUserInfo(String[] userDetails) {
        // Display the user's details (Name, Username, Email, etc.)
        System.out.println("Username: " + userDetails[0]); // Name is at index 1
        System.out.println("Name: " + userDetails[1]); // Name is at index 1
        System.out.println("Username: " + userDetails[3]); // Username is at index 3
        System.out.println("Email: " + userDetails[4]); // Email is at index 4
    }

    private void displayUsers() throws IOException {
        // Assuming the file contains the list of users
        DataFilePaths userFile = new DataFilePaths("src/procurementordertrackingsystem/data");
        File file = userFile.getUserFile();

        List<String> users = Files.readAllLines(file.toPath());

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Displaying Users:");

        // Group users by their role
        Map<String, List<String[]>> usersByRole = new HashMap<>();

        // Group users by their role
        for (String user : users) {
            String[] userDetails = user.split(",");
            String role = userDetails[2]; // Role is at index 2

            // Add the user to the list of users for their role
            usersByRole.computeIfAbsent(role, k -> new ArrayList<>()).add(userDetails);
        }

        // Now display users, grouped by role, with only the role printed once per section
        for (Map.Entry<String, List<String[]>> entry : usersByRole.entrySet()) {
            String role = entry.getKey();
            List<String[]> usersInRole = entry.getValue();

            // Display the section header for the role
            System.out.println("Role : " + role);
            System.out.println();  // Blank line between the role and user details

            // Display each user under the same role
            for (String[] userDetails : usersInRole) {
                displayUserInfo(userDetails);
                System.out.println();  // Add a blank line after each user's details
            }

            // Separator after users of each role
            System.out.println("**********************");
        }
    }

}
