package main.bookit.Servlet;

import main.bookit.DAO.CourseDAO;
import main.bookit.DAO.ListDAO;
import main.bookit.Model.BookingList;
import main.bookit.Model.Course;
import main.bookit.Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/createList")
public class CreateListServlet extends HttpServlet {

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");

            if (user != null && user.isAdmin()) {
                try {
                    String description = request.getParameter("description");
                    String location = request.getParameter("location");
                    Timestamp startTime = Timestamp.valueOf(request.getParameter("start").replace("T", " ") + ":00");
                    int interval = Integer.parseInt(request.getParameter("interval"));
                    int maxSlots = Integer.parseInt(request.getParameter("maxSlots"));
                    int courseId = Integer.parseInt(request.getParameter("courseID")); // Get course ID from the form

                    // Instantiate a new BookingList using the admin's user ID from the session
                    BookingList newList = new BookingList(
                            0, // ID will be set by the database
                            courseId,
                            user.getId(), // Use the admin's user ID from the session
                            description,
                            location,
                            startTime,
                            interval,
                            maxSlots
                    );

                    ListDAO listDao = new ListDAO();
                    boolean isSuccess = listDao.createList(newList);

                    if (isSuccess) {
                        // Set session attributes for successful creation
                        session.setAttribute("bookingStatus", "success");
                        session.setAttribute("bookingMessage", "List has been successfully created.");
                        response.sendRedirect("bookingConfirmation.jsp");
                    } else {
                        // Set session attributes for failure
                        session.setAttribute("bookingStatus", "error");
                        session.setAttribute("bookingMessage", "Unable to create the list.");
                        response.sendRedirect("bookingConfirmation.jsp");
                    }

                } catch (Exception e) {
                    // Set session attributes for error due to invalid input
                    session.setAttribute("bookingStatus", "error");
                    session.setAttribute("bookingMessage", "Invalid input format.");
                    response.sendRedirect("bookingConfirmation.jsp");
                }

            } else {
                response.sendRedirect("login.jsp");
            }
        }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Add logic to fetch the courses available to the user
        /*HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user != null) {
            CourseDAO coursedao = new CourseDAO();
            // This method should return the list of courses available to the user; you may need to implement it
            List<Course> availableCourses = coursedao.getCoursesForUser(user.getId());

            // Set the courses as a request attribute
            request.setAttribute("availableCourses", availableCourses);

            // Forward the request to the JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("createList.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }*/

        response.sendRedirect("createList.jsp");
    }

}

