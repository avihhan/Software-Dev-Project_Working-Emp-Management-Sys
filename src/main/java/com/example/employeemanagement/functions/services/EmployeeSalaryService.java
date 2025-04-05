package com.example.employeemanagement.functions.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryService {
    // Database connection
    private static final String DB_URL = "jdbc:mysql://34.29.27.190:3306/employeedata";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public String updateSalary(int empid, double percentageIncrease) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    
        double lowerSalaryLimit = 58000;
        double upperSalaryLimit = 105000;

        String querySelect = "SELECT salary FROM employees WHERE empid = ?";
        String queryUpdate = "UPDATE employees SET salary = ? WHERE empid = ?";

        try (PreparedStatement stmtSelect = connection.prepareStatement(querySelect);
             PreparedStatement stmtUpdate = connection.prepareStatement(queryUpdate)) {

            // Fetch current salary
            stmtSelect.setInt(1, empid);
            ResultSet rs = stmtSelect.executeQuery();

            if (!rs.next()) {
                return "Employee not found with empid: " + empid;
            }

            double currentSalary = rs.getDouble("salary");

            // Check salary range
            if (currentSalary >= lowerSalaryLimit && currentSalary < upperSalaryLimit) {
                double newSalary = currentSalary + (currentSalary * (percentageIncrease / 100));

                // Update salary
                stmtUpdate.setDouble(1, newSalary);
                stmtUpdate.setInt(2, empid);
                int rowsUpdated = stmtUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    return String.format("Salary updated successfully for empid %d. New salary: %.2f", empid, newSalary);
                } else {
                    return "Error: Failed to update the salary.";
                }
            } else {
                return "Salary does not fall within the specified range (58K - 105K). No update performed.";
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}