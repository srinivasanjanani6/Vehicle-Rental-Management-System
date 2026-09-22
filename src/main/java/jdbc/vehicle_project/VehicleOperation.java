package jdbc.vehicle_project;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;
//import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

public class VehicleOperation {

    public static void addSampleVehicles() {

        String checkSql = "SELECT * FROM vehicle WHERE vehicle_number = ?";

        String insertSql = "INSERT INTO vehicle "
                + "(vehicle_number, brand, model, vehicle_type, rent_per_day, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            addVehicle(con, checkSql, insertSql,
                    "TN01AB1234", "Honda", "City", "Car", 1500, "AVAILABLE");

            addVehicle(con, checkSql, insertSql,
                    "TN02CD5678", "Yamaha", "R15", "Bike", 800, "AVAILABLE");

            addVehicle(con, checkSql, insertSql,
                    "TN03EF9012", "Hyundai", "Creta", "SUV", 2000, "AVAILABLE");

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addVehicle(
            Connection con,
            String checkSql,
            String insertSql,
            String vehicleNumber,
            String brand,
            String model,
            String vehicleType,
            double rentPerDay,
            String status) throws SQLException {

        PreparedStatement checkPs = con.prepareStatement(checkSql);

        checkPs.setString(1, vehicleNumber);

        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {

            System.out.println(vehicleNumber + " already exists.");

        } else {

            PreparedStatement insertPs = con.prepareStatement(insertSql);

            insertPs.setString(1, vehicleNumber);
            insertPs.setString(2, brand);
            insertPs.setString(3, model);
            insertPs.setString(4, vehicleType);
            insertPs.setDouble(5, rentPerDay);
            insertPs.setString(6, status);

            insertPs.executeUpdate();

            System.out.println(vehicleNumber + " added successfully.");

            insertPs.close();
        }

        rs.close();
        checkPs.close();
    }
    
