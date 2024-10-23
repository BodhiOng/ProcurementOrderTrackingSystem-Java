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
        List<String> lines = crudOntoFile.readFromAFile(filename);

        for (String line : lines) {
            String[] parts = line.split(",");
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
    public String[] getPRIDsFromPRFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename);
        List<String> requisitionIdS = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                // Add the Purchase Requisition ID (parts[1]) to the list
                requisitionIdS.add(parts[0]);
            }
        }

        // Convert the List to an array and return it
        return requisitionIdS.toArray(new String[0]);
    }
    
    // Get item IDs that are linked to a PR to be stored in an array
    public String[] getItemIdsFromPRFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename);
        List<String> itemIds = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                // Add the Purchase Requisition ID (parts[1]) to the list
                itemIds.add(parts[1]);
            }
        }

        // Convert the List to an array and return it
        return itemIds.toArray(new String[0]);
    }
    
    // Getters & setters
    public String getPrID() { return prID; }
    public String getItemID() { return itemID; }
    public int getQuantity() { return quantity; }
    public String getDateRequired() { return dateRequired; }
    public String getSupplierID() { return supplierID; }
    public String getUserID() { return userID; }
    
    public void setPrID(String prID) { this.prID = prID; }
    public void setItemID(String itemID) { this.itemID = itemID; }
    public void setQuantity(int quantity) { 
        if (quantity >= 1) this.quantity = quantity; 
        else throw new IllegalArgumentException("Quantity must be >= 1"); 
    }
    public void setDateRequired(String dateRequired) { this.dateRequired = dateRequired; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }
    public void setUserID(String userID) { this.userID = userID; }
}
