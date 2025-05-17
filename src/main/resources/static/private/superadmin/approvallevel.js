var app = angular.module("SuperAdminManagment", []);

app.controller("ApprovalLevelController", function ($scope, $http, $timeout) {
	$scope.users = [];
	$scope.locations = [];
	$scope.companies = [];
	$scope.apvlevels = [];
	$scope.supercompany = "";
	$scope.form = {
		title: "",
		details: {
			id: [],
			userId: [],
			apvlLevel: []
		}
	}
	$scope.form.details = [];
	$scope.edits = {
		title: "",
		details: {
			id: [],
			userId: [],
			apvlLevel: []
		}
	}
	$scope.edits.details = [];
	$scope.logdet = "";
	$scope.addApproveOrderHidden = false;
	$scope.editApproveOrderHidden = true;
	$scope.views = {};
	$scope.views.list = true;

	$scope.toggleForms = function () {
		$scope.addApproveOrderHidden = !$scope.addApproveOrderHidden;
		$scope.editApproveOrderHidden = !$scope.editApproveOrderHidden;
	};



	_autoApprovalOrderListFetch();
	function _autoApprovalOrderListFetch() {
		$http({
			method: 'GET',
			url: 'approval_level/findByAll'
		}).then(function successCallback(response) {
			$scope.approvalOrders = response.data;
			$scope.getUsers();
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
			$scope.supercompany = response.data.data[0].superCompanyName;
			$scope.companies = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.fetchapprovalOrder = function (str) {
		$http({
			method: 'GET',
			params: { 'companyId': str},
			url: 'approval_level/findByAll'
		}).then(function successCallback(response) {
			$scope.approvalOrders = response.data;
			showHideLoad(true);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.apvleveledit = function (str) {
		$http({
			method: 'GET',
			url: 'approval_level/findById',
			params: {
				"id": str
			}
		}).then(function successCallback(response) {
			$scope.form = response.data;
			$scope.form.companyId = $scope.form.companyId;
			$scope.form.details = $scope.form.approvalDetails;
			$scope.getUsers();
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	$scope.getUsers = function (str) {
		if (str == null) {
			str = 0;
		}
		$http({
			method: 'GET',
			url: 'user/fetchActiveUserList',
			params: {
				"companyId": str
			},

		}).then(function successCallback(response) {
			$scope.users = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	_autoRow();
	function _autoRow() {
		$scope.edits.details.push({
			'id': '',
			'userId': '',
			'apvlLevel': ''
		});
	}
	_autoRow1();
	function _autoRow1() {
		$scope.form.details.push({
			'id': '',
			'userId': '',
			'apvlLevel': ''
		});
	}

	$scope.addRow = function (str) {
		$scope.edits.details.push({
			'id': '',
			'userId': '',
			'apvlLevel': ''
		});
	};

	$scope.addRow1 = function (str) {
		$scope.form.details.push({
			'id': '',
			'userId': '',
			'apvlLevel': ''
		});
	};

	$scope.deleteRow = function (str) {
		if ($scope.edits.details.length != 1) {
			$scope.edits.details.splice(str, 1);
		} else {
			alert('Atleast one row needed!');
		}

	}
	$scope.deleteRow1 = function (str) {
		if ($scope.form.details.length != 1) {
			$scope.form.details.splice(str, 1);
		} else {
			alert('Atleast one row needed!');
		}
	}

	$scope.addSaveApvLevel = function () {
		$http({
			method: "POST",
			url: 'approval_level/createAppvlLevel',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success,_error);
	}

	$scope.editApvLevel = function () {
		$http({
			method: "PUT",
			url: 'approval_level/editAppvlLevel',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(_success,_error);
			
	}



	function _success(response) {
		$scope.form = {};
		_autoApprovalOrderListFetch();
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