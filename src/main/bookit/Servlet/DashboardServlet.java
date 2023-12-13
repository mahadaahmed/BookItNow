package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import main.bookit.DAO.CourseDAO;
import main.bookit.Model.Course;
import main.bookit.Model.Reservation;
import main.bookit.Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();
    private CourseDAO courseDAO = new CourseDAO(); // Add the CourseDAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the existing session without creating a new one
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Get the user object from the session
            User user = (User) session.getAttribute("user");

            // Fetch user bookings based on the user ID
            List<Reservation> userBookings = bookingDAO.getBookingsForUser(user.getId());

            // Fetch all courses
            List<Course> courses = courseDAO.getAllCourses();

            // Set the bookings and courses as request attributes
            request.setAttribute("userBookings", userBookings);
            request.setAttribute("courses", courses);

            // Forward to the dashboard view
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } else {
            // If session is invalid, redirect to the login page
            response.sendRedirect("login.jsp");
        }
    }
}
