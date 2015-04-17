package org.activiti.samphire;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONObject;
import org.junit.Test;

public class testReplace {
	
	@Test
	public void startProcess(){
	
	String js = "{\"template\":\"SOME'TEXT123456\",\"shortName\":\"GK TEST 2\"}";
	JSONObject templateJSON = new JSONObject(js);
	String myString = (String) templateJSON.get("template");
	myString = myString.replace("'", "\\'");
	
	System.out.println(myString);
	
	System.out.println(templateJSON.get("template"));
	
}
	

}
