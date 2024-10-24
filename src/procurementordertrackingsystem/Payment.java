package procurementordertrackingsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import procurementordertrackingsystem.utilities.CRUDOntoFile;

public class Payment {
    private String paymentID; // Payment ID
    private String poID; // Purchase Order ID
    private double amount; // Amount of money paid
    private String paymentStatus; // Payment status: Paid
    private LocalDate paymentDate; // The date of payment

    // Default constructor
    public Payment() {
        this.paymentID = "";
        this.poID = "";
        this.amount = 0.0;
        this.paymentStatus = "";
        this.paymentDate = LocalDate.now();
    }
    
    // Constructor
    public Payment(String paymentID, String poID, double amount, String paymentStatus, LocalDate paymentDate) {
        this.paymentID = paymentID;
        this.poID = poID;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }
    
    // Instance of CRUDOntoFile for purchase order related operations
    CRUDOntoFile crudOntoFile = new CRUDOntoFile();
    
    // Method to create a new payment
    public void createPaymentToFile(String filename, String poID, double amount) {
        // Generate random payment ID for usage
        String paymentID = generateRandomPaymentID();
        
        // Create a string representation of the payment
        String paymentData = String.format("%s,%s,%.2f,Paid,%s",
                paymentID, poID, amount, LocalDate.now().toString());
        
        // Print the added payment into the terminal
        String[] parts = paymentData.split(",");
        String output = String.format(
                    "Payment ID: %s, Purchase Order ID: %s, Amount: RM %s, Payment Status: %s, Payment date: %s",
                    parts[0], parts[1], parts[2], parts[3], parts[4]
                );
        System.out.println(output);

        // Write the payment data to the file
        crudOntoFile.createToFile(filename, paymentData);
    }
    
    // Method to generate random paymentID
    public static String generateRandomPaymentID() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); // Generate a random number between 1000 and 9999
        return "P" + randomNumber; // Prefix 'P' to the random number
    }
    
    // Method to read all payments
    public void readPaymentFromFile(String filename) {
        List<String> lines = crudOntoFile.readFromAFile(filename); // Read file contents
        
        // Go through each line for PO details to be printed
        for (String line : lines) {
            // Separate line by commas
            String[] parts = line.split(",");
            // Ensure there are that muc parts in the line & print output to terminal
            if (parts.length == 5) {
                String output = String.format(
                    "Payment ID: %s, Purchase Order ID: %s, Amount: RM %s, Payment Status: %s, Payment date: %s",
                    parts[0], parts[1], parts[2], parts[3], parts[4]
                );
                System.out.println(output);
            }
        }
    }

    // Getters & setters
    public String getPaymentID() { return paymentID; }
    public void setPaymentID(String paymentID) { this.paymentID = paymentID; }

    public String getPoID() { return poID; }
    public void setPoID(String poID) { this.poID = poID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
