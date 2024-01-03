<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.bookit.Model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>

<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>


    <!-- Main container with flexbox layout -->
    <div>
        <!-- Conditional admin panel display -->
        <% if(currentUser.getAdmin() == 1) { %>
            <%@ include file="/adminHome.jsp" %>
        <% } else { %>
            <%@ include file="/userHome.jsp" %>
        <% } %>
    </div>
</body>
</html>





