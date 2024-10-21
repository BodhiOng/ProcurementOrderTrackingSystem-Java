package procurementordertrackingsystem;

public class ProcurementOrderTrackingSystem {
    
    public static void main(String[] args) {
        Item item = new Item("test", "test", 0, 0.0, "test");
        item.readItemsFromFile("src/procurementordertrackingsystem/item.txt");
    }
}
