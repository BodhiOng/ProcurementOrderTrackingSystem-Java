package procurementordertrackingsystem.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CRUDOntoFile {
    // Create data to a file (CREATE)
    public void createToFile(File filename, String newLine) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) { // True means it opens file in append mode
            bw.write(newLine);
            bw.newLine();
        }
    }

    // Read data from a file (READ)
    public List<String> readFromAFile(File filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    
        // Method to write a new line to a specified file
    public void writeToAFile(File file, String line) throws IOException {
        try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
            bw.newLine();
        }
    }
    
    // new method added by inventory manager that has different parametters 
    public void writeToFile(File filename, List<String> lines) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
        for (String line : lines) {
            bw.write(line);
            bw.newLine();
        }
    }
}
    
    
    // Write updated data to a file (UPDATE)
    public void writeUpdatedLinesToFile(File filename, List<String> updatedLines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) { // This opens the file in overwrite mode
            for (String line : updatedLines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}