var app = angular.module("LocationManagment", []);

app.controller("CountryController", function ($scope, $http, $timeout) {
	$scope.form = {};

    _autoCountryListFetch();
	function _autoCountryListFetch() {
		$http({
			method: 'GET',
			url: 'location/findByAllCountry'
		}).then(function successCallback(response) {
			$scope.countries = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

    $scope.addEditCountry = function () {
        $http({
            method: "POST",
            url: 'location/addCountry',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    function _success(response) {
        _autoCountryListFetch();
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

    	$scope.editCountry = function (id) {
            showHideLoad();
            $http({
                method: 'GET',
                params: { 'id': id },
                url: 'location/findByCountryId'
    
            }).then(function successCallback(response) {
                $scope.form = response.data.data;
            }, function errorCallback(response) {
                console.log(response.statusText);
            });
	}

    $scope.countryActivation = function (id) {
        showHideLoad();
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/activeCountry'

        }).then(_success, _error);
}

$scope.countryDeactivation = function (id) {
    showHideLoad();
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'location/deactiveCountry'

    }).then(_success, _error);
}

});

app.controller("StateController", function ($scope, $http, $timeout) {
	$scope.form = {};

    _autoStateListFetch();
	function _autoStateListFetch() {
		$http({
			method: 'GET',
			url: 'location/findByAllState'
		}).then(function successCallback(response) {
			$scope.states = response.data.data;
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


    $scope.addEditStates = function () {
        $http({
            method: "POST",
            url: 'location/addState',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    function _success(response) {
        _autoStateListFetch();
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

    	$scope.editState = function (id) {
            showHideLoad();
            $http({
                method: 'GET',
                params: { 'id': id },
                url: 'location/findByStateId'
    
            }).then(function successCallback(response) {
                $scope.form = response.data.data;
            }, function errorCallback(response) {
                console.log(response.statusText);
            });
	}

    $scope.stateActivation = function (id) {
        showHideLoad();
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/activeState'

        }).then(_success, _error);
}

$scope.stateDeactivation = function (id) {
    showHideLoad();
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'location/deactiveState'

    }).then(_success, _error);
}

});

app.controller("CityController", function ($scope, $http, $timeout) {
	$scope.form = {};

    _autoCityListFetch();
	function _autoCityListFetch() {
		$http({
			method: 'GET',
			url: 'location/findByAllCity'
		}).then(function successCallback(response) {
			$scope.cites = response.data.data;
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

    $scope.addEditCity = function () {
        $http({
            method: "POST",
            url: 'location/addCity',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    function _success(response) {
        _autoCityListFetch();
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

    $scope.editCity = function (id) {
        showHideLoad();
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/findByCityId'
        }).then(function successCallback(response) {
            $scope.form = response.data.data;
            if ($scope.form && $scope.form.countryId) {
                $scope.getStateCountryWise($scope.form.countryId);
            }
        }, function errorCallback(response) {
            console.log("Error: " + response.statusText);
        });
    };
    $scope.cityActivation = function (id) {
        showHideLoad();
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/activeCity'

        }).then(_success, _error);
}

$scope.cityDeactivation = function (id) {
    showHideLoad();
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'location/deactiveCity'

    }).then(_success, _error);
}

});