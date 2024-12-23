package procurementordertrackingsystem.utilities;

import java.util.ArrayList;
import java.util.List;

public class ReferentialIntegrity {
    // Method to filter 2 arrays to only return an array of matching elements
    public String[] match2Arrays(String[] Array1, String[] Array2) {
        if (Array1 == null || Array2 == null) {
            throw new IllegalArgumentException("Input arrays cannot be null.");
        }
        
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
    
    // Method to check if the provided attribute exists in the array or not
    public boolean checkAttributeInArray(String attributeToCheck, String[] arrayOfAttributes) {
        if (arrayOfAttributes == null) {
            throw new IllegalArgumentException("Input array cannot be null.");
        }
        
        for (String singularAttributes : arrayOfAttributes) {
            if (singularAttributes.equals(attributeToCheck)) {
                return true; // Attribute found in array
            }
        }
        return false; // Attribute not found
    }
}
