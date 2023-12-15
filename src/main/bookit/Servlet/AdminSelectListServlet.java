package main.bookit.Servlet;

import main.bookit.DAO.ListDAO;
import main.bookit.Model.BookingList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AdminSelectList")
public class AdminSelectListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        ListDAO listDao = new ListDAO();
        List<BookingList> listsForCourse = listDao.getListsForCourse(courseId);

        request.setAttribute("userId", userId);
        request.setAttribute("courseId", courseId);
        request.setAttribute("listsForCourse", listsForCourse);
        request.getRequestDispatcher("adminSelectTimeSlot.jsp").forward(request, response);
    }
}
