package jdbc.vehicle_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReturnOperation {

    public static void returnVehicle(Scanner sc, int customerId) {

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        String sql = "SELECT r.rental_id, r.vehicle_id, "
                + "r.start_date, r.expected_return_date, "
                + "r.total_amount, r.status, "
                + "v.vehicle_number, v.brand, v.model "
                + "FROM rental r "
                + "JOIN vehicle v ON r.vehicle_id = v.vehicle_id "
                + "WHERE r.customer_id = ? "
                + "AND v.vehicle_number = ? "
                + "AND r.status = 'ACTIVE'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println();
                System.out.println("========== ACTIVE RENTAL ==========");

                System.out.println("Rental ID          : "
                        + rs.getInt("rental_id"));

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

                System.out.println("Current Amount     : "
                        + rs.getDouble("total_amount"));

                System.out.println("Status             : "
                        + rs.getString("status"));
                
                System.out.print("Enter Actual Return Date (yyyy-mm-dd): ");
                String actualReturnDate = sc.nextLine();

                java.time.LocalDate startDate =
                        rs.getDate("start_date").toLocalDate();

                java.time.LocalDate returnDate =
                        java.time.LocalDate.parse(actualReturnDate);

                long rentalDays =
                        java.time.temporal.ChronoUnit.DAYS
                        .between(startDate, returnDate);

                if (rentalDays <= 0) {

                    System.out.println(
                            "Return date must be after start date.");

                } else {

                    System.out.println();
                    System.out.println("Rental Days : " + rentalDays);

                    double rentPerDay = rs.getDouble("total_amount") / 
                                        java.time.temporal.ChronoUnit.DAYS.between(
                                                startDate,
                                                rs.getDate("expected_return_date").toLocalDate()
                                        );

                    double finalAmount = rentalDays * rentPerDay;

                    System.out.println("Final Amount : " + finalAmount);

                    int rentalId = rs.getInt("rental_id");
                    int vehicleId = rs.getInt("vehicle_id");

                    con.setAutoCommit(false);

                    try {

                        String rentalSql =
                                "UPDATE rental SET actual_return_date = ?, "
                                + "total_amount = ?, status = 'COMPLETED' "
                                + "WHERE rental_id = ?";

                        PreparedStatement rentalPs =
                                con.prepareStatement(rentalSql);

                        rentalPs.setString(1, actualReturnDate);
                        rentalPs.setDouble(2, finalAmount);
                        rentalPs.setInt(3, rentalId);

                        rentalPs.executeUpdate();

                        // UPDATE VEHICLE
                        String vehicleSql =
                                "UPDATE vehicle SET status = 'AVAILABLE' "
                                + "WHERE vehicle_id = ?";

                        PreparedStatement vehiclePs =
                                con.prepareStatement(vehicleSql);

                        vehiclePs.setInt(1, vehicleId);

                        vehiclePs.executeUpdate();

                        // COMMIT
                        con.commit();

                        System.out.println();
                        System.out.println("Vehicle returned successfully!");
                        System.out.println("Rental Status : COMPLETED");
                        System.out.println("Vehicle Status: AVAILABLE");

                        rentalPs.close();
                        vehiclePs.close();

                    } catch (SQLException e) {

                        try {
                            con.rollback();
                        } catch (SQLException rollbackError) {
                            rollbackError.printStackTrace();
                        }

                        System.out.println("Return failed.");
                        System.out.println("Transaction rolled back.");

                        e.printStackTrace();
                    }

                    con.setAutoCommit(true);
                }

            } else {

                System.out.println(
                        "No active rental found for this vehicle.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
