package com.example.employeemanagement.functions;
import java.sql.*;

public class EmployeeDataDisplay {
    public static void displayEmployeeDetails(Connection conn, String empid) throws SQLException {
        // Query to get employee details
        String sql = "SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, e.SSN, " +
                     "COALESCE(d.Name, 'N/A') AS DivisionName, " +
                     "COALESCE(jt.job_title, 'N/A') AS JobTitle, " +
                     "CASE WHEN e.admin = 1 THEN 'Yes' ELSE 'No' END AS IsAdmin " +
                     "FROM employees e " +
                     "LEFT JOIN employee_division ed ON e.empid = ed.empid " +
                     "LEFT JOIN division d ON ed.div_ID = d.ID " +
                     "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                     "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                     "WHERE e.empid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n=== YOUR PROFILE ===");
                System.out.println("----------------------------------------");
                System.out.printf("%-15s: %s\n", "Employee ID", rs.getString("empid"));
                System.out.printf("%-15s: %s %s\n", "Name", rs.getString("Fname"), rs.getString("Lname"));
                System.out.printf("%-15s: %s\n", "Email", rs.getString("email"));
                System.out.printf("%-15s: %s\n", "Hire Date", rs.getDate("HireDate"));
                System.out.printf("%-15s: $%,.2f\n", "Salary", rs.getDouble("Salary"));
                System.out.printf("%-15s: %s\n", "SSN", maskSSN(rs.getString("SSN")));
                System.out.printf("%-15s: %s\n", "Division", rs.getString("DivisionName"));
                System.out.printf("%-15s: %s\n", "Job Title", rs.getString("JobTitle"));
                System.out.printf("%-15s: %s\n", "Admin Status", rs.getString("IsAdmin"));
                System.out.println("----------------------------------------\n");
            } else {
                System.out.println("No employee found with ID: " + empid);
            }
        }
    }

    private static String maskSSN(String ssn) {
        if (ssn == null || ssn.length() < 4) return "*****";
        return "***-**-" + ssn.substring(ssn.length()-4);
    }
}