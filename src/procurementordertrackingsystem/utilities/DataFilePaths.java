package procurementordertrackingsystem.utilities;
import java.io.File;

public class DataFilePaths {
    private final String basePath;
        
    public DataFilePaths(String basePath) {
        this.basePath = basePath;
    }
    
    public File getItemFile() {
        return new File(basePath, "item.txt");
    }

    public File getPaymentFile() {
        return new File(basePath, "payment.txt");
    }

    public File getPurchaseOrderFile() {
        return new File(basePath, "purchase_order.txt");
    }

    public File getPurchaseRequisitionFile() {
        return new File(basePath, "purchase_requisition.txt");
    }

    public File getSalesEntryFile() {
        return new File(basePath, "sales_entry.txt");
    }

    public File getSupplierFile() {
        return new File(basePath, "supplier.txt");
    }

    public File getUserFile() {
        return new File(basePath, "user.txt");
    }
}
