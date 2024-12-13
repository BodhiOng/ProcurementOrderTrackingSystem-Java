package procurementordertrackingsystem.entities;

import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.roles.InventoryManager;

public class SupplierRegistration {
    private CRUDOntoFile crudUtil;
    private File supplierFile;

    public SupplierRegistration() {
        crudUtil = new CRUDOntoFile();
        supplierFile = new File("src/procurementordertrackingsystem/data/supplier.txt");
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

    private boolean isDuplicateSupplier(String supplierName, String itemID) {
        try {
            Scanner fileScanner = new Scanner(supplierFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts[1].equalsIgnoreCase(supplierName) && parts[2].equalsIgnoreCase(itemID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Error reading supplier file: " + e.getMessage());
        }
        return false;
    }

    public void addNewSupplier() {
        Scanner scanner = new Scanner(System.in);

        String supplierID = generateSupplierCode();
        String supplierName;
        String itemID;

        do {
            supplierName = getValidStringInput(scanner, "📋 Enter Supplier Name: ");
            itemID = getValidStringInput(scanner, "📦 Enter Item ID: ");

            if (isDuplicateSupplier(supplierName, itemID)) {
                System.out.println("⚠ Error: Supplier with the same name and item ID already exists. Please try again.");
            } else {
                break;
            }
        } while (true);

        String phoneNumber = getValidStringInput(scanner, "📞 Enter Phone Number: ");
        String address = getValidStringInput(scanner, "🏠 Enter Address: ");

        String lineToSave = String.format("%s,%s,%s,%s,%s",
                supplierID, supplierName, itemID, phoneNumber, address);

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
            Scanner fileScanner = new Scanner(supplierFile);
            boolean found = false;
            StringBuilder fileContent = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                fileContent.append(line).append("\n");
            }

            System.out.println("📑 Suppliers in the system:\n" + fileContent);

            String supplierIDToEdit = getValidStringInput(scanner, "✏️ Enter the Supplier ID to edit: ");

            fileScanner = new Scanner(supplierFile);
            StringBuilder updatedContent = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(supplierIDToEdit)) {
                    found = true;
                    System.out.println("✏️ Editing supplier: " + parts[1]);

                    int attribute = getValidIntegerInput(scanner, "Which attribute would you like to edit?\n1: Supplier Name\n2: Item ID\n3: Phone Number\n4: Address\nEnter the number corresponding to the attribute: ");

                    switch (attribute) {
                        case 1:
                            parts[1] = getValidStringInput(scanner, "✏️ Enter new Supplier Name: ");
                            break;
                        case 2:
                            parts[2] = getValidStringInput(scanner, "✏️ Enter new Item ID: ");
                            break;
                        case 3:
                            parts[3] = getValidStringInput(scanner, "✏️ Enter new Phone Number: ");
                            break;
                        case 4:
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

        } catch (IOException e) {
            System.err.println("⚠ Error reading or writing to supplier file: " + e.getMessage());
        }
    }

    public void deleteSupplier() {
        Scanner scanner = new Scanner(System.in);

        try {
            Scanner fileScanner = new Scanner(supplierFile);
            boolean found = false;
            StringBuilder fileContent = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                fileContent.append(line).append("\n");
            }

            System.out.println("📑 Suppliers in the system:\n" + fileContent);

            String supplierIDToDelete = getValidStringInput(scanner, "❌ Enter the Supplier ID to delete: ");

            fileScanner = new Scanner(supplierFile);
            StringBuilder updatedContent = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (!parts[0].equals(supplierIDToDelete)) {
                    updatedContent.append(line).append("\n");
                } else {
                    found = true;
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

        try {
            Scanner scanner = new Scanner(supplierFile);
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

    public static void supplierMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        SupplierRegistration supplierReg = new SupplierRegistration();
        boolean running = true;

        while (running) {
            System.out.println("\n*******************************\n" +
                    "✨ SUPPLIER MANAGEMENT MENU ✨\n" +
                    "*******************************\n" +
                    "1. Add New Supplier\n" +
                    "2. Edit Supplier\n" +
                    "3. Delete Supplier\n" +
                    "4. Exit\n" +
                    "*******************************\n");
            System.out.print("💡 Enter your choice: ");

            int choice = supplierReg.getValidIntegerInput(scanner, "");

            switch (choice) {
                case 1:
                    supplierReg.addNewSupplier();
                    break;
                case 2:
                    supplierReg.editSupplier();
                    break;
                case 3:
                    supplierReg.deleteSupplier();
                    break;
                case 4:
                    System.out.println("❌ Exiting Supplier Management.");
                    InventoryManager.menu();
                    running = false;
                    break;
                default:
                    System.out.println("⚠ Invalid choice. Please try again.\n");
            }
        }
    }
}
