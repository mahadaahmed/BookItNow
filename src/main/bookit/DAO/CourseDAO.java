package main.bookit.DAO;

import main.bookit.Model.Course;
import main.bookit.DAO.utils.DatabaseUtil;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        String sql = "SELECT id, title FROM booking.courses";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("title")));
            }
            return courses;
        });
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT id, title FROM booking.courses WHERE id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            if (rs.next()) {
                return new Course(rs.getInt("id"), rs.getString("title"));
            }
            return null;
        }, courseId);
    }

    public List<Course> getCoursesForUser(int userId) {
        String sql = "SELECT c.* FROM booking.courses c JOIN booking.courseaccess ca ON c.id = ca.course_id WHERE ca.user_id = ?";
        return DatabaseUtil.executeQuery(sql, rs -> {
            List<Course> accessibleCourses = new ArrayList<>();
            while (rs.next()) {
                accessibleCourses.add(new Course(rs.getInt("id"), rs.getString("title")));
            }
            return accessibleCourses;
        }, userId);
    }

    public boolean addCourse(String title) {
        String sql = "INSERT INTO booking.courses (title) VALUES (?)";
       // String title = course.getTitle();
        int affectedRows = DatabaseUtil.executeUpdate(sql, title);

        return affectedRows > 0;
    }
}
