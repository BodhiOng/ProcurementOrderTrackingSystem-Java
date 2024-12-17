/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.ItemEntry;
import procurementordertrackingsystem.entities.PurchaseOrder;
import procurementordertrackingsystem.entities.PurchaseRequisition;
import procurementordertrackingsystem.entities.SalesEntry;
import procurementordertrackingsystem.entities.User;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.LoginPage;

/**
 *
 * @author LENOVO
 */
public class SalesManager {

    DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");

    //Create a class for Item functions that is used exclusively by SM
    private class SMitemfunctions extends Item {

        //Create a method to find an item record with a specific ID
        private List<Item> readItemsIntoObject(File filename) throws IOException {
            //Read the records into a List
            List<String> rawdata = crudOntoFile.readFromAFile(dfp.getItemFile());
            List<Item> itemlist = new ArrayList<>();
            //Iterate through the records to find the matched ID
            for (String lines : rawdata) {
                //Instantiate a new item object for every item record matched
                String[] linesitem = lines.split(",");
                itemlist.add(new Item(linesitem[0], linesitem[1], Integer.parseInt(linesitem[2]), Double.parseDouble(linesitem[3]), linesitem[4]));
            }
            //Return the list of item in the sales update
            return itemlist;
        }
        
        //Add the new records to item file and sales_entry file
        private void ApplySales(List<List<String>> salesdetail){
            //Create a list for only the updated items
            List<Item> itemlist = new ArrayList<>();
            //Create a list of item ID which will be updated
            List<String> idlist = new ArrayList<>();
            //Read all the item file
            try {
                itemlist = readItemsIntoObject(dfp.getItemFile());
            } catch (Exception e) {
                System.out.println("Error Reading Item File!");
            }

            //Update the stock level of the item from the sales report
            for (List<String> onesale : salesdetail) {
                for (Item item : itemlist) {
                    if (onesale.get(0).equals(item.getItemID())) {
                        item.setStockLevel(item.getStockLevel() - Integer.parseInt(onesale.get(1)));
                        break;
                    }
                }
            }

            //Append all the items object into a string list
            List<String> updatedItems = new ArrayList<>();
            for (Item item : itemlist) {
                updatedItems.add(item.toString());
            }

            //Write all the updated values into the text file
            if (updatedItems != null) {
                try {
                    crudOntoFile.writeUpdatedLinesToFile(dfp.getItemFile(), updatedItems);
                } catch (Exception e) {
                    System.out.println("Error Updating Item File!");
                }
            }
        }
        
        //Fetch Item ID from Item Name
        private String FindItemIDFromName(String name) {
            String id = null;
            List<String> itemdata = new ArrayList<>();
            String[] line = null;
            //Read all the item data
            try {
                itemdata = crudOntoFile.readFromAFile(dfp.getItemFile());
            } catch (Exception e) {
                System.out.println("Error Reading Item File!");
            }
            //Find the record with matching name
            for (String lines : itemdata) {
                line = lines.split(",");
                if (line[1].toLowerCase().equals(name.toLowerCase())) {
                    id = line[0];
                    break;
                }
            }
            //return the id of the record
            return id;
        }
        
        //Fetch Supplier ID from Item ID
        private String FindSupplierIDFromItemID(String id){
            String supid = null;
            List<String> itemdata = new ArrayList<>();
            String[] line = null;
            //Read all the item data
            try {
                itemdata = crudOntoFile.readFromAFile(dfp.getItemFile());
            } catch (Exception e) {
                System.out.println("Error Reading Item File!");
            }
            //Find the record with matching ID
            for (String lines : itemdata) {
                line = lines.split(",");
                if (line[0].toLowerCase().equals(id.toLowerCase())) {
                    supid = line[4];
                    break;
                }
            }
            //Return the supplier ID from the matching record
            return supid;
        }
    }

    //Create a class for SalesEntry functions that is used exclusively by SM
    private class SMsalesfunctions extends SalesEntry {

