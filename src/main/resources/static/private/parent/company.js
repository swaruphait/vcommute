var app = angular.module("CompanyManagment", []);

app.controller("SuperCompanyController", function ($scope, $http, $timeout) {
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

	$scope.openEditSubcription = function (id) {
		$scope.form = {};
		$("#editSubcription").modal("show");
		$scope.findSubcriptionById(id);
	}

	$scope.openEditCompany= function (id) {
		$scope.form = {};
		$("#editCompany").modal("show");
		$scope.findById(id);
	}



	_autoCompanyListFetch();
	function _autoCompanyListFetch() {
		$http({
			method: 'GET',
			url: 'company/findByAll'
		}).then(function successCallback(response) {
			$scope.compines = response.data.data;
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

	_autoPlanListFetch();
	function _autoPlanListFetch() {
		$http({
			method: 'GET',
			url: 'plan/findByAllActiveList'
		}).then(function successCallback(response) {
			$scope.plans = response.data.data;
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
			url: 'company/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
			$scope.getStateCountryWise($scope.form.countryId);
			$scope.getCityStateWise($scope.form.stateId);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.addSuperCompany = function () {
		console.log($scope.form);
			$http({
				method: "POST",
				url: 'company/createSuperCompany',
				data: angular.toJson($scope.form),
				headers: {
					'Content-Type': 'application/json'
				},
				transformResponse: angular.identity
			}).then(function (response) {
			Swal.fire({
				text: "Successfully Company Registered",
				icon: "success",
				buttonsStyling: !1,
				confirmButtonText: "Ok, got it!",
				customClass: {
					confirmButton: "btn btn-primary"
				}
			})
			$("#newCompany").modal("hide");
			_autoCompanyListFetch();
			$scope.form={};

			}).catch(function (error) {
				Swal.fire({
					text: response.data.message,
					icon: "error",
					buttonsStyling: !1,
					confirmButtonText: "Ok, got it!",
					customClass: {
						confirmButton: "btn btn-primary"
					}
				})
				console.error(error);
			});
		};


		$scope.editSuperCompany = function () {
			$http({
				method: "PUT",
				url: 'company/editSuperCompany',
				data: angular.toJson($scope.form),
				headers: {
					'Content-Type': 'application/json'
				},
				transformResponse: angular.identity
			}).then(function (response) {
			Swal.fire({
				text: "Successfully Company Registered",
				icon: "success",
				buttonsStyling: !1,
				confirmButtonText: "Ok, got it!",
				customClass: {
					confirmButton: "btn btn-primary"
				}
			})
			$("#editCompany").modal("hide");
			_autoCompanyListFetch();
			$scope.form={};

			}).catch(function (error) {
				Swal.fire({
					text: response.data.message,
					icon: "error",
					buttonsStyling: !1,
					confirmButtonText: "Ok, got it!",
					customClass: {
						confirmButton: "btn btn-primary"
					}
				})
				console.error(error);
			});
		};


	
		function _success(response) {
			_autoCompanyListFetch();
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
	
		$scope.editSubcription = function () {
			$http({
				method: "POST",
				url: 'subcription/addSubcription',
				data: angular.toJson($scope.subcription),
				headers: {
					'Content-Type': 'application/json'
				},
				transformResponse: angular.identity
			}).then(function (response) {
			Swal.fire({
				text: "Successfully Updated",
				icon: "success",
				buttonsStyling: !1,
				confirmButtonText: "Ok, got it!",
				customClass: {
					confirmButton: "btn btn-primary"
				}
			})
			$("#editSubcription").modal("hide");
			_autoCompanyListFetch();
			$scope.form={};

			}).catch(function (error) {
				Swal.fire({
					text: response.data.message,
					icon: "error",
					buttonsStyling: !1,
					confirmButtonText: "Ok, got it!",
					customClass: {
						confirmButton: "btn btn-primary"
					}
				})
				console.error(error);
			});
		};
		
		$scope.findSubcriptionById = function (id) {
			showHideLoad();
			$http({
				method: 'GET',
				params: { 'id': id },
				url: 'subcription/findById'
			}).then(function successCallback(response) {
				$scope.subcription = response.data.data;
				// Convert date strings to Date objects
				if ($scope.subcription.startDate) {
					$scope.subcription.startDate = new Date($scope.subcription.startDate);
				}
				if ($scope.subcription.endDate) {
					$scope.subcription.endDate = new Date($scope.subcription.endDate);
				}
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		}




		$scope.changeView = function (view) {
			if (view == "add" || view == "list" || view == "show") {
				$scope.form = {};	
		$scope.views.add = false;
		$scope.views.edit = false;
		$scope.views.list = false;
		$scope.views.subscription = false;
		$scope.views.subscriptionEdit = false;
		$scope.views[view] = true;
	  }
	}	


	

});