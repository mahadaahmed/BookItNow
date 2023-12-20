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

    public BookingList(int id, int courseId, String adminUsername, String description, String location, Timestamp start, int interval, int maxSlots) {
        this.id = id;
        this.courseId = courseId;
        this.adminUsername = adminUsername;
        this.description = description;
        this.location = location;
        this.start = start;
        this.interval = interval;
        this.maxSlots = maxSlots;
    }


}