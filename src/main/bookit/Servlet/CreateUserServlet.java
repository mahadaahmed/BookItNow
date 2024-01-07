package main.bookit.Servlet;

import main.bookit.DAO.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean isAdmin = request.getParameter("admin") != null;

        PrintWriter out = response.getWriter();

        // Check if username or password is empty
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            out.print("{\"success\": false, \"message\": \"Error: Username and password cannot be empty.\"}");
            out.flush();
            return;
        }

        UserDAO userDao = new UserDAO();

        // Check if the user already exists
        if (userDao.getUserByUsername(username) != null) {
            out.print("{\"success\": false, \"message\": \"Error: User already exists.\"}");
            out.flush();
            return;
        }

        boolean success = userDao.createUser(username, password, isAdmin);

        if (success) {
            out.print("{\"success\": true, \"message\": \"User '" + username + "' created successfully.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Error: Unable to create user. This user may already exist.\"}");
        }
        out.flush();
    }
}
