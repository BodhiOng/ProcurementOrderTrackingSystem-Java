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
    
    /**
     * Overloaded method to write multiple lines to a file.
     *
     * @param filename the file to write to
     * @param newLines the list of lines to append to the file
     */
    public void createToFile(File filename, List<String> newLines) throws IOException {
        // Check if newLines is not null to prevent NullPointerException
        if (newLines == null || newLines.isEmpty()) {
            return; // Nothing to write
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) { // Open in append mode
            for (String line : newLines) {
                bw.write(line);
                bw.newLine();
            }
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