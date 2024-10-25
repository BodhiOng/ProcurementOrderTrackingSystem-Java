package procurementordertrackingsystem;

import java.util.ArrayList;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.util.List;

public class PurchaseRequisition {
    private String prID; // Purchase Requisition ID
    private String itemID; // Item ID
    private int quantity; // Quantity (always  >= 1)
    private String dateRequired; // Date when the Item is needed
    private String supplierID; // Supplier ID
    private String userID; // User ID
    
    // Default constructor
    public PurchaseRequisition(){
        this.prID = "";
        this.itemID = "";
        this.quantity = 0;
        this.dateRequired = "";
        this.supplierID = "";
        this.userID = "";
    }
    
    // Constructor
    public PurchaseRequisition(String prID, String itemID, int quantity, String dateRequired, String supplierID, String userID){
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.dateRequired = dateRequired;
        this.supplierID = supplierID;
        this.userID = userID;
    }
    
    // CRUDOntoFile object instantiation
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();

    // Method to read all PRs
    public void readPurchaseRequisitionFromFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        
        // Go through each line for PO details to be printed
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            // Ensure there are that muc parts in the line & print output to terminal
            if (parts.length == 6) {
                String output = String.format(
                        "Purchase Requisition ID: %s, Item ID: %s, Quantity: %s, Date Required: %s, Supplier ID: %s, User ID: %s",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]
                );
                System.out.println(output);
            }
        }
    }
    
    // Get item IDs that are linked to a PR to be stored in an array
    public String[] getPurchaseRequestIDsFromPurchaseRequestFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        List<String> requisitionIds = new ArrayList<>(); // To hold the PR IDs
        
        // Go through each line
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            if (parts.length == 6) {
                // Add the PR ID to the list
                requisitionIds.add(parts[0]);
            }
        }

        // Convert the List to an array and return it
        return requisitionIds.toArray(new String[0]);
    }
        
    // Getters & setters
    public String getPrID() { return prID; }
    public void setPrID(String prID) { this.prID = prID; }

    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        if (quantity >= 1) this.quantity = quantity; 
        else throw new IllegalArgumentException("Quantity must be >= 1"); 
    }

    public String getDateRequired() { return dateRequired; }
    public void setDateRequired(String dateRequired) { this.dateRequired = dateRequired; }

    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
}
