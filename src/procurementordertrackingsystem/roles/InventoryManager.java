package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.Supplier;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.LoginPage;

public class InventoryManager {

    // Create an instance of DataFilePaths with the appropriate base path
    private static final DataFilePaths filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");

    // Static method to display the menu  
    public static void menu(String role) throws IOException {
        int menuOption;
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

        Scanner userOption = new Scanner(System.in);
        boolean running = true;
        if ("Administrators".equalsIgnoreCase(role)) {
            deMenu += "5. 🔙 Go Back\n";  // Admin role: option 5 is "Go Back"
        } else {
            deMenu += "5. ❌ Logout\n";  // Non-admin: option 5 is "Logout"
            deMenu += "6. ❌ Exit\n";    // Non-admin: option 6 is "Exit"
        }

        InventoryManager inventoryManager = new InventoryManager(); // Create instance
        while (running) {
            System.out.println(deMenu);
            System.out.print("💡 Enter your choice: ");

            if (userOption.hasNextInt()) {
                menuOption = userOption.nextInt();
                userOption.nextLine(); // Consume newline character
                LoginPage loginPage = new LoginPage();

                switch (menuOption) {
                    case 1 -> {
                        System.out.println("\n✅ You selected: 📥 Item Entry");
                        ItemEntry itemEntry = inventoryManager.new ItemEntry();
                        itemEntry.itemMenu(role);
                    }
                    case 2 -> {
                        System.out.println("\n✅ You selected: 📦 Supplier Registration");
                        SupplierRegistration supplierRegistration = inventoryManager.new SupplierRegistration();
                        supplierRegistration.supplierMenu(role); // Use the nested SupplierRegistration class
                    }
                    case 3 -> {
                        System.out.println("\n✅ You selected: 📊 View Stock Levels");
                        ItemEntry itemEntry3 = inventoryManager.new ItemEntry();
                        itemEntry3.viewStockLevels(itemEntry3.itemFile);
                    }
                    case 4 -> {
                        System.out.println("\n✅ You selected: 🛠️ Manage Stock Details");
                        ItemEntry itemEntry4 = inventoryManager.new ItemEntry();
                        itemEntry4.updateStockLevels();
                    }
                    case 5 -> {
                        if ("Administrators".equalsIgnoreCase(role)) {
                            System.out.println("\nGoing Back...");
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
                            System.exit(0);
                        }
                    }
                    default ->
                        System.out.println("⚠ Invalid option. Please choose a valid menu option.\n");
                }
            } else {
                System.out.println("⚠ Error: Please enter a valid number.\n");
                userOption.next();
            }
        }
        userOption.close();
        System.out.println("💤 Shutting down Inventory Manager... Goodbye!\n");
    }

    private class SupplierRegistration extends Supplier {

        private CRUDOntoFile crudUtil;
        private File supplierFile;

        public SupplierRegistration() {
            super(); // Initialize parent class variables
            crudUtil = new CRUDOntoFile();
            supplierFile = filePaths.getSupplierFile(); // Use DataFilePaths to get the supplier file
            checkOrCreateFile();
        }

