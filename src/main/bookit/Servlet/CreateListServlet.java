package main.bookit.Servlet;

import main.bookit.DAO.ListDAO;
import main.bookit.Model.BookingList;
import main.bookit.Model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

// In CreateListServlet.java
@WebServlet("/createList")
public class CreateListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user is an admin
        // You can check session for an isAdmin flag or similar
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        int isAdmin = (int) session.getAttribute("isAdmin");
        if (user != null && isAdmin > 0) {
            // Get parameters from the request
            int ID = Integer.parseInt(request.getParameter("ID"));
            int courseId = Integer.parseInt(request.getParameter("courseID"));
            int userId = Integer.parseInt(request.getParameter("userID"));
            String description = request.getParameter("description");
            String location = request.getParameter("location");
            //Timestamp startTime = Timestamp.valueOf(request.getParameter("start"));
            String startTimeString = request.getParameter("start");
            // Append ":00" to match the SQL Timestamp format "yyyy-MM-dd hh:mm:ss"
            startTimeString = startTimeString.replace("T", " ") + ":00";
            Timestamp startTime = Timestamp.valueOf(startTimeString);

            int interval = Integer.parseInt(request.getParameter("interval"));
            int maxSlots = Integer.parseInt(request.getParameter("maxSlots"));
            String adminUsername = request.getParameter("adminUsername");

            // Create a BookingList object
            BookingList newList = new BookingList(ID,courseId,userId,description,location,startTime,interval,maxSlots);

            // Use ListDAO to create the list
            ListDAO listDao = new ListDAO();
            boolean isSuccess = listDao.createList(newList);

            if (isSuccess) {
                // Redirect or forward to success page or admin dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                // Handle failure: set error message and forward back to form
                request.setAttribute("errorMessage", "Something went wrong when creating the list");
            }
        } else {
            // If user is not admin, redirect to login or unauthorized page
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
