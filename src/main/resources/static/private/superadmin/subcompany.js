var app = angular.module("superadminManagment", []);

app.controller("SuperAdminController", function ($scope, $http, $timeout) {
	$scope.form = {
		priviledgeType: []
	};
	$scope.subcription = {};
	$scope.views = {};
	showHideLoad(true);
	$scope.views.list = true;


	$scope.openNewCompany = function () {
		$scope.form = {};
		$("#newCompany").modal("show");

	}

	$scope.openEditCompany = function (id) {
		$scope.form = {};
		$("#editCompany").modal("show");
		$scope.findById(id);
	}



	_autoCompanyListFetch();
	function _autoCompanyListFetch() {
		$http({
			method: 'GET',
			url: 'subcompany/findByAll'
		}).then(function successCallback(response) {
			$scope.subcompany = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	_autoCountryListFetch();
	function _autoCountryListFetch() {
		$http({
			method: 'GET',
			url: 'location/findByAllActiveCountry'
		}).then(function successCallback(response) {
			$scope.countries = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.getStateCountryWise = function (id) {
		$http({
			method: 'GET',
			params: { 'countryId': id },
			url: 'location/findAllStateCountryWise'

		}).then(function successCallback(response) {
			$scope.states = response.data.data;

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.getCityStateWise = function (id) {
		$http({
			method: 'GET',
			params: { 'stateId': id },
			url: 'location/findAllCityStateWise'

		}).then(function successCallback(response) {
			$scope.cites = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.findById = function (id) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'subcompany/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
			$scope.getStateCountryWise($scope.form.countryId);
			$scope.getCityStateWise($scope.form.stateId);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.addSubCompany = function () {
		$http({
			method: "POST",
			url: 'subcompany/createCompany',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success, _error);
	};

	$scope.activateSubCompany = function (id) {
		alert("Id: "+id);
		$http({
			method: 'GET',
			params: { 'subId': id },
			url: 'subcompany/activeCompany'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
			$scope.getStateCountryWise($scope.form.countryId);
			$scope.getCityStateWise($scope.form.stateId);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.deActivateSubCompany = function (str) {
		$http({
			method: 'GET',
			params: { 'subId': str },
			url: 'subcompany/deactiveCompany'

		}).then(function successCallback(response) {
	       alert(response.data);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}





	function _success(response) {
		_autoCompanyListFetch();
		Swal.fire({
			text: response.data,
			icon: "success",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
		$scope.form = {};
		$("#newCompany").modal("hide");
		$("#editCompany").modal("hide");
		_autoCompanyListFetch();
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



	$scope.changeView = function (view) {
		if (view == "add" || view == "list" || view == "show") {
			$scope.form = {};
			$scope.views.add = false;
			$scope.views.edit = false;
			$scope.views.list = false;
			$scope.views[view] = true;
		}
	}




});