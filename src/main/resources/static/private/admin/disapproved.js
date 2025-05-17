var app = angular.module("AdminManagment", []);
app.controller("DisapprovalController", function ($scope, $http, $timeout) {

	$scope.disApprovedDataList = [];
	$scope.disapprovedDataBck = [];


	_autoDisapproveListFetch();

	function _autoDisapproveListFetch() {
		$scope.nameList = [];
        $scope.uniqueMonths = [];
		$http({
			method: 'GET',
			url: 'commute/fetchDisApprovalList',
		}).then(function successCallback(response) {
			console.log(response);
			$scope.disApprovedDataList = response.data.data;
			angular.forEach($scope.disApprovedDataList, function (val, key) {
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

            angular.forEach($scope.disApprovedDataList, function (item, key) {
                var date = new Date(item.startDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });
            $scope.disapprovedDataBak = $scope.disApprovedDataList;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.getDataNameWise = function (userid, month) {
        console.log(month);
        $scope.uniqueMonths = [];
        $scope.disApprovedDataList = $scope.disapprovedDataBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.disApprovedDataList, function (item, key) {
            var date = new Date(item.startDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

        if (month !== undefined) {
            $scope.disApprovedDataList = $scope.disApprovedDataList.filter(function (item) {
                var date = new Date(item.startDate);
                return date.getMonth() + 1 === month;
            });
        }
    }

    $scope.getRange = function (total) {
        return Array.from({ length: total }, (_, index) => index + 1);
    };



	$scope.approveData = function (id, price) {
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
				$http({
					method: 'PUT',
					url: 'commute/reApprovedData',
					params: {
						id: id,
						price: price
					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				}).then(_success, _error);
			}
		});
	};


	$scope.setImageAsViewed = function () {
		$scope.imageIsViewed = true;
	};

	$scope.uploadImage = function (file, id) {
		console.log(file);
		console.log(id);
		var formData = new FormData;
		formData.append("Images", file);
		formData.append("id", id);
		console.log(formData);
		var config = {
			transformRequest: angular.identity,
			headers: {
				'Content-Type': undefined
			}
		}
		var url = "commute/addDocument";
		$http.post(url, formData, config).then(
			function successCallback(response) {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: 'Upload Faileds',
					showConfirmButton: true,
				}).then(function () {
					location.reload();
				});
				$('#fileUpladModal').modal('hide');
			}, function errorCallback(response) {
				Swal.fire({
					position: 'center',
					icon: 'success',
					title: 'Upload Succesfull',
					showConfirmButton: true,
				}).then(function () {
					location.reload();
					$('#fileUpladModal').modal('hide');
				});
			})
	};

	$scope.form = {};
	$scope.upladFileModal = function (id) {
		$('#fileUpladModal').modal('show');
		$scope.form.id = id;
	}
	$scope.setImageAsViewed = function (index, image) {
		$('#imageViewModal').modal('show');
		$scope.view_image = image;
	}

	function _success(response) {
		$('#fileUpladModal').modal('hide');
		_autoDisapproveListFetch();
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