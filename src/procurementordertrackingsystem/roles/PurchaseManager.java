package procurementordertrackingsystem.roles;

import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.PurchaseOrder;
import procurementordertrackingsystem.entities.PurchaseRequisition;
import procurementordertrackingsystem.entities.Supplier;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.LoginPage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PurchaseManager {

    private final DataFilePaths filePaths;
    private final CRUDOntoFile crudOntoFile;
    private final PurchaseOrder purchaseOrder;
    private final PurchaseRequisition purchaseRequisition;
    private final Supplier supplier;

    public PurchaseManager() {
        this.filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        this.crudOntoFile = new CRUDOntoFile();
        this.purchaseOrder = new PurchaseOrder();
        this.purchaseRequisition = new PurchaseRequisition();
        this.supplier = new Supplier();
    }

// Method to display the menu and handle user selection
    public void displayMenu(String role) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Purchase Manager Menu ---");
            System.out.println("1. View Inventory");
            System.out.println("2. Update Stock Level");
            System.out.println("3. View Suppliers");
            System.out.println("4. View Purchase Requisitions");
            System.out.println("5. Generate Purchase Order");
            System.out.println("6. View Purchase Orders");
            System.out.println("7. Edit Purchase Order");
            System.out.println("8. Delete Purchase Order");
            if ("Administrators".equalsIgnoreCase(role)) {
                System.out.println("9. Go Back");
            } else {
                System.out.println("9. Logout");
                System.out.println("10. Exit");
            }
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character left by nextInt()

            switch (choice) {
                case 1:
                    viewInventory();
                    break;
                case 2:
                    updateStockLevel();
                    break;
                case 3:
                    viewSuppliers();
                    break;
                case 4:
                    viewPurchaseRequisitions();
                    break;
                case 5:
                    generatePurchaseOrder();
                    break;
                case 6:
                    viewPurchaseOrders();
                    break;
                case 7:
                    editPurchaseOrder();
                    break;
                case 8:
                    deletePurchaseOrder();
                    break;
                case 9:
                    if ("Administrators".equalsIgnoreCase(role)) {
                        System.out.println("Going Back...");
                        return;
                    } else {
                        System.out.println("\nLogged out. Returning to login page...");
                        LoginPage loginPage = new LoginPage();
                        loginPage.login(); // Calls the login method again
                    }
                    break;
                case 10:
                    if (!"Administrators".equalsIgnoreCase(role)) {
                        System.out.println("Exiting the system. Goodbye!");
                        System.exit(0);
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10 || "Administrators".equalsIgnoreCase(role));
    }

    // View inventory
    public void viewInventory() {
        File itemFile = new File("src/procurementordertrackingsystem/data/item.txt");
        try {
            System.out.println("----- Current Inventory -----");
            List<String> items = crudOntoFile.readFromAFile(itemFile);
            for (String line : items) {
                String[] parts = line.split(",");
                Item item = new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), parts[4]);
                System.out.println(item);
            }
        } catch (IOException e) {
            System.err.println("Error reading item file: " + e.getMessage());
        }
    }

    //Update Stock Level
    public void updateStockLevel() {
        Scanner scanner = new Scanner(System.in);

        File itemFile = new File("src/procurementordertrackingsystem/data/item.txt");

        try {
            // Read and display the current file content
            List<String> itemData = crudOntoFile.readFromAFile(itemFile);
            System.out.println("\n--- Current Content of Item File ---");
            if (itemData.isEmpty()) {
                System.out.println("The file is empty.");
                return;
            }
            itemData.forEach(System.out::println);

            // Prompt user for Item ID
            System.out.print("\nEnter Item ID to update stock: ");
            String itemID = scanner.nextLine();

            // Check if the Item ID exists
            boolean itemFound = false;

            for (String line : itemData) {
                String[] parts = line.split(",");
                if (parts[0].equals(itemID)) {
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                System.out.println("\nItem ID not found! Please try again with a valid Item ID.");
                return;
            }

            // If Item ID exists, prompt for stock level
            System.out.print("Enter new Stock Level: ");
            int stockLevel = Integer.parseInt(scanner.nextLine());

            // Update the stock level
            StringBuilder updatedItems = new StringBuilder();
            for (String line : itemData) {
                String[] parts = line.split(",");
                if (parts[0].equals(itemID)) {
                    parts[2] = String.valueOf(stockLevel); // Update stock level
                }
                updatedItems.append(String.join(",", parts)).append("\n");
            }

            // Write updated data back to the file
            crudOntoFile.writeUpdatedLinesToFile(itemFile, List.of(updatedItems.toString().trim().split("\n")));
            System.out.println("\nStock level updated successfully!");

            // Display updated content
            System.out.println("\n--- Updated Content of Item File ---");
            crudOntoFile.readFromAFile(itemFile).forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error updating item stock: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for stock level. Please enter a numeric value.");
        }
    }

    // View suppliers using the Supplier class
    public void viewSuppliers() {
        File supplierFile = new File("src/procurementordertrackingsystem/data/supplier.txt");
        try {
            System.out.println("----- Supplier List -----");
            supplier.readSuppliersFromFile(supplierFile);  // Use the Supplier class method to display suppliers
        } catch (IOException e) {
            System.err.println("Error reading supplier file: " + e.getMessage());
        }
    }

    // View purchase requisitions
    public void viewPurchaseRequisitions() {
    File requisitionFile = filePaths.getPurchaseRequisitionFile(); 
    try {
        System.out.println("----- Purchase Requisitions -----");
        List<String> requisitions = crudOntoFile.readFromAFile(requisitionFile);
        for (String line : requisitions) {
            String[] parts = line.split(",");
            if (parts.length < 6) continue;
            System.out.printf("Requisition ID: %s, Item ID: %s, Quantity: %s, Date: %s, Supplier ID: %s, User ID: %s%n",
                    parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
    } catch (IOException e) {
        System.err.println("Error reading purchase requisitions: " + e.getMessage());
    }
}

    // Generate purchase order
   public void generatePurchaseOrder() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter Purchase Order ID: ");
    String poID = scanner.nextLine();

    System.out.print("Enter Purchase Requisition ID: ");
    String reqID = scanner.nextLine();

    System.out.print("Enter Purchase Manager ID: ");
    String purchaseManagerID = scanner.nextLine();

    System.out.print("Enter Approval Status (Approved/Pending/Rejected): ");
    String approvalStatus = scanner.nextLine();

    System.out.print("Enter Payment Status (Paid/Unpaid): ");
    String paymentStatus = scanner.nextLine();

    String lineToSave = String.format("%s,%s,%s,%s,%s,%s", poID, reqID, purchaseManagerID, approvalStatus, "2024-12-13", paymentStatus);

    File purchaseOrderFile = filePaths.getPurchaseOrderFile();

    try {
        List<String> existingOrders = crudOntoFile.readFromAFile(purchaseOrderFile);

        for (String order : existingOrders) {
            String[] parts = order.split(",");
            if (parts.length > 0 && parts[0].equals(poID)) {
                System.err.println("Duplicate Purchase Order ID found: " + poID);
                return;
            }
            if (parts.length > 1 && parts[1].equals(reqID)) {
                System.err.println("Duplicate Purchase Requisition ID found: " + reqID);
                return;
            }
        }

        // Ensure newline after each entry
        crudOntoFile.createToFile(purchaseOrderFile, lineToSave + System.lineSeparator());

        System.out.println("Purchase Order generated successfully!");

    } catch (IOException e) {
        System.err.println("Error reading/writing to purchase order file: " + e.getMessage());
    }
}





    // View purchase orders
    public void viewPurchaseOrders() {
        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");
        try {
            System.out.println("----- Purchase Orders -----");
            purchaseOrder.readPurchaseOrdersFromFile(purchaseOrderFile);
        } catch (IOException e) {
            System.err.println("Error reading purchase orders: " + e.getMessage());
        }
    }

    // Edit purchase order
    public void editPurchaseOrder() {
        Scanner scanner = new Scanner(System.in);
        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");

        try {
            // Read and display the current content of the file
            System.out.println("\n--- Current Content of Purchase Order File ---");
            List<String> purchaseOrders = crudOntoFile.readFromAFile(purchaseOrderFile);
            if (purchaseOrders.isEmpty()) {
                System.out.println("The file is empty.");
                return;
            }
            purchaseOrders.forEach(System.out::println);

            // Prompt user for Purchase Order ID
            System.out.print("\nEnter Purchase Order ID to edit: ");
            String poID = scanner.nextLine();

            boolean poFound = false;
            StringBuilder updatedPOs = new StringBuilder();

            for (String line : purchaseOrders) {
                String[] parts = line.split(",");
                if (parts[0].equals(poID)) {
                    // Only update Approval Status and Payment Status
                    System.out.print("Enter new Approval Status (Approved/Pending/Rejected): ");
                    parts[3] = scanner.nextLine();

                    System.out.print("Enter new Payment Status (Paid/Unpaid): ");
                    parts[5] = scanner.nextLine();

                    poFound = true;
                }
                updatedPOs.append(String.join(",", parts)).append("\n");
            }

            // Save updates to file
            if (poFound) {
                crudOntoFile.writeUpdatedLinesToFile(purchaseOrderFile, List.of(updatedPOs.toString().trim().split("\n")));
                System.out.println("\nPurchase Order updated successfully!");

                // Display updated content
                System.out.println("\n--- Updated Content of Purchase Order File ---");
                crudOntoFile.readFromAFile(purchaseOrderFile).forEach(System.out::println);
            } else {
                System.out.println("Purchase Order ID not found!");
            }
        } catch (IOException e) {
            System.err.println("Error editing purchase order: " + e.getMessage());
        }
    }

    // Delete purchase order
    public void deletePurchaseOrder() {
        Scanner scanner = new Scanner(System.in);
        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");

        try {
            // Read and display the current content of the file
            System.out.println("\n--- Current Content of Purchase Order File ---");
            List<String> purchaseOrders = crudOntoFile.readFromAFile(purchaseOrderFile);
            if (purchaseOrders.isEmpty()) {
                System.out.println("The file is empty.");
                return;
            }
            purchaseOrders.forEach(System.out::println);

            // Prompt user for Purchase Order ID to delete
            System.out.print("\nEnter Purchase Order ID to delete: ");
            String poID = scanner.nextLine();

            boolean poFound = false;
            StringBuilder updatedPOs = new StringBuilder();

            for (String line : purchaseOrders) {
                String[] parts = line.split(",");
                if (!parts[0].equals(poID)) {
                    updatedPOs.append(String.join(",", parts)).append("\n");
                } else {
                    poFound = true;
                }
            }

            // Save updates to file
            if (poFound) {
                crudOntoFile.writeUpdatedLinesToFile(purchaseOrderFile, List.of(updatedPOs.toString().trim().split("\n")));
                System.out.println("\nPurchase Order deleted successfully!");

                // Display updated content
                System.out.println("\n--- Updated Content of Purchase Order File ---");
                crudOntoFile.readFromAFile(purchaseOrderFile).forEach(System.out::println);
            } else {
                System.out.println("Purchase Order ID not found!");
            }
        } catch (IOException e) {
            System.err.println("Error deleting purchase order: " + e.getMessage());
        }
    }
}
