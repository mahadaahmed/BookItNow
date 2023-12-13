package main.bookit.DAO;

import main.bookit.Model.User;

import java.sql.*;

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
}

    // Other User-related database operations
