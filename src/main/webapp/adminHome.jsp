<%@ page import="main.bookit.Model.User" %><%--
  Created by IntelliJ IDEA.
  User: ahmed-bashir
  Date: 2024-01-01
  Time: 23:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>


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

<script>
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById('createUserForm').addEventListener('submit', function(e) {
            e.preventDefault(); // Prevent the form from submitting through the browser

            var form = this;
            var formData = new FormData(form);
            var messageContainer = document.getElementById('messageContainer');

            fetch(form.action, {
                method: 'POST',
                body: formData
            })
                .then(response => response.json()) // Assuming your servlet returns JSON
                .then(data => {
                    if (data.success) {
                        messageContainer.className = 'p-4 rounded-md bg-green-100 text-green-800';
                        messageContainer.textContent = data.message;
                        form.reset(); // Reset the form fields
                    } else {
                        messageContainer.className = 'p-4 rounded-md bg-red-100 text-red-800';
                        messageContainer.textContent = data.message;
                    }
                    messageContainer.classList.remove('hidden');
                })
                .catch(error => {
                    messageContainer.className = 'p-4 rounded-md bg-red-100 text-red-800';
                    messageContainer.textContent = 'Error: ' + error;
                    messageContainer.classList.remove('hidden');
                });
        });
    });
</script>

<%@ include file="/common/navbar.jspf" %>
<%@ include file="/common/adminHome.jspf" %>

</body>
</html>
