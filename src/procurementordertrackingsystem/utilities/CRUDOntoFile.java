package procurementordertrackingsystem.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CRUDOntoFile {
    // Create data to a file (CREATE)
    public void createToFile(String filename, String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) { // True means it opens file in append mode
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
        
    // Read data from a file (READ)
    public List<String> readFromAFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return lines;
    }
    
    // Write updated data to a file (UPDATE)
    public void writeUpdatedLinesToFile(String filename, List<String> updatedLines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) { // This opens the file in overwrite mode
            for (String line : updatedLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file: " + e.getMessage());
        }
    }
}
