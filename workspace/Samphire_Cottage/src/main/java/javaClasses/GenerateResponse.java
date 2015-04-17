package javaClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.ibatis.annotations.ResultType;

import com.google.gson.Gson;

public class GenerateResponse implements JavaDelegate {

	private Expression serviceName;
	private Expression enquiryDetails;


	public void execute(DelegateExecution execution) throws SQLException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
	  
		//String input_order = (String) enquiryDetails.getValue(execution);
		Object input_order = enquiryDetails.getValue(execution);
		System.out.println(input_order.toString());
		
		//Connect to the DB
		//SamphireDatabase dbConnection = new SamphireDatabase();
		//dbConnection.connect();
		
		Gson gson = new Gson();
		String input_orderToJson = gson.toJson(input_order);
		
	
		

		//Interpret the JSON object
		//JSONObject enquiryDetailsJSON = new JSONObject(input_order.toString());
		JSONObject enquiryDetailsJSON = new JSONObject(input_orderToJson);

		String cost = enquiryDetailsJSON.getString("cost");
		String startDate = enquiryDetailsJSON.getString("stayStartDate");
		String endDate = enquiryDetailsJSON.getString("stayEndDate");
		String name = enquiryDetailsJSON.getString("name");
		String responder = enquiryDetailsJSON.getString("responder");
		String deposit = "";
		String remainingBalance = "";
		
		//Calculate + format values
		if (cost != ""){
			System.out.println("Calculate Values IN. Cost: " + cost);
			 Double doubleDeposit = (Integer.parseInt(cost)) * 0.3;
			 deposit = String.format( "%.2f", doubleDeposit );
			 Double doubleRemainingBalance = (Integer.parseInt(cost)) * 0.7;
			 remainingBalance = String.format( "%.2f", doubleRemainingBalance );
		}
		
		JSONObject templateObject = enquiryDetailsJSON.getJSONObject("templateObject");
		String shortName = templateObject.getString("shortName");
		String template = templateObject.getString("template");
				
		String updatedText = insertValue(template, name, "<name>");
		updatedText = insertValue(updatedText, cost, "<cost>");
		updatedText = insertValue(updatedText, startDate, "<startDate>");
		updatedText = insertValue(updatedText, endDate, "<endDate>");
		updatedText = insertValue(updatedText, deposit, "<deposit>");
		updatedText = insertValue(updatedText, remainingBalance, "<remainingBalance>");
		updatedText = insertValue(updatedText, responder, "<responder>");
		
		System.out.println(updatedText);
			
		execution.setVariable("responseText", updatedText);
		
		System.out.println(input_serviceName + ": Out");
  
	}
	
	public String insertValue(String responseText, String valueToInsert, String placeholder){
		
		String[] splitText = null;
		String updatedText = "";
		
		//check to see if the placeholder exists, if not just return the unaltered response text
		if (responseText.split(placeholder).length > 1){
			splitText = responseText.split(placeholder);
			
			for (Integer i=0; i<splitText.length; i++){			
				if (i==(splitText.length-1)){
					updatedText += splitText[i];
				}else{
					updatedText += splitText[i] + valueToInsert;
				}
			}
		
			return updatedText;
		}else{
			return responseText;
		}	
	}
}