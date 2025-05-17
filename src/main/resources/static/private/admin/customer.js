var mainApp = angular.module("customerData", []);
mainApp.controller('customerDataController', function($scope, $http) {

	_autoLocation();
	function _autoLocation() {
		$http({
			method: 'GET',
			url: 'fetchAllCity',

		}).then(function successCallback(response) {
			$scope.locations = response.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	$scope.refresh = function() {
		location.reload();
	}

	_autoCustomer();
	function _autoCustomer() {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchCustomer'
		}).then(function successCallback(response) {
			console.log(response);
			$scope.customers = response.data;
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};

	$scope.customeredit = function(str) {
		$http({
			method: 'GET',
			url: 'editcustomer',
			params: {
				"id": str
			}
		}).then(function successCallback(response) {
			$scope.form = response.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	$scope.searchAreaDetails = function(address) {
		$http({
			method: 'GET',
			url: 'getLatLongCutomer',
			params: {
				"address": address
			},
	//		transformResponse: angular.identity
		}).then(function successCallback(response) {	
			$scope.form.latitude = response.data.latitude;
			$scope.form.longitude = response.data.longitude;
			$scope.form.branch_area = response.data.sortArea;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};



	$scope.deletecustomer = function(str) {
		$http({
			method: 'GET',
			url: 'deletecustomer',
			params: {
				"id": str
			},
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Customer Data Deleted',
				showConfirmButton: true,
			}).then(function() {
				location.reload();
			});
		}, function errorCallback(response) {
			alert("failed");
			console.log(response.statusText);
		});
	};
	
	$scope.submitCustomer = function() {
		document.getElementById("loader").style.display = "block";
		$http({
			method: "POST",
			url: 'addCustomer',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
		}).then(function successCallback(response) {
		document.getElementById("loader").style.display = "none";
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: response.data,
				showConfirmButton: true,
			}).then(function() {
				location.reload();
			});
		}, function errorCallback(response) {
		document.getElementById("loader").style.display = "none";
			if (response.status >= 400 && response.status < 500) {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: "Client Error: Something Went Wrong! Please try with proper value",
					showConfirmButton: true,
				});
			} else if (response.status >= 500) {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: "Server Error: Sorry, an unexpected error occurred. Please try again later.",
					showConfirmButton: true,
				});
			} else {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: "An error occurred. Please try again.",
					showConfirmButton: true,
				});
			}
		});
	};
});