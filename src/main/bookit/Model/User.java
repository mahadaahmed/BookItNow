package main.bookit.Model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private int admin; // This could be boolean, but it's an int here to match the schema
    private boolean isAdmin;

    // Constructor
    public User(int id, String username, String password, int admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
