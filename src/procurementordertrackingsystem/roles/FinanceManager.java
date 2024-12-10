package procurementordertrackingsystem.roles;
import procurementordertrackingsystem.entities.Supplier;
import procurementordertrackingsystem.entities.Payment;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.PurchaseOrder;
import procurementordertrackingsystem.entities.PurchaseRequisition;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.utilities.LoginPage;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.ReferentialIntegrity;

public class FinanceManager {

    // Instances to use for Finance Manager related operations
    DataFilePaths filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
    ReferentialIntegrity referentialIntegrity = new ReferentialIntegrity();

    public void displayMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Menu display
        String menu = """
        1. Verify Purchase Orders for Payment
        2. Check Stock Status
        3. Make Payment
        4. View Supplier Payment Status
        5. Logout              
        6. Exit
    """;

        while (true) {
            System.out.println(menu);
            System.out.print("Please select an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    verifyPurchaseOrdersForPayment();
                    break;
                case 2:
                    checkStockStatus();
                    break;
                case 3:
                    makePayment();
                    break;
                case 4:
                    viewSupplierPaymentStatus();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    LoginPage loginPage = new LoginPage();
                    loginPage.login();
                    break;
                case 6:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                    break; // Exit the method
                default:
                    System.out.println("Invalid option. Please select between 1 and 5.");
            }
        }
    }

    // Class of PR features that are only exclusive to the FM
    private class FMExclusivePurchaseRequisitionHandler extends PurchaseRequisition {

        // Method overloading to get item IDs that match the PR IDs in the matched array
        private String[] getItemIdsFromPurchaseRequistionFile(File filename, String[] matchedPRs) throws IOException {
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

        // Get PR IDs that are linked to a specific supplier ID
        private String[] getPurchaseRequisitionIDsUsingSupplierID(File filename, String providedSupplierID) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> prIDs = new ArrayList<>(); // To hold the PR IDs

            // Go through each line
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Store some attributes into string variable
                    String prID = parts[0];
                    String supplierId = parts[4];

                    // If there's a match, add the corresponding PR ID
                    if (supplierId.equals(providedSupplierID)) {
                        prIDs.add(prID);
                    }
                }
            }

            // Convert the List to an array and return it
            return prIDs.toArray(new String[0]);
        }
    }

    // Class of Item features that are only exclusive to the FM
    private class FMExclusiveItemHandler extends Item {

        // Method overloading to read and print only the items with IDs in the matched array
        private void readItemsFromFile(File filename, String[] matchedItemIds) throws IOException {
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

    // Class of PO features that are only exclusive to the FM
    private class FMExclusivePurchaseOrderHandler extends PurchaseOrder {

        // Method to approve or reject purchase order status
        private void updatePurchaseOrderStatusFromFile(File filename, boolean approve, String poID) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> updatedLines = new ArrayList<>(); // To hold the updated lines for the PO file

            // Go through each line for PO details to be modified
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Check if the current PO ID matches the provided poID
                    if (parts[0].equals(poID)) {
                        // Set the status based on the approve parameter
                        parts[3] = approve ? "Approved" : "Rejected";
                    }
                    // Create updated line
                    String updatedLine = String.join(",", parts);
                    updatedLines.add(updatedLine); // Store the updated line
                }
            }

            // Write the updated lines back to the file
            crudOntoFile.writeUpdatedLinesToFile(filename, updatedLines);
        }

        // Get PR IDs that are linked to a PO to be stored in an array
        private String[] getPurchaseRequisitionIDsFromPurchaseOrderFile(File filename) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> requisitionIds = new ArrayList<>(); // To hold the PR IDs

            // Go through each line
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Add the PR ID to the list
                    requisitionIds.add(parts[1]);
                }
            }

            // Convert the List to an array and return it
            return requisitionIds.toArray(new String[0]);
        }

        // Change payment status to paid in the PO file
        private void changePaymentStatusInPurchaseOrderFile(File filename, String poID) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> updatedLines = new ArrayList<>(); // To hold the updated lines for the PO file

            // Go through each line for PO details to be modified
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Check if the current PO ID matches the provided poID
                    if (parts[0].equals(poID)) {
                        // Set the status based on the approve parameter
                        parts[5] = "Paid";
                    }
                    // Create updated line
                    String updatedLine = String.join(",", parts);
                    updatedLines.add(updatedLine); // Store the updated line
                }
            }

            // Write the updated lines back to the file
            crudOntoFile.writeUpdatedLinesToFile(filename, updatedLines);
        }

        // Get PO IDs that are linked to PR IDs
        private String[] getPurchaseOrderIDsUsingPurchaseRequisitionID(File filename, String[] providedPRIDs) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> poIDs = new ArrayList<>(); // To hold the PR IDs

            // Go through each line
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    // Store some attributes into string variable
                    String poID = parts[0];
                    String prID = parts[1];

                    // Check if the prID matches any of the providedPRIDs
                    for (String providedPRID : providedPRIDs) {
                        if (prID.equals(providedPRID)) {
                            poIDs.add(poID); // If there's a match, add the corresponding PO ID
                            break; // Stop checking further for this line if a match is found
                        }
                    }
                }
            }

            // Convert the List to an array and return it
            return poIDs.toArray(new String[0]);
        }
    }

    // Class of Payment features that are only exclusive to the FM
    private class FMExclusivePaymentHandler extends Payment {

        // Get PO IDs that are linked to PR IDs
        private String[] getPaymentIDsUsingPurchaseOrderID(File filename, String[] providedPOIDs) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
            List<String> paymentIDs = new ArrayList<>(); // To hold the PO IDs

            // Go through each line
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    // Store some attributes into string variable                    
                    String paymentID = parts[0];
                    String poID = parts[1];

                    // Check if the prID matches any of the providedPRIDs
                    for (String providedPOID : providedPOIDs) {
                        if (poID.equals(providedPOID)) {
                            paymentIDs.add(paymentID); // If there's a match, add the corresponding Payment ID
                            break; // Stop checking further for this line if a match is found
                        }
                    }
                }
            }

            // Convert the List to an array and return it
            return paymentIDs.toArray(new String[0]);
        }

        // Method overloading that allows reading specific selected set of Payments
        private void readPaymentFromFile(File filename, String[] requestedPayments) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename);

            // Go through each line
            for (String line : lines) {
                // Separate by commas
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    // Store an attribute into string variable
                    String paymentID = parts[0];

                    // Check if paymentID is in the requestedPayments array
                    for (String requestedPayment : requestedPayments) {
                        if (paymentID.equals(requestedPayment.trim())) {
                            System.out.printf(
                                    "Payment ID: %s, Purchase Order ID: %s, Amount: RM %s, Payment Status: %s, Payment date: %s%n",
                                    parts[0], parts[1], parts[2], parts[3], parts[4]
                            );
                            break; // Exit the loop once a match is found
                        }
                    }
                }
            }
        }
    }

    // Class of Supplier features that are only exclusive to the FM
    private class FMExclusiveSupplierHandler extends Supplier {

        // Method to get supplier name using supplier ID
        private String getSupplierNameUsingSupplierID(File filename, String providedSupplierID) throws IOException {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents

            // Go through each line
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    // Store some attributes into string variable                    
                    String supplierID = parts[0];
                    String supplierName = parts[1];

                    if (supplierID.equals(providedSupplierID)) {
                        return supplierName;
                    }
                }
            }
            return null;
        }
    }

    // FM 1st functionality: Verify Purchase Orders for Payment (View POs) – Approve / Reject
    public void verifyPurchaseOrdersForPayment() {
        // Object instantiation neccessary for usage in this method
        Scanner scanner = new Scanner(System.in);
        PurchaseOrder po = new PurchaseOrder();
        FMExclusivePurchaseOrderHandler fepoh = new FMExclusivePurchaseOrderHandler();

        try {
            // Get .txt file path
            File purchaseOrderFile = filePaths.getPurchaseOrderFile();

            // Step 1: Read and display all purchase orders
            System.out.println("----- Purchase Orders -----");
            po.readPurchaseOrdersFromFile(purchaseOrderFile);

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
            fepoh.updatePurchaseOrderStatusFromFile(purchaseOrderFile, approve, poID);

            // Step 6: Confirm that the update has been performed successfully
            String status = approve ? "approved" : "rejected";
            System.out.println("\nPurchase Order " + poID + " has been " + status + " successfully.");
        } catch (IOException e) {
            System.err.println("Error reading purchase order file: " + e.getMessage());
        }

    }

    // FM 2nd functionality: Check Stock Status – updated by IM for the item which POs issued
    public void checkStockStatus() {
        // Object instantiation neccessary for usage in this method
        PurchaseRequisition pr = new PurchaseRequisition();
        FMExclusivePurchaseRequisitionHandler feprh = new FMExclusivePurchaseRequisitionHandler();
        FMExclusiveItemHandler feih = new FMExclusiveItemHandler();
        FMExclusivePurchaseOrderHandler fepoh = new FMExclusivePurchaseOrderHandler();

        // Get the txt file paths
        File purchaseOrderFile = filePaths.getPurchaseOrderFile();
        File purchaseRequisitionFile = filePaths.getPurchaseRequisitionFile();
        File itemFile = filePaths.getItemFile();

        // Step 1: Map the PR IDs in purchase_order to the one in purchase_requisition
        try {
            String[] poRequisitionIds = fepoh.getPurchaseRequisitionIDsFromPurchaseOrderFile(purchaseOrderFile);
            String[] prRequisitionIds = pr.getPurchaseRequestIDsFromPurchaseRequestFile(purchaseRequisitionFile);
            String[] filteredPRIDs = referentialIntegrity.match2Arrays(poRequisitionIds, prRequisitionIds);

            // Step 2: Get the item IDs linked to the set of PR IDs
            try {
                String[] filtereditemIDs = feprh.getItemIdsFromPurchaseRequistionFile(purchaseRequisitionFile, filteredPRIDs);

                // Step 3: Print all the filtered item IDs to the terminal
                try {
                    feih.readItemsFromFile(itemFile, filtereditemIDs);
                } catch (IOException e) {
                    System.err.println("Error reading item file: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Error reading purchase requisition file: " + e.getMessage());
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error reading purchase order file: " + e.getMessage());
        }
    }

    // FM 3rd functionality: Make Payment - Update the PO status
    public void makePayment() {
        // Object instantiation neccessary for usage in this method
        Scanner scanner = new Scanner(System.in);
        PurchaseOrder po = new PurchaseOrder();
        FMExclusivePurchaseOrderHandler fepoh = new FMExclusivePurchaseOrderHandler();
        Payment payment = new Payment();

        // Get the txt file paths
        File purchaseOrderFile = filePaths.getPurchaseOrderFile();
        File paymentFile = filePaths.getPaymentFile();

        // Step 1: Read and display all purchase orders
        try {
            System.out.println("----- Purchase Orders -----");
            po.readPurchaseOrdersFromFile(purchaseOrderFile);
        } catch (IOException e) {
            System.err.println("Error reading purchase order file: " + e.getMessage());
        }

        // Step 2: Prompt for the PO ID for payment creation
        System.out.print("\nEnter the Purchase Order ID (PO ID) you wish to pay for: ");
        String poID = scanner.nextLine();

        // Step 3: Check whether the inputted PO ID exists or not in the PO file
        try {
            String[] existingPOIDs = po.getPurchaseOrderIDsFromPurchaseOrderFile(purchaseOrderFile);
            boolean doesPOIDexist = referentialIntegrity.checkAttributeInArray(poID, existingPOIDs);
            if (!doesPOIDexist) {
                System.out.println("PO ID doesn't exist in the purchase order database.");
                return; // Exit the method if the PO ID does not exist
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error reading purchase order file: " + e.getMessage());
            return; // Exit the method on error
        }

        // Step 4: Prompt for the amount to pay (in Ringgits)
        System.out.print("\nEnter the amount to pay (in RM): ");
        double amount = scanner.nextDouble();

        // Step 5: Create payment data into the Payment file & update payment status in PO file
        try {
            payment.createPaymentToFile(paymentFile, poID, amount);
            fepoh.changePaymentStatusInPurchaseOrderFile(purchaseOrderFile, poID);
            System.out.println("Payment of RM" + Double.toString(amount) + " for " + poID + " had been successfully made");
        } catch (IOException e) {
            System.err.println("Error writing to payment or purchase order file: " + e.getMessage());
        }
    }

    // FM 4th functionality: View Supplier Payment Status - Track and view the payment history and status of suppliers.
    public void viewSupplierPaymentStatus() {
        // Object instantiation neccessary for usage in this method
        Scanner scanner = new Scanner(System.in);
        Supplier supplier = new Supplier();
        FMExclusivePurchaseRequisitionHandler feprh = new FMExclusivePurchaseRequisitionHandler();
        FMExclusivePurchaseOrderHandler fepoh = new FMExclusivePurchaseOrderHandler();
        FMExclusivePaymentHandler feph = new FMExclusivePaymentHandler();
        FMExclusiveSupplierHandler fesh = new FMExclusiveSupplierHandler();

        // Get the txt file paths
        File supplierFile = filePaths.getSupplierFile();
        File purchaseRequisitionFile = filePaths.getPurchaseRequisitionFile();
        File purchaseOrderFile = filePaths.getPurchaseOrderFile();
        File paymentFile = filePaths.getPaymentFile();

        // Step 1: Read and display all suppliers
        try {
            System.out.println("----- Suppliers list -----");
            supplier.readSuppliersFromFile(supplierFile);
        } catch (IOException e) {
            System.err.println("Error reading supplier file: " + e.getMessage());
            return; // Exit the method on error
        }

        // Step 2: Prompt for the Supplier ID for payment status listings
        System.out.print("\nEnter the Supplier ID whose payment status you want to check: ");
        String supplierID = scanner.nextLine();

        // Step 3: Check whether supplier ID exists in PR file or not
        try {
            String[] existingSupplierIDs = supplier.getsupplierIDsFromSupplierFile(supplierFile);
            boolean doesSupplierIDexist = referentialIntegrity.checkAttributeInArray(supplierID, existingSupplierIDs);
            if (!doesSupplierIDexist) {
                System.out.println("Supplier ID doesn't exist in the Supplier database.");
                return; // Exit the method if Supplier ID does not exist
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error reading supplier file: " + e.getMessage());
            return; // Exit the method on error
        }

        // Step 4: Mapping attributes
        try {
            String[] requestedPRIDs = feprh.getPurchaseRequisitionIDsUsingSupplierID(purchaseRequisitionFile, supplierID);
            String[] requestedPOIDs = fepoh.getPurchaseOrderIDsUsingPurchaseRequisitionID(purchaseOrderFile, requestedPRIDs);
            String[] requestedPayments = feph.getPaymentIDsUsingPurchaseOrderID(paymentFile, requestedPOIDs);

            // Step 5: Get Supplier name
            String supplierName = fesh.getSupplierNameUsingSupplierID(supplierFile, supplierID);

            // Step 6: Display the supplier's payment history (including status)
            System.out.println("\n" + supplierName + " payment history");
            feph.readPaymentFromFile(paymentFile, requestedPayments);
        } catch (IOException e) {
            System.err.println("Error reading supplier, purchase requisition, purchase order, or payment files: " + e.getMessage());
        }
    }
}
