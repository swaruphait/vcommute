var mainApp = angular.module("DepartmentManagment", []);
mainApp.controller('DepartmentController', function ($scope, $http) {
    $scope.emp = {};
    $scope.companies = [];
	$scope.supercompany = "";


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


    _autoFetchDepartment();
    function _autoFetchDepartment() {
        $http({
            method: 'GET',
            url: 'department/findByAll',

        }).then(function successCallback(response) {
            $scope.department = response.data.data;

        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.fetchDepartment = function (str) {
        $http({
            method: 'GET',
            params: { 'companyId': str },
            url: 'department/findByAll'
        }).then(function successCallback(response) {
            $scope.department = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.addDepartment = function () {
        $http({
            method: "POST",
            url: 'department/createDepartment',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    }

    function _success(response) {
		$scope.form = {};
		_autoFetchDepartment();
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

    $scope.editDepartment = function (id) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'department/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}
});