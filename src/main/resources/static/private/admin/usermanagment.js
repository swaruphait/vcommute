var mainApp = angular.module('userManagmentData', []);
mainApp.controller('userManagmentController', function ($scope, $http) {
    $scope.form = {};
    $scope.isPasswordVisible = false;

    $scope.togglePassword = function () {
        $scope.isPasswordVisible = !$scope.isPasswordVisible;
        var passwordInput = document.getElementById('idPassword');
        var toggleIcon = document.getElementById('togglePassword');

        if ($scope.isPasswordVisible) {
            passwordInput.type = 'text';
            toggleIcon.classList.remove('icon-toggle-pass-slash');
            toggleIcon.classList.add('icon-toggle-pass');
        } else {
            passwordInput.type = 'password';
            toggleIcon.classList.remove('icon-toggle-pass');
            toggleIcon.classList.add('icon-toggle-pass-slash');
        }
    };


    $scope.ctogglePassword = function () {
        $scope.isPasswordVisible = !$scope.isPasswordVisible;
        var passwordInput = document.getElementById('cidPassword');
        var toggleIcon = document.getElementById('ctogglePassword');

        if ($scope.isPasswordVisible) {
            passwordInput.type = 'text';
            toggleIcon.classList.remove('icon-toggle-pass-slash');
            toggleIcon.classList.add('icon-toggle-pass');
        } else {
            passwordInput.type = 'password';
            toggleIcon.classList.remove('icon-toggle-pass');
            toggleIcon.classList.add('icon-toggle-pass-slash');
        }
    };
    _autoDepartment();
    function _autoDepartment() {
        $http({
            method: 'GET',
            url: 'fetchDepartment',

        }).then(function successCallback(response) {
            $scope.department = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };
    _autoFetachHodReportManager();
    function _autoFetachHodReportManager() {
        $http({
            method: 'GET',
            url: 'fetchWitoutUser',

        }).then(function successCallback(response) {
            $scope.managers = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    _autoFetachFinanceAuthority();
    function _autoFetachFinanceAuthority() {
        $http({
            method: 'GET',
            url: 'fetchFinanceAuthority',

        }).then(function successCallback(response) {
            $scope.authority = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    _autoFetachGrade();
    function _autoFetachGrade() {
        $http({
            method: 'GET',
            url: 'fetchGrade',

        }).then(function successCallback(response) {
            $scope.grades = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    _autoRole();
    function _autoRole() {
        $http({
            method: 'GET',
            url: 'fetchRoleType',

        }).then(function successCallback(response) {
            $scope.roletypes = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };
    _autoLocation();
    function _autoLocation() {
        $http({
            method: 'GET',
            url: 'fetchAllCity',

        }).then(function successCallback(response) {
            $scope.locations = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.submitRegistration = function () {
        console.log($scope.form);
        if ($scope.form.rawPassword === $scope.form.repass) {
            console.log(angular.toJson($scope.form));
            $http({
                method: "POST",
                url: 'userRegistration',
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
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: response.data,
                    showConfirmButton: true,
                })
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Password Not Match!',
                footer: '<a>Both Password Should Be Same</a>'
            })
        }
    };

    $scope.refresh = function () {
        location.reload();
    }
});