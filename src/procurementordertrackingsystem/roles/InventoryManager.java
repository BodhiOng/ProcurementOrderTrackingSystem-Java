package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.SupplierRegistration;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.LoginPage;

public class InventoryManager {

    // Static method to display the menu  
    public static void menu(String role) throws IOException {
        int menuOption = 0;
        String deMenu = """
                        
                        ****************************************
                        \u2728 WELCOME TO INVENTORY MANAGEMENT SYSTEM \u2728
                        ****************************************
                        \ud83d\udccb Choose an option below:
                        1. \ud83d\udce5 Item Entry
                        2. \ud83d\udce6 Supplier Registration
                        3. \ud83d\udcca View Stock Levels
                        4. \ud83d\udee0\ufe0f Manage Stock Details
                        """;

        try (Scanner userOption = new Scanner(System.in)) { // Initialize Scanner with System.in
            boolean running = true;
            // Append options based on the user role
            if ("Administrators".equalsIgnoreCase(role)) {
                deMenu += "5. 🔙 Go Back\n";  // Admin role: option 5 is "Go Back"
            } else {
                deMenu += "5. ❌ Logout\n";  // Non-admin: option 5 is "Logout"
                deMenu += "6. ❌ Exit\n";    // Non-admin: option 6 is "Exit"
            }
            // Create an instance of InventoryManager
            InventoryManager inventoryManager = new InventoryManager();
            while (running) {
                System.out.println(deMenu); // Display the menu
                System.out.print("💡 Enter your choice: ");
                
                // Validate input
                if (userOption.hasNextInt()) {
                    menuOption = userOption.nextInt();
                    userOption.nextLine(); // Consume newline character
                    LoginPage loginPage = new LoginPage();
                    
                    switch (menuOption) {
                        case 1 -> {
                            System.out.println("\n✅ You selected: 📥 Item Entry");
                            ItemEntry itemEntry = inventoryManager.new ItemEntry(); // Create an instance of the nested class
                            itemEntry.itemMenu(role); // Call the item menu
                        }
                        case 2 -> {
                            System.out.println("\n✅ You selected: 📦 Supplier Registration");
                            SupplierRegistration.supplierMenu(role);
                        }
                        case 3 -> {
                            System.out.println("\n✅ You selected: 📊 View Stock Levels");
                            ItemEntry itemEntry3 = inventoryManager.new ItemEntry(); 
                            itemEntry3.viewStockLevels(itemEntry3.itemFile);
                        }
                        case 4 -> {
                            System.out.println("\n✅ You selected: 🛠️ Manage Stock Details");
                            ItemEntry itemEntry4 = inventoryManager.new ItemEntry(); // Create an instance for stock management
                            itemEntry4.updateStockLevels();
                        }
                        case 5 -> {
                            if ("Administrators".equalsIgnoreCase(role)) {
                                System.out.println("\n🔙 Going Back...");
                                return;
                            } else {
                                System.out.println("\n❌ Logged Out Inventory Management System... See you next time!\n");
                                loginPage.login();
                                running = false;
                            }
                        }
                        case 6 -> {
                            if ("Administrators".equalsIgnoreCase(role)) {
                                System.out.println("⚠ Invalid option.");
                            } else {
                                System.out.println("\n❌ Exiting the system.");
                                System.exit(0); // Exit the system for non-admin users
                            }
                        }
                        default -> System.out.println("⚠ Invalid option. Please choose a valid menu option.\n");
                    }
                } else {
                    System.out.println("⚠ Error: Please enter a valid number.\n");
                    userOption.next(); // Consume invalid input
                }
            }
            // Close the scanner when done
        }
        System.out.println("💤 Shutting down Inventory Manager... Goodbye!\n");
    }
    
    private class ItemEntry extends Item {
        private CRUDOntoFile crudUtil;
        private File itemFile;

        // Constructor that calls the parent Item constructor
        public ItemEntry(String itemID, String itemName, int stockLevel, double price, String supplierID) {
            super(itemID, itemName, stockLevel, price, supplierID);
            this.init();
        }

        public ItemEntry() {
            this.init();
        }

        private void init() {
            crudUtil = new CRUDOntoFile();
            itemFile = new File("src/procurementordertrackingsystem/data/item.txt"); 
            checkOrCreateFile();
        }

        private void checkOrCreateFile() {
            try {
                if (!itemFile.exists()) {
                    itemFile.getParentFile().mkdirs(); // Create directories if they don't exist
                    itemFile.createNewFile(); // Create the file
                }
            } catch (IOException e) {
                System.err.println("⚠ Error ensuring the existence of item.txt: " + e.getMessage());
            }
        }

