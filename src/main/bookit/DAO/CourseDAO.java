package main.bookit.DAO;

import main.bookit.Model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookingdb";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, title FROM booking.courses";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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

    // Additional methods to handle other CRUD operations could be added here
}
