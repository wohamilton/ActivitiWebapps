package javaClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

public class CheckStockLevels implements JavaDelegate {

	private Expression serviceName;
	private Expression order;


	public void execute(DelegateExecution execution) throws SQLException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
	  
//		String input_order = (String) order.getValue(execution);
		Object input_order = order.getValue(execution);
		System.out.println(input_order.toString());
		
		//Connect to the DB
		Database dbConnection = new Database();
		dbConnection.connect();
		
	  
		//Access each of the items requested and check if they are in stock
		JSONObject inputOrderJSON = new JSONObject(input_order.toString());

		
//		System.out.println(inputOrderJSON.toString());
		System.out.println(inputOrderJSON.getJSONArray("items").toString());
		JSONArray requestedItems = inputOrderJSON.getJSONArray("items");
		
		Integer itemsListLength = requestedItems.length();
		System.out.println(itemsListLength);
		
		Boolean isAllInStock = true;
		
		for (int i=0; i<itemsListLength; i++){
			String stockCode = requestedItems.getJSONObject(i).getString("stockCode");
			Integer numberRequested = requestedItems.getJSONObject(i).getInt("quantity");
			Integer numberInStock = 0;

			System.out.println("Stock Code: " + stockCode);
			System.out.println("Number requested: " + numberRequested);
			
			String sqlStatement = "SELECT QTY_IN_STOCK FROM dbo.Products WHERE STOCK_CODE = '" + stockCode + "'";

			ResultSet sqlResults = dbConnection.executeQuery(sqlStatement);
						
			if (!sqlResults.next()){
				System.out.println("Number in stock: 0");
				requestedItems.getJSONObject(i).put("numberInStock", 0);
			}else{
				System.out.println("Number in stock: " + sqlResults.getInt(1));
	            numberInStock = sqlResults.getInt(1);
	            requestedItems.getJSONObject(i).put("numberInStock", numberInStock);
			}
			
			if (numberRequested < numberInStock){
				System.out.println("ENOUGH IN STOCK");
				System.out.println("");
				requestedItems.getJSONObject(i).put("isInStock", true);
				
			}else{
				System.out.println("NOT ENOUGH IN STOCK");
				System.out.println("");
				isAllInStock = false;
				requestedItems.getJSONObject(i).put("isInStock", false);
			}
		}
		
		
		execution.setVariable("isStockAvailable", isAllInStock);	
		execution.setVariable("orderRequest", inputOrderJSON.toString());
		System.out.println("**********************");
		System.out.println(inputOrderJSON.toString());
		
		System.out.println(input_serviceName + ": Out");
  
	}
  
}