        private void checkOrCreateFile() {
            try {
                if (!supplierFile.exists()) {
                    supplierFile.getParentFile().mkdirs();
                    supplierFile.createNewFile();
                }
            } catch (IOException e) {
                System.err.println("⚠ Error ensuring the existence of supplier.txt: " + e.getMessage());
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
        // Phone number validation method
        private boolean isValidPhoneNumber(String phoneNumber) {
            return phoneNumber.matches("\\+\\d{10,15}"); // Adjust this regex based on your required format
        }
        private boolean isDuplicateSupplier(String supplierName) {
            try (Scanner fileScanner = new Scanner(supplierFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts[1].equalsIgnoreCase(supplierName)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                System.err.println("⚠ Error reading supplier file: " + e.getMessage());
            }
            return false;
        }

        public void supplierMenu(String role) throws IOException {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("""
                                   *******************************
                                   \u2728 SUPPLIER MANAGEMENT MENU \u2728
                                   *******************************
                                   1. Add New Supplier
                                   2. Edit Supplier
                                   3. Delete Supplier
                                   4. Exit
                                   *******************************
                                   """);
                System.out.print("💡 Enter your choice: ");

                int choice = getValidIntegerInput(scanner, "");

                switch (choice) {
                    case 1 ->
                        addNewSupplier();
                    case 2 ->
                        editSupplier();
                    case 3 ->
                        deleteSupplier();
                    case 4 -> {
                        System.out.println("❌ Exiting Supplier Management.");
                        InventoryManager.menu(role);
                        running = false;
                    }
                    default ->
                        System.out.println("⚠ Invalid choice. Please try again.\n");
                }
            }
        }

        public void addNewSupplier() {
            Scanner scanner = new Scanner(System.in);

            do {
                String supplierName = getValidStringInput(scanner, "📋 Enter Supplier Name: ");
                if (isDuplicateSupplier(supplierName)) {
                    System.out.println("⚠ Error: Supplier with this name already exists. Please try again with a different name.");
                    continue;
                }
                setSupplierName(supplierName);
                break;

            } while (true);

            setItemID(getValidStringInput(scanner, "📦 Enter Item ID: "));

            
            String phoneNumber;
            while (true) {
                phoneNumber = getValidStringInput(scanner, "📞 Enter Phone Number (10 digits): ");
                if (isValidPhoneNumber(phoneNumber)) {
                    setPhoneNumber(phoneNumber);
                    break;
                } else {
                    System.out.println("⚠ Invalid phone number. Please enter a 10-digit number (should start with (+) symbol)...");
                }
            }

            setAddress(getValidStringInput(scanner, "🏠 Enter Address: "));
            setSupplierID(generateSupplierCode());

            String lineToSave = String.format("%s,%s,%s,%s,%s",
                    getSupplierID(), getSupplierName(), getItemID(), getPhoneNumber(), getAddress());

            try {
                crudUtil.createToFile(supplierFile, lineToSave);
                System.out.println("✅ Supplier added successfully!\n");
            } catch (IOException e) {
                System.err.println("⚠ Error writing to supplier file: " + e.getMessage());
            }
        }

        public void editSupplier() {
            Scanner scanner = new Scanner(System.in);

            try {
                readSuppliersFromFile(supplierFile);

                String supplierIDToEdit = getValidStringInput(scanner, "✏️ Enter the Supplier ID to edit: ");

                try (Scanner fileScanner = new Scanner(supplierFile)) {
                    StringBuilder updatedContent = new StringBuilder();
                    boolean found = false;

                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts[0].equals(supplierIDToEdit)) {
                            found = true;
                            System.out.println("✏️ Editing supplier: " + parts[1]);

                            int attribute = getValidIntegerInput(scanner, "Which attribute would you like to edit?\n1: Supplier Name\n2: Item ID\n3: Phone Number\n4: Address\nEnter the number corresponding to the attribute: ");

                            switch (attribute) {
                                case 1: // Supplier Name
                                    String newName;
                                    do {
                                        newName = getValidStringInput(scanner, "✏️ Enter new Supplier Name: ");
                                        if (isDuplicateSupplier(newName)) {
                                            System.out.println("⚠ Error: Supplier with this name already exists. Please try again with a different name.");
                                        }
                                    } while (isDuplicateSupplier(newName));
                                    parts[1] = newName;
                                    break;
                                case 2: // Item ID
                                    parts[2] = getValidStringInput(scanner, "✏️ Enter new Item ID: ");
                                    break;
                                case 3: // Phone Number
                                    String newPhoneNumber;
                                    while (true) {
                                        newPhoneNumber = getValidStringInput(scanner, "✏️ Enter new Phone Number (10 digits): ");
                                        if (isValidPhoneNumber(newPhoneNumber)) {
                                            parts[3] = newPhoneNumber;
                                            break;
                                        } else {
                                            System.out.println("⚠ Invalid phone number. Please enter a 10-digit number (should start with (+) symbol)...");
                                        }
                                    }
                                    break;
                                case 4: // Address
                                    parts[4] = getValidStringInput(scanner, "✏️ Enter new Address: ");
                                    break;
                                default:
                                    System.out.println("⚠ Invalid choice.\n");
                                    return;
                            }

                            updatedContent.append(String.join(",", parts)).append("\n");
                        } else {
                            updatedContent.append(line).append("\n");
                        }
                    }

                    if (!found) {
                        System.out.println("⚠ Supplier ID not found.\n");
                    } else {
                        crudUtil.writeUpdatedLinesToFile(supplierFile, List.of(updatedContent.toString().split("\n")));
                        System.out.println("✅ Supplier updated successfully!\n");
                    }

                }

            } catch (IOException e) {
                System.err.println("⚠ Error reading or writing to supplier file: " + e.getMessage());
            }
        }