        private int getValidIntegerInput(Scanner scanner, String prompt) {
            while (true) {
                try {
                    System.out.print(prompt);
                    return Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Invalid input. Please enter a valid integer.");
                }
            }
        }

        private double getValidDoubleInput(Scanner scanner, String prompt) {
            while (true) {
                try {
                    System.out.print(prompt);
                    return Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Invalid input. Please enter a valid decimal number.");
                }
            }
        }

        private String getValidStringInput(Scanner scanner, String prompt) {
            while (true) {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    return input;
                } else {
                    System.out.println("⚠ Input cannot be empty. Please try again.");
                }
            }
        }

        static String deMenu = "\n************************************\n" +
                "✨ ITEM ENTRY MENU ✨\n" +
                "************************************\n" +
                "Choose an option below:\n" +
                "1. Register Item\n" +
                "2. Update Item Information\n" +
                "3. Delete Item Data\n" +
                "4. View Stock Levels\n" +
                "5. Update Stock Levels\n" +
                "6. Exit\n" +
                "************************************\n";

        public void itemMenu(String role) throws IOException {
            try (Scanner userOption2 = new Scanner(System.in)) {
                boolean running = true;
                while (running) {
                    System.out.println(deMenu); // Display the menu
                    System.out.print("💡 Enter your choice: ");

                    if (userOption2.hasNextInt()) {
                        int menuOption1 = userOption2.nextInt();
                        userOption2.nextLine(); // Consume newline

                        switch (menuOption1) {
                            case 1:
                                System.out.println("\n✅ You selected: Register Item\n");
                                addNewItem();
                                break;
                            case 2:
                                System.out.println("\n✅ You selected: Update Item Information\n");
                                editItem();
                                break;
                            case 3:
                                System.out.println("\n✅ You selected: Delete Item Data\n");
                                deleteItem();
                                break;
                            case 4:
                                System.out.println("\n✅ You selected: View Stock Levels\n");
                                ItemEntry itemEntry3 = new ItemEntry(); 
                                itemEntry3.viewStockLevels(itemEntry3.itemFile);
                                break;
                            case 5:
                                System.out.println("\n✅ You selected: Update Stock Levels\n");
                                updateStockLevels();
                                break;
                            case 6:
                                System.out.println("\n❌ Exiting Item Entry Menu...\n");
                                InventoryManager.menu(role);  
                                running = false;
                                break;
                            default:
                                System.out.println("⚠ Invalid option. Please choose a valid menu option.\n");
                        }
                    } else {
                        System.out.println("⚠ Error: Please enter a valid number.\n");
                        userOption2.next(); // Consume invalid input
                    }
                }
            }
            System.out.println("💤 Returning to Inventory Manager Menu. Goodbye!\n");
        }

        // Add a new item to the inventory
        public void addNewItem() {
            Scanner scanner = new Scanner(System.in);

            String itemID = generateItemCode();

            String itemName = getValidStringInput(scanner, "📋 Enter Item Name: ");
            int stockLevel = getValidIntegerInput(scanner, "🔢 Enter Stock Level: ");
            double price = getValidDoubleInput(scanner, "💸 Enter Price: ");
            String supplierID = getValidStringInput(scanner, "📅 Enter Supplier ID: ");

            Item newItem = new Item(itemID, itemName, stockLevel, price, supplierID);
            String lineToSave = String.format("%s,%s,%d,%.2f,%s",
                    newItem.getItemID(),
                    newItem.getItemName(),
                    newItem.getStockLevel(),
                    newItem.getPrice(),
                    newItem.getSupplierID());

            try {
                crudUtil.createToFile(itemFile, lineToSave);
                System.out.println("✅ Item added successfully!\n");
            } catch (IOException e) {
                System.err.println("⚠ Error writing to item file: " + e.getMessage());
            }
        }

        // Generate a unique item code (simplified version)
        private String generateItemCode() {
            String prefix = "I";
            int nextId = 1;

            try {
                Scanner scanner = new Scanner(itemFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String lastID = parts[0].trim(); // Assuming ID is in the first column
                    String numericPart = lastID.substring(1); // Remove the prefix 'I'
                    nextId = Integer.parseInt(numericPart) + 1;
                }
            } catch (IOException e) {
                System.err.println("⚠ Error reading item file: " + e.getMessage());
            }

            return String.format("%s%04d", prefix, nextId);
        }

