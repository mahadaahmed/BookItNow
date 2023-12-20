package main.bookit.DAO;

import main.bookit.Model.Course;
import main.bookit.DAO.utils.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        String sql = "SELECT id, title FROM booking.courses";
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<List<Course>>() {
            @Override
            public List<Course> handle(ResultSet rs) throws SQLException {
                List<Course> courses = new ArrayList<>();
                while (rs.next()) {
                    courses.add(new Course(rs.getInt("id"), rs.getString("title")));
                }
                return courses;
            }
        });
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT id, title FROM booking.courses WHERE id = ?";
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<Course>() {
            @Override
            public Course handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return new Course(rs.getInt("id"), rs.getString("title"));
                }
                return null;
            }
        }, courseId);
    }

    public List<Course> getCoursesForUser(int userId) {
        String sql = "SELECT c.* FROM booking.courses c JOIN booking.course_access ca ON c.id = ca.course_id WHERE ca.user_id = ?";
        return DatabaseUtil.executeQuery(sql, new DatabaseUtil.ResultSetHandler<List<Course>>() {
            @Override
            public List<Course> handle(ResultSet rs) throws SQLException {
                List<Course> accessibleCourses = new ArrayList<>();
                while (rs.next()) {
                    accessibleCourses.add(new Course(rs.getInt("id"), rs.getString("title")));
                }
                return accessibleCourses;
            }
        }, userId);
    }
}
