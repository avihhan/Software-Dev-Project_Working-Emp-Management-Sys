package com.example.employeemanagement.functions;
import java.sql.*;

public class EmployeeSearch {

    // Function to search employees based on criteria
    public static String searchEmployee(String accessType, String searchBy, String searchValue) {
        // MySQL Database Credentials
        String url = "jdbc:mysql://34.29.27.190/employeedata"; // Replace with your instance IP
        String user = "root"; // Replace with your username
        String password = "root"; // Replace with your actual password

        // Check if the user has admin access
        if (!"admin".equalsIgnoreCase(accessType)) {
            return "Access Denied: You do not have admin privileges.";
        }

        // Validate searchBy input to prevent SQL injection
        String[] validColumns = {"empid", "Fname", "Lname", "email", "SSN"};
        boolean isValidColumn = false;
        for (String col : validColumns) {
            if (col.equalsIgnoreCase(searchBy)) {
                isValidColumn = true;
                break;
            }
        }
        if (!isValidColumn) {
            return "Invalid search parameter.";
        }

        // Query execution
        String query = "SELECT * FROM employees WHERE " + searchBy + " = ?";
        StringBuilder result = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, searchValue);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("empid"))
                      .append(", Name: ").append(rs.getString("Fname")).append(" ").append(rs.getString("Lname"))
                      .append(", Email: ").append(rs.getString("email"))
                      .append(", SSN: ").append(rs.getString("SSN")).append("\n");
            }

            if (result.length() == 0) {
                return "No employee found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }

        return result.toString();
    }

    public static void main(String[] args) {
        // Example Usage:
        String result = searchEmployee("admin", "email", "Bruce.Wayne@example.com");
        System.out.println(result);
        String result2 = searchEmployee("admin", "empid", "0");
        System.out.println(result2);
    }
}
