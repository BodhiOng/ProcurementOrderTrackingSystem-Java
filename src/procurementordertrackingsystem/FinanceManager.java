package procurementordertrackingsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataTXTDirectories;
import procurementordertrackingsystem.utilities.ReferentialIntegrity;

public class FinanceManager {

    // Instances to use for Finance Manager related operations
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    DataTXTDirectories directories = new DataTXTDirectories();

    // Class of PR features that are only exclusive to the FM
    private class FMExclusivePRHandler extends PurchaseRequisition {
        // Default constructor
        public FMExclusivePRHandler() {
            super("", "", 0, "", "", "");
        }

        // Paramaterized contructor
        public FMExclusivePRHandler(String prID, String itemID, int quantity, String dateRequired, String supplierID, String userID) {
            super(prID, itemID, quantity, dateRequired, supplierID, userID);
        }

        // Method overloading to get item IDs that match the PR IDs in the matched array
        private String[] getItemIdsFromPRFile(String filename, String[] matchedPRs) {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> itemIds = new ArrayList<>(); // To hold the matched item IDs

            // Iterate over each line of the file
            for (String line : lines) {
                String[] parts = line.split(","); // Split line into parts
                // Ensure the line has the expected amount of parts
                if (parts.length == 6) { 
                    // Store some attributes into string variable
                    String prID = parts[0]; 
                    String itemId = parts[1]; 

                    // Check if the current PR ID is in the matchedPRs array
                    for (String matchedPR : matchedPRs) {
                        if (prID.equals(matchedPR)) {
                            itemIds.add(itemId); // If there's a match, add the corresponding Item ID
                            break; // Move on to the next line once matched
                        }
                    }
                }
            }

            // Convert the List to an array and return it
            return itemIds.toArray(new String[0]);
        }
    }

    // Class of Item features that are only exclusive to the FM
    private class FMExclusiveItemHandler extends Item {
        // Default constructor
        public FMExclusiveItemHandler() {
            super("", "", 0, 0.0, ""); 
        }

        // Parameterized constructor
        public FMExclusiveItemHandler(String itemID, String itemName, int stockLevel, double price, String supplierID) {
            super(itemID, itemName, stockLevel, price, supplierID);
        }

        // Method overloading to read and print only the items with IDs in the matched array
        private void readItemsFromFile(String filename, String[] matchedItemIds) {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents

            for (String line : lines) {
                String[] parts = line.split(","); // Split line into parts
                // Ensure the line has that much expected parts
                if (parts.length == 5) { 
                    String itemId = parts[0]; 

                    // Check if the current Item ID is in the matchedItemIds array
                    for (String matchedItemId : matchedItemIds) {
                        if (itemId.equals(matchedItemId)) {
                            // If there's a match, print the item details
                            String output = String.format(
                                    "Item ID: %s, Item Name: %s, Stock Level: %s, Price: %s, Supplier ID: %s",
                                    parts[0], parts[1], parts[2], parts[3], parts[4]
                            );
                            System.out.println(output);
                            break; // Move on to the next line once matched
                        }
                    }
                }
            }
        }
    }

    // Class of finance manager's functionalities
    public class FMFunctionalities {
        // FM 1st functionality: Verify Purchase Orders for Payment (View POs) – Approve / Reject
        public void verifyPurchaseOrdersForPayment() {
            Scanner scanner = new Scanner(System.in);
            PurchaseOrder po = new PurchaseOrder();

            // Get .txt file path
            String purchaseOrderTXT = directories.purchaseOrderTXTDirectory;

            // Step 1: Read and display all purchase orders
            System.out.println("----- Purchase Orders -----");
            po.readPurchaseOrdersFromFile(purchaseOrderTXT);

            // Step 2: Prompt for the PO ID to modify
            System.out.print("\nEnter the Purchase Order ID (PO ID) you wish to modify: ");
            String poID = scanner.nextLine();

            // Step 3: Ask whether to approve or reject the purchase order
            System.out.print("Do you want to approve this Purchase Order? (yes/no): ");
            String userInput = scanner.nextLine();

            // Step 4: Determine the approval status based on user input
            boolean approve;
            if (userInput.equalsIgnoreCase("yes")) {
                approve = true;
            } else if (userInput.equalsIgnoreCase("no")) {
                approve = false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                return;
            }

            // Step 5: Update the status of the purchase order
            po.updatePurchaseOrderStatusFromFile(purchaseOrderTXT, approve, poID);

            // Step 6: Confirm that the update has been performed successfully
            String status = approve ? "approved" : "rejected";
            System.out.println("\nPurchase Order " + poID + " has been " + status + " successfully.");
        }

        // FM 2nd functionality: Check Stock Status – updated by IM for the item which POs issued
        public void checkStockStatus() {
            Scanner scanner = new Scanner(System.in);

            // Object instantiation neccessary for usage in this method
            PurchaseOrder po = new PurchaseOrder();
            PurchaseRequisition pr = new PurchaseRequisition();
            ReferentialIntegrity ri = new ReferentialIntegrity();
            FMExclusivePRHandler feph = new FMExclusivePRHandler();
            FMExclusiveItemHandler feih = new FMExclusiveItemHandler();

            // Get the txt file paths
            String purchaseOrderTXT = directories.purchaseOrderTXTDirectory;
            String purchaseRequisitionTXT = directories.purchaseRequisitionTXTDirectory;
            String itemTXT = directories.itemTXTDirectory;

            // Step 1: Map the PR IDs in purchase_order to the one in purchase_requisition
            String[] poRequisitionIds = po.getPurchaseRequisitionIdsFromPOFile(purchaseOrderTXT);
            String[] prRequisitionIds = pr.getPRIDsFromPRFile(purchaseRequisitionTXT);
            String[] filteredPRIDs = ri.match2Arrays(poRequisitionIds, prRequisitionIds);

            // Step 2: Get the item IDs linked to the set of PR IDs
            String[] filtereditemIDs = feph.getItemIdsFromPRFile(purchaseRequisitionTXT, filteredPRIDs);

            // Step 3: Print all the filtered item IDs to the terminal
            feih.readItemsFromFile(itemTXT, filtereditemIDs);
        }
    }
}
