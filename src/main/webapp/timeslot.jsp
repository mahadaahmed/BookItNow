<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="main.bookit.Model.BookingList" %>
<%@ page import="main.bookit.DAO.ListDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
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
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin: 30px auto auto;
        }
        h1 {
            color: #333;
            text-align: center;
            padding-top: 20px;
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

        .disabled-button {
            background: #cccccc;
            color: #666666;
            border: 1px solid #999999;
            pointer-events: none;
        }
        input[type="submit"] {
            display: block;
            padding: 10px 15px;
            margin-top: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        select {
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ddd;
        }
        form {
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Available Time Slots</h1>
    <%
        ListDAO listDao = new ListDAO();
        List<BookingList> listsForCourse = (List<BookingList>) request.getAttribute("listsForCourse");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        if (listsForCourse != null && !listsForCourse.isEmpty()) {
            for (BookingList list : listsForCourse) {
                List<Integer> availableSequences = listDao.getAvailableSlotSequences(list.getId(), list.getMaxSlots());
                Calendar startTime = Calendar.getInstance();

    %>
    <div class="time-slot">
        <h2><%= list.getDescription() %></h2>
        <p>Admin: <%= list.getAdminUsername() %></p>
        <p>Location: <%= list.getLocation() %></p>
        <p>Start Time: <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(list.getStart()) %></p>
        <p>Interval: <%= list.getInterval() %> minutes</p>
        <p>Maximum Slots: <%= list.getMaxSlots() %></p>
        <% if (!availableSequences.isEmpty()) { %>
        <form action="bookTimeSlot" method="post">
            <input type="hidden" name="listId" value="<%= list.getId() %>" />
            <label for="sequence">Choose a time slot:</label>
            <select name="sequence" id="sequence">
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
