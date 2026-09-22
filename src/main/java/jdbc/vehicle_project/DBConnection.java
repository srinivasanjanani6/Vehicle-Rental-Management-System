package jdbc.vehicle_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static String URL = "jdbc:mysql://localhost:3306/";
    static String DATABASE_URL = "jdbc:mysql://localhost:3306/vehicle_rental_db";

    static String USERNAME = "root";
    static String PASSWORD = "YOUR_MYSQL_PASSWORD";

    public static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
}