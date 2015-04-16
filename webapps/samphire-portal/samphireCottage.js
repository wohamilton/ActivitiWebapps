var customPortal = angular.module('customPortal', []);

customPortal.value('userIdValue', "notSet");
customPortal.value('selectedProcessId', "notSet");
customPortal.value('processInstanceId', "notSet");

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
		  when('/enterEnquiryDetails/:taskId', {
			templateUrl: 'templates/enter_enquiry_details.html',
			controller: 'EnterEnquiryDetailsController'
		  }).
		  when('/templateMaintenanceProcess', {
			templateUrl: 'templates/sp_manage_templates.html',
			controller: 'StartManageTemplatesController'
		  }).
		  when('/editExistingTemplate/:taskId', {
			templateUrl: 'templates/edit_existing_template.html',
			controller: 'EditTemplateController'
		 }).
		 when('/deleteTemplate/:taskId', {
			templateUrl: 'templates/delete_template.html',
			controller: 'DeleteTemplateController'
		 }).
		 when('/createNewTemplate/:taskId', {
			templateUrl: 'templates/create_new_template.html',
			controller: 'CreateTemplateController'
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
	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/repository/process-definitions?startableByUser=' + userIdValue;

	$http.get(dynamicUrl).
	  success(function(data) {
		$scope.processList = data;
	  });
	  
	  $scope.editData = {};
	  
	  $scope.setSelectedProcess = function(){
		console.log("Process Started " + $scope.editData.process.name);
		selectedProcessId = $scope.editData.process.id;
		window.location.href="#" + $scope.editData.process.key;
		
	}

	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
	}
	
 
});

customPortal.controller('StartEnquiryResponseController', function($scope, $route, $routeParams, $http) {

		
		$scope.formInput = {processDefinitionId:selectedProcessId, businessKey:"myBusinessKey",variables: [{name:"enquiryDetails", value: {id: "ENQ123",enquiryDate: "01-01-2015",name: "Tom Brown", stayStartDate: "12-12-2014", stayEndDate: "12-12-2014", stayType: "longStay", cost: "350", responder: "Bill"}}]};
		
	  	
			
			var text = $scope.formInput;
			var str = JSON.stringify(text);	
			var jsonVars = str;

			console.log(jsonVars);
			 
			 dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';

			
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					processInstanceId = data.id;
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + processInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#enterEnquiryDetails/" + newTaskId;
						})
				}).
			  error(function(data) {
				console.log(data);
			  })
	
    
		
		$scope.cancel = function(){
			window.location.href="#pleaseSelect";
		}
		
		

});

customPortal.controller('EnterEnquiryDetailsController', function($scope, $route, $routeParams, $http) {

		
		$scope.selectedTaskId = $routeParams.taskId;
		$scope.enquiryDetails = {id: "ENQ123",enquiryDate: "01-01-2014",name: "", stayStartDate: "", stayEndDate: "", templateObject: "", cost: "", responder: ""};
		
	
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId + '/variables/responseTemplates';

		$http.get(dynamicUrl).
		  success(function(data) {
			
			var jsonObj = JSON.parse(data.value);
			$scope.responseTemplates = jsonObj;
		  });
		
				
		$scope.submit = function(){
			console.log($scope.enquiryDetails);
			
			//fix the formatting of the dates
			if ($scope.startDate){
				var split = $scope.startDate.split('-');
				$scope.enquiryDetails.stayStartDate = split[2] + '-' + split[1] + '-' + split[0];
			}
			
			if ($scope.endDate){
				var split = $scope.endDate.split('-');
				$scope.enquiryDetails.stayEndDate = split[2] + '-' + split[1] + '-' + split[0];
			}

			
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
			var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"enquiryDetails\", \"value\" : " + JSON.stringify($scope.enquiryDetails) + "}]}";
			console.log(jsonVars);
			
			
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + processInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#reviewResponse/" + newTaskId;
						})
				})
		}
		
		$scope.cancel = function(){
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
			var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\": \"true\"}]}";
			console.log(jsonVars);
			
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					window.location.href="#pleaseSelect";
					window.location.reload();
				})
		}
		
		

});

