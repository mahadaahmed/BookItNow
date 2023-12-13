package main.bookit.Model;

import java.sql.Timestamp;

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

    public BookingList(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }


}