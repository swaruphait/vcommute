var mainApp = angular.module("gradeData", []);
		mainApp.controller('gradeDataController', function ($scope, $http) {
			$scope.refresh = function () {
				location.reload();
			}

			_autoGrade();
			function _autoGrade() {
				$http({
					method: 'GET',
					url: 'fetchGrade',

				}).then(function successCallback(response) {
					$scope.grades = response.data;
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			};


			$scope.gradeedit = function (str) {
				$http({
					method: 'GET',
					url: 'editGrade',
					params: {
						"id": str
					},

				}).then(function successCallback(response) {
					$scope.form = response.data;
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			};

			$scope.deletegrade = function (str) {
				$http({
					method: 'GET',
					url: 'deleteGrade',
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
						title: 'Grade Data Deleted',
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

            
        $scope.submitGrade = function () {
            // alert(angular.toJson($scope.form));
            $http({
                method: "POST",
                url: 'addGrade',
                data: angular.toJson($scope.form),
                headers: {
                    'Content-Type': 'application/json'
                },
                transformResponse: angular.identity
            }).then(function successCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Success',
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