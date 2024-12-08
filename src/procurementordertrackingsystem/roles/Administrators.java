package procurementordertrackingsystem.roles;

import java.util.Scanner;
import procurementordertrackingsystem.entities.LoginPage;
import procurementordertrackingsystem.utilities.DataFilePaths;

public class Administrators {
    DataFilePaths filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        String menu = """
                1. Manage Users
                2. View All Users
                3. Login as Finance Manager
                4. Login as Purchase Manager
                5. Login as Inventory Manager
                6. Login as Sales Manager
                7. Logout
                8. Exit
            """;

        // Main menu loop
        while (true) {
            System.out.println(menu);
            System.out.print("Please select an option (1-8): ");
            int choice = -1;

            // Debugging: Show the prompt and validate input
            try {
                String input = scanner.nextLine();  // Capture input as a string for debugging
                System.out.println("Debug: User input: " + input);  // Debugging the input
                choice = Integer.parseInt(input);  // Try parsing the integer
                System.out.println("You selected option: " + choice);  // Debugging output
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                continue; // Retry the loop
            }

            // Handle menu options
            switch (choice) {
                case 1:
                    System.out.println("Bro whaat "); // Debugging output
                    manageUserMenu(scanner);  // Pass the scanner to the next method
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    loginAsFinanceManager();
                    break;
                case 4:
                    loginAsPurchaseManager();
                    break;
                case 5:
                    loginAsInventoryManager();
                    break;
                case 6:
                    loginAsSalesManager();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    LoginPage loginPage = new LoginPage();
                    loginPage.login();
                    break;
                case 8:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please select between 1 and 8.");
            }
        }
    }

    private void manageUserMenu(Scanner scanner) {
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
                String input = scanner.nextLine();  // Capture input as a string for debugging
                System.out.println("Debug: Submenu input: " + input);  // Debugging the input
                subChoice = Integer.parseInt(input);  // Try parsing the integer
                System.out.println("You selected submenu option: " + subChoice);  // Debugging output
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                continue; // Retry the loop
            }

            // Handle submenu options
            switch (subChoice) {
                case 1:
                    addNewUser();
                    break;
                case 2:
                    editUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    return;  // Exit to the main menu
                default:
                    System.out.println("Invalid option. Please select between 1 and 4.");
            }
        }
    }

    private void addNewUser() {
        System.out.println("Add New User functionality here.");
    }

    private void editUser() {
        System.out.println("Edit User functionality here.");
    }

    private void deleteUser() {
        System.out.println("Delete User functionality here.");
    }

    private void viewAllUsers() {
        System.out.println("View All Users functionality here.");
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
