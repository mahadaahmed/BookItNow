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

@WebServlet("/timeslot")
public class TimeslotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        ListDAO listDao = new ListDAO();
        List<BookingList> listsForCourse = listDao.getListsForCourse(courseId);

        request.setAttribute("listsForCourse", listsForCourse);
        request.getRequestDispatcher("/timeslot.jsp").forward(request, response);
    }
}
