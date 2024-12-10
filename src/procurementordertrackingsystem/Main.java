package procurementordertrackingsystem;


import java.io.IOException;
import procurementordertrackingsystem.entities.LoginPage;


public class Main {
    public static void main(String[] args) throws IOException {
        
        // Create an instance of LoginPage
        LoginPage loginPage = new LoginPage();
        
        // Call the login method to perform login
        loginPage.login();
    }
}
