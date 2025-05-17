var mainApp = angular.module("attendData", []);

mainApp.controller('attendDataController', function ($scope, $http) {
	_autotravelData();
	function _autotravelData() {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchAttendApprovalData',
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			$scope.attendData = JSON.parse(response.data);
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};

	$scope.approveAttendData = function (str) {

		Swal.fire({
			title: 'Are you sure?',
			text: "You want to approve attendance!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, Approve it!'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: 'approvAttendData',
					params: {
						"id": str

					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity

				}).then(function successCallback(response) {
					document.getElementById("loader").style.display = "block";
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Approved',
						showConfirmButton: true,
					}).then(function () {
						document.getElementById("loader").style.display = "none";
						location.reload();
					});
				}, function errorCallback(response) {
					alert("failed");
					console.log(response.statusText);
				});
			} else {
				location.reload();
			}
		})
	};


	$scope.disapproveAttendData = function (str) {
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to disapprove attendance!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, DisApprove it!'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: 'disapprovAttendData',
					params: {
						"id": str

					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity

				}).then(function successCallback(response) {
					document.getElementById("loader").style.display = "block";
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Disapprove Successfully',
						showConfirmButton: true,
					}).then(function () {
						document.getElementById("loader").style.display = "none";
						location.reload();
					});
				}, function errorCallback(response) {
					alert("failed");
					console.log(response.statusText);
				});
			} else {
				location.reload();
			}
		})
	}

	$scope.GetValueForApprove = function (str) {
		$scope.multiCheck = [];
		var a = 0;
		for (var i = 0; i < $scope.attendData.length; i++) {
			if ($scope.attendData[i].selected) {
				$scope.multiCheck[a] = $scope.attendData[i].id;
			}
			a++;
		}
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to Approved All Attendance!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, Approve it!'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: 'approvAttendDataList',
					params: {
						"ids": $scope.multiCheck

					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity

				}).then(function successCallback(response) {
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Approved',
						showConfirmButton: true,
					}).then(function () {
						location.reload();
					});
				}, function errorCallback(response) {
					alert("failed");
					console.log(response.statusText);
				});
			} else {
				location.reload();
			}
		})
	}

	$scope.selected = [];
	$scope.exist = function (item) {
		return $scope.selected.indexOf(item) > -1;

	}

	$scope.GetValueForDisApprove = function (str) {
		$scope.multiCheck = [];
		var a = 0;
		for (var i = 0; i < $scope.attendData.length; i++) {
			if ($scope.attendData[i].selected) {
				$scope.multiCheck[a] = $scope.attendData[i].id;
			}
			a++;
		}
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to Disapproved All Attendance!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, DisApprove it!'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: 'disapprovAttendDataList',
					params: {
						"ids": $scope.multiCheck

					},
					data: angular.toJson($scope.multiCheck),
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity

				}).then(function successCallback(response) {
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Disapproved',
						showConfirmButton: true,
					}).then(function () {
						location.reload();
					});
				}, function errorCallback(response) {
					alert("failed");
					console.log(response.statusText);
				});

			} else {
				location.reload();
			}
		})
	}
});