<%@ page contentType="text/html;charset=UTF-8" %>
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
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.1);
      margin: 30px auto auto;
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
<%
  // No need to declare 'session', just use the implicit object
  String bookingStatus = (String) session.getAttribute("bookingStatus");
  String bookingMessage = (String) session.getAttribute("bookingMessage");
  session.removeAttribute("bookingStatus");
  session.removeAttribute("bookingMessage");
%>
<div class="container">
  <h1>Booking Confirmation</h1>
  <div class="message <%= bookingStatus != null && bookingStatus.equals("success") ? "success" : "error" %>">
    <%= bookingMessage != null ? bookingMessage : "An unexpected error occurred." %>
  </div>
  <!-- Link to go back to the dashboard or any other page -->
  <a href="bookings.jsp">Return to Bookings</a>
</div>
</body>
</html>