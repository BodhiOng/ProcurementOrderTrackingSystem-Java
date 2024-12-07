package procurementordertrackingsystem.roles;

import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.DisplayData;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;

public class InventoryManager {
    
    private final DataFilePaths filePaths;
    private final CRUDOntoFile crudOntoFile;  // Create an instance of CRUDOntoFile
    private final Item item;

    static int menuOption = 0;  
    static String deMenu = "Hello Inventory Manager, how would you like to manage the inventory?\n" +  
            "Select an option from the below choices:\n" +  
            "1. Item Entry\n" +  
            "2. Supplier Registration\n" +  
            "3. View Stock Level\n" +  
            "4. Update Stock\n" +  
            "5. Manage Stock Details\n" +  
            "********************************";  

    // Static method to display the menu  
    public static void menu() {  
        Scanner userOption = new Scanner(System.in); // Initialize Scanner with System.in  
        boolean running = true;  

        while (running) {  
            System.out.println(deMenu); // Display the menu  
            System.out.print("Choose an option: ");  

            // Validate input  
            if (userOption.hasNextInt()) {  
                menuOption = userOption.nextInt();  
                userOption.nextLine(); // Consume newline character  
                InventoryManager us = new InventoryManager();
                File dataFile = new File("C:\\Users\\user\\Downloads\\OODJ\\ProcurementOrderTrackingSystem-Java-main\\ProcurementOrderTrackingSystem-Java\\src\\procurementordertrackingsystem\\data\\user.txt");
                DisplayData viewDeData = new DisplayData();
                switch (menuOption) {  
                    case 1:  
                        System.out.println("You selected Item Entry.");  
                        us.addNewItem();
                        break;  
                    case 2:  
                        System.out.println("You selected Supplier Registration.");  
                        // Call your supplier registration method here  
                        break;  
                    case 3:  
                        System.out.println("You selected View Stock Level.");  
                        viewDeData.displayItems(dataFile);
                        break;  
                    case 4:  
                        System.out.println("You selected Update Stock.");  
                        // Call your stock updating method here  
                        break;  
                    case 5:  
                        System.out.println("You selected Manage Stock Details.");  
                        // Call your stock details management method here  
                        break;  
                    case 0: // Option to exit  
                        running = false;  
                        break;  
                    default:  
                        System.out.println("Invalid option. Please try again.");  
                }  
            } else {  
                System.out.println("Please enter a valid number.");  
                userOption.next(); // Consume invalid input  
            }  
        }  

        userOption.close(); // Close the scanner when done  
        System.out.println("Exiting the Inventory Manager. Goodbye!");  
    }  


    
    public InventoryManager() {
        // Initialize file paths and item entity
        this.filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        this.item = new Item(); // Adjust the constructor as needed
        this.crudOntoFile = new CRUDOntoFile(); // Correctly initialize CRUDOntoFile
    }

    // Method to add a new item to inventory
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
            crudOntoFile.writeToAFile(itemFile, lineToSave); // Use the instance of CRUDOntoFile
            System.out.println("Item added successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to item file: " + e.getMessage());
        }
    }

    // Method to update stock level
    public void updateStockLevel() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter Item ID to update stock: ");
        String itemID = scanner.nextLine();
        
        System.out.print("Enter new Stock Level: ");
        int stockLevel = Integer.parseInt(scanner.nextLine());
        
        File itemFile = filePaths.getItemFile();
        
        try {
            // Load all items, then find and update the specific item
            List<String> itemData = crudOntoFile.readFromAFile(itemFile); // Using List<String>
            boolean itemFound = false;
            StringBuilder updatedItems = new StringBuilder();
            
            for (String line : itemData) {
                String[] parts = line.split(",");
                if (parts[0].equals(itemID)) {
                    // If item found, update stock level
                    parts[2] = String.valueOf(stockLevel); // Update stock level
                    itemFound = true;
                }
                // Reconstruct the line
                String updatedLine = String.join(",", parts);
                updatedItems.append(updatedLine).append("\n");
            }
            
            // Write back the updated inventory to file
            if (itemFound) {
                crudOntoFile.writeUpdatedLinesToFile(itemFile, List.of(updatedItems.toString().trim().split("\n"))); // Using the instance
                System.out.println("Stock level updated successfully!");
            } else {
                System.out.println("Item ID not found!");
            }
            
        } catch (IOException e) {
            System.err.println("Error updating item stock: " + e.getMessage());
        }
    }

    // Method to view current inventory
    public void viewInventory() {
        File itemFile = filePaths.getItemFile();
        try {
            System.out.println("----- Current Inventory -----");
            List<String> items = crudOntoFile.readFromAFile(itemFile); // Read items using CRUDOntoFile
            for (String line : items) {
                System.out.println(line);
}
        } catch (IOException e) {
            System.err.println("Error reading item file: " + e.getMessage());
        }
    }
}
