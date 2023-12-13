<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="main.bookit.Model.Reservation" %>
<%@ page import="main.bookit.Model.Course" %>
<%@ page import="main.bookit.Model.User" %>
<%@ page import="main.bookit.DAO.CourseDAO" %>
<%@ page import="main.bookit.DAO.BookingDAO" %>
<%@ page import="main.bookit.DAO.ListDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="main.bookit.Model.BookingList" %>
<!DOCTYPE html>
<html>
<head>
  <title>Dashboard</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background: #f4f4f4;
    }
    .container {
      width: 80%;
      margin: auto;
      overflow: hidden;
    }
    h1, h2 {
      color: #333;
    }
    .welcome {
      background: #333;
      color: #fff;
      padding: 10px;
      text-align: center;
    }
    .course-list, .booking-list {
      margin-top: 20px;
    }
    .course-list a, .booking-item {
      display: block;
      padding: 10px;
      background: #007bff;
      color: white;
      text-decoration: none;
      border-radius: 5px;
      margin-bottom: 5px;
      text-align: center;
    }
    .course-list a:hover, .booking-item:hover {
      background: #0056b3;
    }
    .logout a {
      display: inline-block;
      padding: 10px 15px;
      background-color: #f44336;
      color: white;
      text-decoration: none;
      border-radius: 5px;
      margin-top: 20px;
    }
    .logout a:hover {
      background-color: #d32f2f;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Dashboard</h1>
  <%
    User user = (User) session.getAttribute("user");
    CourseDAO courseDao = new CourseDAO();
    BookingDAO bookingDao = new BookingDAO();
    ListDAO listDao = new ListDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    if(user != null) {
  %>
  <div class="welcome">
    <p>Welcome, <%= user.getUsername() %>!</p>
  </div>
  <%
    List<Course> courses = courseDao.getAllCourses();
    List<Reservation> bookings = bookingDao.getBookingsForUser(user.getId());

    if (courses != null && !courses.isEmpty()) {
  %>
  <h2>Your Courses</h2>
  <div class="course-list">
    <% for (Course course : courses) { %>
    <a href="timeslot?courseId=<%= course.getId() %>"><%= course.getTitle() %></a>
    <% } %>
  </div>
  <%
    }
    if (bookings != null && !bookings.isEmpty()) {
  %>
  <h2>Your Bookings</h2>
  <div class="booking-list">
    <% for (Reservation booking : bookings) {
      BookingList bookingList = listDao.getBookingListById(booking.getListId());
      if (bookingList != null) {
        Course course = courseDao.getCourseById(bookingList.getCourseId());
        String courseTitle = course != null ? course.getTitle() : "Unknown Course";
        String time = dateFormat.format(bookingList.getStart());
        String place = bookingList.getLocation();
    %>
    <div class="booking-item">
      <strong>Course:</strong> <%= courseTitle %><br>
      <strong>Time:</strong> <%= time %><br>
      <strong>Place:</strong> <%= place %>
    </div>
    <%
        }
      } %>
  </div>
  <%
  } else {
  %>
  <p>No bookings found.</p>
  <%
    }
  %>
  <!-- Logout Button -->
  <div class="logout">
    <a href="logout">Log Out</a>
  </div>
  <%
  } else {
  %>
  <script type="text/javascript">
    alert("Session has expired. Please log in again.");
    window.location.href = "login.jsp";
  </script>
  <%
    }
  %>
</div>
</body>
</html>
