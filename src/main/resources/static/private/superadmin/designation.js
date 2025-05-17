var mainApp = angular.module("DesignationManagment", []);
mainApp.controller('DesignationController', function ($scope, $http) {
   $scope.form = {};
   $scope.supercompany = "";
   $scope.companyId = 0;

   _autoCompanyListFetch();
   function _autoCompanyListFetch() {
       showHideLoad();
       $http({
           method: 'GET',
           url: 'subcompany/findByAllActive'

       }).then(function successCallback(response) {
           $scope.supercompany =  response.data.data[0].superCompanyName;
           $scope.companies = response.data.data;
       }, function errorCallback(response) {
           console.log(response.statusText);
       });
   }


    _autoFetchDesignation();
    function _autoFetchDesignation() {
        $http({
            method: 'GET',
            url: 'designation/findByAll',

        }).then(function successCallback(response) {
            $scope.designation = response.data.data;
            $scope.fetchDepartment();
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.fetchDepartment = function (str) {
        if (str==null) {
            str = 0;
        }
        $http({
            method: 'GET',
            params: { 'companyId': str },
            url: 'department/findByAll'
        }).then(function successCallback(response) {
            $scope.department = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    $scope.fetchDesignation = function (str) {
        $http({
            method: 'GET',
            params: { 'companyId': str },
            url: 'designation/findByAll'
        }).then(function successCallback(response) {
            $scope.designation = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
   
    $scope.findById = function (str) {
        $http({
            method: 'GET',
            url: 'designation/findById',
            params: {
                "id": str
            }
        }).then(function successCallback(response) {
            $scope.form = response.data.data;
            $scope.fetchDepartment($scope.form.companyId);
        }, function errorCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            })
        });
    };

    $scope.addDesignation = function () {
        $http({
            method: "POST",
            url: 'designation/createDesgination',
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

        });
    };

});