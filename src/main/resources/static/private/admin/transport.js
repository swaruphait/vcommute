var mainApp = angular.module("transportData", []);
mainApp
    .controller(
        'transportDataController',
        function ($scope, $http) {

            $scope.refresh = function () {
                location.reload();
            }

            _autoLevels();
            function _autoLevels() {
                document.getElementById("loader").style.display = "block";
                $http({
                    method: 'GET',
                    url: 'fetchLevelDescriptions',

                }).then(function successCallback(response) {
                    $scope.levels = response.data;
                    document.getElementById("loader").style.display = "none";
                }, function errorCallback(response) {
                    console.log(response.statusText);
                    document.getElementById("loader").style.display = "none";
                });
            };

            $scope.leveldesedit = function (str) {
                $http({
                    method: 'GET',
                    url: 'editLevelDescriptions',
                    params: {
                        "id": str
                    }

                })
                    .then(
                        function successCallback(response) {
                            $scope.form = response.data;
                        },
                        function errorCallback(response) {
                            console.log(response.statusText);
                        });
            };

            $scope.deleteleveldes = function (str) {
                $http({
                    method: 'GET',
                    url: 'deleteLevelDescriptions',
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
                        title: 'Level Descriptions Data Deleted',
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
                    console.log(response.statusText);
                });
            };

            $scope.submitLevelDes = function () {
                $http({
                    method: "POST",
                    url: 'addLevelDes',
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