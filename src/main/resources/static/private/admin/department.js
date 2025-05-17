var mainApp = angular.module("departmentData", []);
mainApp.controller(
    'departmentController',
    function ($scope, $http) {
        $scope.department = [];
        $scope.refresh = function () {
            location.reload();
        }

        _autoLevels();
        function _autoLevels() {
            $http({
                method: 'GET',
                url: 'fetchDepartment',

            }).then(function successCallback(response) {
                $scope.department = response.data;
            }, function errorCallback(response) {
                console.log(response.statusText);
            });
        }
        ;

        $scope.deleteDepartment = function (str) {
            $http({
                method: 'GET',
                url: 'deleteDepartment',
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
                    title: response.sata,
                    showConfirmButton: true,
                }).then(function () {
                    location.reload();
                });
            }, function errorCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: response.sata,
                    showConfirmButton: true,
                })
            });
        };

        $scope.editDepartment = function (str) {
            $http({
                method: 'GET',
                url: 'editDepartment',
                params: {
                    "id": str
                }

            }).then(function successCallback(response) {
                $scope.form = response.data;
            }, function errorCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: response.sata,
                    showConfirmButton: true,
                })
            });
        };

        $scope.submitLevelDes = function () {
            $http({
                method: "POST",
                url: 'addDepartment',
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