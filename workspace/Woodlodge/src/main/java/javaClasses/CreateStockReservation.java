package javaClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

public class CreateStockReservation implements JavaDelegate {

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
		Integer itemsListLength = requestedItems.length();
//		System.out.println(itemsListLength);
		
		//Access each of the items requested and update the product table
		//reducing the quantity in stock and increasing the quantity allocated
		for (int i=0; i<itemsListLength; i++){
			String stockCode = requestedItems.getJSONObject(i).getString("stockCode");
			Integer numberRequested = requestedItems.getJSONObject(i).getInt("quantity");
			Integer numberInStock = 0;
			Integer numberAllocated = 0;

			System.out.println("Stock Code: " + stockCode);
			System.out.println("Number requesred: " + numberRequested);
			
			String sqlStatement = "SELECT QTY_IN_STOCK, QTY_ALLOCATED FROM dbo.Products WHERE STOCK_CODE = '" + stockCode + "'";
					
			ResultSet sqlResults = dbConnection.executeQuery(sqlStatement) ; 
			
			while (sqlResults.next()) {
	            System.out.println("Number in stock: " + sqlResults.getInt(1));
	            numberInStock = sqlResults.getInt(1);
	            
	            System.out.println("Number in allocated: " + sqlResults.getInt(2));
	            numberAllocated = sqlResults.getInt(2);
	        }
			
			Integer updatedNumberInStock = numberInStock - numberRequested;
			Integer updatedNumberAllocated = numberAllocated + numberRequested;
			
			sqlStatement = "UPDATE dbo.Products SET QTY_IN_STOCK = " + updatedNumberInStock + ", QTY_ALLOCATED = " + updatedNumberAllocated + " WHERE STOCK_CODE = '" + stockCode + "'";
			System.out.println(sqlStatement);
			dbConnection.execute(sqlStatement);	

		}
		
		Thread.sleep(5000);
		System.out.println(input_serviceName + ": Out");
  
	}
  
}

