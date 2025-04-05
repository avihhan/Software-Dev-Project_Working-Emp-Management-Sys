package com.example.employeemanagement.functions;
import java.sql.*;

public class EmployeeUpdateSalary {

    // Function to update employee salary based on empid and a salary range
    public static String updateSalary(String accessType, int empid, double percentageIncrease) {
        // MySQL Database Credentials
        String url = "jdbc:mysql://34.29.27.190/employeedata"; // Replace with your instance IP
        String user = "root"; // Replace with your username
        String password = "root"; // Replace with your actual password

        // Check if the user has admin access
        if (!"admin".equalsIgnoreCase(accessType)) {
            return "Access Denied: You do not have admin privileges.";
        }

        // Define the salary range for update: >= 58K and < 105K
        double lowerSalaryLimit = 58000;
        double upperSalaryLimit = 105000;

        // Query to get the current salary of the employee
        String querySelect = "SELECT salary FROM employees WHERE empid = ?";
        String queryUpdate = "UPDATE employees SET salary = ? WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmtSelect = conn.prepareStatement(querySelect);
             PreparedStatement stmtUpdate = conn.prepareStatement(queryUpdate)) {

            // Fetch the current salary of the employee
            stmtSelect.setInt(1, empid);
            ResultSet rs = stmtSelect.executeQuery();

            if (!rs.next()) {
                return "Employee not found with empid: " + empid;
            }

            double currentSalary = rs.getDouble("salary");

            // Check if the salary is within the specified range
            if (currentSalary >= lowerSalaryLimit && currentSalary < upperSalaryLimit) {
                // Calculate the new salary after the percentage increase
                double newSalary = currentSalary + (currentSalary * (percentageIncrease / 100));

                // Update the salary in the database
                stmtUpdate.setDouble(1, newSalary);
                stmtUpdate.setInt(2, empid);
                int rowsUpdated = stmtUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    return "Salary updated successfully for empid " + empid + ". New salary: " + newSalary;
                } else {
                    return "Error: Failed to update the salary.";
                }
            } else {
                return "Salary does not fall within the specified range (58K - 105K). No update performed.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        // Example Usage:
        // Admin updates salary for empid 123 with a 3.2% increase
        String result = updateSalary("admin", 1, 3.2);
        System.out.println(result);
    }
}
