package main.bookit.Model;

public class Reservation {
    private int id;
    private int listId;
    private int userId;
    private int coopId;
    private int sequence;

    // Constructor
    public Reservation(int id, int listId, int userId, int coopId, int sequence) {
        this.id = id;
        this.listId = listId;
        this.userId = userId;
        this.coopId = coopId;
        this.sequence = sequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoopId() {
        return coopId;
    }

    public void setCoopId(int coopId) {
        this.coopId = coopId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
// Getters and Setters
    // ... implement getters and setters for all fields ...
}
