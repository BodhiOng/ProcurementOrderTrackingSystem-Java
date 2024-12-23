/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.IDGenerator;

/**
 *
 * @author LENOVO
 */
public class SalesEntry implements IDGenerator{
    private String salesid, itemid;
    private int quantity;
    private Date salesdate;
    
    public CRUDOntoFile cof = new CRUDOntoFile();
    public DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");
    
    @Override
    //Override the generateID method from IDGenerator to generate a sequential ID
    public String generateID() {
        List<String> rawdata = getAllSales();
        List<Integer> idlist = new ArrayList<>();
        //Find the highest ID in the record
        for (String eachdata : rawdata){
            try {
                idlist.add(Integer.valueOf(eachdata.split(",")[0].substring(2)));
            } catch (Exception e) {
                System.out.println("Error reading id!");
            }
        }
        Collections.sort(idlist);
        int lastid = idlist.getLast();
        //Create an ID after the highest ID
        return String.format("SE%04d", lastid+1);
    }
    
    //Method to read one sales record given its ID
    public String[] readSalesbyid(String id){
        //Get all the sales record
        List<String> rawdata = getAllSales();
        String[] line = null;
        String[] toreturn = null;
        //Find the record that match the ID
        for (String lines : rawdata){
            line = lines.split(",");
            if (line[0].toLowerCase().equals(id.toLowerCase())){
                toreturn = line;
                break;
            }
        }
        //Return all the record with matching ID
        return toreturn;
    }
    
    //Method to read one sale record given its ID
    public String displayOneSaleById(String id){
        //Get the sale with the specified ID
        String[] onesale = readSalesbyid(id);
        //Check if the record exists
        if (Objects.isNull(onesale)){
            System.out.println("No sale record is found with that ID!");
            return null;
        }
        else{
            //Get and return the item name from the record
            String itemname = fetchItemNameFromId(onesale[1]);
            return String.format("SalesID: %s, Item Name: %s, Quantity: %s, Sale Date: %s", onesale[0], itemname, onesale[2], onesale[3]);
        }
    }
    
    //Method to read all sales entry
    public void readAllSales(){
        //Get all the sales record
        List<String> rawdata = getAllSales();
        String[] line = null;
        String itemname;
        //Display all the value of the record
        for (String lines : rawdata){
            line = lines.split(",");
            //Find the item name of the item ID
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
    protected List<String> getAllSales(){
        List<String> rawdata = new ArrayList<>();
        try {
            rawdata = cof.readFromAFile(dfp.getSalesEntryFile());
        } catch (Exception e) {
            System.out.println("Error reading Sales Entry file!");
        }
        return rawdata;
    }
    
    //Private method to get the item name
    protected String fetchItemNameFromId(String id){
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
            if (line[0].equals(id)){
                itemname = line[1];
                break;
            }
        }
        //return the item name that has a matching ID
        return itemname;
    }
}
