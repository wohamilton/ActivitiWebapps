package javaClasses;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONObject;

import com.google.gson.Gson;


public class WriteResponseTemplate implements JavaDelegate {

	private Expression serviceName;
	private Expression newTemplate;

	public void execute(DelegateExecution execution) throws SQLException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
		
		Object input_newTemplate = newTemplate.getValue(execution);
		System.out.println(input_newTemplate.toString());

		Gson gson = new Gson();
		String templateJSONStringToJson = gson.toJson(input_newTemplate);
		JSONObject templateJSON = new JSONObject(templateJSONStringToJson);
			  	
		String templateForDatabase = templateJSON.getString("template");
		templateForDatabase = templateForDatabase.replace("'", "\\'");
		
		//Connect to the DB
		SamphireDatabase dbConnection = new SamphireDatabase();
		dbConnection.connect();
		
		//Check if the shortName exists in the DB
		String sqlQuery = "SELECT * FROM enquiryresponse where responsename='" + templateJSON.getString("shortName") + "'";
		ResultSet rs = dbConnection.executeQuery(sqlQuery);
		
		if (rs.next()){
			sqlQuery = "UPDATE enquiryresponse SET responseText='" + templateForDatabase + "' WHERE responseName ='" + templateJSON.getString("shortName") + "'";
			dbConnection.execute(sqlQuery);	
		}else{
			sqlQuery = "INSERT INTO enquiryresponse VALUES ('"+ (dbConnection.getMaxInt("enquiryresponse", "id")+1) +"', '" + templateJSON.getString("shortName") +  "', '" + templateForDatabase + "')";
			dbConnection.execute(sqlQuery);				
		}
		

		System.out.println(input_serviceName + ": Out");
		

  
	}
  
}



