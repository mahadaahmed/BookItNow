package main.bookit.DAO;

import main.bookit.Model.Course;
import main.bookit.DAO.utils.DatabaseUtil;
import main.bookit.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    // In CourseDAO or a new DAO, perhaps UserCourseDAO
    public Map<User, List<Course>> getUserAccessMap() {
        String sql = "SELECT u.id as user_id, u.username, c.id as course_id, c.title "
                + "FROM booking.users u "
                + "JOIN booking.courseaccess ca ON u.id = ca.user_id "
                + "JOIN booking.courses c ON ca.course_id = c.id";
        return DatabaseUtil.executeQuery(sql, rs -> {
            Map<User, List<Course>> accessMap = new HashMap<>();
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                User user = new User(userId, username); // Assuming User class has this constructor.

                int courseId = rs.getInt("course_id");
                String title = rs.getString("title");
                Course course = new Course(courseId, title); // Using the constructor you provided.

                if (!accessMap.containsKey(user)) {
                    accessMap.put(user, new ArrayList<>());
                }
                accessMap.get(user).add(course);
            }
            return accessMap;
        });
    }

}
