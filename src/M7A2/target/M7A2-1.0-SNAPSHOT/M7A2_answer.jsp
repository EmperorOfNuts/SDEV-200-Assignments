<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Addition Quiz Answer - M7A2</title>
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
        .result-item {
            margin: 8px 0;
            padding: 5px;
            border-bottom: 1px solid #eee;
        }
        .correct {
            color: green;
            font-weight: bold;
        }
        .wrong {
            color: red;
            font-weight: bold;
        }
        .total {
            margin-top: 20px;
            padding: 10px;
            background-color: #e8f5e8;
            border-radius: 4px;
            text-align: center;
            font-size: 18px;
            font-weight: bold;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #0066cc;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Addition Quiz Answer - M7A2</h2>

    <%
        // Retrieve the problems from session
        int[][] problems = (int[][]) session.getAttribute("problems");
        int correctCount = 0;

        if (problems != null) {
            for (int i = 0; i < problems.length; i++) {
                int num1 = problems[i][0];
                int num2 = problems[i][1];
                int correctAnswer = num1 + num2;

                // Get user's answer
                String answerStr = request.getParameter("answer" + i);
                int userAnswer = 0;
                boolean isCorrect = false;

                try {
                    userAnswer = Integer.parseInt(answerStr);
                    isCorrect = (userAnswer == correctAnswer);
                    if (isCorrect) correctCount++;
                } catch (NumberFormatException e) {
                    isCorrect = false;
                }
    %>
    <div class="result-item">
        <%= num1 %> + <%= num2 %> = <%= userAnswer %>
        <span class="<%= isCorrect ? "correct" : "wrong" %>">
                            <%= isCorrect ? "Correct" : "Wrong" %>
                        </span>
    </div>
    <%
            }
        } else {
            // Redirect to Quiz
            response.sendRedirect("M7A2.jsp");
        }
    %>

    <div class="total">
        The total correct count is <%= correctCount %>
    </div>

    <a href="M7A2.jsp" class="back-link">Take another quiz</a>
</div>
</body>
</html>