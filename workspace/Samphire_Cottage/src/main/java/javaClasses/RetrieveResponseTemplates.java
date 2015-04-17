package javaClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.ibatis.annotations.ResultType;

public class RetrieveResponseTemplates implements JavaDelegate {

	private Expression serviceName;

	public void execute(DelegateExecution execution) throws SQLException {
	  
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
	  		
		//Connect to the DB
		SamphireDatabase dbConnection = new SamphireDatabase();
		dbConnection.connect();

		String sqlQuery = "select * from enquiryresponse";
		ResultSet sqlResults = dbConnection.executeQuery(sqlQuery);
		
		//count results
		Integer resultsLength = 0;
		if (sqlResults.next()){
			resultsLength++;
		}
		
		//System.out.println("resultsLength: " + resultsLength);
		
		JSONArray responseTemplatesJSON = new JSONArray();
//		ResponseTemplate[] responseTemplateList = new ResponseTemplate[resultsLength+1];
		Integer i = 0;
				
		
		//get first result
//		System.out.println(sqlResults.getString(1));
//		System.out.println(sqlResults.getString(2));
//		System.out.println(sqlResults.getString(3));
		
		JSONObject temp = new JSONObject();
		temp.put("shortName", sqlResults.getString(2));
		temp.put("template", sqlResults.getString(3));	
		
		responseTemplatesJSON.put(temp);
		
//		responseTemplateList[i] = new ResponseTemplate(sqlResults.getString(2), sqlResults.getString(3));
		i++;
		
		//get subsequent results
		while (sqlResults.next()){
						
			temp = new JSONObject();
			temp.put("shortName", sqlResults.getString(2));
			temp.put("template", sqlResults.getString(3));	
			
			responseTemplatesJSON.put(temp);
			
//			System.out.println(sqlResults.getString(1));
//			System.out.println(sqlResults.getString(2));
//			System.out.println(sqlResults.getString(3));
			
//			responseTemplateList[i] = new ResponseTemplate(sqlResults.getString(2), sqlResults.getString(3));
			i++;
		}
		
		//System.out.println("***"+responseTemplatesJSON.toString());
		
		
		
		
		
		execution.setVariable("responseTemplates", responseTemplatesJSON.toString());
		
		System.out.println(input_serviceName + ": Out");
  
	}
  
}



