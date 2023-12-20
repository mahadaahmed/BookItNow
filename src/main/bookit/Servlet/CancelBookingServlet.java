package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CancelBookingServlet", urlPatterns = {"/cancelBooking"})
public class CancelBookingServlet extends HttpServlet {
    private final BookingDAO bookingDAO = new BookingDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        boolean cancelSuccess = bookingDAO.cancelBooking(bookingId);

        if (cancelSuccess) {
            // Optional: Set an attribute to show a success message on the dashboard
            HttpSession session = request.getSession();
            session.setAttribute("cancelMessage", "Booking cancelled successfully.");
        } else {
            // Optional: Set an attribute to show an error message on the dashboard
            HttpSession session = request.getSession();
            session.setAttribute("cancelMessage", "Failed to cancel booking.");
        }

        response.sendRedirect("dashboard");
    }
}
