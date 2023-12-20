package main.bookit.DAO;

import main.bookit.Model.User;
import main.bookit.config.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Database connection details
    private final Config config = Config.getInstance();
    private final String dbURL = config.getDbURL();
    private final String dbUSER = config.getDbUSER();
    private final String dbPASS = config.getDbPASS();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Handle the exception, possibly rethrow or log it
            throw new ExceptionInInitializerError(e);
        }
    }

    public User authenticateUser(String username, String hashedPassword) throws SQLException {
        String sql = "SELECT id, username, admin FROM booking.users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(config.getDbURL(), config.getDbUSER(), config.getDbPASS());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword); // Assuming password is hashed

            DatabaseMetaData metaData = conn.getMetaData();
            int jdbcVersion = metaData.getJDBCMajorVersion();

            System.out.println("JDBC Major Version: " + jdbcVersion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            null,
                            rs.getInt("admin")
                    );
                }
            }
        } catch (Exception e){
            throw e;
        }
        return null; // User not found or incorrect password
    }
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM booking.users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
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
