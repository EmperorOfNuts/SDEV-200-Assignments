<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Addition Quiz - M7A2</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }
        .quiz-item {
            margin: 10px 0;
            padding: 5px;
            border-bottom: 1px solid #eee;
        }
        input[type="text"] {
            width: 60px;
            padding: 5px;
            margin-left: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        input[type="submit"] {
            display: block;
            width: 100%;
            padding: 10px;
            margin: 20px 0 10px 0;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .note {
            text-align: center;
            color: #777;
            font-size: 12px;
            margin-top: 20px;
        }
        .refresh-note {
            text-align: center;
            color: #0066cc;
            font-size: 14px;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Addition Quiz - M7A2</h2>

    <form action="M7A2_answer.jsp" method="post">
        <%
            // Generate 10 random addition problems
            int[][] problems = new int[10][2];
            for (int i = 0; i < 10; i++) {
                problems[i][0] = (int)(Math.random() * 30) + 1;  // Random number 1-30
                problems[i][1] = (int)(Math.random() * 30) + 1;  // Random number 1-30
            }
            session.setAttribute("problems", problems);

            // Display each problem with an input field
            for (int i = 0; i < 10; i++) {
        %>
        <div class="quiz-item">
            <%= problems[i][0] %> + <%= problems[i][1] %> =
            <input type="text" name="answer<%= i %>" size="5" required>
        </div>
        <%
            }
        %>

        <input type="submit" value="Submit">
    </form>

    <div class="refresh-note">
        Click the browser's Refresh button to get a new quiz.
    </div>

    <div class="note">
        Module 7 Assignment 2
    </div>
</div>
</body>
</html>