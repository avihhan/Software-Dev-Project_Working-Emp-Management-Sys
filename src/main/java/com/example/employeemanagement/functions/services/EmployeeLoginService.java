package com.example.employeemanagement.functions.services;

import java.sql.*;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLoginService {
    // Database connection
    private static final String DB_URL = "jdbc:mysql://34.29.27.190:3306/employeedata";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String company_name = "company";

    public boolean authenticate(String empid, String password) {
        // Check password pattern first (company_name_pw + empid)
        String expectedPassword = company_name + "_pw" + empid;
        if (!password.equals(expectedPassword)) {
            return false;
        }
        
        // Verify empid exists in database
        String sql = "SELECT empid FROM employees WHERE empid = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empid);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next(); // Returns true if empid exists
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String empid) {
        String sql = "SELECT admin FROM employees WHERE empid = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Returns true if admin column equals 1
                return rs.getInt("admin") == 1;
            }
            return false;
            
        } catch (SQLException e) {
            throw new RuntimeException("Database error while checking admin status: " + e.getMessage());
        }
    }
}
