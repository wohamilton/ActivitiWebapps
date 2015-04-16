var customPortal = angular.module('customPortal', []);


//Define Routing for the portal
//If the user selects a certain type of task in the tasklist, we need to define the routing to the view port here
//This is the html page will be displayed in the ng-view div and the controller that serves it

customPortal.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		  when('/notifySalesRep/:taskId', {
			//templateUrl: 'templates/notify_sales_rep.html',
			//controller: 'NotifySalesRepController'
			templateUrl: 'templates/notify_sales_office.html',
			controller: 'NotifySalesOfficeController'
		}).
		  when('/notifySalesOffice/:taskId', {
			templateUrl: 'templates/notify_sales_office.html',
			controller: 'NotifySalesOfficeController'
		  }).
		  when('/completionConfirmation/:taskId', {
			templateUrl: 'templates/completion_confirmation.html',
			controller: 'CompletionConfirmationController'
		  }).
		  when('/processlist', {
			templateUrl: 'templates/process_list.html',
			controller: 'ProcessListController'
		  }).
		 when('/createSalesOrder', {
			templateUrl: 'templates/spp_createSalesOrder.html',
			controller: 'StartProcessController'
			
		  }).
		  when('/pleaseSelect', {
			templateUrl: 'templates/please_select.html',
			controller: 'PleaseSelectController'
			
		  }).
		  otherwise({
			//templateUrl: 'templates/no_screen_found.html',
			//controller: 'NoScreenFoundController'
		  });
}]);
	 
	 
customPortal.controller('NotifySalesRepController', function($scope, $routeParams, $http) {

	$scope.selectedTaskId = $routeParams.taskId;
	 
});
 
 
customPortal.controller('NotifySalesOfficeController', function($scope, $route, $routeParams, $http) {
 
	$scope.selectedTaskId = $routeParams.taskId;
	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;

	$http.get(dynamicUrl).
	  success(function(data) {
		$scope.selectedTask = data;
	  });
	 
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId + "/variables";

	$http.get(dynamicUrl).
	  success(function(data) {
		var jsonObj = JSON.parse(data[0].value);
			
		$scope.selectedTaskData = jsonObj;
		
		console.log(jsonObj.items[0]);
	 });
	 
		
	$scope.approve = function(){
		
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\" : \"decision\", \"value\" : \"continue\"}]}";
				
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
		});

	}
	
	$scope.reject = function(){
		
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\" : \"decision\", \"value\" : \"cancel\"}]}";
				
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
		});
	}
	
	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
	}
	

		
	
 
});

customPortal.controller('ProcessListController', function($scope, $route, $routeParams, $http) {
 	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/repository/process-definitions';

	$http.get(dynamicUrl).
	  success(function(data) {
		$scope.processList = data;
	  });

	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
	}
	
 
});


customPortal.controller('StartProcessController', function($scope, $route, $routeParams, $http) {

		$scope.formInput = {processDefinitionId:"createSalesOrder:2:124076", businessKey:"myBusinessKey",variables: [{name:"orderRequest", value: {id: "ORDER_REQ123",date: "01-01-2015",seller: {id: "SELLER123",name: "Tom Brown"}, customer: {id: "NOR24", name: "Norton Green Farm Nurseries", "isExistingCustomer": 'true'}, items: [{stockCode: "ZCTREE", name: "Red Pot", quantity: "1000000", agreedPrice: "4.95"},{stockCode: "TANGO",name: "Green Pot",quantity: "23", agreedPrice: "15.25"}]}}]};

	  	$scope.submit = function(){
			

		var text = $scope.formInput;
		
		var str = JSON.stringify(text);
				
		var jsonVars = str;
		
		console.log(jsonVars);
		 
		 dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';
		 
				
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
				  }).
		  error(function(data) {
			console.log(data);
		  })
	
        }
		
		$scope.cancel = function(){
			window.location.href="#pleaseSelect";
		}
		
		

});

customPortal.controller('NoScreenFoundController', function($scope) {
 
	$scope.message = 'Uh Oh';
 
});

customPortal.controller('CompletionConfirmationController', function($scope, $http, $routeParams) {
 
	$scope.selectedTaskId = $routeParams.taskId;
	$scope.message = 'Complete';
	$scope.ok = function(){
		
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : []}";
				
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
		});
	}
 
});

customPortal.controller('PleaseSelectController', function($scope) {
 
	$scope.message = 'Please select a task';
	
	$scope.getProcessList = function(){
		window.location.href="#processlist";
	}
 
});

function portalLoginController($scope, $http) {
	
	$scope.login = function(){
	
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/identity/users/' + $scope.userName;

		$http.get(dynamicUrl).
		  success(function(data) {
			$scope.user = data;
			window.location.href="portal.html?userId=" + data.id + "#pleaseSelect";
		  }).
		  error(function(data) {
			$scope.validationMessage = "Invalid User Name";
		  })
	}	
}

function portalController($scope, $http) {
	
	//get the username passed through from the login screen and bring back the user data from the REST API
	var userId = location.search.split('userId=')[1];
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/identity/users/' + userId;
	$http.get(dynamicUrl).success(function(data) {$scope.user = data;})
	
	//get the list of tasks for the logged in user
	//dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?assignee=' + userId;
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?candidateOrAssigned=' + userId;
	$http.get(dynamicUrl).success(function(data) {$scope.tasks = data;})
	
		
	$scope.logOut = function(){
		window.location.href="index.html";
	}
	
	$scope.getProcessList = function(){
		window.location.href="#processlist";
	}
	
	$scope.getTaskList = function(){
		//get the list of tasks for the logged in user
		//dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?assignee=' + userId;
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?candidateOrAssigned=' + userId;
		$http.get(dynamicUrl).success(function(data) {$scope.tasks = data;})
	}
	
		
}