        //Method to add new sales entry into file
        private void EnterSales(List<List<String>> salesdata) throws IOException {
            String id = null;
            //Iterate through each sales list
            for (int i = 0; i < salesdata.size(); i++) {
                id = generateID();
                //Format the record into the specified format
                String newline = String.format("%s,%s,%s,%s", id, salesdata.get(i).get(0), salesdata.get(i).get(1), java.time.LocalDate.now());
                //Write each line into the sales_entry file
                try {
                    cof.createToFile(dfp.getSalesEntryFile(), newline);
                } catch (Exception e) {
                    System.out.println("Error Adding New Sales");
                }
            }
        }
        //Method to remove sales entry from file
        private void RemoveSales(List<String> id){
            List<String> allsales = getAllSales();
            //Instantiate an Iterator for the sales record
            Iterator<String> iterator = allsales.iterator();
            SMitemfunctions sif = new SMitemfunctions();
            //Record the length of the sales record
            int length = allsales.size();

            //Create a list of all the sales that needed to be reverted
            List<List<String>> saletorevert = new ArrayList<>();
            List<String> updatedsale = new ArrayList<>();
            for (String oneid : id) {
                String[] onesale = null;
                onesale = readSalesbyid(oneid);
                int qty = Integer.parseInt(onesale[2]);
                qty *= -1;
                onesale[2] = String.valueOf(qty);
                updatedsale.add(onesale[1]);
                updatedsale.add(onesale[2]);
                saletorevert.add(updatedsale);
            }

            //Revert all the sales from the item quantity
            sif.ApplySales(saletorevert);

            //Remove the sale from sales record
            while (iterator.hasNext()) {
                String[] sale = iterator.next().split(",");
                for (String eachid : id) {
                    if (sale[0].toLowerCase().equals(eachid.toLowerCase())) {
                        iterator.remove();
                        break;
                    }
                }
            }
            
            //Write the updated records into sales_entry file
            try {
                cof.writeUpdatedLinesToFile(dfp.getSalesEntryFile(), allsales);
            } catch (Exception e) {
                System.out.println("Error Updating Sales Entry!");
            }
            //Check if theres a change in the size
            if (allsales.size() == length) {
                System.out.println("No ID matched the Sales ID!");
            } else {
                System.out.println("Sales Removed Successfully!");
            }
        }
        //method to edit sales entry from file
        private List<List<String>> PreviewUpdateSales(String id, String item, int qty){
            List<List<String>> updatedsales = new ArrayList<>();
            List<String> updateitem1 = new ArrayList<>(), updatesale = new ArrayList<>(), updateitem2 = new ArrayList<>();
            SMitemfunctions sif = new SMitemfunctions();
            
            String itemid = sif.FindItemIDFromName(item);
            
            String[] onesale = readSalesbyid(id);
            String olditemid = onesale[1];
            
            int qtydiff = Integer.parseInt(onesale[2]) - qty;
            onesale[1] = itemid;
            onesale[2] = String.valueOf(qty);
            System.out.println("Preview of the updated sales:");
            String itemname = fetchItemNameFromId(itemid);
            if (Objects.isNull(itemname)) {
                System.out.println("Invalid item name!");
                return null;
            }
            System.out.println(String.format("SalesID: %s, Item Name: %s, Quantity: %s, Sale Date: %s", onesale[0], itemname, onesale[2], onesale[3]));
            updateitem1.add(olditemid);
            updateitem1.add(String.valueOf(qty*-1));
            updateitem2.add(itemid);
            updateitem2.add(String.valueOf(qty));
            updatedsales.add(updateitem1);
            updatedsales.add(updateitem2);
            updatesale.addAll(Arrays.asList(onesale));
            updatedsales.add(updatesale);
            return updatedsales;
        }
        //method to apply the edited sales entry into the file
        private void UpdateSales(List<List<String>> updatedsale){
            SMitemfunctions sif = new SMitemfunctions();
            List<List<String>> updateditem = new ArrayList<>();
            updateditem.add(updatedsale.get(0));
            updateditem.add(updatedsale.get(1));
            String updatedsales = String.format("%s,%s,%s,%s", updatedsale.get(2).get(0), updatedsale.get(2).get(1), updatedsale.get(2).get(2), updatedsale.get(2).get(3));
            sif.ApplySales(updateditem);
            List<String> allsales = getAllSales();
            for (int i = 0; i < allsales.size(); i++){
                if (allsales.get(i).split(",")[0].toLowerCase().equals(updatedsale.get(2).get(0).toLowerCase())) {
                    allsales.set(i, updatedsales);
                    break;
                }
            }
            try {
                cof.writeUpdatedLinesToFile(dfp.getSalesEntryFile(), allsales);
            } catch (Exception e) {
                System.out.println("Error Updating Sales Entry File!");
                return;
            }
            System.out.println("Sales Edited Successfully!");
        }
    }
    
