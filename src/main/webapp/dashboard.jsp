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
<%@ page import="java.util.Calendar" %>
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

    .cancel-booking-form {
      display: inline-block; /* Align with the rest of the content */
      margin-top: 10px; /* Space it a bit from the booking info */
    }

    .cancel-booking-form input[type="submit"] {
      padding: 5px 10px;
      background-color: #dc3545; /* Red color for cancellation */
      color: white;
      text-decoration: none;
      border: none;
      border-radius: 5px;
      cursor: pointer; /* To indicate it is clickable */
      transition: background-color 0.3s; /* Smooth background color transition */
    }

    .cancel-booking-form input[type="submit"]:hover {
      background-color: #bd2130; /* A darker shade of red on hover */
    }

    /* Admin Options Styles */
    .admin-section {
      background: #ffffff;
      border: 2px solid #007bff;
      border-radius: 8px;
      padding: 20px;
      margin-top: 20px;
      box-shadow: 0 4px 8px rgba(0, 75, 159, 0.2);
    }

    .admin-section h2 {
      color: #007bff;
      border-bottom: 1px solid #eee;
      padding-bottom: 10px;
      margin-bottom: 20px;
    }

    .admin-section button {
      background-color: #28a745;
      border: none;
      color: white;
      padding: 10px 15px;
      text-align: center;
      text-decoration: none;
      display: inline-block;
      font-size: 16px;
      margin: 4px 2px;
      cursor: pointer;
      border-radius: 4px;
      transition: background-color 0.3s ease;
    }

    .admin-section button:hover {
      background-color: #218838;
    }

    .list-item {
      background: #f8f9fa;
      border: 1px solid #ddd;
      padding: 10px;
      margin-bottom: 10px;
      border-radius: 4px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .list-item span {
      font-weight: bold;
    }

    .list-item form {
      margin: 0;
    }

    .list-item input[type="submit"] {
      background-color: #dc3545;
      border: none;
      color: white;
      padding: 6px 12px;
      text-decoration: none;
      cursor: pointer;
      border-radius: 4px;
      transition: background-color 0.3s ease;
    }

    .list-item input[type="submit"]:hover {
      background-color: #c82333;
    }

  </style>
</head>
<body>
<div class="container">
  <h1>Dashboard</h1>
  <%
    User user = (User) session.getAttribute("user");
    boolean isAdmin = (user != null) && user.isAdmin(); // Replace with your User class admin check
    CourseDAO courseDao = new CourseDAO();
    BookingDAO bookingDao = new BookingDAO();
    ListDAO listDao = new ListDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    if(user != null) {
  %>
  <div class="welcome">
    <p>Welcome, <%= user.getUsername() %>!</p>
  </div>
  <% if (isAdmin) { %>
  <div class="admin-section">
    <h2>Admin Options</h2>
    <button onclick="window.location.href='createList.jsp'">Add New List</button>
    <%
      List<BookingList> allLists = listDao.getAllAvailableLists();
      for (BookingList list : allLists) {
    %>
    <div class="list-item">
      <span><%= list.getDescription() %></span>
      <form action="deleteList" method="post">
        <input type="hidden" name="listId" value="<%= list.getId() %>" />
        <input type="submit" value="Delete" />
      </form>
    </div>
    <%
      }
    %>
  </div>
  <% } %>

  <h2>Your Courses</h2>
  <div class="course-list">
    <%
      List<Course> courses = courseDao.getAllCourses();
      for (Course course : courses) {
    %>
    <a href="timeslot?courseId=<%= course.getId() %>"><%= course.getTitle() %></a>
    <%
      }
    %>
  </div>

  <h2>Your Bookings</h2>
  <div class="booking-list">
    <%
      List<Reservation> bookings = bookingDao.getBookingsForUser(user.getId());
      for (Reservation booking : bookings) {
        BookingList bookingList = listDao.getBookingListById(booking.getListId());
        Course course = courseDao.getCourseById(bookingList.getCourseId());
        String courseTitle = course != null ? course.getTitle() : "Unknown Course";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingList.getStart());
        calendar.add(Calendar.MINUTE, booking.getSequence() * bookingList.getInterval());
        String time = dateFormat.format(calendar.getTime());
        String place = bookingList.getLocation();
    %>
    <div class="booking-item">
      <strong>Course:</strong> <%= courseTitle %><br>
      <strong>Time:</strong> <%= time %><br>
      <strong>Place:</strong> <%= place %>
      <form class="cancel-booking-form" action="cancelBooking" method="post">
        <input type="hidden" name="bookingId" value="<%= booking.getId() %>" />
        <input type="submit" value="Cancel Booking" />
      </form>
    </div>
    <%
      }
    %>
  </div>

  <div class="logout">
    <a href="logout">Log Out</a>
  </div>

  <% } else { %>
  <script type="text/javascript">
    alert("Session has expired. Please log in again.");
    window.location.href = "login.jsp";
  </script>
  <% } %>
</div>
</body>

</html>
