<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="main.bookit.Model.BookingList" %>
<%@ page import="main.bookit.DAO.ListDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Time Slots</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50">
<div class="min-h-screen flex flex-col justify-center items-center px-4 py-8">
    <div class="w-full max-w-6xl mx-auto p-6 bg-white shadow-md rounded-lg">
        <h1 class="text-3xl font-semibold text-center text-gray-800 mb-10">Available Time Slots</h1>
        <div class="flex flex-wrap -mx-4">
            <%
                ListDAO listDao = new ListDAO();
                List<BookingList> listsForCourse = (List<BookingList>) request.getAttribute("listsForCourse");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                if (listsForCourse != null && !listsForCourse.isEmpty()) {
                    for (BookingList list : listsForCourse) {
                        List<Integer> availableSequences = listDao.getAvailableSlotSequences(list.getId(), list.getMaxSlots());
                        Calendar startTime = Calendar.getInstance();
            %>
            <div class="w-full md:w-1/2 lg:w-1/3 px-4 mb-6">
                <div class="bg-blue-100 p-4 rounded-t-lg">
                    <h2 class="text-xl font-medium text-blue-600"><%= list.getDescription() %></h2>
                </div>
                <div class="bg-gray-100 p-4 border border-blue-200 rounded-b-lg">
                    <p class="text-gray-600">Admin: <%= list.getAdminUsername() %></p>
                    <p class="text-gray-600">Location: <%= list.getLocation() %></p>
                    <p class="text-gray-600">Start Time: <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(list.getStart()) %></p>
                    <p class="text-gray-600">Interval: <%= list.getInterval() %> minutes</p>
                    <p class="text-gray-600">Maximum Slots: <%= list.getMaxSlots() %></p>
                    <% if (!availableSequences.isEmpty()) { %>
                    <form action="bookTimeSlot" method="post">
                        <input type="hidden" name="listId" value="<%= list.getId() %>" />
                        <label for="sequence" class="block text-gray-700 mt-3">Choose a time slot:</label>
                        <div class="flex items-center gap-4 mt-1">
                            <select name="sequence" id="sequence" class="flex-1 p-2 rounded border border-gray-300">
                                <%
                                    for (Integer sequence : availableSequences) {
                                        startTime.setTime(list.getStart());
                                        startTime.add(Calendar.MINUTE, sequence * list.getInterval());
                                %>
                                <option value="<%= sequence %>"><%= "Slot " + (sequence + 1) + " (" + timeFormat.format(startTime.getTime()) + ")" %></option>
                                <% } %>
                            </select>
                            <input type="submit" value="Book Slot" class="bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700 cursor-pointer">
                        </div>
                    </form>
                    <% } else { %>
                    <p class="text-gray-600 mt-3">No available time slots left.</p>
                    <% } %>
                </div>
            </div>
            <%
                }
            } else {
            %>
            <p class="w-full text-center py-5 text-gray-600">No time slots available for this course.</p>
            <%
                }
            %>
        </div>
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"><a href="dashboard">BACK</a></button>
    </div>
</div>
</body>
</html>
