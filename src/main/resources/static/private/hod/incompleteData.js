var mainApp = angular.module("incompleteData", []);
mainApp.controller('incompleteDataController', function ($scope, $http) {
	var today = new Date();
	$scope.date = {
		startdate: "",
		enddate: ""
	};

	$scope.stdt  = new Date(today.getFullYear(), today.getMonth() - 2, 1);
	$scope.eddt  = new Date(today.getFullYear(), today.getMonth() + 1, 0);
	console.log("Start Date: "+$scope.stdt);
	console.log("End Date: "+$scope.eddt);
	//*************Unfish Travel Data***********************
	_fetchUnfinishtravelDataHod();
	function _fetchUnfinishtravelDataHod() {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'unfinishCommuteAuthority',
			params: {
				"stdate": $scope.stdt,
				"enddate": $scope.eddt
			},
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			//console.log(response.data);
			/* 	alert(response.data) */
			$scope.travelDataHod = JSON.parse(response.data);
			document.getElementById("loader").style.display = "none";
			//alert($scope.travelData)
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};
	// $scope.resetFilter = function() {
	// 	$http({
	// 		method: 'GET',
	// 		url: 'UnfinishedTravelData',
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity
	// 	}).then(function successCallback(response) {
	// 		$scope.travelDataHod = JSON.parse(response.data);
	// 	}, function errorCallback(response) {
	// 		console.log(response.statusText);
	// 	});
	// };
	$scope.fetchUnfinishTravelDataHod = function (str) {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'unfinishCommuteAuthority',
			params: {
				"stdate": $scope.date.startdate,
				"enddate": $scope.date.enddate
			},
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			$scope.travelDataHod = JSON.parse(response.data);
			document.getElementById("loader").style.display = "none";
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Data  Fetched',
				showConfirmButton: true,
			}).then(function () {
			});
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};

});