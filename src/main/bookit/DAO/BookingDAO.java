package main.bookit.DAO;

import main.bookit.Model.Reservation;
import main.bookit.DAO.utils.DatabaseUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public List<Reservation> getBookingsForUser(int userId) {
        String sql = "SELECT * FROM booking.reservations WHERE user_id = ?";
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<List<Reservation>>() {
            @Override
            public List<Reservation> handle(ResultSet rs) throws SQLException {
                List<Reservation> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(mapToReservation(rs));
                }
                return bookings;
            }
        }, userId);
    }

    private Reservation mapToReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("list_id"),
                rs.getInt("user_id"),
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
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<Boolean>() {
            @Override
            public Boolean handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return rs.getInt("slotCount") == 0;
                }
                return false;
            }
        }, listId, sequence);
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM booking.reservations WHERE id = ?";
        int affectedRows = DatabaseUtil.executeUpdate(sql, bookingId);
        return affectedRows > 0;
    }

    public List<Reservation> getAllReservations() {
        String sql = "SELECT * FROM booking.reservations";
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<List<Reservation>>() {
            @Override
            public List<Reservation> handle(ResultSet rs) throws SQLException {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapToReservation(rs));
                }
                return reservations;
            }
        });
    }

}
