package jdbc.vehicle_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PaymentOperation {

	public static void makePayment(Scanner sc, int customerId) {

	    System.out.print("Enter Rental ID: ");

	    int rentalId = sc.nextInt();
	    sc.nextLine();

	    String sql = "SELECT r.rental_id, r.total_amount, r.status "
	            + "FROM rental r "
	            + "WHERE r.rental_id = ? "
	            + "AND r.customer_id = ?";

	    try {

	        Connection con = DBConnection.getConnection();

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setInt(1, rentalId);
	        ps.setInt(2, customerId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            double amount = rs.getDouble("total_amount");

	            String rentalStatus = rs.getString("status");

	            System.out.println();
	            System.out.println("========== PAYMENT ==========");
	            System.out.println("Rental ID    : " + rentalId);
	            System.out.println("Amount       : " + amount);
	            System.out.println("Rental Status: " + rentalStatus);

	          
	            if (!rentalStatus.equals("COMPLETED")) {

	                System.out.println();
	                System.out.println(
	                        "Payment can be made after returning the vehicle.");

	                rs.close();
	                ps.close();
	                con.close();

	                return;
	            }

	          

	            String checkPaymentSql =
	                    "SELECT payment_id FROM payment WHERE rental_id = ?";

	            PreparedStatement checkPs =
	                    con.prepareStatement(checkPaymentSql);

	            checkPs.setInt(1, rentalId);

	            ResultSet paymentRs =
	                    checkPs.executeQuery();

	            if (paymentRs.next()) {

	                System.out.println();
	                System.out.println(
	                        "Payment already completed for this rental.");

	                System.out.println(
	                        "Payment ID: "
	                        + paymentRs.getInt("payment_id"));

	                paymentRs.close();
	                checkPs.close();
	                rs.close();
	                ps.close();
	                con.close();

	                return;
	            }

	            paymentRs.close();
	            checkPs.close();

	       

	            System.out.println();
	            System.out.println("Select Payment Method:");
	            System.out.println("1. CASH");
	            System.out.println("2. UPI");
	            System.out.println("3. CARD");

	            System.out.print("Enter your choice: ");

	            int choice = sc.nextInt();
	            sc.nextLine();

	            String paymentMethod = "";

	            if (choice == 1) {

	                paymentMethod = "CASH";

	            } else if (choice == 2) {

	                paymentMethod = "UPI";

	            } else if (choice == 3) {

	                paymentMethod = "CARD";

	            } else {

	                System.out.println("Invalid payment method.");

	                rs.close();
	                ps.close();
	                con.close();

	                return;
	            }

	          

	            String paymentSql =
	                    "INSERT INTO payment "
	                    + "(rental_id, amount, payment_method, "
	                    + "payment_status, payment_date) "
	                    + "VALUES (?, ?, ?, ?, CURDATE())";

	            PreparedStatement paymentPs =
	                    con.prepareStatement(paymentSql);

	            paymentPs.setInt(1, rentalId);
	            paymentPs.setDouble(2, amount);
	            paymentPs.setString(3, paymentMethod);
	            paymentPs.setString(4, "PAID");

	            paymentPs.executeUpdate();

	            System.out.println();
	            System.out.println("Payment successful!");
	            System.out.println("Payment Amount : " + amount);
	            System.out.println("Payment Method : "
	                    + paymentMethod);
	            System.out.println("Payment Status : PAID");

	            paymentPs.close();

	        } else {

	            System.out.println();
	            System.out.println(
	                    "Rental not found or does not belong to you.");
	        }

	        rs.close();
	        ps.close();
	        con.close();

	    } catch (SQLException e) {

	        System.out.println("Payment failed.");
	        e.printStackTrace();
	    }
	}
    
    
    public static void viewMyPayments(int customerId) {

        String sql = "SELECT p.payment_id, p.rental_id, "
                + "p.amount, p.payment_method, "
                + "p.payment_status, p.payment_date "
                + "FROM payment p "
                + "JOIN rental r ON p.rental_id = r.rental_id "
                + "WHERE r.customer_id = ? "
                + "ORDER BY p.payment_id DESC";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== MY PAYMENTS ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Payment ID     : "
                        + rs.getInt("payment_id"));

                System.out.println("Rental ID      : "
                        + rs.getInt("rental_id"));

                System.out.println("Amount         : "
                        + rs.getDouble("amount"));

                System.out.println("Payment Method : "
                        + rs.getString("payment_method"));

                System.out.println("Payment Status : "
                        + rs.getString("payment_status"));

                System.out.println("Payment Date   : "
                        + rs.getDate("payment_date"));

                System.out.println("--------------------------------");
            }

            if (!found) {

                System.out.println("No payments found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public static void viewAllPayments() {

        String sql = "SELECT p.payment_id, "
                + "c.name, "
                + "r.rental_id, "
                + "p.amount, "
                + "p.payment_method, "
                + "p.payment_status, "
                + "p.payment_date "
                + "FROM payment p "
                + "JOIN rental r ON p.rental_id = r.rental_id "
                + "JOIN customer c ON r.customer_id = c.customer_id";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== ALL PAYMENTS ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Payment ID       : "
                        + rs.getInt("payment_id"));

                System.out.println("Customer Name    : "
                        + rs.getString("name"));

                System.out.println("Rental ID        : "
                        + rs.getInt("rental_id"));

                System.out.println("Amount           : "
                        + rs.getDouble("amount"));

                System.out.println("Payment Method   : "
                        + rs.getString("payment_method"));

                System.out.println("Payment Status   : "
                        + rs.getString("payment_status"));

                System.out.println("Payment Date     : "
                        + rs.getDate("payment_date"));

                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No payments found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewPaidPayments() {

        System.out.println();
        System.out.println("========== PAID PAYMENTS ==========");

        String sql = "SELECT p.payment_id, c.name, p.rental_id, "
                + "p.amount, p.payment_method, p.payment_status, "
                + "p.payment_date "
                + "FROM payment p "
                + "JOIN rental r ON p.rental_id = r.rental_id "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "WHERE p.payment_status = 'PAID'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Payment ID       : " + rs.getInt("payment_id"));
                System.out.println("Customer Name    : " + rs.getString("name"));
                System.out.println("Rental ID        : " + rs.getInt("rental_id"));
                System.out.println("Amount           : " + rs.getDouble("amount"));
                System.out.println("Payment Method   : " + rs.getString("payment_method"));
                System.out.println("Payment Status   : " + rs.getString("payment_status"));
                System.out.println("Payment Date     : " + rs.getDate("payment_date"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No paid payments found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view paid payments.");
            e.printStackTrace();
        }
    }
    
    public static void viewPendingPayments() {

        System.out.println();
        System.out.println("========== PENDING PAYMENTS ==========");

        String sql = "SELECT p.payment_id, c.name, p.rental_id, "
                + "p.amount, p.payment_method, p.payment_status, "
                + "p.payment_date "
                + "FROM payment p "
                + "JOIN rental r ON p.rental_id = r.rental_id "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "WHERE p.payment_status = 'PENDING'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Payment ID       : " + rs.getInt("payment_id"));
                System.out.println("Customer Name    : " + rs.getString("name"));
                System.out.println("Rental ID        : " + rs.getInt("rental_id"));
                System.out.println("Amount           : " + rs.getDouble("amount"));
                System.out.println("Payment Method   : " + rs.getString("payment_method"));
                System.out.println("Payment Status   : " + rs.getString("payment_status"));
                System.out.println("Payment Date     : " + rs.getDate("payment_date"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No pending payments found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view pending payments.");
            e.printStackTrace();
        }
    }
    
    public static void viewPaymentByRentalId(Scanner sc) {

        System.out.println();
        System.out.println("========== PAYMENT BY RENTAL ID ==========");

        System.out.print("Enter Rental ID: ");
        int rentalId = sc.nextInt();

        String sql = "SELECT p.payment_id, c.name, p.rental_id, "
                + "p.amount, p.payment_method, p.payment_status, "
                + "p.payment_date "
                + "FROM payment p "
                + "JOIN rental r ON p.rental_id = r.rental_id "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "WHERE p.rental_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, rentalId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Payment ID       : " + rs.getInt("payment_id"));
                System.out.println("Customer Name    : " + rs.getString("name"));
                System.out.println("Rental ID        : " + rs.getInt("rental_id"));
                System.out.println("Amount           : " + rs.getDouble("amount"));
                System.out.println("Payment Method   : " + rs.getString("payment_method"));
                System.out.println("Payment Status   : " + rs.getString("payment_status"));
                System.out.println("Payment Date     : " + rs.getDate("payment_date"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No payment found for this Rental ID.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view payment.");
            e.printStackTrace();
        }
    }
}
