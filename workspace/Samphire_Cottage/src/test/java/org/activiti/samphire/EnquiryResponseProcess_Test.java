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

public class EnquiryResponseProcess_Test {

	private String filename = "C:\\Users\\Administrator\\workspace\\Samphire_Cottage\\src\\main\\resources\\diagrams\\Enquiry Response.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void startProcess() throws Exception {
		System.out.println("Start Enquiry Response Test Process Script");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("enquiryResponseProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
			
		String enquiryDetails = "{\"id\":\"ENQ123\",\"enquiryDate\":\"01/01/2015\",\"name\":\"Jack Black\",\"stayStartDate\":\"01/01/2015\",\"stayEndDate\":\"01/01/2015\",\"templateObject\":{\"shortName\":\"BILLTEST\", \"template\":\"WAWAWA <name> \"},\"cost\":\"350\",\"responder\":\"Bill\"}";
		
		variableMap.put("enquiryDetails", enquiryDetails);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("enquiryResponseProcess", variableMap);
		assertNotNull(processInstance.getId());
		
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		
		SamphireDatabase database = new SamphireDatabase();
		database.connect();
		
		
		
		
		TaskService taskService =  activitiRule.getTaskService();
		
		// Get the first task	    
	    Task task = taskService.createTaskQuery().singleResult();
	    System.out.println("TASK NAME: " + task.getName());
	    
	    // claim it
	    taskService.claim(task.getId(), "kermit");
	    System.out.println("--Claimed: Kermit" );
	    runtimeService.setVariable(processInstance.getId(), "decision", "continue"); 
	    
	    //complete it
	    System.out.println("--Human Task Completed: Kermit" );
	    Map<String, Object> taskVariables = new HashMap<String, Object>();
	    taskVariables.put("isCancel", "false");
	    taskService.complete(task.getId(), taskVariables);
		
		System.out.println("End Enquiry Response Test Process Script");

	}
	
}