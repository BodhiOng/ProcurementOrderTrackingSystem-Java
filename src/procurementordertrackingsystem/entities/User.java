package procurementordertrackingsystem.entities;

import java.util.List;

public class User {
    private String userID, name, role, username, email, password;
    

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
    
    
}