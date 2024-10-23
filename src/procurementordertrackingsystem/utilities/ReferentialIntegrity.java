package procurementordertrackingsystem.utilities;

import java.util.ArrayList;
import java.util.List;

public class ReferentialIntegrity {
    // Method to filter 2 arrays to only return an array of matching elements
    public static String[] match2Arrays(String[] Array1, String[] Array2) {
        List<String> matchedElements = new ArrayList<>(); 

        // Iterate through the elements of 1st array and check if they exist in the 2nd array
        for (String arrayElement1 : Array1) {
            for (String arrayElement2 : Array2) {
                if (arrayElement1.equals(arrayElement2)) {
                    // If there's a match, add the matching element to the matchedElements list
                    matchedElements.add(arrayElement1);
                }
            }
        }

        // Convert the List to an array and return it
        return matchedElements.toArray(new String[0]);
    }
}
