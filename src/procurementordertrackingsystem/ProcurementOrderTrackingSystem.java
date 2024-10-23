package procurementordertrackingsystem;

public class ProcurementOrderTrackingSystem {
    
    public static void main(String[] args) {
        PurchaseRequisition pr = new PurchaseRequisition("test", "test", 0, "test", "test", "test");
        pr.readPurchaseRequisitionFromFile("src/procurementordertrackingsystem/data/purchase_requisition.txt");
    }
}
