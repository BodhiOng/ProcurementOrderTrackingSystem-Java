 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.roles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import procurementordertrackingsystem.entities.Item;
import procurementordertrackingsystem.entities.SalesEntry;
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
        private List<Item> readFileById(List<String> ID, File filename) throws IOException{
            //Read the records into a List
            List<String> rawdata = crudOntoFile.readFromAFile(dfp.getItemFile());
            List<Item> itemlist = new ArrayList<>();
            //Iterate through the records to find the matched ID
            for (String lines : rawdata){
                for (String eachid : ID){
                    if (lines.contains(eachid)){
                        //Instantiate a new item object for every item record matched
                        String[] linesitem = lines.split(",");
                        itemlist.add(new Item(linesitem[0], linesitem[1], Integer.parseInt(linesitem[2]), Double.parseDouble(linesitem[3]), linesitem[4]));
                    }
                }
            }
            //Return the list of item in the sales update
            return itemlist;
        }
        
        private void ApplySales(List<String> salesdetail) throws Exception{
            
            //Create a list of item ID which will be updated
            List<String> idlist = new ArrayList<>();
            for (int i = 0; i <= salesdetail.size(); i+=2){
                idlist.add(salesdetail.get(i));
            }
            
            //Read only the records that needs to be updated
            List<Item> itemlist = readFileById(idlist, dfp.getItemFile());
            
            //Update the stock level of the item from the sales report
            for (String lines : salesdetail){
                String[] salesdata = lines.split(",");
                for (Item item : itemlist){
                    if (salesdata[0].equals(item.getItemID())){
                        item.setStockLevel(item.getStockLevel() - Integer.parseInt(salesdata[1]));
                        break;
                    }
                }
            }
            
            //Append all the items object into a string list
            List<String> updatedItems = null;
            for (Item item : itemlist){
                updatedItems.add(item.toString());
            }
            
            //Write all the updated values into the text file
            if (updatedItems != null) {
                crudOntoFile.writeUpdatedLinesToFile(dfp.getItemFile(), updatedItems);
            }
        }
    }
    
    //Create a class for SalesEntry functions that is used exclusively by SM
    private class SMsalesfunctions extends SalesEntry {
        //Method to add new sales entry into file
        private void EnterSales(String item, int quantity) throws IOException {
            String id = generateID();
            String newline = String.format("%s,%s,%s,%s", id, item, quantity, java.time.LocalDate.now());
            cof.createToFile(dfp.getSalesEntryFile(), newline);
        }
    }
    
    public void DisplayMenu() throws IOException{
        Scanner sc = new Scanner(System.in);
        String menu = """
                      1. View List of Items
                      2. Sales Entry
                      3. Sales Report
                      4. View Stock Level
                      5. Create Purchase Requisition
                      6. List of Purchase Orders
                      7. Logout
                      8. Exit
                      """;
        
        while (true) {
            System.out.println(menu);
            System.out.print("Please Select a menu (1-8): ");
            int choice;
            try {
                choice = sc.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu. Please input a number from 1-8");
                continue;
            }
            switch(choice){
                case 1:
                    ViewItems();
                    break;
                case 7:
                    System.out.println("Logging out...");
                    LoginPage loginPage = new LoginPage();
                    loginPage.login();
                    break;
                case 8:
                    System.exit(0);
                default:
                    System.out.println("Invalid menu. Please input a number from 1-8");
            }
        }
    }
    
    public void ViewItems(){
        SMitemfunctions smif = new SMitemfunctions();
        smif.readItemsFromFile(dfp.getItemFile());
    }
}
