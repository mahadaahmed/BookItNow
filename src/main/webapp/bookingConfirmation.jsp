<%@ page import="java.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Booking Confirmation</title>
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
      text-align: center;
    }
    .message {
      padding: 20px;
      margin-top: 20px;
      border: 1px solid #ccc;
      background: #e8e8e8;
      border-radius: 5px;
    }
    .success {
      color: #28a745;
    }
    .error {
      color: #dc3545;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Booking Confirmation</h1>
  <div class="<%= request.getAttribute("bookingStatus") != null && request.getAttribute("bookingStatus").equals("success") ? "success" : "error" %>">
    <%-- Use expression tags to display the message --%>
    <%= request.getAttribute("bookingMessage") != null ? request.getAttribute("bookingMessage") : "An unexpected error occurred." %>
  </div>
  <!-- Link to go back to the dashboard or any other page -->
  <a href="dashboard">Return to Dashboard</a>
</div>
</body>
</html>
