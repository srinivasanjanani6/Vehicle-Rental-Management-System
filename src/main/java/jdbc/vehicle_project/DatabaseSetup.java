package jdbc.vehicle_project;



import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createDatabase() {

        String sql = "CREATE DATABASE IF NOT EXISTS vehicle_rental_db";

        try {
            Connection con = DBConnection.getServerConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(sql);

            System.out.println("Database created successfully.");

            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            createAdminTable(stmt);
            createCustomerTable(stmt);
            createVehicleTable(stmt);
            createRentalTable(stmt);
            createPaymentTable(stmt);

            stmt.close();
            con.close();

            System.out.println("All tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createAdminTable(Statement stmt) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS admin ("
                + "admin_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "username VARCHAR(50) NOT NULL UNIQUE, "
                + "password VARCHAR(100) NOT NULL"
                + ")";

        stmt.executeUpdate(sql);

        System.out.println("Admin table created.");
    }

    private static void createCustomerTable(Statement stmt) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS customer ("
                + "customer_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(100) NOT NULL, "
                + "phone VARCHAR(15) NOT NULL UNIQUE, "
                + "email VARCHAR(100) NOT NULL UNIQUE, "
                + "license_number VARCHAR(50) NOT NULL UNIQUE, "
                + "password VARCHAR(100) NOT NULL"
                + ")";

        stmt.executeUpdate(sql);

        System.out.println("Customer table created.");
    }

    private static void createVehicleTable(Statement stmt) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS vehicle ("
                + "vehicle_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "vehicle_number VARCHAR(20) NOT NULL UNIQUE, "
                + "brand VARCHAR(50) NOT NULL, "
                + "model VARCHAR(50) NOT NULL, "
                + "vehicle_type VARCHAR(30) NOT NULL, "
                + "rent_per_day DOUBLE NOT NULL, "
                + "status VARCHAR(20) NOT NULL"
                + ")";

        stmt.executeUpdate(sql);

        System.out.println("Vehicle table created.");
    }

    private static void createRentalTable(Statement stmt) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS rental ("
                + "rental_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "customer_id INT NOT NULL, "
                + "vehicle_id INT NOT NULL, "
                + "start_date DATE NOT NULL, "
                + "expected_return_date DATE NOT NULL, "
                + "actual_return_date DATE, "
                + "total_amount DOUBLE, "
                + "status VARCHAR(20) NOT NULL, "
                + "FOREIGN KEY (customer_id) REFERENCES customer(customer_id), "
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)"
                + ")";

        stmt.executeUpdate(sql);

        System.out.println("Rental table created.");
    }

    private static void createPaymentTable(Statement stmt) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS payment ("
                + "payment_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "rental_id INT NOT NULL, "
                + "amount DOUBLE NOT NULL, "
                + "payment_method VARCHAR(20) NOT NULL, "
                + "payment_status VARCHAR(20) NOT NULL, "
                + "payment_date DATE, "
                + "FOREIGN KEY (rental_id) REFERENCES rental(rental_id)"
                + ")";

        stmt.executeUpdate(sql);

        System.out.println("Payment table created.");
    }
}