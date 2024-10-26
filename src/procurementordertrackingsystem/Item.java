package procurementordertrackingsystem;

import java.io.File;
import java.io.IOException;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.util.List;

public class Item {
    private String itemID; // Unique identifier for the item
    private String itemName; // Name of the item
    private int stockLevel; // Quantity of the item in stock
    private double price; // Price of the item
    private String supplierID; // ID of the supplier

    // Default constructor
    public Item() {
        this.itemID = "";
        this.itemName = "";
        this.stockLevel = 0;
        this.price = 0.0;
        this.supplierID = "";
    }
    
    // Constructor
    public Item(String itemID, String itemName, int stockLevel, double price, String supplierID) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.stockLevel = stockLevel;
        this.price = price;
        this.supplierID = supplierID;
    }
    
    // Instance of CRUDOntoFile for operations related to items
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to read all items
    public void readItemsFromFile(File filename) {
        try {
            List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents

            // Go through each line for item details to be printed
            for (String line : lines) {
                // Separate line by commas
                String[] parts = line.split(",");
                // Ensure there are that muc parts in the line & print output to terminal
                if (parts.length == 5) {
                    String output = String.format(
                            "Item ID: %s, Item Name: %s, Stock Level: %s, Price: %s, Supplier ID: %s",
                            parts[0], parts[1], parts[2], parts[3], parts[4]
                    );
                    System.out.println(output);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading items from file: " + e.getMessage());
        }
    }

    // Getters and Setters
    public String getItemID() { return itemID; }
    public void setItemID(String itemID) { this.itemID = itemID; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public int getStockLevel() { return stockLevel; }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }
}
