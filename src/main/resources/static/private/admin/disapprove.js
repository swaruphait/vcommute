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

    $scope.logdet = "";

    _autoFetchData();
    function _autoFetchData() {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'fetchFirstLevelDisapprovData',

        }).then(function successCallback(response) {
            document.getElementById("loader").style.display = "none";
            $scope.disapprovedData = response.data;
        }, function errorCallback(response) {
            document.getElementById("loader").style.display = "none";
            console.log(response.statusText);
        });
    }
    ;

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
                    url: 'firstLevelReApprove',
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
            url: 'approvalTravelData',
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