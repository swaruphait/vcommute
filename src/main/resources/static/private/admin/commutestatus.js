var mainApp = angular.module("disapproveData", []);
	mainApp.controller('disapproveDataController', function($scope, $http) {
        var today = new Date();
        $scope.date = {
            startdate: "",
            enddate: ""
        };

        $scope.stdt =  new Date(today.getFullYear(), today.getMonth(), 1);
        $scope.eddt = new Date(today.getFullYear(), today.getMonth() + 1, 0);

        console.log("Start Date: "+$scope.stdt);
        console.log("End Date: "+$scope.eddt);

_autotravelData();
function _autotravelData() {
	document.getElementById("loader").style.display = "block";
	$http({
		method: 'GET',
		url: 'fetchAllCommuteData',
        params: {
            "stdate": $scope.stdt,
            "enddate": $scope.eddt
        },
		headers: {
			'Content-Type': 'application/json'
		},
		transformResponse: angular.identity

	}).then(function successCallback(response) {
		document.getElementById("loader").style.display = "none";
		$scope.travelData = JSON.parse(response.data);
		console.log($scope.travelData)
		//alert($scope.travelData)
	}, function errorCallback(response) {
		console.log(response.statusText);
		document.getElementById("loader").style.display = "none";
	});
};

$scope.fetchTravelDataByDate = function(str) {
	document.getElementById("loader").style.display = "block";
	console.log("Start Date: "+$scope.date.startdate);
	console.log("End Date: "+$scope.date.enddate);
	$http({
		method : 'GET',
		url : 'fetchAllCommuteData',
		params : {
			"stdate" : $scope.date.startdate,
			"enddate" : $scope.date.enddate

		},
		headers : {
			'Content-Type' : 'application/json'
		}
	}).then(function successCallback(response) {
		$scope.travelData = response.data;
		console.log("Data: "+response.data);
		document.getElementById("loader").style.display = "none";
		Swal.fire({
			position : 'center',
			icon : 'success',
			title : 'Customer Fetched',
			showConfirmButton : true,
		}).then(function() {
		});
	}, function errorCallback(response) {
		console.log(response.statusText);
		document.getElementById("loader").style.display = "none";
	});
};


});