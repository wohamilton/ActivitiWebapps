package javaClasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

public class CreateSalesOrder implements JavaDelegate {

	private Expression serviceName;
	private Expression order;


	public void execute(DelegateExecution execution) throws SQLException, InterruptedException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
	  
		Object input_order = order.getValue(execution);
		System.out.println(input_order);
		
		//Connect to the DB
		Database dbConnection = new Database();
		dbConnection.connect();

		JSONObject inputOrderJSON = new JSONObject(input_order.toString());
		JSONArray requestedItems = inputOrderJSON.getJSONArray("items");
		String customerRef = inputOrderJSON.getJSONObject("customer").getString("id");
		Integer itemsListLength = requestedItems.length();
//		System.out.println(itemsListLength);
		
		
		//Create Sales Order Header
		SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();
		String str_currentDate = currentDate.format(now);
		
				
		String sqlStatement = "INSERT INTO dbo.Sales_Order_Headers (soh_DocType, soh_AccRef, soh_Date) VALUES ('O', '" + customerRef + "', CAST('" + str_currentDate + "' AS DATETIME))";
		dbConnection.execute(sqlStatement);	
		
		
		//Create Sales Order Line
		Integer ordln_soh_no = dbConnection.getMaxInt("dbo.Sales_Order_Headers", "soh_no");
		
		for (int i=0; i<itemsListLength; i++){
			Integer ordln_LineNo = i+1;
			String ordln_Stock_Code = requestedItems.getJSONObject(i).getString("stockCode");
			Integer ordln_Qty = requestedItems.getJSONObject(i).getInt("quantity");
			Double ordln_Unit_Price = requestedItems.getJSONObject(i).getDouble("agreedPrice");
			Double ordln_Nett = ordln_Qty * ordln_Unit_Price;
			Double ordln_VAT = ordln_Nett * 0.2;
			Double ordln_Gross = ordln_Nett +  ordln_VAT;
			
			sqlStatement = "Insert into dbo.Sales_Order_Lines (ordln_soh_No, ordln_LineNo, ordln_SubLineNo, ordln_Stock_Code, ordln_Qty, ordln_Unit_Price, ordln_Nett, ordln_VAT, ordln_Gross) VALUES (" + ordln_soh_no + ", " + ordln_LineNo + ", 0, '" + ordln_Stock_Code + "', " + ordln_Qty + ", " + ordln_Unit_Price + ", " + ordln_Nett + ", " + ordln_VAT + ", " + ordln_Gross + ")";
			dbConnection.execute(sqlStatement);	
		}

		
		
		
		
		
		
		
		
		
	
		
		
		
		
		
		
//		//Access each of the items requested and update the product table
//		//reducing the quantity in stock and increasing the quantity allocated
//		for (int i=0; i<itemsListLength; i++){
//			String stockCode = requestedItems.getJSONObject(i).getString("stockCode");
//			Integer numberRequested = requestedItems.getJSONObject(i).getInt("quantity");
//			Integer numberInStock = 0;
//			Integer numberAllocated = 0;
//
//			System.out.println("Stock Code: " + stockCode);
//			System.out.println("Number requesred: " + numberRequested);
//			
//			String sqlStatement = "SELECT QTY_IN_STOCK, QTY_ALLOCATED FROM dbo.Products WHERE STOCK_CODE = '" + stockCode + "'";
//					
//			ResultSet sqlResults = dbConnection.executeQuery(sqlStatement) ; 
//			
//			while (sqlResults.next()) {
//	            System.out.println("Number in stock: " + sqlResults.getInt(1));
//	            numberInStock = sqlResults.getInt(1);
//	            
//	            System.out.println("Number in allocated: " + sqlResults.getInt(2));
//	            numberAllocated = sqlResults.getInt(2);
//	        }
//			
//			Integer updatedNumberInStock = numberInStock - numberRequested;
//			Integer updatedNumberAllocated = numberAllocated + numberRequested;
//			
//			sqlStatement = "UPDATE dbo.Products SET QTY_IN_STOCK = " + updatedNumberInStock + ", QTY_ALLOCATED = " + updatedNumberAllocated + " WHERE STOCK_CODE = '" + stockCode + "'";
//			System.out.println(sqlStatement);
//			dbConnection.execute(sqlStatement);	
//
//		}
//		
		System.out.println(input_serviceName + ": Out");
  
	}
  
}

