package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestCreateSalesOrder {

	private String filename = "C:\\Users\\Administrator\\workspace\\Order Creation\\src\\main\\resources\\diagrams\\Create Sales Order.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	//Create a serialized order in JSON
//	String str = "{ \"name\": \"Green Pot\", \"id\": GRE123, \"number\": 20 }";
//	JSONObject obj = new JSONObject(str);
//	String n = obj.getString("name");
//	int a = obj.getInt("age");

	

	@Test
	public void startProcess() throws Exception {
		System.out.println("Start Test Process Script");
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("createSalesOrder.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
//		String orderStr = "{\"orderRequest\": {\"id\": \"ORDER_REQ123\",\"date\": \"01/01/2015\",\"seller\": {\"id\": \"SELLER123\",\"name\": \"Tom Brown\"}, \"customer\": {\"id\": \"NOR24\",\"name\": \"Norton Green Farm Nurseries\",\"isExistingCustomer\": \"true\"},\"item\": [{\"stockCode\": \"ZCTREE\",\"name\": \"Red Pot\",\"quantity\": 1000000,\"agreedPrice\": \"4.95\"	},{\"stockCode\": \"TANGO\",\"name\": \"Green Pot\",\"quantity\": 23,\"agreedPrice\": \"15.25\"},]}}";
		String orderStr = "{\"id\":\"ORDER_REQ123\",\"date\":\"01/01/2015\",\"seller\":{\"id\":\"SELLER123\",\"name\":\"Tom Brown\"},\"customer\":{\"id\":\"NOR24\",\"name\":\"Norton Green Farm Nurseries\",\"isExistingCustomer\":\"true\"},\"items\":[{\"stockCode\":\"ZCTREE\",\"name\":\"Red Pot\",\"quantity\":\"100\",\"agreedPrice\":\"4.95\"},{\"stockCode\":\"TANGO\",\"name\":\"Green Pot\",\"quantity\":\"23\",\"agreedPrice\":\"15.25\"}]}";
		
		variableMap.put("orderRequest", orderStr);
		
//		variableMap.put("customerId", "CUST1");
//		variableMap.put("item1StockCode", "TANGO");
//		variableMap.put("item1Qty", 430000);
//		variableMap.put("item1Price", 12.99);
//		variableMap.put("item2StockCode", "ZCTREE");
//		variableMap.put("item2Qty", 21);
//		variableMap.put("item2Price", 18.99);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("createSalesOrder", variableMap);
		assertNotNull(processInstance.getId());
		//System.out.println("id " + processInstance.getId() + " "
		//		+ processInstance.getProcessDefinitionId());
		
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
	    taskService.complete(task.getId());
	    
		
		
		
		System.out.println("End Test Process Script");
	}
}