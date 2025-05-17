var app = angular.module("AdminManagment", []);
app.controller("UserController", function ($scope, $http, $timeout) {

    $scope.form = {};
    $scope.views = {};
    $scope.views.list = true;
    $scope.emp = {};
    $scope.companies = [];
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


    $scope.openEditUser = function (str) {
        $scope.form = {};
        $("#editUser").modal("show");
        $scope.editUserById(str);
    }

    _autoCompanyListFetch();
    function _autoCompanyListFetch() {
        $http({
            method: 'GET',
            url: 'subcompany/findByAllActive'
        }).then(function successCallback(response) {
            $scope.supercompany = response.data.data[0].superCompanyName;
            $scope.companies = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    _autoUsersListFetch();
    function _autoUsersListFetch() {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'user/fetchAllUser'
        }).then(function successCallback(response) {
            $scope.employees = response.data.data;
            document.getElementById("loader").style.display = "none";
        }, function errorCallback(response) {
            document.getElementById("loader").style.display = "none";
            console.log(response.statusText);
        });
    }

    _autoStateListFetch();
    function _autoStateListFetch() {
        $http({
            method: 'GET',
            url: 'location/findByAllState'
        }).then(function successCallback(response) {
            $scope.states = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.fetchCityName = function (str) {
        $http({
            method: 'GET',
            params: { 'stateId': str },
            url: 'location/findAllCityStateWise'
        }).then(function successCallback(response) {
            $scope.locations = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    _fetchRoles();
    function _fetchRoles() {
        $http({
            method: 'GET',
            url: 'role/findByAllActive'
        }).then(function successCallback(response) {
            $scope.roles = response.data.data;
            if ($scope.form.role == null) {
                $scope.form.role = $scope.roles[0].name;
            }
            showHideLoad(true);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    _fetchDepartment();
    function _fetchDepartment() {
        $http({
            method: 'GET',
            url: 'department/findByAllActive',

        }).then(function successCallback(response) {
            $scope.department = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


    $scope.fetchDesignation = function (str) {
        $http({
            method: 'GET',
            params: { 'deptId': str },
            url: 'designation/findByDepartmentAll',

        }).then(function successCallback(response) {
            $scope.designation = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


    _fetchApprovalLevel();
    function _fetchApprovalLevel() {
        $http({
            method: 'GET',
            url: 'approval_level/findByAllActive',

        }).then(function successCallback(response) {
            $scope.appvlLevel = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


    _fetchGrade();
    function _fetchGrade() {
        $http({
            method: 'GET',
            url: 'grade/findByAllActive',

        }).then(function successCallback(response) {
            $scope.grades = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


    $scope.addSaveEmployee = function () {
        $http({
            method: "POST",
            url: 'user/creatUser',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    $scope.fetchUsers = function (str) {
        $http({
            method: 'GET',
            params: { 'companyId': str },
            url: 'user/fetchAllUser'
        }).then(function successCallback(response) {
            $scope.employees = response.data.data;
            showHideLoad(true);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    $scope.resetDeviceId = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: "GET",
            url: 'user/resetDeviceId',
            params: { 'userId': str },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    $scope.resetPassword = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: "GET",
            url: 'user/resetPassword',
            params: { 'userId': str },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };

    
    $scope.resetDeviceIdAll = function () {
        document.getElementById("loader").style.display = "block";
        $http({
            method: "GET",
            url: 'user/resetDeviceIdAll',
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };


    function _success(response) {
        $scope.form ={};
        $("#newUser").modal("hide");
        $("#editUser").modal("hide");
        _autoUsersListFetch();
        document.getElementById("loader").style.display = "none";
        Swal.fire({
            text: response.data,
            icon: "success",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }

    function _error(response) {
        document.getElementById("loader").style.display = "none";
        showHideLoad(true);
        Swal.fire({
            text: response.data,
            icon: "error",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }

    $scope.checkUserNameAvailability = function (username) {
        $http({
            method: 'GET',
            params: { 'username': username },
            url: 'user/usernameAvailability'

        }).then(function successCallback(response) {
            console.log(response.data);
            if (response.data == false) {
                $('#user-name-error').hide();
                $('#user-name-error-edit').hide();
            } else {
                $('#user-name-error').show();
                $('#user-name-error-edit').show();
            }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.editUserById = function (str) {
        $http({
            method: 'GET',
            params: { 'id': str },
            url: 'user/findById'
        }).then(function successCallback(response) {
            $scope.form = response.data.data;
            $scope.fetchDesignation($scope.form.deptId);
            $scope.fetchCityName($scope.form.stateId)
            showHideLoad(true);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.userActivation = function (str) {
        $http({
            method: 'GET',
            url: 'user/activeUser',
            params: {
                "id": str
            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(_success, _error);
    };

    $scope.userDeactivation = function (str) {
        $http({
            method: 'GET',
            url: 'user/deactiveUser',
            params: {
                "id": str
            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(_success, _error);
    };
});
