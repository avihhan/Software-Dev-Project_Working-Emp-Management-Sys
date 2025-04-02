import java.sql.*;
import java.util.Scanner;

public class EmployeeLoginSystem {
    // Database connection
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeeData";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Your_Password";
    
    // Company name for password pattern
    private static final String company_name = "company";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== EMPLOYEE LOGIN SYSTEM ===");
        
        // Login prompt
        System.out.print("Enter Employee ID: ");
        String empid = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        // Check credentials
        if (authenticate(empid, password)) {
            System.out.println("\nLogin successful! Welcome, Employee " + empid);
            
            // After successful login, automatically display employee data
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                EmployeeDataDisplay.displayEmployeeDetails(conn, empid);
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("\nInvalid credentials. Access denied.");
        }
        
        scanner.close();
    }
    
    private static boolean authenticate(String empid, String password) {
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
}