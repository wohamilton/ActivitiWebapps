var customPortal = angular.module('customPortal', []);

customPortal.service('ActivitiAPIService', function($http, $q){
    this.getData = function(url){
		//dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances/134015/variables';
		
		var deferObject = $q.defer();
			
		$http.get(dynamicUrl).
			success(function(data) {
				deferObject.resolve(data);
			}).
			error(function(data) {
				deferObject.reject(data);
			});
		 
		var promiseObject = deferObject.promise;

		promiseObject  
			.then(function(data) {
				console.log('My first promise succeeded', data);
				//$scope.procInstId = data.processInstanceId;
				return data;
			}, function(error) {
				console.log('My first promise failed', error);
			});
			
        
	}               
});

customPortal.factory('ActivitiAPIFactory', function($http) {
    return {
      getData: function(url) {
         //return $http.get(url);
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
      }
    }
  });

//Define Routing for the portal
//If the user selects a certain type of task in the tasklist, we need to define the routing to the view port here
//This is the html page will be displayed in the ng-view div and the controller that serves it

customPortal.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
		  when('/reviewResponse/:taskId', {
			templateUrl: 'templates/review_response.html',
			controller: 'ReviewResponseController'
		}).
		when('/processlist', {
			templateUrl: 'templates/process_list.html',
			controller: 'ProcessListController'
		  }).
		  when('/enquiryResponseProcess', {
			templateUrl: 'templates/sp_enquiry_response.html',
			controller: 'StartEnquiryResponseController'
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
	 
customPortal.controller('ProcessListController', function($scope, $route, $routeParams, $http) {
 	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/repository/process-definitions?startableByUser' +  ;

	$http.get(dynamicUrl).
	  success(function(data) {
		$scope.processList = data;
	  });

	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
	}
	
 
});

customPortal.controller('StartEnquiryResponseController', function($scope, $route, $routeParams, $http) {

		
		$scope.formInput = {processDefinitionId:"enquiryResponseProcess:5:151466", businessKey:"myBusinessKey",variables: [{name:"enquiryDetails", value: {id: "ENQ123",enquiryDate: "01-01-2015",name: "Tom Brown", stayStartDate: "12-12-2014", stayEndDate: "12-12-2014", stayType: "longStay", cost: "350", responder: "Bill"}}]};
		
		//$scope.formInput = {processDefinitionId:"enquiryResponseProcess:3:134014", businessKey:"myBusinessKey",variables: [{name:"orderRequest", value: {id: "ORDER_REQ123",date: "01-01-2015",seller: {id: "SELLER123",name: "Tom Brown"}, customer: {id: "NOR24", name: "Norton Green Farm Nurseries", "isExistingCustomer": 'true'}, items: [{stockCode: "ZCTREE", name: "Red Pot", quantity: "1000000", agreedPrice: "4.95"},{stockCode: "TANGO",name: "Green Pot",quantity: "23", agreedPrice: "15.25"}]}}]};

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

customPortal.controller('PleaseSelectController', function($scope) {
 
	$scope.message = 'Please select a task';
	
	$scope.getProcessList = function(){
		window.location.href="#processlist";
	}

});

customPortal.controller('ReviewResponseController', function($scope, $routeParams, $http, ActivitiAPIService, $q) {



	$scope.selectedTaskId = $routeParams.taskId;
	
	//get the task variables
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId + "/variables";

	//get the process instance id, to then get the process variables
	
	var defer = $q.defer();
	
	defer.promise.
		then(function(data){
			
			console.log("PROMISE HAS RUN: " + data[1].value);
			
			//***************************************************Need to do something here to make it safe html
			$scope.responseText = data[1].value;

			
			
		});
	
	$http.get(dynamicUrl).
		success(function(data) {
			defer.resolve(data);
		})
	
	$scope.done = function(){
	
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : []}";
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}
	
	
	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
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
	
	console.log("Searching using the candidate or assigned user");
	
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
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?assignee=' + userId;
		$http.get(dynamicUrl).success(function(data) {$scope.tasks = data;})
	}
	
		
}

