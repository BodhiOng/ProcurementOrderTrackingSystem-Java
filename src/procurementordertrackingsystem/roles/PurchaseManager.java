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

            // Append options based on the role
            if ("Administrators".equalsIgnoreCase(role)) {
                System.out.println("9. Go Back");  // Admins get "Go Back" instead of logout
            } else {
                System.out.println("9. Logout");  // Non-admins get "Logout"
                System.out.println("10. Exit");   // Non-admins get "Exit"
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
                        System.out.println("\n Going Back...");
                        // Implement your logic for "Go Back" for admins (e.g., navigate to the previous menu)
                        // For example, calling the main menu again:
                        // PurchaseManager.displayMenu(role); // if you need to go back to the menu with role
                    } else {
                        System.out.println("\n Logged Out Inventory Management System... See you next time!\n");
                        LoginPage loginPage = new LoginPage();
                        loginPage.login();
                    }
                    break;
                case 10:
                    if (!"Administrators".equalsIgnoreCase(role)) {
                        System.out.println("\n Exiting the system.");
                        System.exit(0); // Exit the system for non-admins
                    } else {
                        System.out.println(" Invalid option. Admin users cannot exit.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9); // Keep showing the menu until the user chooses to exit or log out
    }

    // View inventory
    public void viewInventory() {
        File itemFile = new File("src/procurementordertrackingsystem/data/item.txt");
        try {
            System.out.println("----- Current Inventory -----");
            List<String> items = crudOntoFile.readFromAFile(itemFile);
            for (String line : items) {
                String[] parts = line.split(",");
                // Create an Item object using the parsed parts of the line
                Item item = new Item(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), parts[4]);
                // Print item details using the toString method or direct access to fields
                System.out.println(item);
            }
        } catch (IOException e) {
            System.err.println("Error reading item file: " + e.getMessage());
        }
    }

    // Update stock level
    public void updateStockLevel() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Item ID to update stock: ");
        String itemID = scanner.nextLine();
        System.out.print("Enter new Stock Level: ");
        int stockLevel = Integer.parseInt(scanner.nextLine());

        File itemFile = new File("src/procurementordertrackingsystem/data/item.txt");

        try {
            List<String> itemData = crudOntoFile.readFromAFile(itemFile);
            boolean itemFound = false;
            StringBuilder updatedItems = new StringBuilder();

            for (String line : itemData) {
                String[] parts = line.split(",");
                if (parts[0].equals(itemID)) {
                    parts[2] = String.valueOf(stockLevel);
                    itemFound = true;
                }
                updatedItems.append(String.join(",", parts)).append("\n");
            }

            if (itemFound) {
                crudOntoFile.writeUpdatedLinesToFile(itemFile, List.of(updatedItems.toString().trim().split("\n")));
                System.out.println("Stock level updated successfully!");
            } else {
                System.out.println("Item ID not found!");
            }

        } catch (IOException e) {
            System.err.println("Error updating item stock: " + e.getMessage());
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
        File requisitionFile = new File("src/procurementordertrackingsystem/data/purchase_requisition.txt");
        try {
            System.out.println("----- Purchase Requisitions -----");
            List<String> requisitions = crudOntoFile.readFromAFile(requisitionFile);
            for (String line : requisitions) {
                String[] parts = line.split(",");
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

        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");

        try {
            // Use the createToFile method to append the new purchase order to the file
            crudOntoFile.createToFile(purchaseOrderFile, lineToSave);
            System.out.println("Purchase Order generated successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to purchase order file: " + e.getMessage());
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
        System.out.print("Enter Purchase Order ID to edit: ");
        String poID = scanner.nextLine();

        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");
        try {
            List<String> purchaseOrders = crudOntoFile.readFromAFile(purchaseOrderFile);
            boolean poFound = false;
            StringBuilder updatedPOs = new StringBuilder();

            for (String line : purchaseOrders) {
                String[] parts = line.split(",");
                if (parts[0].equals(poID)) {
                    System.out.print("Enter new Purchase Requisition ID: ");
                    String reqID = scanner.nextLine();
                    System.out.print("Enter new Purchase Manager ID: ");
                    String purchaseManagerID = scanner.nextLine();
                    System.out.print("Enter new Approval Status (Approved/Pending/Rejected): ");
                    String approvalStatus = scanner.nextLine();
                    System.out.print("Enter new Payment Status (Paid/Unpaid): ");
                    String paymentStatus = scanner.nextLine();

                    parts[1] = reqID;
                    parts[2] = purchaseManagerID;
                    parts[3] = approvalStatus;
                    parts[4] = "2024-12-13"; // Current date
                    parts[5] = paymentStatus;

                    poFound = true;
                }
                updatedPOs.append(String.join(",", parts)).append("\n");
            }

            if (poFound) {
                crudOntoFile.writeUpdatedLinesToFile(purchaseOrderFile, List.of(updatedPOs.toString().trim().split("\n")));
                System.out.println("Purchase Order updated successfully!");
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
        System.out.print("Enter Purchase Order ID to delete: ");
        String poID = scanner.nextLine();

        File purchaseOrderFile = new File("src/procurementordertrackingsystem/data/purchase_order.txt");
        try {
            List<String> purchaseOrders = crudOntoFile.readFromAFile(purchaseOrderFile);
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

            if (poFound) {
                crudOntoFile.writeUpdatedLinesToFile(purchaseOrderFile, List.of(updatedPOs.toString().trim().split("\n")));
                System.out.println("Purchase Order deleted successfully!");
            } else {
                System.out.println("Purchase Order ID not found!");
            }

        } catch (IOException e) {
            System.err.println("Error deleting purchase order: " + e.getMessage());
        }
    }
}
