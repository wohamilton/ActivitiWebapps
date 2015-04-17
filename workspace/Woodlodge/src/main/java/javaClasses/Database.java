package javaClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private String db_connect_string = "jdbc:sqlserver://WIN-P5RH95SIC6L\\SQLEXPRESS;databaseName=System3;" ;
	private String db_userid = "system3";
	private String db_password = "aN0icePwd";
	private Connection dbConnection;

	public void connect() {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dbConnection = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			System.out.println("Connectd to DB");
		} catch (Exception e) {
			e.printStackTrace();
		}    
	}
	
	public ResultSet executeQuery(String sqlStatement) throws SQLException{
		System.out.println("Executing sql: " + sqlStatement);
	    Statement statement = dbConnection.createStatement();
	    ResultSet sqlResultSet = statement.executeQuery(sqlStatement);
	    	    	
		return sqlResultSet;
	}
	
	public void execute(String sqlStatement) throws SQLException{
		System.out.println("Executing sql: " + sqlStatement);
	    Statement statement = dbConnection.createStatement();
	    statement.execute(sqlStatement);
	}
	
	public Integer getMaxInt (String table, String column) throws SQLException{
		ResultSet rs = executeQuery("select MAX(" + column + ") from " + table);
		Integer max = 0;
		
		while (rs.next()) {
            System.out.println("max: " + rs.getInt(1));
            max = rs.getInt(1);
        }
	
		return max;
	}
	
	
}