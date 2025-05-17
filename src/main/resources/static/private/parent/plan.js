var app = angular.module("SubcriptionManagment", []);

app.controller("PlanController", function ($scope, $http, $timeout) {
	$scope.form = {};

    _autoPlanListFetch();
	function _autoPlanListFetch() {
		$http({
			method: 'GET',
			url: 'plan/findByAll'

		}).then(function successCallback(response) {
			console.log(response);
			$scope.plans = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    $scope.openNewPlan = function () {
		$scope.form = {};
		$("#newPlan").modal("show");

	}

	$scope.openEditPlan = function (str) {
		$scope.form = {};
		$("#editPlan").modal("show");
		$scope.editPlan(str);

	}
    $scope.addEditPlan = function () {
        $http({
            method: "POST",
            url: 'plan/addPlan',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    function _success(response) {
		$("#newPlan").modal("hide");
		$("#editPlan").modal("hide");
        _autoPlanListFetch();
		Swal.fire({
			text: response.data.message,
			icon: "success",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
		$scope.form={};
	}

	function _error(response) {
		Swal.fire({
			text: response.data.message,
			icon: "error",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
	}


    $scope.editPlan = function (id) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'plan/findById'

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
		$scope.views.subscription = false;
		$scope.views.subscriptionEdit = false;
		$scope.views[view] = true;
	}



});