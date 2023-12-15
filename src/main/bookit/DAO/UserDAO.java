package main.bookit.DAO;

import main.bookit.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Database connection details
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookingdb";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM booking.users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"), // Should be hashed
                            rs.getInt("admin")
                    );
                }
            }
        } catch (SQLException e) {
            // Handle exceptions, possibly rethrow as a custom exception
        }

        return null;
    }

    public String getAdminUsernameById(int userId) {
        String username = null;
        String sql = "SELECT username FROM booking.users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }


    public String getUsernameById(int userId) {
        String username = null;
        String sql = "SELECT username FROM booking.users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions or rethrow as needed
        }

        return username;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, admin FROM booking.users;";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("admin")
                );
                // If you're using the isAdmin field, you might want to set it here too
                user.setIsAdmin(rs.getInt("admin")); // Assuming 1 for admin, 0 for non-admin
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
        return users;
    }


    public List<User> getUsersWithoutAccess(int courseId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM booking.users WHERE id NOT IN (SELECT user_id FROM booking.course_access WHERE course_id = ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("admin")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean createUser(String username, String password, boolean isAdmin) {
        String sql = "INSERT INTO booking.users (username, password, admin) VALUES (?, ?, ?);";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Ideally, you should hash the password
            pstmt.setInt(3, isAdmin ? 1 : 0);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}


    // Other User-related database operations
