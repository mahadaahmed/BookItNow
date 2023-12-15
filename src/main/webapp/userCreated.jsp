<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>User Creation Status</title>
  <style>
    .message {
      border: 1px solid #ddd;
      padding: 10px;
      margin: 20px auto;
      width: 60%;
      text-align: center;
    }
    .success {
      background-color: #ddffdd;
      border-color: #d4edda;
      color: #155724;
    }
    .error {
      background-color: #f8d7da;
      border-color: #f5c6cb;
      color: #721c24;
    }
  </style>
</head>
<body>
<div class="message <%= (Boolean) request.getAttribute("success") ? "success" : "error" %>">
  <%= request.getAttribute("message") %>
</div>
<a href="dashboard.jsp">Return to Dashboard</a>
</body>
</html>
