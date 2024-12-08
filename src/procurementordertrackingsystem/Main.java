package procurementordertrackingsystem;


import procurementordertrackingsystem.entities.LoginPage;


public class Main {
    public static void main(String[] args) {
        
        // Create an instance of LoginPage
        LoginPage loginPage = new LoginPage();
        
        // Call the login method to perform login
        loginPage.login();

//        // Create an instance of FinanceManager
//         FinanceManager financeManager = new FinanceManager();
//
//        // Call the method to verify purchase orders for payment
//        financeManager.viewSupplierPaymentStatus();
    }
}