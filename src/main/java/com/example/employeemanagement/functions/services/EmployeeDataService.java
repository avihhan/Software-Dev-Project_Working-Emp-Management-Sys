package com.example.employeelogin.services;

import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeDataService {
    
    private static final String DB_URL = "jdbc:mysql://34.29.27.190:3306/employeedata";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public Map<String, Object> getEmployeeDetails(String empid) {
        Map<String, Object> employeeDetails = new HashMap<>();
        
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

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employeeDetails.put("employeeId", rs.getString("empid"));
                employeeDetails.put("firstName", rs.getString("Fname"));
                employeeDetails.put("lastName", rs.getString("Lname"));
                employeeDetails.put("email", rs.getString("email"));
                employeeDetails.put("hireDate", rs.getDate("HireDate"));
                employeeDetails.put("salary", rs.getDouble("Salary"));
                employeeDetails.put("ssn", maskSSN(rs.getString("SSN")));
                employeeDetails.put("division", rs.getString("DivisionName"));
                employeeDetails.put("jobTitle", rs.getString("JobTitle"));
                employeeDetails.put("isAdmin", rs.getString("IsAdmin"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        
        return employeeDetails;
    }

    private String maskSSN(String ssn) {
        if (ssn == null || ssn.length() < 4) return "*****";
        return "***-**-" + ssn.substring(ssn.length()-4);
    }
}
