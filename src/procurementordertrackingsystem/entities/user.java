package procurementordertrackingsystem.entities;

import java.util.List;

public class User {
    private String userID, name, role, username, email, password;
    private final List<String> usersdata;

    // Default constructor
    public User() {
        this.userID = "";
        this.name = "";
        this.role = "";
        this.username = "";
        this.email = "";
        this.password = "";
        this.usersdata = null; // Initialize to null or empty list if needed
    }

    // Constructor to accept a list of users
    public User(List<String> users) {
        this.usersdata = users;
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

    // Method to display users
    public void displayUsers() {
        if (usersdata == null || usersdata.isEmpty()) {
            System.out.println("No users to display.");
            return;
        }

        System.out.println("ID    | Name                | Role               | Username         | Email");
        System.out.println("--------------------------------------------------------------------------");
        for (String user : usersdata) {
            String[] userDetails = user.split(",");
            if (userDetails.length >= 6) {
                System.out.printf("%-6s| %-20s| %-18s| %-18s| %s%n",
                    userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4]);
            }
        }
    }
}
