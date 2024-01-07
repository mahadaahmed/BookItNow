package main.bookit.Servlet;

import main.bookit.DAO.CourseDAO;
import main.bookit.Model.Course;
import main.bookit.Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/createcourse")
public class CreateCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                String title = request.getParameter("title");

                //Course course = new Course();
                //course.setTitle(title);

                CourseDAO courseDAO = new CourseDAO();
                boolean success = courseDAO.addCourse(title);

            if (success) {
                request.setAttribute("message", "Course '" + title + "' created successfully.");
                request.setAttribute("success", true);
            } else {
                request.setAttribute("message", "Error: Unable to create user. This user already exists.");
                request.setAttribute("success", false);
            }

        request.getRequestDispatcher("courseCreated.jsp").forward(request, response);
    }
}

