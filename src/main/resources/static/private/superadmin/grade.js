var app = angular.module("SuperAdminManagment", []);

app.controller("SubGradeMasterController", function ($scope, $http, $timeout) {
	$scope.form = {};
	$scope.views = {};
	$scope.views.list = true;
	$scope.emp = {};
    $scope.companies = [];
    $scope.supercompany = "";

	_autoGradeListFetch();
	function _autoGradeListFetch() {
		$http({
			method: 'GET',
			url: 'grade/findByAll'

		}).then(function successCallback(response) {
			$scope.grades = response.data.data;
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
 
 

	$scope.fetchGrade = function (str) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'companyId': str },
			url: 'grade/findByAll'
		}).then(function successCallback(response) {
			$scope.grades = response.data.data;
			showHideLoad(true);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.addSaveGrade = function () {
		showHideLoad();
		var method = "POST";
		var url = 'grade/createGrade';
		console.log($scope.form);
		$http({
			method: method,
			url: url,
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success, _error);
	};


	function _success(response) {
		$scope.form = {};
		_autoGradeListFetch();
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
			text: response.data,
			icon: "error",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
	}
	$scope.editGrade = function (id) {
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'grade/findById'

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