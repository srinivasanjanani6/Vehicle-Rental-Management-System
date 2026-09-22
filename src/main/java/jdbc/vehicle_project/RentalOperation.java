package jdbc.vehicle_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;
public class RentalOperation {

    public static void rentVehicle(Scanner sc, int customerId) {

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        String sql = "SELECT * FROM vehicle WHERE vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String status = rs.getString("status");

                if (status.equals("AVAILABLE")) {

                    double rentPerDay = rs.getDouble("rent_per_day");

                    System.out.println();
                    System.out.println("Vehicle is available.");

                    System.out.println("Vehicle Number : "
                            + rs.getString("vehicle_number"));

                    System.out.println("Brand          : "
                            + rs.getString("brand"));

                    System.out.println("Model          : "
                            + rs.getString("model"));

                    System.out.println("Rent Per Day   : "
                            + rentPerDay);

                    System.out.print("Enter Start Date (yyyy-mm-dd): ");
                    String startDate = sc.nextLine();

                    System.out.print("Enter Expected Return Date (yyyy-mm-dd): ");
                    String expectedReturnDate = sc.nextLine();

                    java.time.LocalDate start =
                            java.time.LocalDate.parse(startDate);

                    java.time.LocalDate end =
                            java.time.LocalDate.parse(expectedReturnDate);

                    long rentalDays =
                            java.time.temporal.ChronoUnit.DAYS
                            .between(start, end);

                    if (rentalDays <= 0) {

                        System.out.println(
                                "Return date must be after start date.");

                    } else {

                        double totalAmount =
                                rentalDays * rentPerDay;

                        System.out.println();
                        System.out.println("Rental Days  : " + rentalDays);
                        System.out.println("Total Amount : " + totalAmount);

                        int vehicleId =
                                rs.getInt("vehicle_id");

                        // START TRANSACTION
                        con.setAutoCommit(false);

                        try {

                            // INSERT RENTAL
                            String rentalSql =
                                    "INSERT INTO rental "
                                    + "(customer_id, vehicle_id, start_date, "
                                    + "expected_return_date, total_amount, status) "
                                    + "VALUES (?, ?, ?, ?, ?, ?)";

                            PreparedStatement rentalPs =
                                    con.prepareStatement(rentalSql);

                            rentalPs.setInt(1, customerId);
                            rentalPs.setInt(2, vehicleId);
                            rentalPs.setString(3, startDate);
                            rentalPs.setString(4, expectedReturnDate);
                            rentalPs.setDouble(5, totalAmount);
                            rentalPs.setString(6, "ACTIVE");

                            rentalPs.executeUpdate();

                           
                            String vehicleSql =
                                    "UPDATE vehicle SET status = 'RENTED' "
                                    + "WHERE vehicle_id = ?";

                            PreparedStatement vehiclePs =
                                    con.prepareStatement(vehicleSql);

                            vehiclePs.setInt(1, vehicleId);

                            vehiclePs.executeUpdate();

                           
                            con.commit();

                            System.out.println();
                            System.out.println(
                                    "Vehicle rented successfully!");

                            System.out.println(
                                    "Vehicle Status: RENTED");

                            rentalPs.close();
                            vehiclePs.close();

                        } catch (SQLException e) {

                            try {

                                con.rollback();

                            } catch (SQLException rollbackError) {

                                rollbackError.printStackTrace();
                            }

                            System.out.println("Rental failed.");
                            System.out.println(
                                    "Transaction rolled back.");

                            e.printStackTrace();
                        }

                        con.setAutoCommit(true);
                    }

                } else {

                    System.out.println("Vehicle is not available.");
                }

            } else {

                System.out.println("Vehicle not found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    
    
    
    public static void viewMyRentals(int customerId) {

        String sql = "SELECT r.rental_id, v.vehicle_number, v.brand, v.model, "
                + "r.start_date, r.expected_return_date, r.actual_return_date, "
                + "r.total_amount, r.status "
                + "FROM rental r "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE r.customer_id = ? "
                + "ORDER BY r.rental_id DESC";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== MY RENTALS ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Rental ID       : "
                        + rs.getInt("rental_id"));

                System.out.println("Vehicle Number  : "
                        + rs.getString("vehicle_number"));

                System.out.println("Brand           : "
                        + rs.getString("brand"));

                System.out.println("Model           : "
                        + rs.getString("model"));

                System.out.println("Start Date      : "
                        + rs.getDate("start_date"));

                System.out.println("Expected Return : "
                        + rs.getDate("expected_return_date"));

                System.out.println("Actual Return   : "
                        + rs.getDate("actual_return_date"));

                System.out.println("Total Amount    : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status          : "
                        + rs.getString("status"));

                System.out.println("--------------------------------");
            }

            if (!found) {

                System.out.println("No rentals found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public static void viewAllRentals() {

        String sql = "SELECT r.rental_id, "
                + "c.name, "
                + "v.vehicle_number, "
                + "v.brand, "
                + "v.model, "
                + "r.start_date, "
                + "r.expected_return_date, "
                + "r.actual_return_date, "
                + "r.total_amount, "
                + "r.status "
                + "FROM rental r "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== ALL RENTALS ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Rental ID          : "
                        + rs.getInt("rental_id"));

                System.out.println("Customer Name      : "
                        + rs.getString("name"));

                System.out.println("Vehicle Number     : "
                        + rs.getString("vehicle_number"));

                System.out.println("Brand              : "
                        + rs.getString("brand"));

                System.out.println("Model              : "
                        + rs.getString("model"));

                System.out.println("Start Date         : "
                        + rs.getDate("start_date"));

                System.out.println("Expected Return    : "
                        + rs.getDate("expected_return_date"));

                System.out.println("Actual Return      : "
                        + rs.getDate("actual_return_date"));

                System.out.println("Total Amount       : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status             : "
                        + rs.getString("status"));

                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No rentals found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void totalRentalRevenue() {

        String sql = "SELECT SUM(total_amount) AS total_revenue "
                + "FROM rental "
                + "WHERE status = 'COMPLETED'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== TOTAL RENTAL REVENUE ==========");

            if (rs.next()) {

                double totalRevenue = rs.getDouble("total_revenue");

                System.out.println("Total Revenue : " + totalRevenue);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void totalRentalCount() {

        String sql = "SELECT COUNT(*) AS total_rentals FROM rental";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== TOTAL RENTALS ==========");

            if (rs.next()) {

                int totalRentals = rs.getInt("total_rentals");

                System.out.println("Total Rentals : " + totalRentals);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void rentalStatusReport() {

        String sql = "SELECT status, COUNT(*) AS total "
                + "FROM rental "
                + "GROUP BY status";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== RENTAL STATUS REPORT ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        rs.getString("status")
                        + " : "
                        + rs.getInt("total"));
            }

            if (!found) {
                System.out.println("No rental records found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewActiveRentals() {

        System.out.println();
        System.out.println("========== ACTIVE RENTALS ==========");

        String sql = "SELECT r.rental_id, c.name, v.vehicle_number, "
                + "r.start_date, r.expected_return_date, r.total_amount, r.status "
                + "FROM rental r "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE r.status = 'ACTIVE'";

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Rental ID          : "
                        + rs.getInt("rental_id"));

                System.out.println("Customer Name      : "
                        + rs.getString("name"));

                System.out.println("Vehicle Number     : "
                        + rs.getString("vehicle_number"));

                System.out.println("Start Date         : "
                        + rs.getDate("start_date"));

                System.out.println("Expected Return    : "
                        + rs.getDate("expected_return_date"));

                System.out.println("Total Amount       : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status             : "
                        + rs.getString("status"));

                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No active rentals.");
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view active rentals.");
            e.printStackTrace();
        }
    }
    
    public static void viewCompletedRentals() {

        System.out.println();
        System.out.println("========== COMPLETED RENTALS ==========");

        String sql = "SELECT r.rental_id, c.name, v.vehicle_number, "
                + "v.brand, v.model, r.start_date, "
                + "r.expected_return_date, r.actual_return_date, "
                + "r.total_amount, r.status "
                + "FROM rental r "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE r.status = 'COMPLETED'";

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Rental ID          : "
                        + rs.getInt("rental_id"));

                System.out.println("Customer Name      : "
                        + rs.getString("name"));

                System.out.println("Vehicle Number     : "
                        + rs.getString("vehicle_number"));

                System.out.println("Brand              : "
                        + rs.getString("brand"));

                System.out.println("Model              : "
                        + rs.getString("model"));

                System.out.println("Start Date         : "
                        + rs.getDate("start_date"));

                System.out.println("Expected Return    : "
                        + rs.getDate("expected_return_date"));

                System.out.println("Actual Return      : "
                        + rs.getDate("actual_return_date"));

                System.out.println("Total Amount       : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status             : "
                        + rs.getString("status"));

                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No completed rentals.");
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view completed rentals.");
            e.printStackTrace();
        }
    }
    
    public static void viewCustomerRentalHistory(Scanner sc) {

        System.out.println();
        System.out.println("========== CUSTOMER RENTAL HISTORY ==========");

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();

        String sql = "SELECT r.rental_id, c.name, v.vehicle_number, "
                + "v.brand, v.model, r.start_date, "
                + "r.expected_return_date, r.actual_return_date, "
                + "r.total_amount, r.status "
                + "FROM rental r "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE r.customer_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Rental ID          : "
                        + rs.getInt("rental_id"));

                System.out.println("Customer Name      : "
                        + rs.getString("name"));

                System.out.println("Vehicle Number     : "
                        + rs.getString("vehicle_number"));

                System.out.println("Brand              : "
                        + rs.getString("brand"));

                System.out.println("Model              : "
                        + rs.getString("model"));

                System.out.println("Start Date         : "
                        + rs.getDate("start_date"));

                System.out.println("Expected Return    : "
                        + rs.getDate("expected_return_date"));

                System.out.println("Actual Return      : "
                        + rs.getDate("actual_return_date"));

                System.out.println("Total Amount       : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status             : "
                        + rs.getString("status"));

                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No rental history found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view customer rental history.");
            e.printStackTrace();
        }
    }
    
    public static void viewVehicleRentalHistory(Scanner sc) {

        System.out.println();
        System.out.println("========== VEHICLE RENTAL HISTORY ==========");

        System.out.print("Enter Vehicle Number: ");
        sc.nextLine();
        String vehicleNumber = sc.nextLine();

        String sql = "SELECT r.rental_id, c.name, v.vehicle_number, "
                + "v.brand, v.model, r.start_date, "
                + "r.expected_return_date, r.actual_return_date, "
                + "r.total_amount, r.status "
                + "FROM rental r "
                + "JOIN customer c ON r.customer_id = c.customer_id "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE v.vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Rental ID          : " + rs.getInt("rental_id"));
                System.out.println("Customer Name      : " + rs.getString("name"));
                System.out.println("Vehicle Number     : " + rs.getString("vehicle_number"));
                System.out.println("Brand              : " + rs.getString("brand"));
                System.out.println("Model              : " + rs.getString("model"));
                System.out.println("Start Date         : " + rs.getDate("start_date"));
                System.out.println("Expected Return    : " + rs.getDate("expected_return_date"));
                System.out.println("Actual Return      : " + rs.getDate("actual_return_date"));
                System.out.println("Total Amount       : " + rs.getDouble("total_amount"));
                System.out.println("Status             : " + rs.getString("status"));
                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No rental history found for this vehicle.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view vehicle rental history.");
            e.printStackTrace();
        }
    }
    
    public static void monthlyRevenue() {

        System.out.println();
        System.out.println("========== MONTHLY REVENUE ==========");

        String sql = "SELECT YEAR(start_date) AS year, "
                + "MONTH(start_date) AS month, "
                + "SUM(total_amount) AS revenue "
                + "FROM rental "
                + "WHERE status = 'COMPLETED' "
                + "GROUP BY YEAR(start_date), MONTH(start_date) "
                + "ORDER BY YEAR(start_date), MONTH(start_date)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Year       : " + rs.getInt("year"));
                System.out.println("Month      : " + rs.getInt("month"));
                System.out.println("Revenue    : " + rs.getDouble("revenue"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No monthly revenue found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to generate monthly revenue.");
            e.printStackTrace();
        }
    }
    
    public static void mostRentedVehicle() {

        System.out.println();
        System.out.println("========== MOST RENTED VEHICLE ==========");

        String sql = "SELECT v.vehicle_number, v.brand, v.model, "
                + "COUNT(r.rental_id) AS rental_count "
                + "FROM rental r "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "GROUP BY v.vehicle_id, v.vehicle_number, v.brand, v.model "
                + "ORDER BY rental_count DESC "
                + "LIMIT 1";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println();
                System.out.println("Vehicle Number   : " + rs.getString("vehicle_number"));
                System.out.println("Brand            : " + rs.getString("brand"));
                System.out.println("Model            : " + rs.getString("model"));
                System.out.println("Rental Count     : " + rs.getInt("rental_count"));
                System.out.println("----------------------------------");

            } else {

                System.out.println("No rental records found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to generate most rented vehicle report.");
            e.printStackTrace();
        }
    }
    
    public static void cancelRental(Scanner sc) {

        System.out.println();
        System.out.println("========== CANCEL RENTAL ==========");

        System.out.print("Enter Rental ID: ");
        int rentalId = sc.nextInt();

        String findSql = "SELECT vehicle_id, status "
                + "FROM rental "
                + "WHERE rental_id = ?";

        String updateRentalSql = "UPDATE rental "
                + "SET status = 'CANCELLED' "
                + "WHERE rental_id = ? AND status = 'ACTIVE'";

        String updateVehicleSql = "UPDATE vehicle "
                + "SET status = 'AVAILABLE' "
                + "WHERE vehicle_id = ?";

        try {

            Connection con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            PreparedStatement findPs = con.prepareStatement(findSql);

            findPs.setInt(1, rentalId);

            ResultSet rs = findPs.executeQuery();

            if (!rs.next()) {

                System.out.println("Rental not found.");

                rs.close();
                findPs.close();
                con.close();

                return;
            }

            int vehicleId = rs.getInt("vehicle_id");
            String rentalStatus = rs.getString("status");

            rs.close();
            findPs.close();

            // Check rental status
            if (!rentalStatus.equals("ACTIVE")) {

                System.out.println(
                        "Only ACTIVE rentals can be cancelled."
                );

                con.close();

                return;
            }

            // Cancel rental
            PreparedStatement rentalPs =
                    con.prepareStatement(updateRentalSql);

            rentalPs.setInt(1, rentalId);

            int rentalRows = rentalPs.executeUpdate();

            // Make vehicle available again
            PreparedStatement vehiclePs =
                    con.prepareStatement(updateVehicleSql);

            vehiclePs.setInt(1, vehicleId);

            int vehicleRows = vehiclePs.executeUpdate();

            // Check both operations
            if (rentalRows > 0 && vehicleRows > 0) {

                con.commit();

                System.out.println();
                System.out.println("Rental cancelled successfully.");
                System.out.println("Vehicle is now available.");

            } else {

                con.rollback();

                System.out.println(
                        "Rental cancellation failed."
                );
            }

            rentalPs.close();
            vehiclePs.close();
            con.close();

        } catch (SQLException e) {

            System.out.println(
                    "Unable to cancel rental."
            );

            e.printStackTrace();
        }
    }
}