var app = angular.module("SuperAdminManagment", []);

app.controller("LeaveMasterController", function ($scope, $http, $timeout) {
    $scope.users = [];
    $scope.locations = [];
    $scope.company = [];
    $scope.leaveCompany = {};
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
 

    _autoLeaveListFetch();
    function _autoLeaveListFetch() {
        showHideLoad();
        $http({
            method: 'GET',
            url: 'leave/findByAll'
        }).then(function successCallback(response) {
            $scope.leaveList = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.fetchLeave = function (str) {
        $http({
            method: 'GET',
            params: { 'companyId': str },
            url: 'leave/findByAll'
        }).then(function successCallback(response) {
            $scope.leaveList = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    $scope.addLeaveList = function () {
        $http({
            method: "POST",
            url: 'leave/createLeave',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };

    function _success(response) {
		$scope.form = {};
		_autoLeaveListFetch();
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

    $scope.editLeaveList = function (str) {
        $http({
            method: 'GET',
            url: 'leave/findById',
            params: {
                'id': str,
            },
        }).then(function successCallback(response) {
            $scope.form = response.data;
       
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.activeLeave = function (str) {
        $http({
            method: 'GET',
            url: 'leave/activeLeave',
            params: {
                'id': str,
            },
        }).then(_success, _error);
    };

    $scope.deactiveLeave = function (str) {
        $http({
            method: 'GET',
            url: 'leave/deactiveLeave',
            params: {
                'id': str,
            },
        }).then(_success, _error);
    };

    $scope.changeView = function (view) {
        if (view == "add" || view == "list" || view == "show") {
            $scope.edits = {};
        }
        $scope.views.add = false;
        $scope.views.edit = false;
        $scope.views.list = false;
        $scope.views[view] = true;
    }


});