    public static void viewAvailableVehicles() {

        String sql = "SELECT * FROM vehicle WHERE status = 'AVAILABLE'";

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            System.out.println();
            System.out.println("========== AVAILABLE VEHICLES ==========");

            while (rs.next()) {

                System.out.println("Vehicle Number : " + rs.getString("vehicle_number"));
                System.out.println("Brand          : " + rs.getString("brand"));
                System.out.println("Model          : " + rs.getString("model"));
                System.out.println("Vehicle Type   : " + rs.getString("vehicle_type"));
                System.out.println("Rent Per Day   : " + rs.getDouble("rent_per_day"));
                System.out.println("----------------------------------------");
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public static void viewRentedVehicles() {

        System.out.println();
        System.out.println("========== RENTED VEHICLES ==========");

        String sql = "SELECT * FROM vehicle WHERE status = 'RENTED'";

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Vehicle Number : " + rs.getString("vehicle_number"));
                System.out.println("Brand          : " + rs.getString("brand"));
                System.out.println("Model          : " + rs.getString("model"));
                System.out.println("Vehicle Type   : " + rs.getString("vehicle_type"));
                System.out.println("Rent Per Day   : " + rs.getDouble("rent_per_day"));
                System.out.println("----------------------------------------");
            }

            if (!found) {
                System.out.println("No rented vehicles.");
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to view rented vehicles.");
            e.printStackTrace();
        }
    }
    
    public static void searchVehicle(Scanner sc) {

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        String sql = "SELECT * FROM vehicle WHERE vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println();
                System.out.println("========== VEHICLE DETAILS ==========");

                System.out.println("Vehicle Number : " + rs.getString("vehicle_number"));
                System.out.println("Brand          : " + rs.getString("brand"));
                System.out.println("Model          : " + rs.getString("model"));
                System.out.println("Vehicle Type   : " + rs.getString("vehicle_type"));
                System.out.println("Rent Per Day   : " + rs.getDouble("rent_per_day"));
                System.out.println("Status         : " + rs.getString("status"));

            } else {

                System.out.println("Vehicle not found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public static void sendVehicleToMaintenance(Scanner sc) {

        System.out.println();
        System.out.println("========== SEND VEHICLE TO MAINTENANCE ==========");

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        String sql = "UPDATE vehicle SET status = 'MAINTENANCE' "
                + "WHERE vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Vehicle sent to maintenance successfully.");
            } else {
                System.out.println("Vehicle not found.");
            }

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Vehicle could not be sent to maintenance.");
            e.printStackTrace();
        }
    }
    
    public static void updateVehicle(Scanner sc) {

        System.out.println();
        System.out.println("========== UPDATE VEHICLE ==========");

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        System.out.print("Enter New Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter New Model: ");
        String model = sc.nextLine();

        System.out.print("Enter New Vehicle Type: ");
        String vehicleType = sc.nextLine();

        System.out.print("Enter New Rent Per Day: ");
        double rentPerDay = sc.nextDouble();
        sc.nextLine();

        String sql = "UPDATE vehicle SET brand = ?, model = ?, "
                + "vehicle_type = ?, rent_per_day = ? "
                + "WHERE vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setString(3, vehicleType);
            ps.setDouble(4, rentPerDay);
            ps.setString(5, vehicleNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Vehicle updated successfully.");
            } else {
                System.out.println("Vehicle not found.");
            }

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Vehicle could not be updated.");
            e.printStackTrace();
        }
    }
    
    public static void deleteVehicle(Scanner sc) {

        System.out.println();
        System.out.println("========== DELETE VEHICLE ==========");

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        String sql = "DELETE FROM vehicle WHERE vehicle_number = ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Vehicle deleted successfully.");
            } else {
                System.out.println("Vehicle not found.");
            }

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Vehicle could not be deleted.");
            e.printStackTrace();
        }
    }
    
    public static void viewAllVehicles() {

        String sql = "SELECT * FROM vehicle";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== ALL VEHICLES ==========");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Vehicle ID     : "
                        + rs.getInt("vehicle_id"));

                System.out.println("Vehicle Number : "
                        + rs.getString("vehicle_number"));

                System.out.println("Brand          : "
                        + rs.getString("brand"));

                System.out.println("Model          : "
                        + rs.getString("model"));

                System.out.println("Vehicle Type   : "
                        + rs.getString("vehicle_type"));

                System.out.println("Rent Per Day   : "
                        + rs.getDouble("rent_per_day"));

                System.out.println("Status         : "
                        + rs.getString("status"));

                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No vehicles found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void totalAvailableVehicles() {

        String sql = "SELECT COUNT(*) AS available_vehicles "
                + "FROM vehicle "
                + "WHERE status = 'AVAILABLE'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== AVAILABLE VEHICLES ==========");

            if (rs.next()) {

                int availableVehicles =
                        rs.getInt("available_vehicles");

                System.out.println(
                        "Available Vehicles : " + availableVehicles);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void addVehicle(Scanner sc) {

        System.out.println();
        System.out.println("========== ADD VEHICLE ==========");

        System.out.print("Enter Vehicle Number: ");
        String vehicleNumber = sc.nextLine();

        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter Model: ");
        String model = sc.nextLine();

        System.out.print("Enter Vehicle Type: ");
        String vehicleType = sc.nextLine();

        System.out.print("Enter Rent Per Day: ");
        double rentPerDay = sc.nextDouble();
        sc.nextLine();

        String sql = "INSERT INTO vehicle "
                + "(vehicle_number, brand, model, vehicle_type, rent_per_day, status) "
                + "VALUES (?, ?, ?, ?, ?, 'AVAILABLE')";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);
            ps.setString(2, brand);
            ps.setString(3, model);
            ps.setString(4, vehicleType);
            ps.setDouble(5, rentPerDay);

            ps.executeUpdate();

            System.out.println("Vehicle added successfully.");

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Vehicle could not be added.");
            e.printStackTrace();
        }
    }
    
    public static void availableVehiclesReport() {

        System.out.println();
        System.out.println("========== AVAILABLE VEHICLES REPORT ==========");

        String sql = "SELECT vehicle_id, vehicle_number, brand, model, "
                + "vehicle_type, rent_per_day, status "
                + "FROM vehicle "
                + "WHERE status = 'AVAILABLE'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Vehicle ID       : " + rs.getInt("vehicle_id"));
                System.out.println("Vehicle Number   : " + rs.getString("vehicle_number"));
                System.out.println("Brand            : " + rs.getString("brand"));
                System.out.println("Model            : " + rs.getString("model"));
                System.out.println("Vehicle Type     : " + rs.getString("vehicle_type"));
                System.out.println("Rent Per Day     : " + rs.getDouble("rent_per_day"));
                System.out.println("Status           : " + rs.getString("status"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No available vehicles found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to generate available vehicles report.");
            e.printStackTrace();
        }
    }
    
    public static void rentedVehiclesReport() {

        System.out.println();
        System.out.println("========== RENTED VEHICLES REPORT ==========");

        String sql = "SELECT vehicle_id, vehicle_number, brand, model, "
                + "vehicle_type, rent_per_day, status "
                + "FROM vehicle "
                + "WHERE status = 'RENTED'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println();
                System.out.println("Vehicle ID       : " + rs.getInt("vehicle_id"));
                System.out.println("Vehicle Number   : " + rs.getString("vehicle_number"));
                System.out.println("Brand            : " + rs.getString("brand"));
                System.out.println("Model            : " + rs.getString("model"));
                System.out.println("Vehicle Type     : " + rs.getString("vehicle_type"));
                System.out.println("Rent Per Day     : " + rs.getDouble("rent_per_day"));
                System.out.println("Status           : " + rs.getString("status"));
                System.out.println("----------------------------------");
            }

            if (!found) {
                System.out.println("No rented vehicles found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println("Unable to generate rented vehicles report.");
            e.printStackTrace();
        }
    }
}
