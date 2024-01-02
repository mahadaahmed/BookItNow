package main.bookit.DAO;

import main.bookit.Model.Reservation;
import main.bookit.DAO.utils.DatabaseUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private List<Reservation> executeQueryAndMapToReservations(String sql, Object... params) {
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(mapToReservation(rs));
            }
            return reservations;
        }, params);
    }

    public List<Reservation> getBookingsForUser(int userId) {
        // Updated SQL query to join with the users table and select the username
        String sql = "SELECT r.*, u.username FROM booking.reservations r " +
                "JOIN booking.users u ON r.user_id = u.id " +
                "WHERE r.user_id = ?";
        return executeQueryAndMapToReservations(sql, userId);
    }

    private Reservation mapToReservation(ResultSet rs) throws SQLException {
        // Assuming Reservation model does not include username
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("list_id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getInt("coop_id"),
                rs.getInt("sequence")
        );
    }

    public boolean createBooking(int listId, int userId, int sequence) {
        String sql = "INSERT INTO booking.reservations (list_id, user_id, sequence) VALUES (?, ?, ?);";
        int affectedRows = DatabaseUtil.executeUpdate(sql, listId, userId, sequence);
        return affectedRows > 0;
    }

    public boolean isTimeslotAvailable(int listId, int sequence) {
        String sql = "SELECT COUNT(*) AS slotCount FROM booking.reservations WHERE list_id = ? AND sequence = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("slotCount") == 0;
            }
            return false;
        }, listId, sequence);
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM booking.reservations WHERE id = ?";
        int affectedRows = DatabaseUtil.executeUpdate(sql, bookingId);
        return affectedRows > 0;
    }

    public List<Reservation> getAllReservations() {
        // The SQL query now joins the reservations table with the users table
        String sql = "SELECT r.*, u.username FROM booking.reservations r JOIN booking.users u ON r.user_id = u.id;";
        return executeQueryAndMapToReservations(sql);
    }

}
