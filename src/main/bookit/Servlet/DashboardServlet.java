package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import main.bookit.DAO.CourseDAO;
import main.bookit.DAO.ListDAO;
import main.bookit.Model.User;
import main.bookit.Model.BookingList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final ListDAO listDAO = new ListDAO(); // Instantiate ListDAO to handle booking lists

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");

            // Set the user bookings and courses as request attributes
            request.setAttribute("userBookings", Collections.unmodifiableList(bookingDAO.getBookingsForUser(user.getId())));
            request.setAttribute("courses", courseDAO.getAllCourses());

            // Check if the user is an admin to set admin-specific attributes
            if (user.isAdmin()) {
                // Get all booking lists and set them as request attributes
                List<BookingList> bookingLists = listDAO.getAllAvailableLists();
                request.setAttribute("bookingLists", bookingLists);
                request.setAttribute("isAdmin", true); // This will be used to display admin controls in JSP
            } else {
                request.setAttribute("isAdmin", false);
            }

            // Forward to the dashboard view
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } else {
            // Redirect to the login page if the session is invalid
            response.sendRedirect("login.jsp");
        }
    }
}
