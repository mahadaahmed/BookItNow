<%@ page import="main.bookit.Model.User" %><%--
  Created by IntelliJ IDEA.
  User: ahmed-bashir
  Date: 2024-01-01
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Courses</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>

<script>
    $(document).ready(function() {
        // AJAX for Granting Course Access
        $("#grantAccessForm").submit(function(event) {
            event.preventDefault(); // Prevent the default form submission
            var formData = $(this).serialize(); // Serialize form data

            $.ajax({
                url: 'GrantCourseAccess', // Servlet URL
                type: 'POST',
                data: formData,
                success: function(response) {
                    // Check the response status
                    if(response.status === "success") {
                        alert(response.message); // Success message
                        // Update your page as needed based on the response
                        // For example, refresh a part of your page or clear the form
                    } else {
                        alert(response.message); // Error message
                    }
                },
                error: function() {
                    // Handle error in AJAX call
                    alert("AJAX error: request failed.");
                }
            });
        });

        // You can add more AJAX calls here for other forms or actions
    });
</script>



<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        // User not logged in, redirect to login page
        response.sendRedirect("login.jsp");
        return;
    } else if (user.getAdmin() != 1) {
        // User is not an admin, redirect to user home page
        response.sendRedirect("userHome.jsp");
        return;
    }
    // If execution reaches here, the user is an admin and can proceed to see the admin page content
%>



<%@ include file="/common/navbar.jspf" %>
<%@ include file="/common/courses.jspf" %>

<!--script>
    function populateCourses() {
        var userId = document.getElementById('userRevoke').value;
        // Assuming you have an endpoint 'getAccessibleCourses' that returns a JSON array of courses for the user
        fetch('getAccessibleCourses?userId=' + userId)
            .then(response => response.json())
            .then(courses => {
                var coursesDropdown = document.getElementById('courseRevoke');
                coursesDropdown.innerHTML = ''; // Clear current options
                courses.forEach(course => {
                    var option = document.createElement('option');
                    option.value = course.id;
                    option.textContent = course.title;
                    coursesDropdown.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching courses:', error));
    }
</script-->

</body>
</html>
