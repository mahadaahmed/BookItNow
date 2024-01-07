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

    /*public List<Reservation> getBookingsForUser(int userId) {
        // Updated SQL query to join with the users table and select the username
        String sql = "SELECT r.*, u.username FROM booking.reservations r " +
                "JOIN booking.users u ON r.user_id = u.id " +
                "WHERE r.user_id = ?";
        return executeQueryAndMapToReservations(sql, userId);
    }*/

    /*public List<Reservation> getBookingsForUser(int userId) {
        String sql = "SELECT r.*, u.username, l.description AS listName " + // Alias the description as listName
                "FROM booking.reservations r " +
                "JOIN booking.users u ON r.user_id = u.id " +
                "JOIN booking.lists l ON r.list_id = l.id " + // Ensure you're joining with the lists table
                "WHERE r.user_id = ?;"; // Assuming you want to filter by user ID
        return executeQueryAndMapToReservations(sql, userId); // Pass any required parameters
    }*/


    public List<Reservation> getBookingsForUser(int userId) {
        String sql = "SELECT DISTINCT r.*, u.username, l.description AS listName, coalesce(uco2.username, 'Alone') AS coworker " +
                "FROM booking.reservations r " +
                "JOIN booking.users u ON r.user_id = u.id " +
                "JOIN booking.lists l ON r.list_id = l.id " +
                "LEFT JOIN booking.usercooperations uco ON r.coop_id = uco.id " +
                "LEFT JOIN booking.users uco2 ON (uco.user1_id = uco2.id AND uco.user1_id != r.user_id) OR (uco.user2_id = uco2.id AND uco.user2_id != r.user_id) " +
                "WHERE r.user_id = ? OR uco.user1_id = ? OR uco.user2_id = ?;";

        return DatabaseUtil.executeQuery(sql, rs -> {
            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                Reservation reservation = mapToReservation(rs);
                reservation.setCoWorker(rs.getString("coworker")); // Assuming you have a setCoWorker method in Reservation class
                reservations.add(reservation);
            }
            return reservations;
        }, userId, userId, userId);
    }


    private Reservation mapToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation(
                rs.getInt("id"),
                rs.getInt("list_id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getInt("coop_id"),
                rs.getInt("sequence"),
                rs.getString("listName"),
                null  // Set default null or another appropriate default value for coworker
        );

        try {
            // Check if the coworker column exists and is not null
            String coworker = rs.getString("coworker");
            if (coworker != null) {
                reservation.setCoWorker(coworker);
            }
        } catch (SQLException e) {
            // If the column doesn't exist, handle the exception (e.g., log it or ignore it)
            // You can log the exception or handle it as appropriate
        }

        return reservation;
    }


    public int createBooking(int listId, int userId, int sequence) {
        String sql = "INSERT INTO booking.reservations (list_id, user_id, sequence) VALUES (?, ?, ?) RETURNING id;";
        return DatabaseUtil.executeInsertWithIdReturn(sql, listId, userId, sequence);
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
        // The SQL query now joins the reservations table with the users and lists tables
        // Assuming 'description' is the column in 'lists' table that you want to show as 'listName'
        String sql = "SELECT r.*, u.username, l.description AS listName " +
                "FROM booking.reservations r " +
                "JOIN booking.users u ON r.user_id = u.id " +
                "JOIN booking.lists l ON r.list_id = l.id;"; // Join with lists table to get the list name
        return executeQueryAndMapToReservations(sql);
    }


    /*public int createCooperation(String coopName) {
        String sql = "INSERT INTO booking.cooperations (name) VALUES (?) RETURNING id;";
        return DatabaseUtil.executeInsertWithIdReturn(sql, coopName);
    }*/


    public int createCooperation(int user1Id, int user2Id) {
        String sql = "INSERT INTO booking.usercooperations (user1_id, user2_id) VALUES (?, ?) RETURNING id;";
        return DatabaseUtil.executeInsertWithIdReturn(sql, user1Id, user2Id);
    }


    public List<String> getCooperationsForUserAndBooking(int userId, int reservationId) {
        String sql = "SELECT users.username FROM booking.users users " +
                "JOIN booking.usercooperations uco ON users.id = uco.user2_id " +
                "JOIN booking.reservations res ON uco.id = res.coop_id " +
                "WHERE res.id = ? AND uco.user1_id = ? " +
                "UNION " +
                "SELECT users.username FROM booking.users users " +
                "JOIN booking.usercooperations uco ON users.id = uco.user1_id " +
                "JOIN booking.reservations res ON uco.id = res.coop_id " +
                "WHERE res.id = ? AND uco.user2_id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<String> partnerUsernames = new ArrayList<>();
            while (rs.next()) {
                partnerUsernames.add(rs.getString(1));
            }
            return partnerUsernames;
        }, reservationId, userId, reservationId, userId);
    }




    public boolean linkCooperationToReservation(int reservationId, int coopId) {
        String sql = "UPDATE booking.reservations SET coop_id = ? WHERE id = ?;";
        int affectedRows = DatabaseUtil.executeUpdate(sql, coopId, reservationId);
        return affectedRows > 0;
    }

}
