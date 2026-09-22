package jdbc.vehicle_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class CustomerOperation {

    public static void registerCustomer(String name, String phone,
            String email, String licenseNumber, String password) {

        String sql = "INSERT INTO customer "
                + "(name, phone, email, license_number, password) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, licenseNumber);
            ps.setString(5, password);

            ps.executeUpdate();

            System.out.println("Customer registered successfully.");

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Customer registration failed.");
            e.printStackTrace();
        }
    }

    public static int customerLogin(String email, String password) {

        String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int customerId = rs.getInt("customer_id");

                System.out.println("Customer Login Successful!");
                System.out.println("Customer ID: " + customerId);

                rs.close();
                ps.close();
                con.close();

                return customerId;

            } else {

                System.out.println("Invalid email or password.");

                rs.close();
                ps.close();
                con.close();

                return 0;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;
        }
    }
    
    public static void viewAllCustomers() {

        String sql = "SELECT * FROM customer";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== ALL CUSTOMERS ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Customer ID    : "
                        + rs.getInt("customer_id"));

                System.out.println("Name           : "
                        + rs.getString("name"));

                System.out.println("Phone          : "
                        + rs.getString("phone"));

                System.out.println("Email          : "
                        + rs.getString("email"));

                System.out.println("License Number : "
                        + rs.getString("license_number"));

                System.out.println("-----------------------------------");
            }

            if (!found) {

                System.out.println("No customers found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public static void searchCustomer(Scanner sc) {

        System.out.println();
        System.out.println("========== SEARCH CUSTOMER ==========");
        System.out.println("1. Search by Customer ID");
        System.out.println("2. Search by Phone");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();

        if (choice == 1) {

            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt();

            String sql = "SELECT * FROM customer WHERE customer_id = ?";

            try {

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, customerId);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    System.out.println();
                    System.out.println("Customer ID     : " + rs.getInt("customer_id"));
                    System.out.println("Name            : " + rs.getString("name"));
                    System.out.println("Phone           : " + rs.getString("phone"));
                    System.out.println("Email           : " + rs.getString("email"));
                    System.out.println("License Number  : " + rs.getString("license_number"));

                } else {

                    System.out.println("Customer not found.");
                }

                rs.close();
                ps.close();
                con.close();

            } catch (SQLException e) {

                System.out.println("Unable to search customer.");
                e.printStackTrace();
            }

        } else if (choice == 2) {

            sc.nextLine();

            System.out.print("Enter Phone Number: ");
            String phone = sc.nextLine();

            String sql = "SELECT * FROM customer WHERE phone = ?";

            try {

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, phone);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    System.out.println();
                    System.out.println("Customer ID     : " + rs.getInt("customer_id"));
                    System.out.println("Name            : " + rs.getString("name"));
                    System.out.println("Phone           : " + rs.getString("phone"));
                    System.out.println("Email           : " + rs.getString("email"));
                    System.out.println("License Number  : " + rs.getString("license_number"));

                } else {

                    System.out.println("Customer not found.");
                }

                rs.close();
                ps.close();
                con.close();

            } catch (SQLException e) {

                System.out.println("Unable to search customer.");
                e.printStackTrace();
            }

        } else {

            System.out.println("Invalid choice.");
        }
    }
    
    public static void deleteCustomer(Scanner sc) {

        System.out.print("Enter Customer ID to delete: ");
        int customerId = sc.nextInt();

        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer not found.");
            }

            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Customer cannot be deleted.");
            e.printStackTrace();
        }
    }
    
    public static void updateCustomer(Scanner sc) {

        System.out.println();
        System.out.println("========== UPDATE CUSTOMER ==========");

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        // First check whether customer exists
        String checkSql = "SELECT * FROM customer WHERE customer_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, customerId);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {

                System.out.println("Customer not found.");

                rs.close();
                checkPs.close();
                con.close();

                return;
            }

            System.out.println();
            System.out.println("Current Customer Details");
            System.out.println("----------------------------------");
            System.out.println("Name           : " + rs.getString("name"));
            System.out.println("Phone          : " + rs.getString("phone"));
            System.out.println("Email          : " + rs.getString("email"));
            System.out.println("License Number : " + rs.getString("license_number"));
            System.out.println("----------------------------------");

            rs.close();
            checkPs.close();

            // Get new details
            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            System.out.print("Enter New Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter New Email: ");
            String email = sc.nextLine();

            System.out.print("Enter New License Number: ");
            String licenseNumber = sc.nextLine();

            String updateSql = "UPDATE customer SET name = ?, phone = ?, "
                    + "email = ?, license_number = ? "
                    + "WHERE customer_id = ?";

            PreparedStatement ps = con.prepareStatement(updateSql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, licenseNumber);
            ps.setInt(5, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println();
                System.out.println("Customer details updated successfully.");
            } else {
                System.out.println("Customer details were not updated.");
            }

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to update customer details.");
            e.printStackTrace();
        }
    }
    
    public static void forgotPassword(Scanner sc) {

        System.out.println();
        System.out.println("========== FORGOT PASSWORD ==========");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        String checkSql = "SELECT customer_id FROM customer "
                + "WHERE email = ? AND phone = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setString(1, email);
            checkPs.setString(2, phone);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {

                System.out.println("Email and Phone Number do not match.");

                rs.close();
                checkPs.close();
                con.close();

                return;
            }

            int customerId = rs.getInt("customer_id");

            rs.close();
            checkPs.close();

            System.out.print("Enter New Password: ");
            String newPassword = sc.nextLine();

            System.out.print("Confirm New Password: ");
            String confirmPassword = sc.nextLine();

            if (!newPassword.equals(confirmPassword)) {

                System.out.println("Passwords do not match.");

                con.close();

                return;
            }

            String updateSql = "UPDATE customer SET password = ? "
                    + "WHERE customer_id = ?";

            PreparedStatement updatePs = con.prepareStatement(updateSql);

            updatePs.setString(1, newPassword);
            updatePs.setInt(2, customerId);

            int rows = updatePs.executeUpdate();

            if (rows > 0) {

                System.out.println();
                System.out.println("Password updated successfully.");

            } else {

                System.out.println("Password update failed.");
            }

            updatePs.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to reset password.");
            e.printStackTrace();
        }
    }
}