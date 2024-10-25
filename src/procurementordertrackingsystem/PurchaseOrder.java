package procurementordertrackingsystem;

import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
    private String poID; // Purchase Order ID
    private String prID; // Purchase Requisition ID
    private String purchaseManagerID; // Purchase Manager ID
    private String status; // Status: Pending, Approved, Rejected
    private String dateGenerated; // Date when the PO was generated
    private String paidStatus; // Paid status: Unpaid or Paid

    // Default constructor
    public PurchaseOrder() {
        this.poID = "";
        this.prID = "";
        this.purchaseManagerID = "";
        this.status = "";
        this.dateGenerated = "";
        this.paidStatus = "";
    }
    
    // Constructor
    public PurchaseOrder(String poID, String prID, String purchaseManagerID, String status, String dateGenerated, String paidStatus) {
        this.poID = poID;
        this.prID = prID;
        this.purchaseManagerID = purchaseManagerID;
        this.status = status;
        this.dateGenerated = dateGenerated;
        this.paidStatus = paidStatus;
    }
        
    // Instance of CRUDOntoFile for purchase order related operations
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to read all POs
    public void readPurchaseOrdersFromFile(String filename) throws IOException {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        
        // Go through each line for PO details to be printed
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            // Ensure there are that much parts in the line & print output to terminal
            if (parts.length == 6) {
                String output = String.format(
                    "Purchase Order ID: %s, Purchase Requisition ID: %s, Purchase Manager ID: %s, Status: %s, Date Generated: %s, Paid Status: %s",
                    parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]
                );
                System.out.println(output);
            }
        }
    }
  
    
    // Get PO IDs from PO file to be stored in an array
    public String[] getPurchaseOrderIDsFromPurchaseOrderFile(String filename) throws IOException {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        List<String> poIDs = new ArrayList<>(); // To hold the PO IDs

        // Go through each line
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            if (parts.length == 6) {
                // Add the PO ID to the list
                poIDs.add(parts[0]);
            }
        }

        // Convert the List to an array and return it
        return poIDs.toArray(new String[0]);
    }
    
    // Getters and setters
    public String getPoID() { return poID; }
    public void setPoID(String poID) { this.poID = poID; }

    public String getPrID() { return prID; }
    public void setPrID(String prID) { this.prID = prID; }

    public String getPurchaseManagerID() { return purchaseManagerID; }
    public void setPurchaseManagerID(String purchaseManagerID) { this.purchaseManagerID = purchaseManagerID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateGenerated() { return dateGenerated; }
    public void setDateGenerated(String dateGenerated) { this.dateGenerated = dateGenerated; }
    
    public String getPaidStatus() { return paidStatus; }
    public void setPaidStatus(String paidStatus) { this.paidStatus = paidStatus; }
}