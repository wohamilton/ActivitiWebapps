package org.woodlodge.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class SetToUpperCase implements JavaDelegate {
	
	private Expression customerName;
	  
	  public void execute(DelegateExecution execution) throws Exception {
		     
		String var = (String) customerName.getValue(execution);
		System.out.println("String passed in: " + var);
	    var = var.toUpperCase();
	    System.out.println("String in uppercase: " + var);
	    execution.setVariable("processName", var);	
	    
		  
	  }
	  
	}
