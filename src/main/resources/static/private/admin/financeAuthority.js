var mainApp = angular.module("financeAuthorityData", []);
mainApp.controller('financeAuthorityController', function ($scope, $http) {


    _autoFinanceAuthority();
    function _autoFinanceAuthority() {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'fetchFinanceAuthority',

        }).then(function successCallback(response) {
            $scope.authority = response.data;
            document.getElementById("loader").style.display = "none";
        }, function errorCallback(response) {
            document.getElementById("loader").style.display = "none";
            console.log(response.statusText);
        });
    };


    _autoFetachFinanceRole();
    function _autoFetachFinanceRole() {
        $http({
            method: 'GET',
            url: 'fetchAllFinaceRole',

        }).then(function successCallback(response) {
            $scope.managers = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.submitFinanceAuthority = function () {
        $http({
            method: "POST",
            url: 'addFinanceAuthority',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function successCallback(response) {
            console.log("Grade: "+$scope.grade)
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: "Succesfully Saved",
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.data);
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: "Something went wrong! please try again....",
                showConfirmButton: true,
            }).then(function () {
            });
        });
    };

    $scope.authorityEdit = function (str) {
        $http({
            method: 'GET',
            url: 'editFinanceAuthority',
            params: {
                "id": str
            }

        }).then(function successCallback(response) {
            $scope.form = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.deleteSAuthority = function (str) {
        $http({
            method: 'GET',
            url: 'deleteFinanceAuthority',
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
                title: 'Deleted Successfully',
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            alert("failed");
            console.log(response.statusText);
        });
    };


    

});