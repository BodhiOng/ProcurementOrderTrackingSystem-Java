/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.SalesEntry;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.LoginPage;
import procurementordertrackingsystem.utilities.ReferentialIntegrity;

/**
 *
 * @author LENOVO
 */
public class SalesManager {

    DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");
    ReferentialIntegrity ri = new ReferentialIntegrity();

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
                    System.out.println("Sales Updated Successfuly!");
                } catch (Exception e) {
                    System.out.println("Error Updating Item File!");
                }
            }
        }

        private String FindItemIDFromName(String name) {
            String id = null;
            List<String> itemdata = new ArrayList<>();
            String[] line = null;
            try {
                itemdata = crudOntoFile.readFromAFile(dfp.getItemFile());
            } catch (Exception e) {
                System.out.println("Error Reading Item File!");
            }
            for (String lines : itemdata) {
                line = lines.split(",");
                if (line[1].toLowerCase().equals(name.toLowerCase())) {
                    id = line[0];
                    break;
                }
            }
            return id;
        }
    }

    //Create a class for SalesEntry functions that is used exclusively by SM
    private class SMsalesfunctions extends SalesEntry {

        //Method to add new sales entry into file
        private void EnterSales(List<List<String>> salesdata) throws IOException {
            String id = null;
            for (int i = 0; i < salesdata.size(); i++) {
                id = generateID();
                String newline = String.format("%s,%s,%s,%s", id, salesdata.get(i).get(0), salesdata.get(i).get(1), java.time.LocalDate.now());
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
            Iterator<String> iterator = allsales.iterator();
            SMitemfunctions sif = new SMitemfunctions();
            int length = allsales.size();

            //Create all the sales needed to revert into one list
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

            //Remove the sales from sale record
            while (iterator.hasNext()) {
                String[] sale = iterator.next().split(",");
                for (String eachid : id) {
                    if (sale[0].toLowerCase().equals(eachid.toLowerCase())) {
                        iterator.remove();
                        break;
                    }
                }
            }
            try {
                cof.writeUpdatedLinesToFile(dfp.getSalesEntryFile(), allsales);
            } catch (Exception e) {
                System.out.println("Error Updating Sales Entry!");
            }
            if (allsales.size() == length) {
                System.out.println("No ID matched the Sales ID!");
            } else {
                System.out.println("Sales Removed Successfully!");
            }
        }
        //method to edit sales entry from file
        private List<List<String>> PreviewUpdateSales(String id, String item, int qty){
            SMitemfunctions sif = new SMitemfunctions();
            String itemid = sif.FindItemIDFromName(item);
            List<List<String>> updatedsales = new ArrayList<>();
            List<String> updateitem = new ArrayList<>();
            List<String> updatesale = new ArrayList<>();
            String[] onesale = readSalesbyid(id);
            int qtydiff = Integer.parseInt(onesale[2]) - qty;
            onesale[1] = itemid;
            onesale[2] = String.valueOf(qty);
            System.out.println("Preview of the updated sales:");
            String itemname = fetchItemNameFromId(itemid);
            if (Objects.isNull(itemname)) {
                System.out.println("Invalid item name!");
            }
            System.out.println(String.format("SalesID: %s, Item Name: %s, Quantity: %s, Sale Date: %s", onesale[0], itemname, onesale[2], onesale[3]));
            updateitem.add(onesale[1]);
            updateitem.add(String.valueOf(qtydiff));
            updatedsales.add(updateitem);
            updatesale.addAll(Arrays.asList(onesale));
            updatedsales.add(updatesale);
            return updatedsales;
        }
        //method to apply the edited sales entry into the file
        private void UpdateSales(List<List<String>> updatedsale){
            SMitemfunctions sif = new SMitemfunctions();
            List<List<String>> updateditem = new ArrayList<>();
            updateditem.add(updatedsale.get(0));
            String updatedsales = String.format("%s,%s,%s,%s", updatedsale.get(1).get(0), updatedsale.get(1).get(1), updatedsale.get(1).get(2), updatedsale.get(1).get(3));
            sif.ApplySales(updateditem);
            List<String> allsales = getAllSales();
            for (int i = 0; i < allsales.size(); i++){
                if (allsales.get(i).split(",")[0].toLowerCase().equals(updatedsale.get(1).get(0).toLowerCase())) {
                    allsales.set(i, updatedsales);
                    break;
                }
            }
            try {
                cof.writeUpdatedLinesToFile(dfp.getSalesEntryFile(), allsales);
            } catch (Exception e) {
                System.out.println("Error Updating Sales Entry File!");
            }
        }
    }

    //Method to display menu in CLI
    public void DisplayMenu(String role) throws IOException {
        Scanner sc = new Scanner(System.in);
        String menu = """
                  1. View List of Items
                  2. Sales Entry
                  3. Sales Report
                  4. View Stock Level
                  5. Create Purchase Requisition
                  6. List of Purchase Orders
                  """;

        // Append options based on role
        if ("Administrators".equalsIgnoreCase(role)) {
            menu += "7. Go Back\n"; // Admin role gets "Go Back"
        } else {
            menu += "7. Logout\n";   // Non-admin role gets "Logout"
            menu += "8. Exit\n";     // Non-admin role gets "Exit"
        }

        int choice;

        // Loop the menu until the user chooses to exit or log out
        while (true) {
            System.out.println("------------------------------");
            System.out.println(menu);
            System.out.print("Please Select a Menu (1-8): ");

            // Take input from user
            try {
                choice = sc.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu. Please input a number from 1-8");
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
                    Sales_Entry_Menu:
                    while (true) {
                        System.out.println(salesEntryMenu);
                        System.out.println("Please Select a Menu (1-5): ");
                        try {
                            choice = sc.nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid menu. Please input a number from 1-5");
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                ViewSales();
                                break;
                            case 2:
                                AddSales();
                                break;
                            case 3:
                                EditSales();
                                break;
                            case 4:
                                DeleteSales();
                                break;
                            case 5:
                                break Sales_Entry_Menu; // Go back to main menu
                            default:
                                System.out.println("Invalid menu. Please select a number from 1-5");
                        }
                        System.out.println("------------------------------");
                    }
                    break;
                case 7:
                    if ("Administrators".equalsIgnoreCase(role)) {
                        System.out.println("Going Back...");
                        // Implement logic for going back, like returning to the previous menu
                        // For example, call the main menu again or navigate back:
                        // DisplayMenu(role); // if you want to return to the main menu
                    } else {
                        System.out.println("Logging out...");
                        LoginPage loginPage = new LoginPage();
                        loginPage.login();
                    }
                    break;
                case 8:
                    if (!"Administrators".equalsIgnoreCase(role)) {
                        System.out.println(" Exiting the system...");
                        System.exit(0); // Exit for non-admins
                    } else {
                        System.out.println("Invalid menu. Please select a number from 1-7");
                    }
                    break;
                default:
                    System.out.println("Invalid menu. Please select a number from 1-8");
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

        try {
            System.out.println("Enter the item name: ");
            item = sc.next();
        } catch (Exception e) {
            System.out.println("Error reading item name!");
            return;
        }
        try {
            System.out.println("Enter quantity sold: ");
            salequantity = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error reading quantity!");
            return;
        }
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
        salesentry.add(onesale);

        String addSalesMenu = """
                              Please select an option
                              1. Add Item
                              2. Apply Sale(s)
                              3. Return to Sales Menu
                              """;
        int choice;
        Add_Sales_Entry:
        while (true) {
            System.out.println(addSalesMenu);
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    try {
                        System.out.println("Enter the item name: ");
                        item = sc.next();
                    } catch (Exception e) {
                        System.out.println("Error reading item name!");
                        return;
                    }
                    try {
                        System.out.println("Enter quantity sold: ");
                        salequantity = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Error reading quantity!");
                        return;
                    }
                    List<String> anothersale = new ArrayList<>();
                    anothersale.add(sif.FindItemIDFromName(item));
                    anothersale.add(String.valueOf(salequantity));
                    salesentry.add(anothersale);
                    System.out.println(salesentry);
                    break;
                case 2:
                    ssf.EnterSales(salesentry);
                    sif.ApplySales(salesentry);
                    System.out.println("Sales Updated Sucessfully!");
                case 3:
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
        int choice = 0;
        
        String itemname = null;
        int qty = 0;
        System.out.println("Enter the Sales ID you want to edit: ");
        String id = sc.next();
        
        if (Objects.isNull(ssf.displayOneSaleById(id))) {
            return;
        }
        else{
            System.out.println(ssf.displayOneSaleById(id));
        }

        try {
            System.out.println("Enter the new item name: ");
            itemname = sc.next();
        } catch (Exception e) {
            System.out.println("Error reading item name!");
        }
        try {
            System.out.println("Enter the new sale quantity: ");
            qty = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error reading quantity!");
        }
        List<List<String>> onesale = ssf.PreviewUpdateSales(id, itemname, qty);

        System.out.println("""
                           1. Confirm Update
                           2. Return to Sales Menu
                           """);
        try {
            choice = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Please input a number between 1-2");
        }
        switch (choice) {
            case 1:
                ssf.UpdateSales(onesale);
                break;
            case 2:
                break;
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
        Delete_Sales_Menu:
        while (true){
            int choice;
            System.out.println("Enter the Sales ID to be removed: ");
            checkid = sc.next();
            if (Objects.isNull(ssf.displayOneSaleById(checkid))) {
                System.out.println("Invalid Sales ID!");
            }
            else{
                id.add(checkid);
            }
            System.out.println("""
                               1. Add another sales
                               2. Delete from record
                               3. Return to Sales Menu
                               
                               Please Select a Menu 1-3: 
                               """);
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    continue;
                case 2:
                    if (id.size() < 1) {
                        System.out.println("No Sale to be removed!");
                    }
                    else{
                        ssf.RemoveSales(id);
                    }
                    break Delete_Sales_Menu;
                case 3:
                    break Delete_Sales_Menu;
                default:
                    System.out.println("Invalid Input! Please select a number from 1-3");
                    ;
            }
        }
    }
}
