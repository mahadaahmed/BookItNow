package main.bookit.Servlet;

import main.bookit.DAO.CourseDAO;
import main.bookit.DAO.UserDAO;
import main.bookit.Model.Course;
import main.bookit.Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/revokeCourseAccess")
public class RevokeCourseAccessServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));

            UserDAO userDao = new UserDAO();
            CourseDAO courseDao = new CourseDAO();
            boolean success = courseDao.revokeCourseAccessForUser(userId, courseId);


            if (success) {
                User adminUser = (User) request.getSession().getAttribute("user");
                String username = userDao.getUsernameById(userId);
                Course course = courseDao.getCourseById(courseId);
                String courseTitle = course.getTitle();
                String adminUsername = adminUser.getUsername();

                request.setAttribute("username", username);
                request.setAttribute("courseTitle", courseTitle);
                request.setAttribute("adminUsername", adminUsername);

                request.getSession().setAttribute("status", "Access revoked successfully.");
                request.getRequestDispatcher("revokeGranted.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("status", "Failed to revoke access.");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard.jsp?error=true");
            request.getSession().setAttribute("status", "Invalid user or course ID.");
        }

    }

}
