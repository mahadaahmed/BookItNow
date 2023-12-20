package main.bookit.DAO;

import main.bookit.Model.Course;
import main.bookit.config.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Database connection details
    private final Config config = Config.getInstance();
    private final String dbURL = config.getDbURL();
    private final String dbUSER = config.getDbUSER();
    private final String dbPASS = config.getDbPASS();

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, title FROM booking.courses";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course(rs.getInt("id"), rs.getString("title"));
                courses.add(course);
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return courses;
    }

    public String getCourseTitleById(int courseId) {
        String title = null;
        String sql = "SELECT title FROM booking.courses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    title = rs.getString("title");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return title;
    }


    public Course getCourseById(int courseId) {
        Course course = null;
        String sql = "SELECT id, title FROM booking.courses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course = new Course(rs.getInt("id"), rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }


    public List<Course> getCoursesForUser(int userId) {
        List<Course> accessibleCourses = new ArrayList<>();
        String sql = "SELECT c.* FROM booking.courses c JOIN booking.course_access ca ON c.id = ca.course_id WHERE ca.user_id = ?;";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course(rs.getInt("id"), rs.getString("title"));
                accessibleCourses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }

        return accessibleCourses;
    }

    // Additional methods to handle other CRUD operations could be added here
}
