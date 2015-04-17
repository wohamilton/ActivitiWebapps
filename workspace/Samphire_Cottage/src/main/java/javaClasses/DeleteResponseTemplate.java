package javaClasses;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONObject;

import com.google.gson.Gson;


public class DeleteResponseTemplate implements JavaDelegate {

	private Expression serviceName;
	private Expression activeTemplate;

	public void execute(DelegateExecution execution) throws SQLException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
		
		Object input_activeTemplate = activeTemplate.getValue(execution);
		System.out.println(input_activeTemplate.toString());
		
		Gson gson = new Gson();
		String templateJSONStringToJson = gson.toJson(input_activeTemplate);
		JSONObject templateJSON = new JSONObject(templateJSONStringToJson);
		
		
		//Connect to the DB
		SamphireDatabase dbConnection = new SamphireDatabase();
		dbConnection.connect();

		String sqlQuery = "DELETE FROM enquiryresponse where responsename='" + templateJSON.getString("shortName") + "'";
		dbConnection.execute(sqlQuery);	
	
		System.out.println(input_serviceName + ": Out");
		

  
	}
  
}



