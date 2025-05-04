package com.example.employeemanagement.functions.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUpdateService {
    // Database connection
    private static final String DB_URL = "jdbc:mysql://database-1.c1qc88ayc19c.us-east-2.rds.amazonaws.com:3306/";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "avihan123";

    public String updateEmployee(String empid, String columnToUpdate, String newValue) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    
        // Validate columnToUpdate input
        String[] validColumns = {"Fname", "Lname", "email", "SSN"};
        boolean isValidColumn = Arrays.stream(validColumns)
                                   .anyMatch(col -> col.equalsIgnoreCase(columnToUpdate));
        
        if (!isValidColumn) {
            throw new IllegalArgumentException("Invalid column to update.");
        }

        String query = "UPDATE employees SET " + columnToUpdate + " = ? WHERE empid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setString(2, empid);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Employee information updated successfully.";
            } else {
                return "No employee found with empid: " + empid;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}