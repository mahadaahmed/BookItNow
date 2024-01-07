<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>User Creation Status</title>
  <style>
    /* ... Your existing styles ... */
  </style>
  <!-- Include Tailwind CSS -->
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>
<%-- Assuming 'user' is set in session if logged in. Redirect if not set. --%>
<%-- Your existing script and navbar includes --%>
<div id="messageContainer" class="hidden message">
  <!-- This will be filled with the message from the server -->
</div>
<a href="dashboard.jsp">Return to Dashboard</a>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    var statusContainer = document.getElementById('userCreationStatus');

    document.getElementById('createUserForm').addEventListener('submit', function(e) {
      e.preventDefault(); // Prevent the form from submitting through the browser

      var form = this;
      var formData = new FormData(form);

      fetch(form.action, {
        method: 'POST',
        body: formData
      })
              .then(response => response.json()) // Assuming your servlet returns JSON
              .then(data => {
                if (data.success) {
                  statusContainer.innerHTML = `<div class="message success">${data.message}</div>`;
                } else {
                  statusContainer.innerHTML = `<div class="message error">${data.message}</div>`;
                }
                statusContainer.classList.remove('hidden');
              })
              .catch(error => {
                statusContainer.innerHTML = `<div class="message error">Error: ${error}</div>`;
                statusContainer.classList.remove('hidden');
              });
    });
  });
</script>


</body>
</html>
