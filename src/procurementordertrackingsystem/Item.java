package procurementordertrackingsystem;

import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.util.List;

public class Item {
    private String itemID; // Unique identifier for the item
    private String itemName; // Name of the item
    private int stockLevel; // Quantity of the item in stock
    private double price; // Price of the item
    private String supplierID; // ID of the supplier

    // Constructor
    public Item(String itemID, String itemName, int stockLevel, double price, String supplierID) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.stockLevel = stockLevel;
        this.price = price;
        this.supplierID = supplierID;
    }

    // Check the stock status of the item (instance method)
    public String checkStockStatus() { return stockLevel > 0 ? "In Stock" : "Out of Stock"; }
    
    // CRUDOntoFile object instantiation
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to read all items
    public void readItemsFromFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String output = String.format(
                        "Item ID: %s, Item Name: %s, Stock Level: %s, Price: %s, Supplier ID: %s",
                        parts[0], parts[1], parts[2], parts[3], parts[4]
                );
                System.out.println(output);
            }
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
