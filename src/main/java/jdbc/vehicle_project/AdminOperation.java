package jdbc.vehicle_project;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminOperation {
	public static boolean adminLogin(String username, String password) {

	    String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

	    try {
	        Connection con = DBConnection.getConnection();

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setString(1, username);
	        ps.setString(2, password);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            System.out.println("Admin Login Successful!");

	            rs.close();
	            ps.close();
	            con.close();

	            return true;

	        } else {

	            System.out.println("Invalid username or password.");

	            rs.close();
	            ps.close();
	            con.close();

	            return false;
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();
	        return false;
	    }
	}
}