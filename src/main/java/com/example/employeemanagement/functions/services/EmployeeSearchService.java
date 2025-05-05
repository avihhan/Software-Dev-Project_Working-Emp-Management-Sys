package com.example.employeemanagement.functions.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSearchService {
    // Database connection
    private static final String DB_URL = "jdbc:mysql://database-1.c1qc88ayc19c.us-east-2.rds.amazonaws.com:3306/";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "avihan123";
    
    public List<Map<String, Object>> searchEmployees(String searchBy, String searchValue) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        List<Map<String, Object>> results = new ArrayList<>();
        
        // Validate searchBy input to prevent SQL injection
        String[] validColumns = {"empid", "Fname", "Lname", "email", "SSN"};
        boolean isValidColumn = Arrays.stream(validColumns)
                                   .anyMatch(col -> col.equalsIgnoreCase(searchBy));
        
        if (!isValidColumn) {
            throw new IllegalArgumentException("Invalid search parameter.");
        }

        String query = "SELECT * FROM employees WHERE " + searchBy + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, searchValue);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> employee = new HashMap<>();
                employee.put("id", rs.getInt("empid"));
                employee.put("firstName", rs.getString("Fname"));
                employee.put("lastName", rs.getString("Lname"));
                employee.put("email", rs.getString("email"));
                employee.put("ssn", rs.getString("SSN"));
                results.add(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }

        return results;
    }
}