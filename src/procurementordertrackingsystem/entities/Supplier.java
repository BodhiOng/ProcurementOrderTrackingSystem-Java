package procurementordertrackingsystem.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import procurementordertrackingsystem.utilities.CRUDOntoFile;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private String itemID;
    private String phoneNumber;
    private String address;

    // Default constructor
    public Supplier(){
        this.supplierID = "";
        this.supplierName = "";
        this.itemID = "";
        this.phoneNumber = "";
        this.address = "";
    }
    
    // Constructor
    public Supplier(String supplierID, String supplierName, String itemID, String phoneNumber, String address) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.itemID = itemID;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    
    // Instance of CRUDOntoFile for purchase order related operations
    public CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to read all POs
    public void readSuppliersFromFile(File filename) throws IOException {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        
        // Go through each line for PO details to be printed
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            // Ensure there are that much parts in the line & print output to terminal
            if (parts.length == 5) {
                String output = String.format(
                    "Supplier ID: %s, Supplier Name: %s, Item ID: %s, Phone Number: %s, Address: %s",
                    parts[0], parts[1], parts[2], parts[3], parts[4]
                );
                System.out.println(output);
            }
        }
    }
    
    // Get Supplier IDs from Supplier file to be stored in an array
    public String[] getsupplierIDsFromSupplierFile(File filename) throws IOException {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        List<String> supplierIDs = new ArrayList<>(); // To hold the Supplier IDs

        // Go through each line
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            if (parts.length == 5) {
                // Add the Supplier ID to the list
                supplierIDs.add(parts[0]);
            }
        }

        // Convert the List to an array and return it
        return supplierIDs.toArray(new String[0]);
    }

    // Getters & setters
    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

