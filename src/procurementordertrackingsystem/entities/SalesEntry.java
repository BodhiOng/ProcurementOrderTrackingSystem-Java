/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.entities;

import com.sun.source.tree.BreakTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.IDGenerator;
import procurementordertrackingsystem.utilities.ReferentialIntegrity;

/**
 *
 * @author LENOVO
 */
public class SalesEntry implements IDGenerator{
    private String salesid, itemid;
    private int quantity;
    private Date salesdate;
    private ReferentialIntegrity ri = new ReferentialIntegrity();
    
    public CRUDOntoFile cof = new CRUDOntoFile();
    public DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");
    
    @Override
    //Override the generateID method from IDGenerator to generate a sequential ID
    public String generateID() {
        List<String> rawdata = getAllSales();
        return String.format("%04d", rawdata.size());
    }
    
    //Method to read one sales record given its ID
    public String[] readSalesbyid(String id){
        List<String> rawdata = getAllSales();
        String[] line = null;
        for (String lines : rawdata){
            line = lines.split(",");
            if (line[0].equals(id)){
                break;
            }
        }
        return line;
    }
    
    //Method to read all sales entry
    public void readAllSales(){
        List<String> rawdata = getAllSales();
        String[] line = null;
        String itemname;
        for (String lines : rawdata){
            line = lines.split(",");
            itemname = fetchItemNameFromId(line[1]);
            System.out.println(String.format("SalesID: %s, Item Name: %s, Quantity: %s, Sale Date: %s", line[0], itemname, line[2], line[3]));
        }
    }
    
    //Getter and Setter methods
    public String getSalesid() {return salesid;}

    public void setSalesid(String salesid) {this.salesid = salesid;}

    public String getItemid() {return itemid;}

    public void setItemid(String itemid) {this.itemid = itemid;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public Date getSalesdate() {return salesdate;}

    public void setSalesdate(Date salesdate) {this.salesdate = salesdate;}
    
    //Private method to fetch all sales entry
    private List<String> getAllSales(){
        List<String> rawdata = new ArrayList<>();
        try {
            rawdata = cof.readFromAFile(dfp.getSalesEntryFile());
        } catch (Exception e) {
            System.out.println("Error reading Sales Entry file!");
        }
        return rawdata;
    }
    
    //Private method to get the item name
    private String fetchItemNameFromId(String id){
        List<String> rawdata = new ArrayList<>();
        String itemname = null;
        //read the item file
        try {
            rawdata = cof.readFromAFile(dfp.getItemFile());
        } catch (Exception e) {
            System.out.println("Error fetching item name!");
        }
        String[] line = new String[2];
        //iterate through the item record
        for (String lines : rawdata){
            line = lines.split(",");
            //find the one that has matching ID
            if (ri.checkAttributeInArray(itemid, line)){
                itemname = line[1];
                break;
            }
            else{
                itemname = null;
            }
        }
        //return the item name that has a matching ID
        return itemname;
    }
}
