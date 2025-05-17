var mainApp = angular.module("clusterData", []);
		mainApp.controller('clusterDataController', function ($scope, $http) {

			$scope.refresh = function () {
				location.reload();
			}

			_autoCluster();
			function _autoCluster() {
				$http({
					method: 'GET',
					url: 'fetchCluster',

				}).then(function successCallback(response) {
					$scope.cluster = response.data;
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			}
			;

		
			$scope.clusterEdit = function (str) {
				$http({
					method: 'PUT',
					url: 'editCluster',
					params: {
						"id": str
					}

				}).then(function successCallback(response) {
					$scope.form = response.data;					
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			};

			$scope.deleteCluster = function (str) {
				$http({
					method: 'DELETE',
					url: 'deleteCluster',
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
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: response.data,
						showConfirmButton: true,
					})
				});
			};

			$scope.submitCluster = function () {
				$http({
					method: 'POST',
					url: 'addCluster',
					data: angular.toJson($scope.form),
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				}).then(function successCallback(response) {
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: response.data,
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