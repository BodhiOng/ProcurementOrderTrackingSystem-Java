package procurementordertrackingsystem.roles;

import procurementordertrackingsystem.entities.Item;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import procurementordertrackingsystem.entities.ItemEntry;
import procurementordertrackingsystem.entities.SupplierRegistration;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.LoginPage;

public class InventoryManager {
    
    private final DataFilePaths filePaths;
    private final CRUDOntoFile crudOntoFile;  // Create an instance of CRUDOntoFile
    private final Item item;

    static int menuOption = 0;  
    static String deMenu = "\n****************************************\n" +
            "✨ WELCOME TO INVENTORY MANAGEMENT SYSTEM ✨\n" +
            "****************************************\n" +
            "📋 Choose an option below:\n" +  
            "1. 📥 Item Entry\n" +  
            "2. 📦 Supplier Registration\n" +  
            "3. 📊 View Stock Levels\n" +  
            "4. 🛠️ Manage Stock Details\n" +  
            "5. ❌ Exit\n" +  
            "****************************************\n";  

    // Static method to display the menu  
    public static void menu() throws IOException {  
        Scanner userOption = new Scanner(System.in); // Initialize Scanner with System.in  
        boolean running = true;  

        while (running) {  
            System.out.println(deMenu); // Display the menu  
            System.out.print("💡 Enter your choice: ");  

            // Validate input  
            if (userOption.hasNextInt()) {  
                menuOption = userOption.nextInt();  
                userOption.nextLine(); // Consume newline character  
                InventoryManager us = new InventoryManager();
                ItemEntry item1 = new ItemEntry();
                LoginPage loginPage = new LoginPage();
                File dataFile = new File("src/procurementordertrackingsystem/data/user.txt");
                switch (menuOption) {  
                    case 1:  
                        System.out.println("\n✅ You selected: 📥 Item Entry");  
                        item1.itemMenu();
                        break;  
                    case 2:  
                        System.out.println("\n✅ You selected: 📦 Supplier Registration");  
                        SupplierRegistration.supplierMenu();
                        break;  
                    case 3:  
                        System.out.println("\n✅ You selected: 📊 View Stock Levels");  
                        item1.viewStockLevels();
                        break;  
                    case 4:  
                        System.out.println("\n✅ You selected: 🛠️ Manage Stock Details");  
                        item1.updateStockLevels(); 
                        break;  
                    case 5:  
                        System.out.println("\n❌ Logged Out Inventory Management System... See you next time!\n");  
                        loginPage.login();
                        running = false;
                        break;  
                    default:  
                        System.out.println("⚠ Invalid option. Please choose a valid menu option.\n");  
                }  
            } else {  
                System.out.println("⚠ Error: Please enter a valid number.\n");  
                userOption.next(); // Consume invalid input  
            }  
        }  

        userOption.close(); // Close the scanner when done  
        System.out.println("💤 Shutting down Inventory Manager... Goodbye!\n");  
    }  

    public InventoryManager() {
        // Initialize file paths and item entity
        this.filePaths = new DataFilePaths("src/procurementordertrackingsystem/data");
        this.item = new Item(); // Adjust the constructor as needed
        this.crudOntoFile = new CRUDOntoFile(); // Correctly initialize CRUDOntoFile
    }
}