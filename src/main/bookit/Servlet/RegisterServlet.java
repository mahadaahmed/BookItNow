package main.bookit.Servlet;

import main.bookit.DAO.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean isAdmin = true;

        UserDAO userDao = new UserDAO();
        boolean success = userDao.createUser(username, password, isAdmin);

        if (success) {
            // Redirect to login page or dashboard if direct login is preferred
            request.setAttribute("message", "User '" + username + "' created successfully.");
            request.setAttribute("success", true);
            response.sendRedirect("register.jsp");
        } else {
            // Handle registration failure
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}

