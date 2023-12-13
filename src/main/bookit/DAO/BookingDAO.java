package main.bookit.DAO;

import main.bookit.Model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookingdb";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public List<Reservation> getBookingsForUser(int userId) {
        List<Reservation> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking.reservations WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation booking = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("list_id"),
                            rs.getInt("user_id"),
                            rs.getInt("coop_id"),
                            rs.getInt("sequence")
                    );
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
        }

        return bookings;
    }

    public boolean createBooking(int listId, int userId, int sequence) {
        // SQL query to insert a new reservation
        String sql = "INSERT INTO booking.reservations (list_id, user_id, sequence) VALUES (?, ?, ?);";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, sequence);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Additional methods to handle other CRUD operations could be added here
}
