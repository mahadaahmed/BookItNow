package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import main.bookit.Model.User;

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
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            int listId = Integer.parseInt(request.getParameter("listId"));
            int sequence = Integer.parseInt(request.getParameter("sequence"));
            int userId = user.getId(); // Assuming 'User' class has getId() method

            BookingDAO bookingDAO = new BookingDAO();

            // Check if the timeslot is available before attempting to book
            if (bookingDAO.isTimeslotAvailable(listId, sequence)) {
                boolean bookingSuccess = bookingDAO.createBooking(listId, userId, sequence);

                if (bookingSuccess) {
                    session.setAttribute("bookingStatus", "success");
                    session.setAttribute("bookingMessage", "Your booking was successful!");
                    response.sendRedirect("bookingConfirmation.jsp");
                } else {
                    request.setAttribute("errorMessage", "Failed to book the time slot. An error occurred.");
                    request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Time slot is already booked.");
                request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
