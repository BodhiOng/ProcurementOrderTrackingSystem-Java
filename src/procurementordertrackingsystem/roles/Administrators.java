package procurementordertrackingsystem.roles;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
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

            // Debugging: Show the prompt and validate input
            try {
                String input = scanner.nextLine();  // Capture input as a string for debugging
                choice = Integer.parseInt(input);  // Try parsing the integer 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                continue; // Retry the loop
            }

            // Handle menu options
            switch (choice) {
                case 1:
                    manageUserMenu(scanner);  // Pass the scanner to the next method
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
                    LoginPage loginPage = new LoginPage();
                    loginPage.login();
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
        int maxID = 0;  // Initialize maxID to 0

        // Get the path of the user file
        DataFilePaths userFilePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        File userFile = userFilePaths.getUserFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            // Read through each line of the file
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");  // Assuming the ID is the first column (index 0)
                String userID = userDetails[0];  // Get the ID from the first column
                if (userID.startsWith("U")) {  // Check if it starts with 'U'
                    try {
                        int currentID = Integer.parseInt(userID.substring(1));  // Get the numeric part of the ID
                        if (currentID > maxID) {
                            maxID = currentID;  // Update maxID if current ID is greater
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format: " + userID);  // Handle parsing errors
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }

        // Increment the maxID by 1 to get the next ID
        int newID = maxID + 1;

        // Return the new ID formatted with leading zeros (e.g., U0001, U0002, etc.)
        return String.format("U%04d", newID);  // Format with leading zeros to ensure 4 digits
    }

    private void manageUserMenu(Scanner scanner) throws IOException {
        String manageUsersMenu = """
                Manage Users
                1. Add New User
                2. Edit User
                3. Delete User
                4. Back to Main Menu
            """;

        // Submenu loop for managing users
        while (true) {
            System.out.println(manageUsersMenu);
            System.out.print("Please select an option (1-4): ");
            int subChoice = -1;

            // Validate submenu input
            try {
                String input = scanner.nextLine();
                subChoice = Integer.parseInt(input); 

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                continue; // Retry the loop
            }

            // Handle submenu options
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
                    return;  // Exit to the main menu
                default:
                    System.out.println("Invalid option. Please select between 1 and 4.");
            }
        }
    }

    private void addNewUser(Scanner scanner) throws IOException {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();

        System.out.println("Select role:");
        // Displaying the roles
        System.out.println("1. Sales Manager");
        System.out.println("2. Purchase Manager");
        System.out.println("3. Finance Manager");
        System.out.println("4. Inventory Manager");
        System.out.println("5. Administrators");

        // Input to select the role
        String role = "";
        boolean validRole = false;

        while (!validRole) {
            System.out.print("Enter the number corresponding to the role: ");
            String roleChoice = scanner.nextLine();

            // Validating the role selection
            switch (roleChoice) {
                case "1":
                    role = "Sales Manager";  // Store role1
                    validRole = true;
                    break;
                case "2":
                    role = "Purchase Manager";  // Store role2
                    validRole = true;
                    break;
                case "3":
                    role = "Finance Manager";  // Store role3
                    validRole = true;
                    break;
                case "4":
                    role = "Inventory Manager";  // Store role4
                    validRole = true;
                    break;
                case "5":
                    role = "Administrators";  // Store role5
                    validRole = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a valid role.");
                    break;
            }
        }

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        String email = "";
        boolean validEmail = false;

        // Validate email format
        while (!validEmail) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();

            // Email pattern to match a simple email structure
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);

            if (matcher.matches()) {
                validEmail = true;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String userID = generateID();

        String newUserLine = String.format("%s,%s,%s,%s,%s,%s", userID, name, role, username, email, password);
        File userfile = filePaths.getUserFile();

        try {
            crudOntoFile.createToFile(userfile, newUserLine);  // Use CRUDOntoFile class to append the new user
            System.out.println("New user added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding new user: " + e.getMessage());
        }
    }

    private void editUser(Scanner scanner) throws IOException {
        DataFilePaths userFile = new DataFilePaths("src/procurementordertrackingsystem/data");
        File file = userFile.getUserFile();

        // Read all lines from the file
        List<String> users = Files.readAllLines(file.toPath());

        // Check if there are users
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        User userinformation = new User(users);
        userinformation.displayUsers();

        System.out.print("Enter the User ID of the user to edit: ");
        String userID = scanner.nextLine();

        // Flag to check if user is found
        boolean found = false;

        // Find the user by ID
        for (int i = 0; i < users.size(); i++) {
            String[] userDetails = users.get(i).split(",");
            if (userDetails[0].equals(userID)) {
                found = true;
                System.out.println("User found: " + users.get(i));

                // Present options to edit
                System.out.println("Which field would you like to edit?");
                System.out.println("1. Name");
                System.out.println("2. Role");
                System.out.println("3. Username");
                System.out.println("4. Email");
                System.out.println("5. Cancel");

                int choice = Integer.parseInt(scanner.nextLine());

                // Edit the chosen field
                switch (choice) {
                    case 1: // Edit Name
                        System.out.print("Enter new name : ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            userDetails[1] = newName;
                        }
                        break;
                    case 2: // Edit Email
                        System.out.print("Enter new Role : ");
                        String newEmail = scanner.nextLine();
                        if (!newEmail.isEmpty()) {
                            userDetails[2] = newEmail;
                        }
                        break;
                    case 3: // Edit Username
                        System.out.print("Enter new username : ");
                        String newUsername = scanner.nextLine();
                        if (!newUsername.isEmpty()) {
                            userDetails[3] = newUsername;
                        }
                        break;
                    case 4: // Edit Role
                        System.out.print("Enter new Email : ");
                        String newRole = scanner.nextLine();
                        if (!newRole.isEmpty()) {
                            userDetails[4] = newRole;
                        }
                        break;
                    case 5: // Cancel
                        System.out.println("Edit canceled.");
                        return;
                    default:
                        System.out.println("Invalid option. Edit canceled.");
                        return;
                }
                // Update the user line in the list with the edited details
                String updatedUser = String.format("%s,%s,%s,%s,%s,%s", userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5]);
                users.set(i, updatedUser);

                // Write the updated list back to the file
                Files.write(file.toPath(), users);
                System.out.println("User updated successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("User ID not found.");
        }
    }

    private void deleteUser(Scanner scanner) throws IOException {

        // Read the user data
        DataFilePaths userFile = new DataFilePaths("src/procurementordertrackingsystem/data");
        File file = userFile.getUserFile();

        // Read all lines from the file
        List<String> users = Files.readAllLines(file.toPath());

        User userinformation = new User(users);
        userinformation.displayUsers();

        System.out.print("Enter the User ID of the user to delete: ");
        String userID = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            String[] userDetails = users.get(i).split(",");
            if (userDetails[0].equals(userID)) {
                found = true;
                users.remove(i); // Remove the user from the list
                Files.write(file.toPath(), users); // Write back the updated list to file
                System.out.println("User deleted successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("User ID not found.");
        }
    }

    private void loginAsFinanceManager() {
        System.out.println("Login as Finance Manager functionality here.");
    }

    private void loginAsPurchaseManager() {
        System.out.println("Login as Purchase Manager functionality here.");
    }

    private void loginAsInventoryManager() {
        System.out.println("Login as Inventory Manager functionality here.");
    }

    private void loginAsSalesManager() {
        System.out.println("Login as Sales Manager functionality here.");
    }
}
