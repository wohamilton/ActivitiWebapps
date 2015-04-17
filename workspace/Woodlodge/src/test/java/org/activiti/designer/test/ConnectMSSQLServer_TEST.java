package org.activiti.designer.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectMSSQLServer_TEST
{
	
//	String url = "jdbc:sqlserver://WIN-P5RH95SIC6L\\SQLEXPRESS;databaseName=MYDB;integratedSecurity=true";
//	String url = "jdbc:sqlserver://WIN-P5RH95SIC6L\\SQLEXPRESS;integratedSecurity=true";
	
   public void dbConnect()
   {
      try {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://WIN-P5RH95SIC6L\\SQLEXPRESS;databaseName=System3;",
                  "system3", "aN0icePwd");
         
         
//         Connection conn = DriverManager.getConnection(url);
         
         
         System.out.println("connected");
         Statement statement = conn.createStatement();
         String queryString = "select * from sysobjects where type='u'";
         ResultSet rs = statement.executeQuery(queryString);
         while (rs.next()) {
            System.out.println(rs.getString(1));
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args)
   {
      ConnectMSSQLServer_TEST connServer = new ConnectMSSQLServer_TEST();
      connServer.dbConnect();
   }
}