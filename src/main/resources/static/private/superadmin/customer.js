var app = angular.module("SuperAdminManagment", []);

app.controller("SubCustomerController", function ($scope, $http, $timeout) {
	$scope.form = {};
    $scope.companies = [];
    $scope.customers = [];
    $scope.locations = [];
	$scope.supercompany = "";


	_autoCustomerListFetch();
	function _autoCustomerListFetch() {

		showHideLoad();
		$http({
			method: 'GET',
			url: 'customer/findByAll'

		}).then(function successCallback(response) {
			$scope.customers = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	_autoCompanyListFetch();
	function _autoCompanyListFetch() {
		showHideLoad();
		$http({
			method: 'GET',
			url: 'subcompany/findByAllActive'

		}).then(function successCallback(response) {
			$scope.supercompany =  response.data.data[0].superCompanyName;
			$scope.companies = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}



	$scope.fetchCustomer = function (str) {
		$http({
			method: 'GET',
			params: { 'companyId': str },
			url: 'customer/findByAll'
		}).then(function successCallback(response) {
			$scope.customers = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	_autofetchLocation();
	function _autofetchLocation() {
		$http({
			method: 'GET',
			url: 'location/findByAllCity'
		}).then(function successCallback(response) {
			//	console.log(response);
			$scope.locations = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.addCustomer = function () {
		$http({
			method: "POST",
			url:  "customer/createCustomer",
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success, _error);
	}


	function _success(response) {
		$scope.form = {};
		_autoCustomerListFetch();
		Swal.fire({
			text: response.data,
			icon: "success",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
	}

	function _error(response) {
		Swal.fire({
			text: response.data || "An unexpected error occurred.",
			icon: "error",
			buttonsStyling: false,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		});
	}


	$scope.editCustomer = function (id) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'customer/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	
	$scope.changeView = function (view) {
		if (view == "add" || view == "list" || view == "show") {
			$scope.form = {};
		}
		$scope.views.add = false;
		$scope.views.edit = false;
		$scope.views.list = false;
		$scope.views[view] = true;
	}


});