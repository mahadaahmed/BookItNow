package main.bookit.DAO;

import main.bookit.Model.BookingList;

import java.io.IOException;
import java.sql.*;
import java.util.*;


public class ListDAO {

    // Database connection details
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookingdb";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public List<BookingList> getAllAvailableLists() {
        List<BookingList> lists = new ArrayList<>();
        String sql = "SELECT * FROM booking.lists;"; // Adjust this query as necessary

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BookingList bookingList = new BookingList(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getInt("user_id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getTimestamp("start"),
                        rs.getInt("interval"),
                        rs.getInt("max_slots")
                );
                lists.add(bookingList);
            }
        } catch (SQLException e) {
            // Handle exceptions, possibly rethrow as a custom exception
            e.printStackTrace();
        }

        return lists;
    }

    // In ListDAO.java

    // Method to get available slots for all lists
    public ArrayList<Integer> getAvailableSlots() {
        ArrayList<Integer> availableSlots = new ArrayList<>();
        String sql = "SELECT l.id, l.max_slots, COUNT(r.id) as booked_slots " +
                "FROM booking.lists l " +
                "LEFT JOIN booking.reservations r ON l.id = r.list_id " +
                "GROUP BY l.id, l.max_slots;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int maxSlots = rs.getInt("max_slots");
                int bookedSlots = rs.getInt("booked_slots");
                availableSlots.add(maxSlots - bookedSlots); // Calculate available slots
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableSlots;
    }


    public List<BookingList> getListsForCourse(int courseId) {
        List<BookingList> listsForCourse = new ArrayList<>();
        String sql = "SELECT l.*, u.username AS admin_username FROM booking.lists l JOIN booking.users u ON l.user_id = u.id WHERE l.course_id = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BookingList bookingList = new BookingList(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getString("admin_username"), // Fetch the admin's username
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getTimestamp("start"),
                        rs.getInt("interval"),
                        rs.getInt("max_slots")
                );
                listsForCourse.add(bookingList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listsForCourse;
    }

    public BookingList getBookingListById(int listId) {
        BookingList bookingList = null;
        String sql = "SELECT * FROM booking.lists WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    bookingList = new BookingList(
                            rs.getInt("id"),
                            rs.getInt("course_id"),
                            new UserDAO().getAdminUsernameById(rs.getInt("user_id")),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getTimestamp("start"),
                            rs.getInt("interval"),
                            rs.getInt("max_slots")
                    );
                    // Optionally join with the users table to set adminUsername
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }



    // Method to get a list of booked slot sequences for a particular list
    public List<Integer> getBookedSlotSequences(int listId) {
        List<Integer> bookedSlots = new ArrayList<>();
        String sql = "SELECT sequence FROM booking.reservations WHERE list_id = ? ORDER BY sequence ASC;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookedSlots.add(rs.getInt("sequence"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSlots;
    }

    // Method to get available slot sequences for a particular list
    public List<Integer> getAvailableSlotSequences(int listId, int maxSlots) {
        List<Integer> availableSlots = new ArrayList<>();
        List<Integer> bookedSlots = getBookedSlotSequences(listId);

        for (int i = 0; i < maxSlots; i++) {
            if (!bookedSlots.contains(i)) {
                availableSlots.add(i);
            }
        }
        return availableSlots;
    }


//------------------------------------------------------For ADMIN ------------------------------------------------------//


    // In ListDAO.java
    public boolean createList(BookingList newList) {
        // Implementation to insert a new list into the database
        // Return true if insertion is successful, false otherwise
        Boolean inserted = false;

        int courseId = newList.getCourseId();
        int userId = newList.getUserId();
        String description = newList.getDescription();
        String location = newList.getLocation();
        Timestamp start = newList.getStart();
        int interval = newList.getInterval();
        int maxSlots = newList.getMaxSlots();


        String sql = "INSERT INTO booking.lists (course_id, user_id, description, location, start, interval, max_slots) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";


        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId); // Set courseId from newList object
            pstmt.setInt(2, userId); // Set the username directly or get it from newList if it varies
            pstmt.setString(3, description); // Set description from newList object
            pstmt.setString(4, location); // Set location from newList object
            pstmt.setTimestamp(5, start); // Set start from newList object, ensure this is a Timestamp object
            pstmt.setInt(6, interval); // Set interval from newList object
            pstmt.setInt(7, maxSlots); // Set maxSlots from newList object

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // The insert was successful.
                System.out.println("The insert was successful");
                inserted = true;
            } else {
                // The insert failed.
                System.out.println("The insert failed!!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return inserted;
    }

    public boolean deleteList(int listId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false); // Start transaction

            // First, delete any reservations associated with the list
            String deleteReservationsSql = "DELETE FROM booking.reservations WHERE list_id = ?";
            pstmt = conn.prepareStatement(deleteReservationsSql);
            pstmt.setInt(1, listId);
            pstmt.executeUpdate();

            // Then, delete the list itself
            String deleteListSql = "DELETE FROM booking.lists WHERE id = ?";
            pstmt = conn.prepareStatement(deleteListSql);
            pstmt.setInt(1, listId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }


}


    // Other List-related database operations
