package main.bookit.Servlet;

import main.bookit.DAO.CourseDAO;
import main.bookit.DAO.ListDAO;
import main.bookit.DAO.UserDAO;
import main.bookit.Model.Course;
import main.bookit.Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GrantCourseAccess")
public class GrantCourseAccessServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        UserDAO userDao = new UserDAO();
        ListDAO listDao = new ListDAO();
        CourseDAO courseDao = new CourseDAO();
        boolean accessExists = listDao.hasCourseAccess(courseId, userId);

        if (!accessExists) {
            boolean success = listDao.addCourseAccess(userId, courseId);

            if (success) {
                User adminUser = (User) request.getSession().getAttribute("user");
                String username = userDao.getUsernameById(userId);
                Course course = courseDao.getCourseById(courseId);
                String courseTitle = course.getTitle();
                String adminUsername = adminUser.getUsername();

                request.setAttribute("username", username);
                request.setAttribute("courseTitle", courseTitle);
                request.setAttribute("adminUsername", adminUsername);
                request.getRequestDispatcher("accessGranted.jsp").forward(request, response);
            } else {
                // Handle the scenario where access couldn't be granted due to some error
                request.setAttribute("error", "Unable to grant access.");
                request.getRequestDispatcher("grantAccess.jsp").forward(request, response);
            }
        } else {
            // Handle the scenario where user already has access
            request.setAttribute("error", "User already has access to this course.");
            request.getRequestDispatcher("accessGranted.jsp").forward(request, response);
        }
    }
}
