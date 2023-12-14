package main.bookit.Model;

public class User {
    private int id;
    private String username;
    private String password;
    private int admin; // This could be boolean, but it's an int here to match the schema
    private int isAdmin;

    // Constructor
    public User(int id, String username, String password, int admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    // Getters and Setters
    // ... implement getters and setters for all fields ...


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        if(isAdmin > 0){
            return true;
        }
        return false;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