        public void deleteSupplier() {
            Scanner scanner = new Scanner(System.in);

            try {
                StringBuilder fileContent = new StringBuilder();
                try (Scanner fileScanner = new Scanner(supplierFile)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        fileContent.append(line).append("\n");
                    }
                }
                System.out.println("📑 Suppliers in the system:\n" + fileContent);

                String supplierIDToDelete = getValidStringInput(scanner, "❌ Enter the Supplier ID to delete: ");

                boolean found = false;
                StringBuilder updatedContent = new StringBuilder();
                try (Scanner fileScanner = new Scanner(supplierFile)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        if (!parts[0].equals(supplierIDToDelete)) {
                            updatedContent.append(line).append("\n");
                        } else {
                            found = true;
                        }
                    }
                }

                if (!found) {
                    System.out.println("⚠ Supplier ID not found.\n");
                } else {
                    crudUtil.writeUpdatedLinesToFile(supplierFile, List.of(updatedContent.toString().split("\n")));
                    System.out.println("✅ Supplier deleted successfully!\n");
                }

            } catch (IOException e) {
                System.err.println("⚠ Error reading or writing to supplier file: " + e.getMessage());
            }
        }

        private String generateSupplierCode() {
            String prefix = "S";
            int nextId = 1;

            try (Scanner scanner = new Scanner(supplierFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String lastID = parts[0].trim();
                    String numericPart = lastID.substring(1);
                    nextId = Integer.parseInt(numericPart) + 1;
                }
            } catch (IOException e) {
                System.err.println("⚠ Error reading supplier file: " + e.getMessage());
            }

            return String.format("%s%04d", prefix, nextId);
        }

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
            itemFile = filePaths.getItemFile(); // Use DataFilePaths to get the item file
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

        static String deMenu = """
                               
                               ************************************
                               \u2728 ITEM ENTRY MENU \u2728
                               ************************************
                               Choose an option below:
                               1. Register Item
                               2. Update Item Information
                               3. Delete Item Data
                               4. View Stock Levels
                               5. Update Stock Levels
                               6. Exit
                               ************************************
                               """;

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
                            case 1 -> {
                                System.out.println("\n✅ You selected: Register Item\n");
                                addNewItem();
                            }
                            case 2 -> {
                                System.out.println("\n✅ You selected: Update Item Information\n");
                                editItem();
                            }
                            case 3 -> {
                                System.out.println("\n✅ You selected: Delete Item Data\n");
                                deleteItem();
                            }
                            case 4 -> {
                                System.out.println("\n✅ You selected: View Stock Levels\n");
                                ItemEntry itemEntry3 = new ItemEntry();
                                itemEntry3.viewStockLevels(itemEntry3.itemFile);
                            }
                            case 5 -> {
                                System.out.println("\n✅ You selected: Update Stock Levels\n");
                                updateStockLevels();
                            }
                            case 6 -> {
                                System.out.println("\n❌ Exiting Item Entry Menu...\n");
                                InventoryManager.menu(role);
                                running = false;
                            }
                            default ->
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

            // Loop until a valid item name is provided
            String itemName;
            while (true) {
                itemName = getValidStringInput(scanner, "📋 Enter Item Name: ");
                if (isDuplicateItemName(itemName)) {
                    System.out.println("⚠ Item name '" + itemName + "' already exists. Please try again.");
                } else {
                    break; // Break the loop if no duplicate found
                }
            }

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

        // Check for duplicate item names
        private boolean isDuplicateItemName(String itemName) {
            try (Scanner fileScanner = new Scanner(itemFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String existingItemName = parts[1].trim(); // Assuming item name is the second column
                    if (existingItemName.equalsIgnoreCase(itemName)) {
                        return true; // Duplicate found
                    }
                }
            } catch (IOException e) {
                System.err.println("⚠ Error reading item file: " + e.getMessage());
            }
            return false; // No duplicates found
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
                            case 1 -> {
                                // Item Name
                                String newItemName;
                                while (true) {
                                    newItemName = getValidStringInput(scanner, "Enter new Item Name: ");
                                    // Validate new item name for duplication
                                    if (isDuplicateItemName(newItemName)) {
                                        System.out.println("⚠ Item name '" + newItemName + "' already exists. Please try again.");
                                    } else {
                                        parts[1] = newItemName; // Update item name
                                        break; // Break if the name is unique
                                    }
                                }
                            }
                            case 2 -> // Stock Level
                                parts[2] = String.valueOf(getValidIntegerInput(scanner, "Enter new Stock Level: "));
                            case 3 -> // Price
                                parts[3] = String.valueOf(getValidDoubleInput(scanner, "Enter new Price: "));
                            case 4 -> // Supplier ID
                                parts[4] = getValidStringInput(scanner, "Enter new Supplier ID: ");
                            default -> {
                                System.out.println("Invalid choice.");
                                return;
                            }
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