        // Edit an existing item in the inventory
        public void editItem() {
            Scanner scanner = new Scanner(System.in);

            try {
                // Read the file and print its contents
                Scanner fileScanner = new Scanner(itemFile);
                boolean found = false;
                StringBuilder fileContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    fileContent.append(line).append("\n");
                }

                // Display items
                System.out.println("Items in inventory:\n" + fileContent);

                String itemIDToEdit = getValidStringInput(scanner, "Enter the Item ID to edit: ");

                // Read the file again to update the data
                fileScanner = new Scanner(itemFile);
                StringBuilder updatedContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts[0].equals(itemIDToEdit)) {
                        found = true;
                        System.out.println("Editing item: " + parts[1]);

                        // Prompt for which field to edit
                        int attribute = getValidIntegerInput(scanner, "Which attribute would you like to edit?\n1: Item Name\n2: Stock Level\n3: Price\n4: Supplier ID\nEnter the number corresponding to the attribute: ");

                        switch (attribute) {
                            case 1:
                                parts[1] = getValidStringInput(scanner, "Enter new Item Name: ");
                                break;
                            case 2:
                                parts[2] = String.valueOf(getValidIntegerInput(scanner, "Enter new Stock Level: "));
                                break;
                            case 3:
                                parts[3] = String.valueOf(getValidDoubleInput(scanner, "Enter new Price: "));
                                break;
                            case 4:
                                parts[4] = getValidStringInput(scanner, "Enter new Supplier ID: ");
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                return;
                        }

                        // Rebuild the line with updated fields
                        updatedContent.append(String.join(",", parts)).append("\n");
                    } else {
                        updatedContent.append(line).append("\n");
                    }
                }

                if (!found) {
                    System.out.println("Item ID not found.");
                } else {
                    // Write updated content back to file
                    crudUtil.writeUpdatedLinesToFile(itemFile, List.of(updatedContent.toString().split("\n")));
                    System.out.println("Item updated successfully!");
                }

            } catch (IOException e) {
                System.err.println("Error reading or writing to item file: " + e.getMessage());
            }
        }

        // Delete an item from the inventory
        public void deleteItem() {
            Scanner scanner = new Scanner(System.in);

            try {
                // Read the file and print its contents
                Scanner fileScanner = new Scanner(itemFile);
                boolean found = false;
                StringBuilder fileContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    fileContent.append(line).append("\n");
                }

                // Display items
                System.out.println("Items in inventory:\n" + fileContent);

                String itemIDToDelete = getValidStringInput(scanner, "Enter the Item ID to delete: ");

                // Read the file again to remove the data
                fileScanner = new Scanner(itemFile);
                StringBuilder updatedContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (!parts[0].equals(itemIDToDelete)) {
                        updatedContent.append(line).append("\n");
                    } else {
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("Item ID not found.");
                } else {
                    // Write updated content back to file
                    crudUtil.writeUpdatedLinesToFile(itemFile, List.of(updatedContent.toString().split("\n")));
                    System.out.println("Item deleted successfully!");
                }

            } catch (IOException e) {
                System.err.println("Error reading or writing to item file: " + e.getMessage());
            }
        }


        // Update stock levels after receiving stock from suppliers
        public void updateStockLevels() {
            Scanner scanner = new Scanner(System.in);

            try {
                // Read the file and print its contents
                Scanner fileScanner = new Scanner(itemFile);
                boolean found = false;
                StringBuilder finalUpdatedContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    finalUpdatedContent.append(line).append("\n");
                }

                // Display items
                System.out.println("Items in inventory:\n" + finalUpdatedContent);

                String itemIDToUpdate = getValidStringInput(scanner, "Enter the Item ID to update stock: ");

                // Read the file again to update stock level
                fileScanner = new Scanner(itemFile);
                StringBuilder updatedContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts[0].equals(itemIDToUpdate)) {
                        found = true;
                        parts[2] = String.valueOf(getValidIntegerInput(scanner, "Enter new stock level: "));
                        updatedContent.append(String.join(",", parts)).append("\n");
                    } else {
                        updatedContent.append(line).append("\n");
                    }
                }

                if (!found) {
                    System.out.println("Item ID not found.");
                } else {
                    // Write updated content back to the file
                    crudUtil.writeUpdatedLinesToFile(itemFile, List.of(updatedContent.toString().split("\n")));
                    System.out.println("Stock level updated successfully!");
                }

            } catch (IOException e) {
                System.err.println("Error reading or writing to item file: " + e.getMessage());
            }
        }
    }

        
}
