var mainApp = angular.module("userRoleData", []);
mainApp.controller('userRoleDataController', function($scope, $http) {
    $scope.refresh = function() {
        location.reload();
    }

    _autoRole();
    function _autoRole() {
        $http({
            method : 'GET',
            url : 'fetchRoleType',

        }).then(function successCallback(response) {
            $scope.usertypes = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
    ;

    $scope.usertypeedit = function(str) {
        $http({
            method : 'PUT',
            url : 'editusertype',
            params : {
                "id" : str
            }

        }).then(function successCallback(response) {
            $scope.form= response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.deleteusertype = function(str) {
        $http({
            method : 'DELETE',
            url : 'deleteusertype',
            params : {
                "id" : str
            },
            headers : {
                'Content-Type' : 'application/json'
            },
            transformResponse : angular.identity

        }).then(function successCallback(response) {
            Swal.fire({
                position : 'center',
                icon : 'success',
                title : 'User Type Deleted',
                showConfirmButton : true,
            }).then(function() {
                location.reload();
            });
        }, function errorCallback(response) {
            Swal.fire({
                position : 'center',
                icon : 'error',
                title : response.data,
                showConfirmButton : true,
            })
            console.log(response.statusText);
        });
    };

    $scope.submitLocation = function() {
        $http({
            method : "POST",
            url : 'addRoleType',
            data : angular.toJson($scope.form),
            headers : {
                'Content-Type' : 'application/json'
            },
            transformResponse : angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position : 'center',
                icon : 'success',
                title : response.data,
                showConfirmButton : true,
                timer : 1500
            }).then(function() {
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
            });;
        });
    };

});