    //Create a class for PurchaseRequisition functions that is used exclusively by SM
    private class SMprfunctions extends PurchaseRequisition{
        //Method to create new Purchase Requisition
        private void AddPR(List<String> prlist){
            try {
                crudOntoFile.createToFile(dfp.getPurchaseRequisitionFile(), prlist);
            } catch (Exception e) {
                System.out.println("Error updating Purchase Requisition file!");
                return;
            }
            System.out.println("Purchase Requisition Submitted Successfully!");
        }
    }
    
    //Method to display menu in CLI
    public void DisplayMenu(String role) throws IOException {
        Scanner sc = new Scanner(System.in);
        String menu = """
                  1. View List of Items
                  2. Sales Menu
                  3. View Stock Level
                  4. Create Purchase Requisition
                  5. List of Purchase Orders
                  """;

        // Append options based on role
        if ("Administrators".equalsIgnoreCase(role)) {
            menu += "6. Go Back\n"; // Admin role gets "Go Back"
        } else {
            menu += "6. Logout\n";   // Non-admin role gets "Logout"
            menu += "7. Exit\n";     // Non-admin role gets "Exit"
        }

        // Loop the menu until the user chooses to exit or log out
        Main_Menu:
        while (true) {
            int choice;
            System.out.println("\n------------------------------");
            System.out.println(menu);
            System.out.print("Please Select a Menu (1-7): ");

            // Take input from user
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid menu. Please input a number from 1-7");
                sc.nextLine(); 
                continue;
            }

            // Evaluate the user input to match the preset menu numbers
            switch (choice) {
                case 1:
                    ViewItems();
                    break;
                case 2:
                    String salesEntryMenu = """
                                        1. View Sales
                                        2. Add Sales
                                        3. Edit Sales
                                        4. Delete Sales
                                        5. Main Menu
                                        """;
                    //Loop the menu until the user chooses to return to main menu
                    Sales_Entry_Menu:
                    while (true) {
                        System.out.println(salesEntryMenu);
                        System.out.println("Please Select a Menu (1-5): ");
                        try {
                            choice = sc.nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid menu. Please input a number from 1-5");
                            sc.nextLine(); 
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                ViewSales(); //Go into view sales function
                                break;
                            case 2:
                                AddSales(); //Go into add sales function
                                break;
                            case 3:
                                EditSales(); //Go into edit sales function
                                break;
                            case 4:
                                DeleteSales(); //Go into delete sales function
                                break;
                            case 5:
                                break Sales_Entry_Menu; // Go back to main menu
                            default:
                                System.out.println("Invalid menu. Please select a number from 1-5");
                        }
                        System.out.println("------------------------------");
                    }
                    break;
                case 3:
                    ViewStocks(); //Go into view stock function
                    break;
                case 4:
                    CreatePR(); //Go into create purchase requisition function
                    break;
                case 5:
                    ViewPO(); //Go into view purchase orders function
                    break;
                case 6:
                    //Go back into administrator menu if the user is admin
                    if ("Administrators".equalsIgnoreCase(role)) {
                        System.out.println("Going Back...");
                        Administrators admin = new Administrators();
                        admin.displayMenu();
                        break Main_Menu;
                    //Logout from the system if the user is not admin
                    } else {
                        System.out.println("Logging out...");
                        LoginPage loginPage = new LoginPage();
                        loginPage.login();
                    }
                    break;
                case 7: //Option for non-admin to exit the system
                    if (!"Administrators".equalsIgnoreCase(role)) {
                        System.out.println(" Exiting the system...");
                        System.exit(0); // Exit for non-admins
                    } else {
                        System.out.println("Invalid menu. Please select a number from 1-7");
                    }
                    break;
                default:
                    System.out.println("Invalid menu. Please select a number from 1-7");
                    break;
            }
            System.out.println("------------------------------");
        }
    }

    //SM 1st Functionality (View list of items)
    private void ViewItems() {
        SMitemfunctions smif = new SMitemfunctions();
        smif.readItemsFromFile(dfp.getItemFile());
    }

    //SM 2nd Functionality, 1st sub-function (View List of Sales)
    private void ViewSales() {
        SalesEntry se = new SalesEntry();
        se.readAllSales();
    }
    
    //SM 2nd Functionality 2nd sub-function (Add Sales Record)
    private void AddSales() throws IOException {
        String item = null;
        int salequantity = 0;
        List<List<String>> salesentry = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        SMitemfunctions sif = new SMitemfunctions();
        SMsalesfunctions ssf = new SMsalesfunctions();
        
        //Ask the user for item and quantity
        System.out.println("Enter the item name: ");
        try {
            item = sc.next();
        } catch (Exception e) {
            System.out.println("Error reading item name!");
            sc.nextLine(); 
            return;
        }
        System.out.println("Enter quantity sold: ");
        try {
            salequantity = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error reading quantity!");
            sc.nextLine(); 
            return;
        }
        //Find the item ID from the input item name
        List<String> onesale = new ArrayList<>();
        String itemid = sif.FindItemIDFromName(item);
        if (Objects.isNull(itemid)) {
            System.out.println("Invalid item name!");
            return;
        }
        else{
            onesale.add(itemid);        
        }
        onesale.add(String.valueOf(salequantity));
        //Add all the input into one list
        salesentry.add(onesale);

        String addSalesMenu = """
                              Please select an option
                              1. Add Item
                              2. Apply Sale(s)
                              3. Return to Sales Menu
                              """;
        int choice = 0;
        //Loop the menu until the user chooses to apply sales or return
        Add_Sales_Entry:
        while (true) {
            System.out.println("------------------------------");
            System.out.println(addSalesMenu);
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input! Please Select a number from 1-3");
                sc.nextLine();
                continue;
            }
            switch (choice) {
                case 1:
                    //Ask the user for another item and quantity
                    try {
                        System.out.println("Enter the item name: ");
                        item = sc.next();
                    } catch (Exception e) {
                        System.out.println("Error reading item name!");
                        sc.nextLine(); 
                        return;
                    }
                    try {
                        System.out.println("Enter quantity sold: ");
                        salequantity = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Error reading quantity!");
                        sc.nextLine(); 
                        return;
                    }
                    
                    //Add all the entry into the main list
                    List<String> anothersale = new ArrayList<>();
                    anothersale.add(sif.FindItemIDFromName(item));
                    anothersale.add(String.valueOf(salequantity));
                    salesentry.add(anothersale);
                    System.out.println(salesentry);
                    break;
                case 2:
                    //Add and apply all the sales entry
                    ssf.EnterSales(salesentry);
                    sif.ApplySales(salesentry);
                    System.out.println("Sales Updated Successfuly!");
                case 3:
                    //Go back to main menu
                    break Add_Sales_Entry;
                default:
                    System.out.println("Invalid Input! Please Select a number from 1-3");
            }
        }
    }
    
    //SM 2nd Functionality 3th sub-function (Edit Sales Record)
    private void EditSales(){
        Scanner sc = new Scanner(System.in);
        SMsalesfunctions ssf = new SMsalesfunctions();
        SMitemfunctions sif = new SMitemfunctions();
        List<List<String>> updatedsale = new ArrayList<>();
        int choice = 0, qty = 0;
        String itemname = null, id = null;
        
        //Ask the user for the Sales ID they want to edit
        System.out.println("Enter the Sales ID you want to edit: ");
        try {
            id = sc.next();
        } catch (Exception e) {
            System.out.println("Please enter a correct ID!");
            sc.nextLine(); 
            return;
        }
        
        //Check and Display the existence of the sales record
        if (Objects.isNull(ssf.displayOneSaleById(id))) {
            return;
        }
        else{
            System.out.println(ssf.displayOneSaleById(id));
        }
        
        
        
        //Ask user for the new details
        System.out.println("Enter the new item name: ");
        try {
            itemname = sc.next();
        } catch (Exception e) {
            System.out.println("Error reading item name!");
            sc.nextLine();
            return;
        }
        System.out.println("Enter the new sale quantity: ");
        try {
            qty = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error reading quantity!");
            sc.nextLine();
            return;
        }
        
        //Check the integrity of the new details
        List<List<String>> onesale = ssf.PreviewUpdateSales(id, itemname, qty);
        if (Objects.isNull(onesale)){
            return;
        }
        
        //Ask user for update confirmation
        System.out.println("------------------------------");
        System.out.println("""
                           1. Confirm Update
                           2. Return to Sales Menu
                           """);
        try {
            choice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Please input a number between 1-2");
            sc.nextLine(); 
            return;
        }
        switch (choice) {
            case 1:
                ssf.UpdateSales(onesale); //Apply the updated record
                break;
            case 2:
                break; //Return to the sales menu
            default:
                System.out.println("Invalid Input! Please select a number from 1-2");
        }           
    }
    
    //SM 2nd Functionality 4th sub-function (Delete Sales Record)
    private void DeleteSales(){
        SMsalesfunctions ssf = new SMsalesfunctions();
        Scanner sc = new Scanner(System.in);
        List<String> id = new ArrayList<>();
        String checkid = null;
        
        //Loop the menu until the user chooses to return to main menu
        Delete_Sales_Menu:
        while (true){
            int choice = 0;
            
            //Ask the user to input the SalesID to be removed
            System.out.println("Enter the Sales ID to be removed: ");
            try {
                checkid = sc.next();
            } catch (Exception e) {
                System.out.println("Invalid Sales ID!");
                sc.nextLine(); 
                return;
            }
            //Check if the ID exists
            if (Objects.isNull(ssf.displayOneSaleById(checkid))) {
                System.out.println("Invalid Sales ID!");
                sc.nextLine();
                return;
            }
            else{
                //Show the preview of the record that will be deleted
                id.add(checkid);
                System.out.println("Confirm remove the following sale:");
                System.out.println(ssf.displayOneSaleById(checkid));
            }
            //Ask the user for confirmation and if they want to remove another record
            System.out.println("------------------------------");
            System.out.println("""
                               1. Add sale to be removed
                               2. Confirm delete from record
                               3. Return to Sales Menu
                               
                               Please Select a Menu 1-3: 
                               """);
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input! Please Select a number from 1-3");
                sc.nextLine(); 
                return;
            }
            switch (choice) {
                case 1:
                    continue;
                case 2:
                    //Check if they accidentally try to remove nothing
                    if (id.size() < 1) {
                        System.out.println("No Sale to be removed!");
                    }
                    else{
                        ssf.RemoveSales(id); //remove the records
                    }
                    break Delete_Sales_Menu;
                case 3:
                    break Delete_Sales_Menu; //Go back to sales menu
                default:
                    System.out.println("Invalid Input! Please select a number from 1-3");
                    ;
            }
        }
    }
    
    //SM 3rd Functionality (View Stock Level)
    private void ViewStocks(){
        ItemEntry ie = new ItemEntry();
        ie.viewStockLevels();
    }
    
    //SM 4th Functionality (Creat PR)
    private void CreatePR(){
        SMitemfunctions sif = new SMitemfunctions();
        SMprfunctions spf = new SMprfunctions();
        User user = new User();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Scanner sc = new Scanner(System.in);
        String itemname = null, username = null, dateinput = null;
        List<String> prlist = new ArrayList<>();
        int qty = 0;
        Date date = new Date();
        
        //Ask the user for their username
        System.out.println("Enter your username: ");
        try {
            username = sc.next();
        } catch (Exception e) {
            System.out.println("Please Enter a Correct Username!");
            sc.nextLine(); 
            return;
        }
        
        //Check their username existence
        String userid = user.getUserIDfromUsername(username);
        if (Objects.isNull(userid)) {
            System.out.println("username is not found!");
            return;
        }
        
        int choice = 0, count = 0, id = 0;
        String strid;
        //Loop the menu until the user chooses to return to main menu
        Create_PR_Menu:
        while (true) {
            //Ask the user for the PR details
            System.out.println("Enter item name: ");
            try {
                itemname = sc.next();
            } catch (Exception e) {
                System.out.println("Invalid Item Name!");
                sc.nextLine(); 
                break;
            }
            System.out.println("Enter quantity to order: ");
            try {
                qty = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter Quantity in numbers!");
                sc.nextLine(); 
                break;
            }
            System.out.println("Enter the expected resupply date (yyyy-mm-dd): ");
            try {
                dateinput = sc.next();
            } catch (Exception e) {
                System.out.println("Please input a correct date in (yyyy-mm-dd)!");
                sc.nextLine(); 
                break;
            }
            //Validate the date input
            try {
                date = df.parse(dateinput);
            } catch (Exception e) {
                System.out.println("Please input a correct date in (yyyy-mm-dd)!");
                sc.nextLine(); 
                break;
            }
            //Check the existence of the item
            String itemid = sif.FindItemIDFromName(itemname);
            if (Objects.isNull(itemid)) {
                System.out.println("Could not find an item with that name!");
                sc.nextLine(); 
                break;
            }
            //Fetch the supplier ID of the item
            String supplyid = sif.FindSupplierIDFromItemID(itemid);
            if (Objects.isNull(supplyid)) {
                System.out.println("Could not find a supplier for that item!");
                sc.nextLine(); 
                break;
            }
            
            //Preview the Purchase Requisition
            id = Integer.parseInt(spf.generateID().substring(2));
            strid = String.format("PR%04d", id+count);
            prlist.add(String.format("%s,%s,%s,%s,%s,%s", strid, itemid, qty, df.format(date), supplyid, userid));
            count++;
            System.out.println("Purchase Requisition preview:");
            System.out.println(String.format(
                                "PR ID: %s, Itemname: %s, Quantity: %s, Date: %s, Supplier ID: %s, Raised by: %s",
                                prlist.getLast().split(",")[0], itemid, qty, df.format(date), supplyid, userid));
            //Ask user for confirmation or if they want to add another Purchase Requisition
            System.out.println("------------------------------");
            System.out.println("""
                               1. Add Purchase Requisition
                               2. Submit Purchase Requisition
                               3. Return to Main Menu
                               """);
            System.out.println("Please Select a menu 1-3: ");
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input! Please select a number 1-3");
                sc.nextLine(); 
                continue;
            }
            switch (choice) {
                case 1:
                    continue; //Add another Purchase Requisition
                case 2:
                    spf.AddPR(prlist); //Create the new Purchase Requisition(s)
                    break Create_PR_Menu;
                case 3:
                    break Create_PR_Menu; //Go back to main menu
                default:
                    System.out.println("Invalid input! Please select a number 1-3");
            }
        }
    }
    
    //SM 5th Functionality (View Purchase Orders)
    private void ViewPO(){
        PurchaseOrder po = new PurchaseOrder();
        try {
            po.readPurchaseOrdersFromFile(dfp.getPurchaseOrderFile());
        } catch (Exception e) {
            System.out.println("Error reading Purchase Order file!");
            return;
        }
    }
}