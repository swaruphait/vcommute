var app = angular.module("SuperAdminManagment", []);

app.controller("TransportDescController", function ($scope, $http, $timeout) {
	$scope.form = {};
	$scope.views = {};
	$scope.views.list = true;
    $scope.companies = [];
    $scope.supercompany = "";

	_autoTransDescListFetch();
	function _autoTransDescListFetch() {
		showHideLoad();
		$http({
			method: 'GET',
			url: 'transport/findByAll'

		}).then(function successCallback(response) {
			$scope.transDescs = response.data.data;
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
 
	$scope.fetchTransDesc = function (str) {
		$http({
			method: 'GET',
			params: { 'companyId': str},
			url: 'transport/findByAll'
		}).then(function successCallback(response) {
			$scope.transDescs = response.data.data;
			showHideLoad(true);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.addSaveTransportDesc = function () {
		$http({
			method: "POST",
			url: 'transport/createTransport',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success,_error);
	}

	$scope.editTransportDesc = function (id) {
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'transport/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    function _success(response) {
		$scope.form = {};
		_autoTransDescListFetch();
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