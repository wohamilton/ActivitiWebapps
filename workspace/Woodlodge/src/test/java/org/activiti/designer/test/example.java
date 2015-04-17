package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class example {

	private String filename = "C:\\GK\\activiWS\\newws\\Demo\\src\\main\\resources\\diagrams\\expense process.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("expenseProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		//variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("expenseProcess", variableMap);
		assertNotNull(processInstance.getId());
		
		TaskService taskService =  activitiRule.getTaskService();
		
		// Get the first task	    
	    Task task = taskService.createTaskQuery().singleResult();
	    System.out.println("TASK NAME: " + task.getName());
	    
	    // claim it
	    taskService.claim(task.getId(), "kermit");
	    System.out.println("--Claimed: Kermit" );
	    
	    taskService.setVariable(task.getId(), "variableToWork", "variable set in first task");

	    // Complete the task
	    System.out.println("--Completed" );
	    taskService.complete(task.getId());
		
		
		
		
		
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
}