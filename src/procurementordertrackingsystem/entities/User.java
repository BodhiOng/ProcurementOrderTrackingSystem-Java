package procurementordertrackingsystem.entities;

import java.util.ArrayList;
import java.util.List;
import procurementordertrackingsystem.utilities.CRUDOntoFile;
import procurementordertrackingsystem.utilities.DataFilePaths;


public class User {
    private String userID, name, role, username, email, password;
    CRUDOntoFile cof = new CRUDOntoFile();
    DataFilePaths dfp = new DataFilePaths("src/procurementordertrackingsystem/data");

    // Default constructor
    public User() {
        this.userID = "";
        this.name = "";
        this.role = "";
        this.username = "";
        this.email = "";
        this.password = "";
    }

    // Getters and setters
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String getUserIDfromUsername(String un){
        List<String> userdata = new ArrayList<>();
        String id = null;
        try {
            userdata = cof.readFromAFile(dfp.getUserFile());
        } catch (Exception e) {
            System.out.println("Error reading user file!");
        }
        for (String eachuser : userdata){
            if (eachuser.split(",")[3].toLowerCase().equals(un.toLowerCase())) {
                id = eachuser.split(",")[0];
                break;
            }
        }
        return id;
    }
}
