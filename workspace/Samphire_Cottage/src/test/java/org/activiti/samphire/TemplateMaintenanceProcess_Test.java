package org.activiti.samphire;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;


import javaClasses.SamphireDatabase;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class TemplateMaintenanceProcess_Test {

	private String filename = "C:\\Users\\Administrator\\workspace\\Samphire_Cottage\\src\\main\\resources\\diagrams\\Template Maintenance.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void startProcess() throws Exception {
		System.out.println("Start Template Maintenance Test Process Script");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("templateMaintenanceProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();			
		variableMap.put("manageRoute", "EDIT");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("templateMaintenanceProcess", variableMap);
		assertNotNull(processInstance.getId());
		
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		TaskService taskService =  activitiRule.getTaskService();
		
		// Get the first task	    
	    Task task = taskService.createTaskQuery().singleResult();
	    System.out.println("TASK NAME: " + task.getName());
	    
	    // claim it
	    taskService.claim(task.getId(), "kermit");
	    System.out.println("--Claimed: Kermit" );
	    //runtimeService.setVariable(processInstance.getId(), "decision", "continue"); 
	    
	    //complete it
	    System.out.println("--Human Task Completed: Kermit" );
	    
	    Map<String, Object> taskVariables = new HashMap<String, Object>();
	    taskVariables.put("activeTemplate", "{\"template\":\"SOME</BR>TEXT123456\",\"shortName\":\"GK TEST 2\"}");
	    //{"action" : "complete", "variables" : [{"name":"newTemplate", "value": {"template":"sdsds entered text more more, more","shortName":"Gary Test"}}]}
	    taskService.complete(task.getId(), taskVariables);
	    
		
		System.out.println("End Template Maintenance Test Process Script");

	}
	
}