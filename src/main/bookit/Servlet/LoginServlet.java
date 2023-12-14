package main.bookit.Servlet;

import main.bookit.Model.User;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/"})
public class LoginServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookingdb";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password"); // This should be hashed

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("PostgreSQL JDBC driver not found", e);
        }

        // Update the SQL query to use hashed passwords
        // For example purposes, this is using plain text (not recommended)
        String sql = "SELECT id, username, admin FROM booking.users WHERE username = ? AND password = ?";



        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use a hashed password

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", rs.getInt("id")); // Store user ID
                    session.setAttribute("username", rs.getString("username")); // Store username
                    session.setAttribute("isAdmin", rs.getInt("admin")); // Store admin flag
                    // Create a User object to store in the session
                    User user = new User(rs.getInt("id"), rs.getString("username"), null, rs.getInt("admin"));
                    session.setAttribute("user", user); // Store the User object

                    System.out.println("User ID: " + session.getAttribute("userId")); // Check if attributes are set
                    System.out.println("Username: " + session.getAttribute("username"));
                    System.out.println("Is Admin: " + session.getAttribute("isAdmin"));
                    // Redirect to the 'DashboardServlet' instead of 'dashboard.jsp'
                    response.sendRedirect("dashboard"); // URL pattern for DashboardServlet
                } else {
                    request.setAttribute("errorMessage", "Invalid username or password");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
