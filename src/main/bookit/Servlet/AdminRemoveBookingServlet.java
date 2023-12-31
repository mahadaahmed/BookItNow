package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AdminRemoveBooking")
public class AdminRemoveBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        BookingDAO bookingDao = new BookingDAO();
        boolean success = bookingDao.cancelBooking(bookingId);

        if (success) {
            response.sendRedirect("bookings.jsp?bookingRemoved=true");
        } else {
            response.sendRedirect("adminHome.jsp?error=true");
        }
    }
}
