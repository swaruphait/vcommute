var mainApp = angular.module('basicData', []);
mainApp.controller('basicDataController', function ($scope, $http) {
    $scope.submitForgoPass = function () {
        $http({
            method: "PUT",
            url: 'resetPass',
            params: {
                "password": $scope.resetpass.password,
                "newPassword": $scope.resetpass.newPassword
            },
            transformResponse: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {

            });
        }, function errorCallback(response) {
            console.log('data');
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            })
        });
    };

});