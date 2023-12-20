<%@ page contentType="text/html;charset=UTF-8" %>
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
<%@ page import="main.bookit.DAO.UserDAO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
  <title>Dashboard</title>
  <!-- Your styles here -->
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background: #f4f4f4;
    }
    .container {
      width: 90%;
      margin: 20px auto;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }
    h1, h2, h3 {
      color: #333;
      margin-bottom: 20px;
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

    .admin-panel {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      margin-bottom: 20px;
    }

    .admin-section {
      margin-bottom: 20px;
    }

    .form-style {
      background: #f9f9f9;
      padding: 15px;
      border-radius: 8px;
      border: 1px solid #ddd;
      margin-bottom: 15px;
    }

    .input-group {
      margin-bottom: 15px;
    }

    .input-group label {
      display: block;
      margin-bottom: 5px;
    }

    .input-group input[type="text"],
    .input-group input[type="password"],
    .input-group select,
    .input-group input[type="submit"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .input-group input[type="checkbox"] {
      margin-top: 3px;
    }

    .admin-button {
      background-color: #5cb85c;
      border: none;
      padding: 10px;
      margin-top: 10px;
      color: white;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
    }

    .admin-button:hover {
      background-color: #4cae4c;
    }

    .admin-button.delete {
      background-color: #d9534f;
    }

    .admin-button.delete:hover {
      background-color: #c9302c;
    }

    .admin-list-item {
      background: #fff;
      border: 1px solid #ddd;
      padding: 10px;
      margin-bottom: 10px;
      border-radius: 4px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .admin-list-item span {
      flex-grow: 1;
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
    CourseDAO courseDao = new CourseDAO();
    BookingDAO bookingDao = new BookingDAO();
    ListDAO listDao = new ListDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    if(user != null) {
      String welcomeMessage = user.getAdmin() == 1 ? "Welcome, admin " + user.getUsername() : "Welcome, " + user.getUsername();
  %>
  <div class="welcome">
    <p><%= welcomeMessage %></p>
  </div>

  <% if(user.getAdmin() == 1) { %>
  <div class="admin-panel">
    <h2>Admin Panel</h2>
    <!-- Admin content here -->


    <!-- Create New Booking List -->
    <div class="admin-section">
      <h3>Create New Booking List</h3>
      <a href="createList.jsp" class="admin-button">Create New Booking List</a>
    </div>

    <!-- Create New User Form -->
    <div class="admin-section">
      <h3>Create New User</h3>
      <form action="CreateUserServlet" method="post" class="form-style">
        <div class="input-group">
          <label for="newUsername">Username:</label>
          <input type="text" id="newUsername" name="username" required>
        </div>

        <div class="input-group">
          <label for="newPassword">Password:</label>
          <input type="password" id="newPassword" name="password" required>
        </div>

        <div class="input-group">
          <label for="isAdmin">Is Admin:</label>
          <input type="checkbox" id="isAdmin" name="admin">
        </div>

        <div class="input-group">
          <input type="submit" value="Create User" class="admin-button">
        </div>
      </form>
    </div>

    <!-- Course Selection Form -->
    <% List<Course> allCourses = new CourseDAO().getAllCourses(); %>
    <div class="admin-section">
      <h3>Select a Course to Grant User Access</h3>
      <form action="dashboard.jsp" method="get">
        <label for="selectedCourseId">Select a Course:</label>
        <select id="selectedCourseId" name="selectedCourseId">
          <% for(Course course : allCourses) { %>
          <option value="<%= course.getId() %>"><%= course.getTitle() %></option>
          <% } %>
        </select>
        <input type="submit" value="Select Course">
      </form>
    </div>

    <!-- Grant Access Section -->
    <%
      int selectedCourseId = 0;
      if(request.getParameter("selectedCourseId") != null) {
        selectedCourseId = Integer.parseInt(request.getParameter("selectedCourseId"));
      }
      List<User> usersWithoutAccess = new ArrayList<>();
      if(selectedCourseId > 0) {
        usersWithoutAccess = new UserDAO().getUsersWithoutAccess(selectedCourseId);
      }
    %>
    <% if(!usersWithoutAccess.isEmpty()) { %>
    <div class="admin-panel-actions">
      <h3>Grant Course Access</h3>
      <form action="GrantCourseAccess" method="post">
        <label for="userSelects">Select User:</label>
        <select id="userSelects" name="userId">
          <% for(User users : usersWithoutAccess) { %>
          <option value="<%= users.getId() %>"><%= users.getUsername() %></option>
          <% } %>
        </select>
        <input type="hidden" name="courseId" value="<%= selectedCourseId %>">
        <input type="submit" value="Grant Access" class="admin-button">
      </form>
    </div>
    <% } %>




  <!-- Add Booking for User -->
    <div class="admin-section">
      <h3>Add Booking for User</h3>
      <form action="AdminAddBooking" method="post" class="form-style">
        <div class="input-group">
          <label for="userSelect">Select User:</label>
          <select id="userSelect" name="userId">
            <% for(User userss : new UserDAO().getAllUsers()) { %>
            <option value="<%= userss.getId() %>"><%= userss.getUsername() %></option>
            <% } %>
          </select>
        </div>

        <div class="input-group">
          <label for="listSelect">Select List:</label>
          <select id="listSelect" name="listId">
            <% for(BookingList list : new ListDAO().getAllAvailableLists()) { %>
            <option value="<%= list.getId() %>"><%= list.getDescription() %></option>
            <% } %>
          </select>
        </div>

        <div class="input-group">
          <label for="sequenceInput">Select Sequence:</label>
          <input type="number" id="sequenceInput" name="sequence" required min="0">
        </div>

        <div class="input-group">
          <input type="submit" value="Add Booking" class="admin-button">
        </div>
      </form>
    </div>



    <!-- Remove Booking for User -->
    <div class="admin-section">
      <h3>Remove Booking for User</h3>
      <% List<Reservation> allReservations = new BookingDAO().getAllReservations(); %>
      <% for(Reservation reservation : allReservations) { %>
      <div class="admin-list-item">
        <span>User ID: <%= reservation.getUserId() %>, List ID: <%= reservation.getListId() %>, Sequence: <%= reservation.getSequence() %></span>
        <form action="AdminRemoveBooking" method="post" class="form-style">
          <input type="hidden" name="bookingId" value="<%= reservation.getId() %>">
          <input type="submit" value="Remove Booking" class="admin-button delete">
        </form>
      </div>
      <% } %>
    </div>




    <div class="admin-section">
      <h3>Booking Lists</h3>
      <%
        List<BookingList> adminBookingLists = listDao.getListsByAdmin(user.getId());
        for(BookingList list : adminBookingLists) {
      %>
      <div class="admin-list-item">
        <span><%= list.getDescription() %> - <%= list.getLocation() %></span>
        <form action="deleteList" method="post" class="form-style">
          <input type="hidden" name="listId" value="<%= list.getId() %>" />
          <input type="submit" value="Delete" class="admin-button delete">
        </form>
      </div>
      <% } %>
    </div>
    <!-- The rest of your admin panel content goes here -->


  </div> <!-- Closing the admin-panel div if admin -->
  <% } %>

  <!-- Other content for the user goes here -->
  <h2>Your Courses</h2>
  <div class="course-list">
    <%
      //CourseDAO courseDao = new CourseDAO();
      List<Course> courses = courseDao.getCoursesForUser(user.getId());

      if (courses.isEmpty()) {
        // Display message if no courses are available
    %>
    <p>You don't have access to any courses.</p>
    <%
    } else {
      for (Course course : courses) {
        // Display each course
    %>
    <a href="timeslot?courseId=<%= course.getId() %>"><%= course.getTitle() %></a>
    <%
        }
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
</body>
</html>
