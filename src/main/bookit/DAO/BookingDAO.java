package main.bookit.DAO;

import main.bookit.Model.Reservation;
import main.bookit.config.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {


    // Database connection details
    private final Config config = Config.getInstance();
    private final String dbURL = config.getDbURL();
    private final String dbUSER = config.getDbUSER();
    private final String dbPASS = config.getDbPASS();

    public List<Reservation> getBookingsForUser(int userId) {
        List<Reservation> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking.reservations WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

    public boolean isTimeslotAvailable(int listId, int sequence) {
        String sql = "SELECT COUNT(*) AS slotCount FROM booking.reservations WHERE list_id = ? AND sequence = ?";
        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, listId);
            pstmt.setInt(2, sequence);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("slotCount") == 0; // If count is 0, slot is available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM booking.reservations WHERE id = ?;";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookingId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM booking.reservations";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("list_id"),
                            rs.getInt("user_id"),
                            rs.getInt("coop_id"),
                            rs.getInt("sequence")
                    );
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you can handle or log the exception as needed
        }

        return reservations;
    }




    // Additional methods to handle other CRUD operations could be added here
}
