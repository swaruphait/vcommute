var mainApp = angular.module("advanceData", []);
mainApp.controller('advanceDataController', function ($scope, $http) {
    var today = new Date();

    $scope.users = [];
    $scope.advanceBak = [];
    $scope.nameList = [];
    $scope.date = {
        startdate: "",
        enddate: ""
    };
    $scope.stdt  = new Date(today.getFullYear(), today.getMonth() - 3, 1);
	$scope.eddt  = new Date(today.getFullYear(), today.getMonth() + 1, 0);
	console.log("Start Date: "+$scope.stdt);
	console.log("End Date: "+$scope.eddt);

    $scope.refresh = function () {
        location.reload();
    }

    _autoUsers();
    function _autoUsers() {
        $http({
            method: 'GET',
            url: 'fetchUserDeatils',

        }).then(function successCallback(response) {
            $scope.users = response.data;

        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
    ;

    _autoAdvanceAllowance();
    function _autoAdvanceAllowance() {
        $http({
            method: 'GET',
            url: 'fetchAdavnceCommute',
            params: {
				"stdate": $scope.stdt,
				"enddate": $scope.eddt
			},
            headers: {
				'Content-Type': 'application/json'
			}

        }).then(function successCallback(response) {
            $scope.advanceAllowances = response.data;
            angular.forEach($scope.advanceAllowances, function (val, key) {
                val.clusetrId = id;
                var existingUser = $scope.nameList.find(function (user) {
                    return user.user_id === val.userId;
                });
                if (!existingUser) {
                    $scope.nameList.push({
                        username: val.name,
                        user_id: val.userId
                    });
                }
            });
            $scope.uniqueMonths = [];

                angular.forEach($scope.advanceAllowances, function (item, key) {
                    var date = new Date(item.advanceDate);
                    var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                    var monthNumber = date.getMonth() + 1;
                    var monthYear = `${monthName}${date.getFullYear()}`;
                    var monthObj = { month: monthNumber, name: monthYear };

                    if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                        $scope.uniqueMonths.push(monthObj);
                    }
                });
            $scope.advanceBak = $scope.advanceAllowances;

        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
    ;

    $scope.filterByMonth = function (month) {
        $scope.advanceAllowances = $scope.advanceBak.filter(function (item) {
            var date = new Date(item.advanceDate);
            return date.getMonth() + 1 === month;
        });
    };

    $scope.getDataNameWise = function (userid) {
        $scope.uniqueMonths = [];
        $scope.advanceAllowances = $scope.advanceBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.advanceAllowances, function (item, key) {
            var date = new Date(item.advanceDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

    }
    $scope.fetchAdvanceDateWise = function (str) {
		$http({
			method: 'GET',
			url: 'fetchAdavnceCommute',
			params: {
				"stdate": $scope.date.startdate,
				"enddate": $scope.date.enddate
			},
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			$scope.advanceAllowances = JSON.parse(response.data);
            angular.forEach($scope.advanceAllowances, function (val, key) {
                val.clusetrId = id;
                var existingUser = $scope.nameList.find(function (user) {
                    return user.user_id === val.userId;
                });
                if (!existingUser) {
                    $scope.nameList.push({
                        username: val.name,
                        user_id: val.userId
                    });
                }
            });
            $scope.uniqueMonths = [];

                angular.forEach($scope.advanceAllowances, function (item, key) {
                    var date = new Date(item.advanceDate);
                    var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                    var monthNumber = date.getMonth() + 1;
                    var monthYear = `${monthName}${date.getFullYear()}`;
                    var monthObj = { month: monthNumber, name: monthYear };

                    if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                        $scope.uniqueMonths.push(monthObj);
                    }
                });
            $scope.advanceBak = $scope.advanceAllowances;s
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Data  Fetched',
				showConfirmButton: true,
			}).then(function () {
			});
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

    $scope.advanceAllowanceedit = function (id) {
        $http({
            method: 'GET',
            url: 'fetchAdavnceById',
            params: {
                "id": id
            }
        })
            .then(
                function successCallback(response) {
                    $scope.form= response.data;
                    var date = new Date(response.data.advanceDate);
                    $scope.form.advanceDate = date;
                },
                function errorCallback(response) {
                    console.log(response.data);
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: response.data,
                        showConfirmButton: true,
                    }).then(function () {
                    });
                });
    };

    $scope.deleteAdvance = function (str) {
        $http({
            method: 'POST',
            url: 'deleteAdavnceCommute',
            params: {
                "id": str

            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Cluster Deleted',
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.data);
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
            });
        });
    };

    $scope.submitAdvanceAllowance = function () {
        $http({
            method: "POST",
            url: 'addAdavnceCommute',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Data Saved',
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.data);
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
            });
        });
    };

    $scope.editAdvanceAllowance = function () {
        $http({
            method: "POST",
            url: 'editAdavnceCommute',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Data Saved',
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.data);
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
            });
        });
    };

});