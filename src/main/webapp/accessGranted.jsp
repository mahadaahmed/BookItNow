<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Access Status</title>
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
    .message {
      margin-top: 20px;
      padding: 15px;
      border-radius: 4px;
      text-align: center;
    }
    .success-message {
      background-color: #dff0d8;
      border-color: #d6e9c6;
      color: #3c763d;
    }
    .error-message {
      background-color: #f2dede;
      border-color: #ebccd1;
      color: #a94442;
    }
    .dashboard-link {
      display: block;
      width: 100%;
      text-align: center;
      padding: 10px;
      background-color: #007bff;
      color: white;
      text-decoration: none;
      border-radius: 5px;
      margin-top: 20px;
    }
    .dashboard-link:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>
<div class="container">
  <% if (request.getAttribute("error") == null) { %>
  <h2>Access Granted</h2>
  <div class="message success-message">
    <%= request.getAttribute("username") %> has been granted access to course <%= request.getAttribute("courseTitle") %> by admin <%= request.getAttribute("adminUsername") %>.
  </div>
  <% } else { %>
  <h2>Access Denied</h2>
  <div class="message error-message">
    <%= request.getAttribute("error") %>
  </div>
  <% } %>
  <a href="courses.jsp" class="dashboard-link">Back to Courses</a>
</div>
</body>
</html>
