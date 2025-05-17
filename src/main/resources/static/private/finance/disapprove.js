var mainApp = angular.module("disapproveData", []);
mainApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };

}]);


mainApp.controller('disapproveDataController', function ($scope, $http) {


    $scope.disapprovedDataBck = [];

    $scope.levelChanged = function () {
        if ($scope.selectedLevel === 'first') {
            $scope.firstLevelData();
        } else if ($scope.selectedLevel === 'finance') {
            $scope.financeLevelData();
        }
    };


    $scope.financeLevelData = function () {
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        $http({
            method: 'GET',
            url: 'fetchFinanceLevelDisapprovData',

        }).then(function successCallback(response) {
            $scope.disapprovedData = response.data;

            angular.forEach($scope.disapprovedData, function (val, key) {
                var existingUser = $scope.nameList.find(function (user) {
                    return user.user_id === val.userId;
                });
                if (!existingUser) {
                    $scope.nameList.push({
                        username: val.username,
                        user_id: val.userId
                    });
                }
            });

            $scope.uniqueMonths = [];

            angular.forEach($scope.disapprovedData, function (item, key) {
                var date = new Date(item.startDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });

            console.log($scope.uniqueMonths);

            $scope.disapprovedDataBck = $scope.disapprovedData;
        }, function errorCallback(response) {

            console.log(response.statusText);
        });
    };


    $scope.firstLevelData = function () {
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        $http({
            method: 'GET',
            url: 'fetchFirstLevelDisapprovData',

        }).then(function successCallback(response) {
            $scope.disapprovedData = response.data;
            angular.forEach($scope.disapprovedData, function (val, key) {
                var existingUser = $scope.nameList.find(function (user) {
                    return user.user_id === val.userId;
                });
                if (!existingUser) {
                    $scope.nameList.push({
                        username: val.username,
                        user_id: val.userId
                    });
                }
            });

            $scope.uniqueMonths = [];

            angular.forEach($scope.disapprovedData, function (item, key) {
                var date = new Date(item.startDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });

            console.log($scope.uniqueMonths);

            $scope.disapprovedDataBck = $scope.disapprovedData;
        }, function errorCallback(response) {

            console.log(response.statusText);
        });
    };

    $scope.filterByMonth = function (month) {
        $scope.disapprovedData = $scope.disapprovedDataBck.filter(function (item) {
            var date = new Date(item.startDate);
            return date.getMonth() + 1 === month;
        });
    };


    $scope.getApprovalClasterDataNameWise = function (userid) {
        $scope.uniqueMonths = [];
        $scope.disapprovedData = $scope.disapprovedDataBck.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.disapprovedData, function (item, key) {
            var date = new Date(item.startDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

    }


    $scope.ReApprove = function (str, price) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You want to revert this disapproved data!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#00B571',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Approve it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $http({
                    method: 'GET',
                    url: 'financeLevelReApprove',
                    params: {
                        "id": str,
                        "price": price

                    },
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    transformResponse: angular.identity

                }).then(function successCallback(response) {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Travel data has been reactivated and successfully approved. It will now be forwarded to the next level of approval if required',
                        showConfirmButton: true,
                    }).then(function () {
                        $scope.ApproveData(str, price);
                        location.reload();
                    });
                }, function errorCallback(response) {
                    alert("failed");
                    console.log(response.statusText);
                });
            } else {
                location.reload();
            }
        })
    };
    $scope.ApproveData = function (str, price) {
        $http({
            method: 'GET',
            url: 'approveClusterData',
            params: {
                "id": str,
                "price": price
            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Approved',
                showConfirmButton: true,
            }).then(function () {
                $scope.getApprovalClasterData($scope.clstId);
                $location.path('/');
            });
        }, function errorCallback(response) {
            alert("failed");
            console.log(response.statusText);
        });

    };
    $scope.uploadImage = function (file, id) {
        console.log(file);
        console.log(id);
        var formData = new FormData;
        formData.append("Images", file);
        formData.append("id", id);
        console.log(formData);
        var config = {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        }
        var url = "addDocument";
        $http.post(url, formData, config).then(
            function successCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: 'Upload Faileds',
                    showConfirmButton: true,
                }).then(function () {
                    location.reload();
                });
                $('#fileUpladModal').modal('hide');
            }, function errorCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Upload Succesfull',
                    showConfirmButton: true,
                }).then(function () {
                    location.reload();
                    $('#fileUpladModal').modal('hide');
                });
            })
    };

    $scope.form = {};
    $scope.upladFileModal = function (id) {
        $('#fileUpladModal').modal('show');
        $scope.form.id = id;
    }
    $scope.setImageAsViewed = function (index, image) {
        $('#imageViewModal').modal('show');
        $scope.view_image = image;
    }
});