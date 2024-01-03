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
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
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
