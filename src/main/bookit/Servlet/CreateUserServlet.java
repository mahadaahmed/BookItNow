package main.bookit.Servlet;

import main.bookit.DAO.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean isAdmin = request.getParameter("admin") != null;

        UserDAO userDao = new UserDAO();
        boolean success = userDao.createUser(username, password, isAdmin);

        if (success) {
            request.setAttribute("message", "User '" + username + "' created successfully.");
            request.setAttribute("success", true);
        } else {
            request.setAttribute("message", "Error: Unable to create user. This user already exists.");
            request.setAttribute("success", false);
        }

        request.getRequestDispatcher("userCreated.jsp").forward(request, response);
    }
}

