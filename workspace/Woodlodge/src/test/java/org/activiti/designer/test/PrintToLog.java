package org.activiti.designer.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;


public class PrintToLog implements JavaDelegate {

  private Expression serviceName;

  public void execute(DelegateExecution execution) {
	  
	  String var = (String) serviceName.getValue(execution);
	  System.out.println("System Service Complete: " + var);
	 
  }
  
}