package procurementordertrackingsystem.entities;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.roles.InventoryManager;
import procurementordertrackingsystem.utilities.DataFilePaths;

public class ItemEntry extends Item {
    private CRUDOntoFile crudUtil;
    private File itemFile;
    private DataFilePaths filePaths;

    // Constructor that calls the parent Item constructor
    public ItemEntry(String itemID, String itemName, int stockLevel, double price, String supplierID) {
        super(itemID, itemName, stockLevel, price, supplierID); // Calls the parent constructor
        crudUtil = new CRUDOntoFile();
        itemFile = new File("items.txt"); // File to store item data (you may change this path)
    }
    
        public ItemEntry() {
        crudUtil = new CRUDOntoFile();
        itemFile = new File("items.txt"); // File to store item data (you may change this path)
        this.filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");

    }
    
    static int menuOption1 = 0;  
    static String deMenu = "Hello Inventory Manager, how would you like to manage the inventory?\n" +  
            "Select an option from the below choices:\n" +  
            "1. Item Entry\n" +  
            "2. Supplier Registration\n" +  
            "3. View Stock Level\n" +  
            "4. Update Stock\n" +  
            "5. Manage Stock Details\n" +  
            "********************************"; 
        public static void menu() {  
        Scanner userOption2 = new Scanner(System.in); // Initialize Scanner with System.in  
        boolean running = true;  

        while (running) {  
            System.out.println(deMenu); // Display the menu  
            System.out.print("You are now in the Item Entry Page, Select the Functionality that you would like to use: ");  
            // Validate input  
            if (userOption2.hasNextInt()) {  
                menuOption1 = userOption2.nextInt();  
                userOption2.nextLine(); // Consume newline character  
                DisplayData viewDeData = new DisplayData();
                switch (menuOption1) { 
                    case 1:  
                        System.out.println("You selected Item Entry.");  
                        ItemEntry us = new ItemEntry();
                        us.addNewItem();
                        break;  
                    case 2:  
                        System.out.println("You selected Supplier Registration.");  
                        
                        break;  
                    case 3:  
                        System.out.println("You selected View Stock Level.");  
                        break;  
                    case 4:  
                        System.out.println("You selected Exit Page");  //
                        // Call your stock updating method here  
                        break;  

                    case 0: // Option to exit  
                        running = false;  
                        break;  
                    default:  
                        System.out.println("Invalid option. Please try again.");  
                }  
            } else {  
                System.out.println("Please enter a valid number.");  
                userOption2.next(); // Consume invalid input  
            }  
        }  

        userOption2.close(); // Close the scanner when done  
        System.out.println("Exiting the Inventory Manager. Goodbye!");  
    }  


     
    public void addNewItem() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter Item ID: ");
        String itemID = scanner.nextLine();
        
        System.out.print("Enter Item Name: ");
        String itemName = scanner.nextLine();
        
        System.out.print("Enter Stock Level: ");
        int stockLevel = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Enter Supplier ID: ");
        String supplierID = scanner.nextLine();
        
        // Create new item
        Item newItem = new Item(itemID, itemName, stockLevel, price, supplierID);
        
        // Save to file
        File itemFile = filePaths.getItemFile();
        String lineToSave = String.format("%s,%s,%d,%.2f,%s", 
                                           newItem.getItemID(),
                                           newItem.getItemName(),
                                           newItem.getStockLevel(),
                                           newItem.getPrice(),
                                           newItem.getSupplierID());

        // Use CRUDOntoFile to write the new item
        try {
            crudOntoFile.createToFile(itemFile, lineToSave); // Use the instance of CRUDOntoFile
            System.out.println("Item added successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to item file: " + e.getMessage());
        }
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
        crudUtil.createToFile(itemFile, lines);
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
        crudUtil.createToFile(itemFile, lines);
        System.out.println("Item deleted successfully.");
    }

    
}
