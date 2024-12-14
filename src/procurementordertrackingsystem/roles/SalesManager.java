/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

        private void ApplySales(List<List<String>> salesdetail) {

            //Create a list for only the updated items
            List<Item> itemlist = new ArrayList<>();
            //Create a list of item ID which will be updated
            List<String> idlist = new ArrayList<>();
//            for (int i = 0; i < salesdetail.size(); i++){
//                idlist.add(salesdetail.get(i).get(0));
//            }
//            
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

        private void RemoveSales(List<String> id) {
            List<String> allsales = getAllSales();
            Iterator<String> iterator = allsales.iterator();
            SMitemfunctions sif = new SMitemfunctions();
            int length = allsales.size();

            //Create all the sales needed to revert into one list
            List<List<String>> saletorevert = new ArrayList<List<String>>();
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
                                AddSales(role);
                                break;
                            case 3:
                                // Add logic to edit sales if needed
                                break;
                            case 4:
                                DeleteSales();
                                break;
                            case 5:
                                DisplayMenu(role); // Go back to main menu
                                break Sales_Entry_Menu;
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

    //SM 2nd Functionality 2nd sub-function (Add sales record)
    private void AddSales(String role) throws IOException {
        String item = null;
        int salequantity = 0;
        List<List<String>> salesentry = new ArrayList<List<String>>();
        Scanner sc = new Scanner(System.in);
        SMitemfunctions sif = new SMitemfunctions();
        SMsalesfunctions ssf = new SMsalesfunctions();

        try {
            System.out.println("Enter the item name: ");
            item = sc.next();
        } catch (Exception e) {
            System.out.println("Error reading item name!");
            DisplayMenu(role);
        }
        try {
            System.out.println("Enter quantity sold: ");
            salequantity = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error reading quantity!");
            DisplayMenu(role);
        }
        List<String> onesale = new ArrayList<>();
        onesale.add(sif.FindItemIDFromName(item));
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
                        DisplayMenu(role);
                    }
                    try {
                        System.out.println("Enter quantity sold: ");
                        salequantity = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Error reading quantity!");
                        DisplayMenu(role);
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

    private void DeleteSales() {
        SMsalesfunctions ssf = new SMsalesfunctions();
        Scanner sc = new Scanner(System.in);
        List<String> id = new ArrayList<>();
        System.out.println("Enter the Sales ID to be removed: ");
        id.add(sc.next());
        Delete_Sales_Menu:
        while (true) {
            int choice;
            System.out.println("""
                               1. Add another sales
                               2. Delete from record
                               3. Return to Sales Menu
                               
                               Please Select a Menu 1-3: 
                               """);
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the Sales ID to be removed: ");
                    id.add(sc.next());
                    continue;
                case 2:
                    ssf.RemoveSales(id);
                case 3:
                    break Delete_Sales_Menu;
                default:
                    System.out.println("Invalid Input! Please select a number from 1-3");
                    ;
            }
        }
    }
}
