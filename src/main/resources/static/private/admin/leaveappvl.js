var app = angular.module("AdminManagment", []);

app.controller("LeaveApprovalController", function ($scope, $http, $timeout) {
	$scope.leaveData = [];
	$scope.leaveDataBak = [];

	_autoFetchApprovalData();
	function _autoFetchApprovalData() {
		$scope.nameList = [];
        $scope.uniqueMonths = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'leave_apvl/fetchAprrovalList'
		}).then(function successCallback(response) {
			$scope.leaveData = response.data.data;
			document.getElementById("loader").style.display = "none";
			angular.forEach($scope.leaveData, function (val, key) {
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

            angular.forEach($scope.leaveData, function (item, key) {
                var date = new Date(item.leaveStartDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });
            $scope.leaveDataBak = $scope.leaveData;
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};


	$scope.getDataNameWise = function (userid, month) {
        console.log(month);
        $scope.uniqueMonths = [];
        $scope.leaveData = $scope.leaveDataBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.leaveData, function (item, key) {
            var date = new Date(item.leaveStartDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

        if (month !== undefined) {
            $scope.leaveData = $scope.leaveData.filter(function (item) {
                var date = new Date(item.leaveStartDate);
                return date.getMonth() + 1 === month;
            });
        }
    }

    $scope.getRange = function (total) {
        return Array.from({ length: total }, (_, index) => index + 1);
    };


	$scope.approveData = function (id) {
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to approve data!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, Approve it!'
		}).then((result) => {
			if (result.isConfirmed) {
				document.getElementById("loader").style.display = "block";
	
				$http({
					method: 'PUT',
					url: 'leave_apvl/leaveApproval',
					params: {
						id: id
					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				}).then(_success, _error);
			}
		});
	};
	

	$scope.disapproveData = function (str) {
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to disapprove data!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, DisApprove it!'
		}).then((result) => {
            if (result.isConfirmed) {
					document.getElementById("loader").style.display = "block";
					$http({
						method: 'PUT',
						url: 'leave_apvl/rejectLeave',
						params: {
							"id": str,
						},
						headers: {
							'Content-Type': 'application/json'
						},
						transformResponse: angular.identity

					}).then(_success, _error);
                }
            });
        };

	function _success(response) {
		$('#fileUpladModal').modal('hide');
		_autoFetchApprovalData();
		document.getElementById("loader").style.display = "none";
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
		document.getElementById("loader").style.display = "none";
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
});