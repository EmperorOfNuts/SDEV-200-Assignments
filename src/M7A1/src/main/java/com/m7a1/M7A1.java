package com.m7a1;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.text.NumberFormat;

@WebServlet(name = "LoanServlet", value = "/M7A1")
public class M7A1 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get form parameters
        String loanAmountStr = request.getParameter("loanAmount");
        String interestRateStr = request.getParameter("interestRate");
        String yearsStr = request.getParameter("years");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Parse input values
            double loanAmount = Double.parseDouble(loanAmountStr);
            double annualInterestRate = Double.parseDouble(interestRateStr);
            int numberOfYears = Integer.parseInt(yearsStr);

            // Create Loan object and calculate payments
            Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
            double monthlyPayment = loan.getMonthlyPayment();
            double totalPayment = loan.getTotalPayment();

            // Format and display everything
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>M7A1 - Loan Results</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 30px; background-color: #f5f5f5; }");
            out.println(".container { max-width: 500px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println("h2 { color: #333; text-align: center; }");
            out.println(".result-row { margin: 15px 0; padding: 10px; border-bottom: 1px solid #eee; }");
            out.println(".label { font-weight: bold; display: inline-block; width: 180px; color: #555; }");
            out.println(".value { color: #2c3e50; font-size: 16px; }");
            out.println("a { display: inline-block; margin-top: 20px; padding: 10px 25px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; }");
            out.println("a:hover { background-color: #45a049; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h2>M7A1 - Loan Payment Results</h2>");
            out.println("<div class='result-row'><span class='label'>Loan Amount:</span> <span class='value'>" + currencyFormat.format(loanAmount) + "</span></div>");
            out.println("<div class='result-row'><span class='label'>Annual Interest Rate:</span> <span class='value'>" + annualInterestRate + "%</span></div>");
            out.println("<div class='result-row'><span class='label'>Number of Years:</span> <span class='value'>" + numberOfYears + "</span></div>");
            out.println("<div class='result-row'><span class='label'>Monthly Payment:</span> <span class='value'>" + currencyFormat.format(monthlyPayment) + "</span></div>");
            out.println("<div class='result-row'><span class='label'>Total Payment:</span> <span class='value'>" + currencyFormat.format(totalPayment) + "</span></div>");
            out.println("<a href='M7A1.html'>Calculate Another Loan</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Error</title>");
            out.println("<style>body { font-family: Arial; margin: 30px; }</style>");
            out.println("</head><body>");
            out.println("<h2>Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='M7A1.html'>Try Again</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { response.sendRedirect("M7A1.html"); }
}