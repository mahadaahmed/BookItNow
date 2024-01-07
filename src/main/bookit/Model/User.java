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

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        // Since this constructor does not receive admin information, set a default or retrieve it if needed.
        // This field could be set to a default value or omitted if not used.
        this.admin = 0; // or some default value or fetched from the database if necessary
        this.isAdmin = (this.admin != 0); // This will set isAdmin to true if admin is not 0.
    }


    public boolean isAdmin() {
        return this.admin != 0;
    }


}
