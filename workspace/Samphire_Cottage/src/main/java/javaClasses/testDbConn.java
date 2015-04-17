package javaClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testDbConn {
	
//	private static String db_connect_string = "jdbc:mysql://localhost:3306/samphire_cottage" ;
//	private static String db_userid = "root";
//	private static String db_password = "password";
	private static String db_connect_string = "jdbc:mysql://127.4.209.2:3306/samphire_cottage";
	private static String db_userid = "adminVuUZ2jd";
	private static String db_password = "pdVMVsBtj2Q2";
	private static Connection dbConnection;

	public static void main(String[] args) {
		
		
		try {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection = DriverManager.getConnection(db_connect_string, db_userid, db_password);
		System.out.println("Connectd to DB");
		String sqlStatement = "select * from customer";
		System.out.println("Executing sql: " + sqlStatement);
		
			Statement statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sqlStatement);
			while(rs.next())
			{			    
			    System.out.println(rs.getString(2));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
