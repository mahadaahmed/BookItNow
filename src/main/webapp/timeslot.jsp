<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="main.bookit.Model.BookingList" %>
<!DOCTYPE html>
<html>
<head>
    <title>Time Slots</title>
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
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin-top: 30px;
        }
        h1 {
            color: #333;
            text-align: center;
            padding-top: 20px;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }
        .time-slot {
            background: #f9f9f9;
            padding: 15px;
            margin-bottom: 10px;
            border-left: 5px solid #007bff;
        }
        .time-slot h2 {
            margin-top: 0;
            color: #007bff;
        }
        .time-slot p {
            margin: 5px 0;
            color: #666;
        }
        .no-slots {
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>
<h1>Available Time Slots</h1>
<%
    List<BookingList> listsForCourse = (List<BookingList>) request.getAttribute("listsForCourse");
    if (listsForCourse != null && !listsForCourse.isEmpty()) {
        for (BookingList list : listsForCourse) {
%>
<div>
    <h2><%= list.getDescription() %></h2>
    <p>Admin: <%= list.getAdminUsername() %></p>
    <p>Location: <%= list.getLocation() %></p>
    <p>Start Time: <%= list.getStart() %></p>
    <p>Interval: <%= list.getInterval() %> minutes</p>
    <p>Maximum Slots: <%= list.getMaxSlots() %></p>
    <!-- Add a form for each time slot -->
    <form action="bookTimeSlot" method="post">
        <input type="hidden" name="listId" value="<%= list.getId() %>" />
        <label for="sequence">Choose a time slot:</label>
        <select name="sequence" id="sequence">
            <% for (int i = 0; i < list.getMaxSlots(); i++) { %>
            <option value="<%= i %>">Slot <%= i %></option>
            <% } %>
        </select>
        <input type="submit" value="Book Slot" />
    </form>
</div>
<%
    }
} else {
%>
<p>No time slots available for this course.</p>
<%
    }
%>
</body>
</html>
