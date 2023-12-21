package main.bookit.DAO;

import main.bookit.DAO.utils.DatabaseUtil;
import main.bookit.Model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public User authenticateUser(String username, String plaintextPassword) {
        String sql = "SELECT id, username, password, admin FROM booking.users WHERE username = ?";
        try {
            return DatabaseUtil.executeQuery(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(plaintextPassword, storedHash)) {
                        int admin = rs.getInt("admin");
                        return new User(rs.getInt("id"), rs.getString("username"), null, admin);
                    }
                }
                return null;
            }, username);
        } catch (Exception e) {
            logger.error("Error authenticating user", e);
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getUsernameById(int userId) {
        String sql = "SELECT username FROM booking.users WHERE id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getString("username");
            }
            return null;
        }, userId);
    }

    public List<User> getAllUsers() {
        String sql = "SELECT id, username, password, admin FROM booking.users;";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("admin")
                ));
            }
            return users;
        });
    }

    public List<User> getUsersWithoutAccess(int courseId) {
        String sql = "SELECT * FROM booking.users WHERE id NOT IN (SELECT user_id FROM booking.course_access WHERE course_id = ?)";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("admin")
                ));
            }
            return users;
        }, courseId);
    }


    public boolean createUser(String username, String plaintextPassword, boolean isAdmin) {
        String sql = "INSERT INTO booking.users (username, password, admin) VALUES (?, ?, ?);";
        String hashedPassword = hashPassword(plaintextPassword); // Hash the password with bcrypt
        int affectedRows = DatabaseUtil.executeUpdate(sql, username, hashedPassword, isAdmin ? 1 : 0);
        return affectedRows > 0;
    }

}
