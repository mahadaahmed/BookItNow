package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AdminRemoveBooking")
public class AdminRemoveBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        BookingDAO bookingDao = new BookingDAO();
        boolean success = bookingDao.cancelBooking(bookingId);

        if (success) {
            response.sendRedirect("dashboard.jsp?bookingRemoved=true");
        } else {
            response.sendRedirect("adminDashboard.jsp?error=true");
        }
    }
}
