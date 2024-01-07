<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.bookit.Model.User, main.bookit.DAO.CourseDAO, main.bookit.DAO.BookingDAO, main.bookit.DAO.ListDAO, main.bookit.Model.Course, main.bookit.Model.BookingList, main.bookit.Model.Reservation, java.text.SimpleDateFormat, java.util.Calendar, java.util.List" %>
<%@ page import="main.bookit.DAO.UserDAO" %>

<%
    User loggedInUser = (User) session.getAttribute("user");
    CourseDAO courseDao = new CourseDAO();
    BookingDAO bookingDao = new BookingDAO();
    ListDAO listDao = new ListDAO();
    UserDAO userDAO = new UserDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">

<!--%@ include file="/common/userNavbar.jspf" %-->

<div class="container mx-auto mt-5 p-5 bg-white shadow rounded-lg">
    <h1 class="text-2xl font-bold text-gray-800">Dashboard</h1>
    <div class="mt-3">
        <p class="text-md text-gray-600">Welcome, <%= loggedInUser.getUsername() %></p>
    </div>

    <h2 class="text-xl font-semibold text-gray-700 mt-5">Your Courses</h2>
    <div class="mt-3">
        <%
            List<Course> courses = courseDao.getCoursesForUser(loggedInUser.getId());
            if (courses.isEmpty()) {
        %>
        <p class="text-gray-500">You don't have access to any courses.</p>
        <%
        } else {
            for (Course course : courses) {
        %>
        <div class="mt-2">
            <a href="timeslot?courseId=<%= course.getId() %>"  class="text-blue-600 hover:text-blue-800"><%= course.getTitle() %></a>
        </div>
        <%
                }
            }
        %>
    </div>

    <h2 class="text-xl font-semibold text-gray-700 mt-5">Your Bookings</h2>
    <div class="mt-3">
        <%
            List<Reservation> bookings = bookingDao.getBookingsForUser(loggedInUser.getId());
            for (Reservation booking : bookings) {
                BookingList bookingList = listDao.getBookingListById(booking.getListId());
                Course course = courseDao.getCourseById(bookingList.getCourseId());
                String courseTitle = course != null ? course.getTitle() : "Unknown Course";
                String listType = bookingList.getDescription().substring(0,1).toUpperCase() + bookingList.getDescription().substring(1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(bookingList.getStart());
                calendar.add(Calendar.MINUTE, booking.getSequence() * bookingList.getInterval());
                String time = dateFormat.format(calendar.getTime());
                String place = bookingList.getLocation();
        %>
        <div class="p-4 bg-gray-50 rounded-lg mt-2">
            <strong class="font-medium">Course:</strong> <%= courseTitle %><br>
            <strong class="font-medium">Type:</strong> <%= listType %><br>
            <strong class="font-medium">Time:</strong> <%= time %><br>
            <strong class="font-medium">Place:</strong> <%= place %>
            <form action="cancelBooking" method="post" class="mt-2">
                <!-- Ensure this hidden field's name attribute is 'listId', not 'bookingId' -->
                <input type="hidden" name="bookingId" value="<%= bookingList.getId() %>" />
                <button type="submit" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-600 focus:ring-opacity-50">
                    Cancel List
                </button>
            </form>

        </div>
        <%
            }
        %>
    </div>

    <!-- New Booking Section -->
    <div class="mt-5 p-5 bg-white shadow rounded-lg">
        <h2 class="text-xl font-semibold text-gray-700">Make a New Booking</h2>
        <form action="bookTimeSlot" method="post">
            <div class="mt-3">
                <label for="course" class="block text-sm font-medium text-gray-700">Select Course:</label>
                <select id="course" name="courseId" required class="block w-full pl-3 pr-10 py-2 mt-1 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                    <!--a href="timeslot?courseId=<!%= course.getId() %>"><!%= course.getTitle() %></a-->
                    <% for (Course course : courses) { // Assuming 'courses' contains all bookable courses %>
                    <option value="<%= course.getId() %>"><%= course.getTitle() %></option>
                    <% } %>
                </select>
            </div>
            <div class="mt-3">
                <label for="bookingTime" class="block text-sm font-medium text-gray-700">Select Time:</label>
                <input type="datetime-local" id="bookingTime" name="bookingTime" required class="block w-full pl-3 pr-10 py-2 mt-1 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
            </div>
            <div class="mt-3">
                <input type="submit" value="Book Now" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500" />
            </div>
        </form>
    </div>
</div>
</body>
</html>
