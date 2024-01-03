package main.bookit.DAO;

import main.bookit.Model.BookingList;
import main.bookit.DAO.utils.DatabaseUtil;
import main.bookit.Model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ListDAO {

    public List<BookingList> getAllAvailableLists() {
        //String sql = "SELECT * FROM booking.lists;";
        String sql = "SELECT l.*, c.title AS course_name, u.username AS admin_username " +
                "FROM booking.lists l " +
                "JOIN booking.courses c ON l.course_id = c.id " +
                "JOIN booking.users u ON l.user_id = u.id;";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<BookingList> lists = new ArrayList<>();
            while (rs.next()) {
                lists.add(mapToBookingList(rs));
            }
            return lists;
        });
    }

    private BookingList mapToBookingList(ResultSet rs) throws SQLException {
        // Add additional parameters for course name and admin username if you added them to your BookingList model
        return new BookingList(
                rs.getInt("id"),
                rs.getInt("course_id"), // assuming you have added this to the BookingList model
                rs.getInt("user_id"), // assuming you have added this to the BookingList model
                rs.getString("description"),
                rs.getString("location"),
                rs.getTimestamp("start"),
                rs.getInt("interval"),
                rs.getInt("max_slots")
        );
    }


    public List<BookingList> getListsForCourse(int courseId) {
        String sql = "SELECT l.*, u.username AS admin_username FROM booking.lists l JOIN booking.users u ON l.user_id = u.id WHERE l.course_id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<BookingList> listsForCourse = new ArrayList<>();
            while (rs.next()) {
                BookingList bookingList = new BookingList(
                        rs.getInt("id"),
                        rs.getInt("course_id"),
                        rs.getInt("admin_username"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getTimestamp("start"),
                        rs.getInt("interval"),
                        rs.getInt("max_slots")
                );
                listsForCourse.add(bookingList);
            }
            return listsForCourse;
        }, courseId);
    }

    public BookingList getBookingListById(int listId) {
        String sql = "SELECT * FROM booking.lists WHERE id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            if (rs.next()) {
                return mapToBookingList(rs);
            }
            return null;
        }, listId);
    }

    public List<Integer> getBookedSlotSequences(int listId) {
        String sql = "SELECT sequence FROM booking.reservations WHERE list_id = ? ORDER BY sequence ASC;";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<Integer> bookedSlots = new ArrayList<>();
            while (rs.next()) {
                bookedSlots.add(rs.getInt("sequence"));
            }
            return bookedSlots;
        }, listId);
    }


    public List<Integer> getAvailableSlotSequences(int listId, int maxSlots) {
        List<Integer> bookedSlots = getBookedSlotSequences(listId);
        List<Integer> availableSlots = new ArrayList<>();
        for (int i = 0; i < maxSlots; i++) {
            if (!bookedSlots.contains(i)) {
                availableSlots.add(i);
            }
        }
        return availableSlots;
    }

    public boolean createList(BookingList newList) {
        String sql = "INSERT INTO booking.lists (course_id, user_id, description, location, start, interval, max_slots) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        int courseId = newList.getCourseId();
        int userId = newList.getUserId();
        String description = newList.getDescription();
        String location = newList.getLocation();
        Timestamp start = newList.getStart();
        int interval = newList.getInterval();
        int maxSlots = newList.getMaxSlots();

        int affectedRows = DatabaseUtil.executeUpdate(sql, courseId, userId, description, location, start, interval, maxSlots);
        return affectedRows > 0;
    }

    public boolean deleteList(int listId) {
        String deleteReservationsSql = "DELETE FROM booking.reservations WHERE list_id = ?";
        String deleteListSql = "DELETE FROM booking.lists WHERE id = ?";
        DatabaseUtil.executeUpdate(deleteReservationsSql, listId); // Delete reservations
        int affectedRows = DatabaseUtil.executeUpdate(deleteListSql, listId); // Delete the list
        return affectedRows > 0;
    }

    public List<BookingList> getListsByAdmin(int adminUserId) {
        String sql = "SELECT * FROM booking.lists WHERE user_id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<BookingList> listsByAdmin = new ArrayList<>();
            while (rs.next()) {
                listsByAdmin.add(mapToBookingList(rs));
            }
            return listsByAdmin;
        }, adminUserId);
    }


    public boolean addCourseAccess(int userId, int courseId) {
        String sql = "INSERT INTO booking.courseaccess (user_id, course_id) VALUES (?, ?);";
        int affectedRows = DatabaseUtil.executeUpdate(sql, userId, courseId);
        return affectedRows > 0;
    }

}
