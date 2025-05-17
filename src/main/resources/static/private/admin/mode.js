var app = angular.module("SuperAdminManagment", []);

app.controller("TransportModeController", function ($scope, $http, $timeout) {
	$scope.form = {};
	$scope.views = {};
	$scope.views.list = true;
    $scope.companies = [];
    $scope.supercompany = "";

	_autoModeListFetch();
	function _autoModeListFetch() {
		$http({
			method: 'GET',
			url: 'mode/findByAll'

		}).then(function successCallback(response) {
			$scope.transmode = response.data.data;
            $scope.fetchTransportDesc();
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
			$scope.locations = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}
 
    _autoCompanyListFetch();
    function _autoCompanyListFetch() {
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

	$scope.fetchMode = function (str) {
		$http({
			method: 'GET',
			params: { 'companyId': str},
			url: 'mode/findByAll'
		}).then(function successCallback(response) {
			$scope.transmode = response.data.data;
			showHideLoad(true);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    $scope.fetchTransportDesc = function () {
        $http({
            method: 'GET',
            url: 'transport/findByAllActive'
        }).then(function successCallback(response) {
            $scope.transports = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

	$scope.addSaveModeMaster = function () {
        $http({
            method: "POST",
            url: 'mode/createMode',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success,_error);
    }

	$scope.editMode = function (id) {
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'mode/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
            $scope.fetchTransportDesc($scope.form.companyId);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    function _success(response) {
		$scope.form = {};
		_autoModeListFetch();
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



});