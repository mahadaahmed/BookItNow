package main.bookit.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class BookingList {
    private int id;
    private int courseId;
    private int userId;
    private String description;
    private String location;
    private Timestamp start;
    private int interval;
    private int maxSlots;
    private String adminUsername; // New field for admin's username

    public BookingList(int id, int courseId, int userId, String description, String location, Timestamp start, int interval, int maxSlots) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
        this.description = description;
        this.location = location;
        this.start = start;
        this.interval = interval;
        this.maxSlots = maxSlots;
    }

    // Constructor that matches the fields, including adminUsername
    public BookingList(int id, int courseId, String adminUsername, String description, String location, Timestamp start, int interval, int maxSlots) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId; // You might need to handle this as well, as it was removed from the parameter list
        this.description = description;
        this.location = location;
        this.start = start;
        this.interval = interval;
        this.maxSlots = maxSlots;
        this.adminUsername = adminUsername; // Set the adminUsername field
    }


}