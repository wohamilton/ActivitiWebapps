var mobileApp = angular.module('woodlodgeMobileApp', []);
	 
	 
mobileApp.controller('mobileAppController', function($scope, $http) {
	
	$scope.customerDropdownSelection = "Please Select";	
	$scope.stockDropdownSelection = "Please Select";	
	
	$scope.formInput = {processDefinitionId:"createSalesOrder:3:151493", businessKey:"myBusinessKey",variables: [{name:"orderRequest", value: {id: "ORDER_REQ123",date: "01-01-2015",seller: {id: "SELLER123",name: "Tom Brown"}, customer: {id: "", name: "", "isExistingCustomer": 'true'}, items: []}}]};
	
	$scope.requestedItems = [];
	$scope.stockCode='';
	$scope.name='';
	$scope.quantity='';
	$scope.price='';
	
	$scope.addRow = function(){
	
		var length = $scope.formInput.variables[0].value.items.length;
		$scope.formInput.variables[0].value.items[length] = {stockCode: "", name: "", quantity: "", agreedPrice: ""};
		$scope.formInput.variables[0].value.items[length].stockCode = $scope.stockCode;
		$scope.formInput.variables[0].value.items[length].name = $scope.name;
		$scope.formInput.variables[0].value.items[length].quantity = $scope.quantity;
		$scope.formInput.variables[0].value.items[length].agreedPrice = $scope.agreedPrice;

		
		
		//var text = JSON.stringify($scope.formInput.variables[0].value.items[length]);
		//console.log("THIS IS WHAT IS CEEATED " + text);
	
		
		$scope.requestedItems.push({ 'stockCode':$scope.stockCode, 'name':$scope.name, 'quantity': $scope.quantity, 'price':$scope.agreedPrice });
		$scope.stockCode='';
		$scope.name='';
		$scope.quantity='';
		$scope.agreedPrice='';
		$scope.stockDropdownSelection = 'Please Select';
		
		
		var text = JSON.stringify($scope.formInput);
		//console.log("JSON MESSAGE " + text);
		
		
		$('#addItemModal').modal('hide');
		
		
	};
	
	
		$scope.submit = function(){
		
		var text = $scope.formInput;
		var str = JSON.stringify(text);
		//console.log("TEXT2" + str);		
		var jsonVars = str;	
		console.log(jsonVars);

		 
		 dynamicUrl = 'http://kermit:kermit@awsserver:8080/activiti-rest/service/runtime/process-instances';
		 
				
		$http.post(dynamicUrl, jsonVars).
			success(function(data) {
				//window.location.href="#pleaseSelect";
				window.location.reload();
				  }).
		  error(function(data) {
			console.log(data);
		  })
	
        }
		
		
		
	$scope.selectCustomer = function(customerId){
		
		console.log(customerId);
	
		if (customerId == "NOR24"){
			$scope.customerDropdownSelection = "NOR24";
			$scope.formInput.variables[0].value.customer.id = "NOR24";
			$scope.formInput.variables[0].value.customer.name = "Norton Green Farm Nurseries";
		}else if(customerId == "GAZ"){
			$scope.customerDropdownSelection = "GAZ";
			$scope.formInput.variables[0].value.customer.id = "GAZ";
			$scope.formInput.variables[0].value.customer.name = "Gary's Garden Centre";
		}else{
			$scope.customerDropdownSelection = "BIL";
			$scope.formInput.variables[0].value.customer.id = "BIL";
			$scope.formInput.variables[0].value.customer.name = "Bill's Garden Centre";
		}
		
		//console.log($scope.formInput.variables[0].value.customer);
	}

	$scope.selectStock = function(stockCode){

		
		if (stockCode == "ZCTREE"){
			$scope.stockDropdownSelection = "ZCTREE";
			$scope.stockCode = "ZCTREE";
			$scope.name = "Christmas Tree Pots";
		}else if(stockCode == "TANGO"){
			$scope.stockDropdownSelection = "TANGO";
			$scope.stockCode = "TANGO";
			$scope.name = "Tango Can Pots";
		}else{
			$scope.stockDropdownSelection = "BIL";
			$scope.stockCode = "BIL";
			$scope.name = "Bill Pots";
		}
		
	}
	
	
	
	$scope.addItemPopup = function(){
	
		BootstrapDialog.show({
            message: 'Hi Apple!'
        });
	
	}
	
	
	 
});
 
 
