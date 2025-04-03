import java.sql.*;

public class EmployeeUpdate {

    // Function to update employee information
    public static String updateEmployee(String accessType, String empid, String columnToUpdate, String newValue) {
        // MySQL Database Credentials
        String url = "jdbc:mysql://34.29.27.190/employeedata"; // Replace with your instance IP
        String user = "root"; // Replace with your username
        String password = "root"; // Replace with your actual password

        // Check if the user has admin access
        if (!"admin".equalsIgnoreCase(accessType)) {
            return "Access Denied: You do not have admin privileges.";
        }

        // Validate columnToUpdate input to prevent SQL injection and ensure empid cannot be updated
        String[] validColumns = {"Fname", "Lname", "email", "SSN"};
        boolean isValidColumn = false;
        for (String col : validColumns) {
            if (col.equalsIgnoreCase(columnToUpdate)) {
                isValidColumn = true;
                break;
            }
        }
        if (!isValidColumn) {
            return "Invalid column to update.";
        }

        // Query to update the employee's information
        String query = "UPDATE employees SET " + columnToUpdate + " = ? WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the new value for the column and the employee id
            stmt.setString(1, newValue);
            stmt.setString(2, empid);

            // Execute the update query
            int rowsAffected = stmt.executeUpdate();

            // Check if any rows were updated
            if (rowsAffected > 0) {
                return "Employee information updated successfully.";
            } else {
                return "No employee found with empid: " + empid;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        // Example Usage:
        // Trying to update the 'email' of employee with empid 1001
        String result = updateEmployee("admin", "0", "email", "newemail@example.com");
        System.out.println(result);

        // Trying to update the 'Fname' of employee with empid 1002
        String result2 = updateEmployee("admin", "1", "Fname", "John");
        System.out.println(result2);
    }
}