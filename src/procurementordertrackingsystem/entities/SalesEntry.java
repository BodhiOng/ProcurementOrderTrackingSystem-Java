/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.entities;

import com.sun.source.tree.BreakTree;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
import procurementordertrackingsystem.utilities.IDGenerator;

/**
 *
 * @author LENOVO
 */
public class SalesEntry implements IDGenerator {
    private String salesid, itemid;
    private int quantity;
    private Date salesdate;
    
    public CRUDOntoFile cof = new CRUDOntoFile();
    public DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");
    
    @Override
    //Override the generateID method from IDGenerator to generate ID based on number of records
    public String generateID() {
        List<String> lines = null;
        try {
            lines = cof.readFromAFile(dfp.getSalesEntryFile());
        } catch (IOException ex) {
            System.err.println("Error reading sales entry file");
        }
        return String.format("%04d", lines.size());
    }
    
    public String[] readSalesbyid(String id) throws IOException{
        List<String> rawdata = cof.readFromAFile(dfp.getSalesEntryFile());
        String[] line = null;
        String[] SalesLine = new String[4];
        for (String lines : rawdata){
            line = lines.split(",");
            if (line[0].equals(id)){
                SalesLine = line;
            }
        }
        return line;
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
}
