var mainApp = angular.module("financeCommuteData", []);

// DIRECTIVE - FILE MODEL
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

mainApp.controller('financeCommuteDataController', function ($scope, $http) {

    $scope.travelData = [];
    $scope.travelDataBak = [];
    $scope.nameList = [];
    $scope.isView = false;
    $scope.logdet = "";
    $scope.clstId = null;
    $scope.edit = {
        price: []
    };

    _autoCluster();
    function _autoCluster() {
        document.getElementById("loader").style.display = "none";
        $http({
            method: 'GET',
            url: 'fetchCluster',
        }).then(function successCallback(response) {
            $scope.clusters = response.data;
            console.log($scope.clusters.id);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
    ;

    $scope.getApprovalClasterData = function (id) {
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        $scope.clstId = id;
        document.getElementById("loader").style.display = "block";

        $http({
            method: 'GET',
            url: 'travelDataCluster',
            params: {
                "id": id
            }
        }).then(
            function successCallback(response) {
                console.log(response.data);
                $scope.clusterData = response.data;


                angular.forEach($scope.clusterData, function (val, key) {
                    val.clusetrId = id;
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

                angular.forEach($scope.clusterData, function (item, key) {
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

                $scope.travelDataBak = $scope.clusterData;

                console.log($scope.clusterData);
                console.log($scope.nameList);

                // $scope.getNameClasterData(id);
                document.getElementById("loader").style.display = "none";
            },
            function errorCallback(response) {
                console.log(response.statusText);
                document.getElementById("loader").style.display = "none";
            }
        );

    }

    $scope.filterByMonth = function (month) {
        $scope.clusterData = $scope.travelDataBak.filter(function (item) {
            var date = new Date(item.startDate);
            return date.getMonth() + 1 === month;
        });
    };


    $scope.getApprovalClasterDataNameWise = function (userid) {
        $scope.uniqueMonths = [];
        $scope.clusterData = $scope.travelDataBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.clusterData, function (item, key) {
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
                    $scope.getApprovalClasterDataNameWise();
                });
                $('#fileUpladModal').modal('hide');
            }, function errorCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Upload Succesfull',
                    showConfirmButton: true,
                }).then(function () {
                    $scope.getApprovalClasterData($scope.clstId);
                    $('#fileUpladModal').modal('hide');
                });
            })
    };


    $scope.ApproveData = function (str, clusetrId, price) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You want to approve this data!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#00B571',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Approve it!'
        }).then((result) => {
            if (result.isConfirmed) {
                document.getElementById("loader").style.display = "block";
                $http({
                    method: 'GET',
                    url: 'approveClusterData',
                    params: {
                        "id": str,
                        "clusetrId": clusetrId,
                        "price": price
                    },
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    transformResponse: angular.identity
                }).then(function successCallback(response) {
                    $scope.clusterData = $scope.clusterData.filter(function (item) {
                        return item.id !== str;
                    });
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Approved',
                        showConfirmButton: true,
                    }).then(function () {
                        document.getElementById("loader").style.display = "none";
                    });
                   
                }, function errorCallback(response) {
                    document.getElementById("loader").style.display = "none";
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Failed to approve data!'
                    });
                });
            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'Cancelled',
                    text: 'Approval cancelled!'
                });
            }
        });
    };

    $scope.DisapproveData = function (str, price) {
        Swal.fire({
            input: 'textarea',
            inputLabel: 'Reason:',
            inputPlaceholder: 'Type your reason here...',
            inputAttributes: {
                'aria-label': 'Type your reason here'
            },
            showCancelButton: true,
            allowOutsideClick: false,
            preConfirm: function (text) {
                if (text) {
                    document.getElementById("loader").style.display = "block";
                    $http({
                        method: 'GET',
                        url: 'disapprovalTravelData',
                        params: {
                            "id": str,
                            "price": price,
                            "reason": text

                        },
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        transformResponse: angular.identity

                    }).then(function successCallback(response) {
                        $scope.clusterData = $scope.clusterData.filter(function (item) {
                            return item.id !== str;
                        });
                       
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Disapproved',
                            showConfirmButton: true,
                        }).then(function () {
                            document.getElementById("loader").style.display = "none";
                        });
                    }, function errorCallback(response) {
                        document.getElementById("loader").style.display = "none";
                        // alert("failed");
                        console.log(response.statusText);
                    });
                }
            }
        });
    };

    $scope.GetValueForApprove = function (str) {
        $scope.multiCheck = [];
        document.getElementById("loader").style.display = "block";
        var a = 0;
        for (var i = 0; i < $scope.clusterData.length; i++) {
            if ($scope.clusterData[i].selected) {
                $scope.multiCheck[a] = $scope.clusterData[i]
                /* alert($scope.multiCheck); */
            }
            a++;
        }

        console.log($scope.multiCheck);

        Swal.fire({
            title: 'Are you sure?',
            text: "You want to approved data!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#00B571',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Approve it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $http({
                    method: 'POST',
                    url: 'approveClusterDataList',
                    data: angular.toJson($scope.multiCheck),
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
                        document.getElementById("loader").style.display = "none";
                    });
                }, function errorCallback(response) {
                    alert("failed");
                    console.log(response.statusText);
                });
            } else {
                $scope.getApprovalClasterData($scope.clstId);
                $location.path('/');
            }
        })
    }
    $scope.GetValueForDisApprove = function (str) {
        $scope.multiCheck = [];
        var a = 0;

        for (var i = 0; i < $scope.clusterData.length; i++) {
            if ($scope.clusterData[i].selected) {
                $scope.multiCheck[a] = $scope.clusterData[i];
                //alert($scope.multiCheck);
            }
            a++;
        }
        //alert($scope.multiCheck)

        Swal.fire({
            input: 'textarea',
            inputLabel: 'Reason:',
            inputPlaceholder: 'Type your Reason here...',
            inputAttributes: {
                'aria-label': 'Type your Reason here'
            },
            showCancelButton: true,
            allowOutsideClick: false,
            preConfirm: function (text) {
                document.getElementById("loader").style.display = "block";
                if (text) {
                    $http({
                        method: 'POST',
                        url: 'disapprovalTravelDataList',
                        data: angular.toJson($scope.multiCheck),
                        params: {
                            "reason": text
                        },
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        transformResponse: angular.identity

                    }).then(function successCallback(response) {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Disapproved',
                            showConfirmButton: true,
                        }).then(function () {
                            $scope.getApprovalClasterData($scope.clstId);
                            $location.path('/');
                            document.getElementById("loader").style.display = "none";
                        });
                    }, function errorCallback(response) {
                        alert("failed");
                        console.log(response.statusText);
                        document.getElementById("loader").style.display = "none";
                    });
                }
            }
        });



    }
    $scope.selected = [];
    $scope.exist = function (item) {
        return $scope.selected.indexOf(item) > -1;

    }
    $scope.toggleSelection = function (item) {
        var idx = $scope.selected.indexOf(item);
        if (idx > -1) {
            $scope.selected.splice(idx, 1);
        } else {
            $scope.selected.push(item);
        }
    }
    $scope.form = {};
    $scope.upladFileModal = function (id) {
        $('#fileUpladModal').modal('show');
        $scope.form.id = id;
    }
    $scope.setImageAsViewed = function (index, image) {
        $('#imageViewModal').modal('show');
        $scope.view_image = image;
    }
    $scope.isViewImage = function (index, str) {
        //document.getElementById("demo").style.visibility="visible";
        $scope.clusterData[index].isView = true;
        console.log($scope.isView);
    }

});