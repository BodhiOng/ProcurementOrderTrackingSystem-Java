package procurementordertrackingsystem.utilities;

import java.util.ArrayList;
import java.util.List;

public class ReferentialIntegrity {
    // Method to match Purchase Requisition IDs in purchase_order with the ones in purchase_requisition
    public static String[] matchPRs(String[] poRequisitionIds, String[] prRequisitionIds) {
        List<String> matchedPRs = new ArrayList<>(); // List to hold matched PR IDs

        // Iterate through the Purchase Order PR IDs and check if they exist in the Purchase Requisition PR IDs
        for (String poRequisitionId : poRequisitionIds) {
            for (String prRequisitionId : prRequisitionIds) {
                if (poRequisitionId.equals(prRequisitionId)) {
                    // If there's a match, add the matched PR ID to the list
                    matchedPRs.add(poRequisitionId);
                }
            }
        }

        // Convert the List to an array and return it
        return matchedPRs.toArray(new String[0]);
    }
}
