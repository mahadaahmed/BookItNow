<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="admin-panel flex flex-col gap-6">
    <!-- Section for Admin Panel Heading -->
    <h2 class="text-2xl font-bold text-gray-800">Create Items</h2>

    <!-- Section for Create New Booking List -->
    <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
        <div class="flex flex-wrap gap-6">
            <!-- Create New Booking List Card -->
            <div class="p-4 bg-white shadow-md rounded-lg flex-1">
                <h3 class="text-lg font-semibold text-gray-700">Create New Booking List</h3>
                <a href="createList.jsp" class="inline-block mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Create New Booking List</a>
            </div>


            <!-- Create New User Form -->
            <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">Create New User</h3>
                <form action="CreateUserServlet" method="post" class="space-y-3">
                    <div>
                        <label for="username" class="block text-sm font-medium text-gray-700">Username:</label>
                        <input type="text" id="username" name="username" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
                    </div>
                    <div>
                        <label for="password" class="block text-sm font-medium text-gray-700">Password:</label>
                        <input type="password" id="password" name="password" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:border-indigo-500 focus:ring-indigo-500" />
                    </div>
                    <button type="submit" class="w-full px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-opacity-50">Create User</button>
                </form>
            </div>
    </div>

        <!--%@ include file="/courses.jsp" %-->
    <!--%@ include file="/bookings.jsp" %-->


    <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
        <!-- Add Booking for User -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Add Booking for User</h3>
            <form action="AdminAddBooking" method="post" class="space-y-4">
                <div>
                    <label for="userSelect" class="block text-sm font-medium text-gray-700">Select User:</label>
                    <select id="userSelect" name="userId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                        <c:forEach items="${userDao.allUsers}" var="tempUser">
                            <option value="${tempUser.id}">${tempUser.username}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label for="listSelect" class="block text-sm font-medium text-gray-700">Select List:</label>
                    <select id="listSelect" name="listId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                        <c:forEach items="${listDao.allAvailableLists}" var="list">
                            <option value="${list.id}">${list.description}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label for="sequenceInput" class="block text-sm font-medium text-gray-700">Select Sequence:</label>
                    <input type="number" id="sequenceInput" name="sequence" required min="0" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                </div>

                <div class="flex justify-end">
                    <input type="submit" value="Add Booking" class="cursor-pointer inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                </div>
            </form>
        </div>


        <!-- Remove Booking for User -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Remove Booking for User</h3>

            <c:forEach items="${allReservations}" var="reservation">
                <div class="mb-4 p-4 bg-gray-50 rounded-lg flex items-center justify-between">
                    <c:set var="emailUsername" value="${fn:substringBefore(reservation.username, '@')}" />
                    <c:set var="firstChar" value="${fn:substring(emailUsername, 0, 1)}" />
                    <c:set var="remainingChars" value="${fn:substring(emailUsername, 1, fn:length(emailUsername))}" />
                    <c:set var="capitalizedUsername" value="${fn:toUpperCase(firstChar)}${remainingChars}" />
                    <span class="text-sm font-medium text-gray-700">
                    Username: ${capitalizedUsername},
                    List ID: ${reservation.listId},
                    Sequence: ${reservation.sequence}
                </span>
                    <form action="AdminRemoveBooking" method="post" class="inline-flex">
                        <input type="hidden" name="bookingId" value="${reservation.id}">
                        <button type="submit" class="text-white bg-red-600 hover:bg-red-700 focus:ring-red-500 focus:ring-offset-red-200 text-sm rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-offset-2">
                            Remove Booking
                        </button>
                    </form>
                </div>
            </c:forEach>
        </div>


        <!-- Booking Lists -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Booking Lists</h3>

            <c:forEach items="${listDao.getListsByAdmin(user.id)}" var="list">
                <div class="mb-4 p-4 bg-gray-50 rounded-lg flex items-center justify-between">
                    <span class="text-sm font-medium text-gray-700">${list.description} - ${list.location}</span>
                    <div class="flex gap-2">
                        <form action="EditBookingList" method="post" class="inline-flex">
                            <input type="hidden" name="listId" value="${list.id}" />
                            <button type="submit" class="px-4 py-2 text-sm font-medium text-blue-700 bg-blue-100 hover:bg-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                Edit
                            </button>
                        </form>
                        <form action="DeleteBookingList" method="post" class="inline-flex">
                            <input type="hidden" name="listId" value="${list.id}" />
                            <button type="submit" class="px-4 py-2 text-sm font-medium text-red-700 bg-red-100 hover:bg-red-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500" onclick="return confirm('Are you sure you want to delete this list?');">
                                Delete
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

<div class="bg-white p-6 rounded-lg shadow-lg mb-6">
    <!-- User Selection Form -->
    <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Grant User Access to a Course</h3>
        <form action="GrantCourseAccess" method="post" class="space-y-4">
            <div>
                <label for="user" class="block text-sm font-medium text-gray-700">User:</label>
                <select id="user" name="userId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                    <c:forEach items="${allUsers}" var="tempUser">
                        <c:if test="${!tempUser.admin}">
                            <option value="${tempUser.id}">${tempUser.username}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label for="course" class="block text-sm font-medium text-gray-700">Course:</label>
                <select id="course" name="courseId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                    <c:forEach items="${allCourses}" var="course">
                        <option value="${course.id}">${course.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="flex justify-end">
                <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    Grant Access
                </button>
            </div>
        </form>
    </div>

    <!-- Grant Access Section -->
    <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Grant Course Access</h3>
        <c:choose>
            <c:when test="${not empty param.selectedCourseId and not empty userDao.getUsersWithoutAccess(param.selectedCourseId)}">
                <form action="GrantCourseAccess" method="post" class="space-y-4">
                    <div>
                        <label for="userSelects" class="block text-sm font-medium text-gray-700">Select User:</label>
                        <select id="userSelects" name="userId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md shadow-sm">
                            <c:forEach items="${userDao.getUsersWithoutAccess(param.selectedCourseId)}" var="userItem">
                                <option value="${userItem.id}">${userItem.username}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <input type="hidden" name="courseId" value="${param.selectedCourseId}">
                    <div class="flex justify-end">
                        <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                            Grant Access
                        </button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <p class="text-gray-600">No users available or no course selected.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
    <!-- More admin functionalities can be added here -->
</div>