customPortal.controller('StartManageTemplatesController', function($scope, $route, $routeParams, $http) {
	
		$scope.createNewFormInput = {processDefinitionId:selectedProcessId, businessKey:"myBusinessKey",variables: [{"name":"manageRoute", "value":"NEW"}]};
		$scope.editFormInput = {processDefinitionId:selectedProcessId, businessKey:"myBusinessKey",variables: [{"name":"manageRoute", "value":"EDIT"}]};
		$scope.deleteFormInput = {processDefinitionId:selectedProcessId, businessKey:"myBusinessKey",variables: [{"name":"manageRoute", "value":"DELETE"}]};
		
		$scope.createNew = function(){
		
			var text = $scope.createNewFormInput;
			var str = JSON.stringify(text);	
			var jsonVars = str;

			console.log(jsonVars);
			 
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';
			 
					
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					var newProcessInstanceId = data.id;
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + newProcessInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#createNewTemplate/" + newTaskId;

						})

				}).
			  error(function(data) {
				console.log(data);
			  })

		}

		$scope.editExisting = function(){

			var text = $scope.editFormInput;
			var str = JSON.stringify(text);	
			var jsonVars = str;

			console.log(jsonVars);
			 
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';
			 
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					var newProcessInstanceId = data.id;
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + newProcessInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#editExistingTemplate/" + newTaskId;
						})
				}).
			  error(function(data) {
				console.log(data);
			  })
		}
		
		$scope.deleteTemplate = function(){
		
			var text = $scope.deleteFormInput;
			var str = JSON.stringify(text);	
			var jsonVars = str;

			console.log(jsonVars);
			 
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';
			
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					var newProcessInstanceId = data.id;
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + newProcessInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#deleteTemplate/" + newTaskId;
						})
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

customPortal.controller('EditTemplateController', function($scope, $route, $routeParams, $http) {

	$scope.selectedTaskId = $routeParams.taskId;
	
	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId + '/variables/responseTemplates';

	$http.get(dynamicUrl).
	  success(function(data) {
		
		var jsonObj = JSON.parse(data.value);
		$scope.responseTemplates = jsonObj;
	  });
	  
	
	
	$scope.done = function(){
		
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"activeTemplate\", \"value\": " + JSON.stringify($scope.selectedTemplateObject) + "}]}";
		console.log(jsonVars);
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}
	
	
	$scope.cancel = function(){
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\": \"true\"}]}";
		console.log(jsonVars);
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}

	 

});

customPortal.controller('DeleteTemplateController', function($scope, $route, $routeParams, $http) {

	$scope.selectedTaskId = $routeParams.taskId;
	
	
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId + '/variables/responseTemplates';

	$http.get(dynamicUrl).
	  success(function(data) {
		
		var jsonObj = JSON.parse(data.value);
		$scope.responseTemplates = jsonObj;
	  });
	  
	
	
	$scope.deleteTemplate = function(){
	
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"activeTemplate\", \"value\": " + JSON.stringify($scope.selectedTemplateObject) + "}]}";
		console.log(jsonVars);
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}
	
	
	$scope.cancel = function(){
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\": \"true\"}]}";
		console.log(jsonVars);
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}

	 

});

customPortal.controller('CreateTemplateController', function($scope, $route, $routeParams, $http) {

	$scope.selectedTaskId = $routeParams.taskId;
	$scope.newTemplateJSON = JSON.parse("{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\":\"false\"}, {\"name\":\"activeTemplate\", \"value\": {\"shortName\": \"\", \"template\": \"\"}}]}");
		
	$scope.done = function(){
	
		//var jsonString = '{"shortName":"' + $scope.newShortName + '","template":"' + $scope.newTemplate + '"}';
		//console.log (jsonString);
		
		//var newTemplateObjectJSON = JSON.parse(text);
	
	
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		//var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\":\"false\"}, {\"name\":\"activeTemplate\", \"value\": " + jsonString + "}]}";
		//console.log(jsonVars);
		console.log($scope.newTemplateJSON);
		
		$http.post(dynamicUrl, $scope.newTemplateJSON).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})
	}
	
	
	$scope.cancel = function(){
		
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
		var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isCancel\", \"value\": \"true\"}]}";
		console.log(jsonVars);
		
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				window.location.href="#pleaseSelect";
				window.location.reload();
			})

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
						
			//***************************************************Need to do something here to make it safe html
			//console.log(data[3].value);
			$scope.responseText = data[3].value;

			
			
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
	
	$scope.back = function(){
			dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks/' + $scope.selectedTaskId;
			var jsonVars = "{\"action\" : \"complete\", \"variables\" : [{\"name\":\"isBack\", \"value\": \"true\"}]}";
			console.log(jsonVars);
			
			$http.post(dynamicUrl, jsonVars).
				success(function(data) {
					dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?processInstanceId=' + processInstanceId ;
					
					$http.get(dynamicUrl).
						success(function(data) {
							var newTaskId = data.data[0].id
							window.location.href="#enterEnquiryDetails/" + newTaskId;
						})
				})
		}
	
	$scope.cancel = function(){
		window.location.href="#pleaseSelect";
		window.location.reload();
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
	userIdValue = userId;
	dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/identity/users/' + userId;
	$http.get(dynamicUrl).success(function(data) {$scope.user = data;})
	
	console.log("Searching using the candidate or assigned user");
	
	//get the list of tasks for the logged in user
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
		dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/tasks?candidateOrAssigned=' + userId;
		$http.get(dynamicUrl).success(function(data) {$scope.tasks = data;})
	}
	
		
}

