package procurementordertrackingsystem.entities;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemEntry extends Item {
    private CRUDOntoFile crudUtil;
    private File itemFile;

    // Constructor that calls the parent Item constructor
    public ItemEntry(String itemID, String itemName, int stockLevel, double price, String supplierID) {
        super(itemID, itemName, stockLevel, price, supplierID); // Calls the parent constructor
        crudUtil = new CRUDOntoFile();
        itemFile = new File("items.txt"); // File to store item data (you may change this path)
    }

    // Add a new item (Create)
    public void addNewItem() throws IOException {
        // Prepare the item data in a string format that matches your file storage format
        String itemData = getItemID() + "," + getItemName() + "," + getStockLevel() + "," + getPrice() + "," + getSupplierID();
        
        // Call CRUD utility to write data to the file
        crudUtil.createToFile(itemFile, itemData);
        System.out.println("Item added successfully: " + itemData);
    }

    // Edit an item by itemID or itemName (Update)
    public void editItem(String searchItemID, String newItemName, int newStockLevel, double newPrice, String newSupplierID) throws IOException {
        // Read all items from the file
        List<String> lines = crudUtil.readFromAFile(itemFile);
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] itemData = line.split(",");
            
            // Match by itemID or itemName
            if (itemData[0].equals(searchItemID) || itemData[1].equals(searchItemID)) {
                // Modify item details
                itemData[1] = newItemName; // Update itemName
                itemData[2] = String.valueOf(newStockLevel); // Update stockLevel
                itemData[3] = String.valueOf(newPrice); // Update price
                itemData[4] = newSupplierID; // Update supplierID

                // Rewrite the updated item back into the list
                lines.set(i, String.join(",", itemData));
                break;
            }
        }

        // Overwrite the file with the updated list of items
        crudUtil.writeToFile(itemFile, lines);
        System.out.println("Item updated successfully.");
    }

    // Delete an item by itemID or itemName
    public void deleteItem(String searchItemID) throws IOException {
        // Read all items from the file
        List<String> lines = crudUtil.readFromAFile(itemFile);
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] itemData = line.split(",");
            
            // Match by itemID or itemName
            if (itemData[0].equals(searchItemID) || itemData[1].equals(searchItemID)) {
                // Remove the item from the list
                lines.remove(i);
                break;
            }
        }

        // Overwrite the file with the updated list (after deletion)
        crudUtil.writeToFile(itemFile, lines);
        System.out.println("Item deleted successfully.");
    }

    // Helper method to access item details for getters in Item class
    @Override
    public String getItemID() {
        return super.itemID;
    }
    
    @Override
    public String getItemName() {
        return super.itemName;
    }

    @Override
    public int getStockLevel() {
        return super.stockLevel;
    }

    @Override
    public double getPrice() {
        return super.price;
    }

    @Override
    public String getSupplierID() {
        return super.supplierID;
    }
}
