var app = angular.module("CompanyManagment", []);

app.controller("SuperAdminController", function ($scope, $http, $timeout) {

	$scope.form = {};
	$scope.views = {};
	$scope.views.list = true;


    $scope.openNewSuperadmin = function () {
		$scope.form = {};
		$("#newSuperadmin").modal("show");

	}

    $scope.openEditSuperadmin = function (str) {
		$scope.form = {};
		$("#editSuperadmin").modal("show");
        $scope.findById(str);

	}


	_autoParentCompanyListFetch()
	function _autoParentCompanyListFetch() {
		$http({
			method: 'GET',
			url: 'company/findByAllActiveList'

		}).then(function successCallback(response) {
			$scope.companies = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    _autoSuperadminListFetch()
	function _autoSuperadminListFetch() {
		$http({
			method: 'GET',
			url: 'user/fetchAllSuperadmin'

		}).then(function successCallback(response) {
			$scope.superadmin = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    
  $scope.userActivation = function (id) {
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'user/activeUser'
    }).then(_success, _error);
};

$scope.userDeactivation = function (id) {
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'user/deactiveUser'
    }).then(_success, _error);
};


function _success(response) {
    _autoParentCompanyListFetch();
    Swal.fire({
        text: response.data.message,
        icon: "success",
        buttonsStyling: !1,
        confirmButtonText: "Ok, got it!",
        customClass: {
            confirmButton: "btn btn-primary"
        }
    })
}

function _error(response) {
    console.log(response);

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
	// $scope.changeStatus = function (id) {
	// 	showHideLoad();
	// 	$http({
	// 		method: 'PUT',
	// 		params: { 'id': id },
	// 		url: 'user/accountStatusToggle',
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity

	// 	}).then(function successCallback(response) {
	// 		console.log(response.data);
	// 		autoAdminListFetch();
	// 		showHideLoad(true);
	// 	}, function errorCallback(response) {
	// 		console.log(response.statusText);
	// 	});
	// }
	// $scope.changeExpiryStatus = function (id) {
	// 	showHideLoad();
	// 	$http({
	// 		method: 'PUT',
	// 		params: { 'id': id },
	// 		url: 'user/accountExpiryToggle',
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity

	// 	}).then(function successCallback(response) {
	// 		console.log(response.data);
	// 		autoAdminListFetch();
	// 		showHideLoad(true);
	// 	}, function errorCallback(response) {
	// 		console.log(response.statusText);
	// 	});
	// }

    
	$scope.checkUserNameAvailability = function (username) {
		showHideLoad();
		$http({
			method: 'GET',
			params: { 'username': username },
			url: 'user/usernameAvailability'

		}).then(function successCallback(response) {
			console.log(response.data);
			if (response.data == false) {
				$('#user-name-error').hide();
				$('#user-name-error-edit').hide();
			} else {
				$('#user-name-error').show();
				$('#user-name-error-edit').show();
			}
			showHideLoad(true);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.addSaveSuperAdmin = function () {
		$http({
			method: "POST",
			url: "user/creatSuperadmin",
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: response.data.message,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.data);
			Swal.fire({
				text: response.data.message,
				icon: "error",
				buttonsStyling: !1,
				confirmButtonText: "Ok, got it!",
				customClass: {
					confirmButton: "btn btn-primary"
				}
			})
        });
    }

	$scope.findById = function (id) {
		$http({
			method: 'GET',
			params: { 'id': id },
			url: 'user/findById'

		}).then(function successCallback(response) {
			$scope.form = response.data.data;
            $scope.form.mobile = Number(response.data.data.mobile);
			$scope.fetchCompanies();
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}




	$scope.editSaveAdmin = function () {
		document.getElementById("loader").style.display = "block";
		console.log($scope.form);
		showHideLoad();
		var method = "PUT";
		var url = 'user/updateUser';
		$http({
			method: method,
			url: url,
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Super Admin Updated',
                showConfirmButton: true,
            }).then(function () {
				document.getElementById("loader").style.display = "none";
                location.reload();
            });
        }, function errorCallback(response) {
			document.getElementById("loader").style.display = "none";
            console.log(response.data);
			Swal.fire({
				text: response.data,
				icon: "error",
				buttonsStyling: !1,
				confirmButtonText: "Ok, got it!",
				customClass: {
					confirmButton: "btn btn-primary"
				}
			})
        });
    }

	$scope.checkUserNameAvailability = function (username) {

		$http({
			method: 'GET',
			params: { 'username': username },
			url: 'user/usernameAvailability'

		}).then(function successCallback(response) {
			console.log(response.data);
			if (response.data == false) {
				$('#user-name-error').hide();
				$('#user-name-error-edit').hide();
			} else {
				$('#user-name-error').show();
				$('#user-name-error-edit').show();
			}
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}
	$scope.checkEmailAvailability = function (email) {
		$http({
			method: 'GET',
			params: { 'email': email },
			url: 'user/emailAvailability'

		}).then(function successCallback(response) {
			console.log(response.data);
			if (response.data == false) {
				$('#email-error').hide();
				$('#email-error-edit').hide();
			} else {
				$('#email-error').show();
				$('#email-error-edit').show();
			}
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.deleteAdmin = function (id) {
		Swal.fire({
			title: 'Are you sure?',
			text: "you want to delete this Data!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, delete it!'
		}).then((result) => {
			if (result.isConfirmed) {
				var method = "DELETE";
				var url = 'user/updateUser';
				$http({
					method: method,
					params: { 'id': id },
					url: url,
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				}).then(function successCallback(response) {
					console.log(response);
					Swal.fire(
						'Deleted!',
						response.data,
						'success'
					)
					autoAdminListFetch();
				}, function errorCallback(response) {
					console.log(response);
				});
			}
		})
	}

$scope.changeView = function (view) {
		if (view == "add" || view == "list" || view == "show") {
			$('#user-name-error').hide();
			$scope.form = {};
		}
		if (view == "edit") {
			$('#user-name-error-edit').hide();
		}
		$scope.views.add = false;
		$scope.views.edit = false;
		$scope.views.list = false;
		$scope.views[view] = true;
	}

});
