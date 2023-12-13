package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/bookTimeSlot")
public class BookTimeSlotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            int listId = Integer.parseInt(request.getParameter("listId"));
            int sequence = Integer.parseInt(request.getParameter("sequence"));
            int userId = (Integer) session.getAttribute("userId");

            BookingDAO bookingDAO = new BookingDAO();
            boolean bookingSuccess = bookingDAO.createBooking(listId, userId, sequence);
            if (bookingSuccess) {
                request.setAttribute("bookingStatus", "success");
                request.setAttribute("bookingMessage", "Your booking was successful!");
            } else {
                request.setAttribute("bookingStatus", "error");
                request.setAttribute("bookingMessage", "Failed to book the time slot. It might be already taken or an error occurred.");
            }
            request.getRequestDispatcher("bookingConfirmation.jsp").forward(request, response);
            if (bookingSuccess) {
                // Redirect to a confirmation page
                response.sendRedirect("bookingConfirmation.jsp");
            } else {
                // Handle the error, such as time slot already booked
                request.setAttribute("errorMessage", "Time slot is already booked or error occurred.");
                request.getRequestDispatcher("/timeslots.jsp").forward(request, response);
            }
        } else {
            // If the session or user ID doesn't exist, redirect to login page
            response.sendRedirect("login.jsp");
        }
    }
}
