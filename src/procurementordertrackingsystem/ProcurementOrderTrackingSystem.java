package procurementordertrackingsystem;

import java.util.Arrays;

public class ProcurementOrderTrackingSystem {

    public static void main(String[] args) {
        // Create an instance of FinanceManager
        FinanceManager financeManager = new FinanceManager();
        FinanceManager.FMFunctionalities FMFunctionality = financeManager.new FMFunctionalities();

        // Call the method to verify purchase orders for payment
        FMFunctionality.checkStockStatus();
    }
}