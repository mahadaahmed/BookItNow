<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="main.bookit.Model.BookingList" %>
<%@ page import="main.bookit.DAO.ListDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Include your existing CSS -->
  <style>
    /* Your existing CSS for styling the page */
  </style>
  <title></title>
</head>
<body>
<div class="container">
  <h1>Select a Time Slot for Booking</h1>
  <%
    int userId = (Integer) request.getAttribute("userId");
    List<BookingList> listsForCourse = (List<BookingList>) request.getAttribute("listsForCourse");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    if (listsForCourse != null && !listsForCourse.isEmpty()) {
      for (BookingList list : listsForCourse) {
        List<Integer> availableSequences = new ListDAO().getAvailableSlotSequences(list.getId(), list.getMaxSlots());
        Calendar startTime = Calendar.getInstance();
  %>
  <div class="time-slot">
    <h2><%= list.getDescription() %></h2>
    <% if (!availableSequences.isEmpty()) { %>
    <form action="AdminAddBooking" method="post">
      <input type="hidden" name="userId" value="<%= userId %>" />
      <input type="hidden" name="listId" value="<%= list.getId() %>" />
      <label for="sequence<%= list.getId() %>">Choose a time slot:</label>
      <select name="sequence" id="sequence<%= list.getId() %>">
        <%
          for (Integer sequence : availableSequences) {
            startTime.setTime(list.getStart());
            startTime.add(Calendar.MINUTE, sequence * list.getInterval());
        %>
        <option value="<%= sequence %>"><%= "Slot " + (sequence + 1) + " (" + timeFormat.format(startTime.getTime()) + ")" %></option>
        <% } %>
      </select>
      <input type="submit" value="Book Slot" class="btn btn-primary" />
    </form>
    <% } else { %>
    <p class="no-slots">No available time slots left.</p>
    <% } %>
  </div>
  <%
    }
  } else {
  %>
  <p class="no-slots">No time slots available for this course.</p>
  <%
    }
  %>
</div>
</body>
</html>
