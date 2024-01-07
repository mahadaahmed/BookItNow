package main.bookit.Servlet;

import main.bookit.DAO.BookingDAO;
import main.bookit.DAO.UserDAO;
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
            String coopName = request.getParameter("coopName");

            BookingDAO bookingDAO = new BookingDAO();
            UserDAO userDAO = new UserDAO();

            // Check if the timeslot is available before attempting to book
            if (bookingDAO.isTimeslotAvailable(listId, sequence)) {
                // Create booking and get the generated booking ID
                int bookingId = bookingDAO.createBooking(listId, user.getId(), sequence);

                if (bookingId > 0) {
                    // Booking was successful
                    int coopId = -1;
                    if (coopName != null && !coopName.trim().isEmpty()) {
                        // Get the user ID of the cooperation partner
                        User coopUser = userDAO.getUserByUsername(coopName.trim());
                        if (coopUser != null) {
                            // Create cooperation record and get the generated cooperation ID
                            coopId = bookingDAO.createCooperation(user.getId(), coopUser.getId());
                        } else {
                            // Handle the scenario where the cooperation user does not exist
                            request.setAttribute("errorMessage", "Cooperation user not found.");
                            request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
                            return;
                        }
                    }

                    // If cooperation was added, link it to the reservation
                    if (coopId > 0 && !bookingDAO.linkCooperationToReservation(bookingId, coopId)) {
                        // Handle the case where linking failed
                        request.setAttribute("errorMessage", "Failed to link cooperation to the booking.");
                        request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
                        return;
                    }

                    // Set success message and redirect to the confirmation page
                    session.setAttribute("bookingStatus", "success");
                    session.setAttribute("bookingMessage", "Your booking was successful!");
                    response.sendRedirect("bookingConfirmation.jsp");
                } else {
                    // Handle the scenario where booking creation failed
                    request.setAttribute("errorMessage", "Failed to create the booking.");
                    request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
                }
            } else {
                // Handle the scenario where timeslot is not available
                request.setAttribute("errorMessage", "Time slot is already booked.");
                request.getRequestDispatcher("/timeslot?courseId=" + listId).forward(request, response);
            }
        } else {
            // Redirect to login if session or user is null
            response.sendRedirect("login.jsp");
        }
    }
}
