package javaClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



////This will load the MySQL driver, each DB has its own driver
//Class.forName("com.mysql.jdbc.Driver");
//// Setup the connection with the DB
//connect = DriverManager
//    .getConnection("jdbc:mysql://localhost/feedback?"
//        + "user=sqluser&password=sqluserpw");



public class SamphireDatabase {
	
	//private String db_connect_string = "jdbc:mysql://localhost:3306/samphire_cottage" ;
	//private String db_userid = "root";
	//private String db_password = "password";
	//private String db_connect_string = "jdbc:mysql://127.4.209.2:3306/samphire_cottage";
	//private String db_userid = "adminVuUZ2jd";
	//private String db_password = "pdVMVsBtj2Q2";
	private Connection dbConnection;
	
	
	    
	

	public void connect() {
		//System.out.println("Trying to connect to DB using " + db_connect_string + " " +  db_userid + " " + db_password);
		System.out.println("Trying to connect using datasource");
		try {
			
			//old
			//System.out.println("Trying to connect to DB using " + db_connect_string + " " +  db_userid + " " + db_password);
			//Class.forName("com.mysql.jdbc.Driver");
			//dbConnection = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			//System.out.println("Connectd to DB");
		
			//new
			System.out.println("Trying to connect using datasource");
		    InitialContext ic = new InitialContext();
		    Context initialContext = (Context) ic.lookup("java:comp/env");
		    DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
		    dbConnection = datasource.getConnection();
		    System.out.println("Connectd to DB");
		
		} catch (Exception e) {
			System.out.println("THIS IS THE UPDATED CODEBASE *********************");
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