package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AdminAddBooking")
public class AdminAddBookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int listId = Integer.parseInt(request.getParameter("listId"));
        int userId = Integer.parseInt(request.getParameter("userId")); // User ID is now provided by the form
        int sequence = Integer.parseInt(request.getParameter("sequence"));

        BookingDAO bookingDao = new BookingDAO();
        boolean isAvailable = bookingDao.isTimeslotAvailable(listId, sequence);

        if (isAvailable) {
            boolean success = bookingDao.createBooking(listId, userId, sequence);
            if (success) {
                // Redirect to a confirmation page or display a success message
                response.sendRedirect("dashboard.jsp?bookingAdded=true");
            } else {
                // Redirect to an error page or display an error message
                response.sendRedirect("dashboard.jsp?error=true");
            }
        } else {
            // Redirect to an error page or display an error message about timeslot availability
            response.sendRedirect("dashboard.jsp?timeslotUnavailable=true");
        }
    }
}
