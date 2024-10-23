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

    // Constructor
    public PurchaseOrder(String poID, String prID, String purchaseManagerID, String status, String dateGenerated) {
        this.poID = poID;
        this.prID = prID;
        this.purchaseManagerID = purchaseManagerID;
        this.status = status;
        this.dateGenerated = dateGenerated;
    }
        
    // CRUDOntoFile object instantiation
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to read all POs
    public void readPurchaseOrdersFromFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String output = String.format(
                    "Purchase Order ID: %s, Purchase Requisition ID: %s, Purchase Manager ID: %s, Status: %s, Date Generated: %s",
                    parts[0], parts[1], parts[2], parts[3], parts[4]
                );
                System.out.println(output);
            }
        }
    }

    // Method to update PO status from a file (approve/reject)
    public void updatePurchaseOrderStatusFromFile(String filename, boolean approve, String poID) {
        List<String> lines = crudOntoFile.readFromAFile(filename);
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
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
}