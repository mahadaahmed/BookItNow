package main.bookit.Servlet;

import main.bookit.DAO.ListDAO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// In DeleteListServlet.java
@WebServlet("/deleteList")
public class DeleteListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Similar admin check as above
        // Get list ID from request
        int listId = Integer.parseInt(request.getParameter("listId"));


        // Use ListDAO to delete the list
        ListDAO listDao = new ListDAO();
        boolean isSuccess = listDao.deleteList(listId);

        // Handle redirect or forward based on success or failure

        if (isSuccess) {
            // Redirect or forward to success page or admin dashboard
            //request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            response.sendRedirect("dashboard.jsp");
        } else {
            // Handle failure: set error message and forward back to form
            request.setAttribute("errorMessage", "Something went wrong when creating the list");
        }
    }
}
