/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurementordertrackingsystem.entities;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;
/**
 *
 * @author user
 */
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DisplayData extends Item {  
    
    // Method to display items with headers and formatting  
    public void displayItems(File file) {  
        try {  
            List<String> lines = crudOntoFile.readFromAFile(file); // Read file contents  
            
            // Print the headers  
            System.out.printf("%-10s %-15s %-12s %-10s %-10s%n", "Item ID", "Item Name", "Stock Level", "Price", "Supplier ID");  
            System.out.println("------------------------------------------------------------");  
            
            // Go through each line for item details to be printed  
            for (String line : lines) {  
                // Separate line by commas  
                String[] parts = line.split(",");  
                // Ensure there are that many parts in the line & print output to the terminal  
                if (parts.length == 5) {  
                    String output = String.format("%-10s %-15s %-12d $%-9.2f %-10s",  
                            parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), parts[4]);  
                    System.out.println(output);  
                }  
            }  
        } catch (IOException e) {  
            System.out.println("Error reading items from file: " + e.getMessage());  
        }  
    }  
}  