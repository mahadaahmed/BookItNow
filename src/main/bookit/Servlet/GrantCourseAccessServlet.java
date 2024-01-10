package main.bookit.Servlet;

import main.bookit.DAO.ListDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GrantCourseAccess")
public class GrantCourseAccessServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = Integer.parseInt(request.getParameter("userId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        ListDAO listDao = new ListDAO();
        boolean accessExists = listDao.hasCourseAccess(courseId, userId);

        if (!accessExists) {
            boolean success = listDao.addCourseAccess(userId, courseId);
            if (success) {
                String jsonResponse = "{\"status\":\"success\", \"message\":\"Access granted.\"}";
                response.getWriter().write(jsonResponse);
            } else {
                String jsonResponse = "{\"status\":\"error\", \"message\":\"Unable to grant access.\"}";
                response.getWriter().write(jsonResponse);
            }
        } else {
            String jsonResponse = "{\"status\":\"error\", \"message\":\"User already has access to this course.\"}";
            response.getWriter().write(jsonResponse);
        }
    }
}
