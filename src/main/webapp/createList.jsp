<%@ page import="main.bookit.Model.Course" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create New List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form > div {
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"],
        input[type="datetime-local"],
        input[type="number"],
        input[type="submit"],
        .back-button {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 4px;
            border: 1px solid #ddd;
        }
        input[type="submit"],
        .back-button {
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        input[type="submit"]:hover,
        .back-button:hover {
            background: #0056b3;
        }
        .back-button {
            background: #6c757d;
            text-align: center;
        }
        .back-button:hover {
            background: #5a6268;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Create a New List</h2>
    <form action="createList" method="post">
        <!-- Removed ID input as it should be set by the database automatically -->
        <div>
            <label for="courseID">Course ID:</label>
            <input type="text" id="courseID" name="courseID" required>
            <!--label for="courseID">Course:</label>
            <select id="courseID" name="courseID" required>
                <!% List<Course> courses = request.getAttribute("availableCourses");
                    for(Course course : courses) { %>
                <option value="<!%= course.getId() %>"><!%= course.getTitle() %></option>
                <!% } %>
            </select-->

        </div>
        <div>
            <label for="description">Description:</label>
            <input type="text" id="description" name="description" required>
        </div>
        <div>
            <label for="location">Location:</label>
            <input type="text" id="location" name="location" required>
        </div>
        <div>
            <label for="start">Start Time:</label>
            <input type="datetime-local" id="start" name="start" required>
        </div>
        <div>
            <label for="interval">Interval (in minutes):</label>
            <input type="number" id="interval" name="interval" required>
        </div>
        <div>
            <label for="maxSlots">Max Slots:</label>
            <input type="number" id="maxSlots" name="maxSlots" required>
        </div>
        <div class="form-group">
            <input type="submit" value="Create List">
            <!-- Back to Dashboard Button -->
            <button type="button" onclick="window.location.href='dashboard.jsp'" class="back-button">Back to Dashboard</button>
        </div>
    </form>
</div>

</body>
</html>