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
    //Override the generateID method from IDGenerator to generate ID based on number of records
    public String generateID() {
        List<String> rawdata = getAllSales();
        return String.format("%04d", rawdata.size());
    }
    
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
    
    public String getSalesid() {
        return salesid;
    }

    public void setSalesid(String salesid) {
        this.salesid = salesid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getSalesdate() {
        return salesdate;
    }

    public void setSalesdate(Date salesdate) {
        this.salesdate = salesdate;
    }
    
    private List<String> getAllSales(){
        List<String> rawdata = new ArrayList<>();
        try {
            rawdata = cof.readFromAFile(dfp.getSalesEntryFile());
        } catch (Exception e) {
            System.out.println("Error reading Sales Entry file!");
        }
        return rawdata;
    }
    
    private String fetchItemNameFromId(String id){
        List<String> rawdata = new ArrayList<>();
        String itemname = null;
        try {
            rawdata = cof.readFromAFile(dfp.getItemFile());
        } catch (Exception e) {
            System.out.println("Error fetching item name!");
        }
        String[] line = new String[2];
        for (String lines : rawdata){
            line = lines.split(",");
            if (ri.checkAttributeInArray(itemid, line)){
                itemname = line[1];
                break;
            }
            else{
                itemname = null;
            }
        }
        return itemname;
    }
}
