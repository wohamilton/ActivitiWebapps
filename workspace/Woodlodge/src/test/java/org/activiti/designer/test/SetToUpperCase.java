package org.activiti.designer.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class SetToUpperCase implements JavaDelegate {

	private Expression inputObj;
	private Expression serviceName;

  public void execute(DelegateExecution execution) {
		String var = (String) inputObj.getValue(execution);
		String input_serviceName = (String) serviceName.getValue(execution);
		System.out.println(input_serviceName + ": In");
		System.out.println("String passed in: " + var);
	    var = var.toUpperCase();
	    System.out.println("String in uppercase: " + var);
	    execution.setVariable("name", var);	
	    System.out.println(input_serviceName + ": Out");
  }
  
}

