var app = angular.module("FinanceManagment", []);

app.directive('fileModel', ['$parse', function ($parse) {
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
app.controller("CliamManangementController", function ($scope, $http, $timeout) {

    $scope.form = {};
    $scope.travelData = [];
    $scope.travelDataBak = [];
    $scope.nameList = [];
    $scope.views = {};
    $scope.logdet = "";
    $scope.clstId = null;
    $scope.edit = {
        price: []
    };
    $scope.views.list = true;

    _autoCluster();
    function _autoCluster() {
        $http({
            method: 'GET',
            url: 'cluster/findByAllActive',
        }).then(function successCallback(response) {
            $scope.clusters = response.data.data;
        }, function errorCallback(response) {
        });
    };

    $scope.getApprovalClasterData = function (id) {
        document.getElementById("loader").style.display = "block";
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        $scope.clstId = id;
        $http({
            method: 'GET',
            url: 'commute/fetchFinaceApprovalList',
            params: {
                "clusterId": id
            }
        }).then(
            function successCallback(response) {
                $scope.clusterData = response.data.data;

                document.getElementById("loader").style.display = "none";
                angular.forEach($scope.clusterData, function (val, key) {
                    val.clusetrId = id;
                    var existingUser = $scope.nameList.find(function (user) {
                        return user.user_id === val.userId;
                    });
                    if (!existingUser) {
                        $scope.nameList.push({
                            username: val.name,
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
                $scope.travelDataBak = $scope.clusterData;
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

    $scope.getApprovalClasterDataNameWise = function () {
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

    $scope.setImageAsViewed = function () {
		$scope.imageIsViewed = true;
	};


	$scope.uploadImage = function (file, id) {
		var formData = new FormData();
		formData.append("Images", file);
		formData.append("id", id);
		
		var config = {
			transformRequest: angular.identity,
			headers: {
				'Content-Type': undefined
			}
		};
		var url = "commute/addDocument";
		$http.put(url, formData, config).then(
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

	function _success(response) {
		$('#fileUpladModal').modal('hide');
		_autoFetchApprovalData();
		document.getElementById("loader").style.display = "none";
        Swal.fire({
            text: response.data.message,
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
        Swal.fire({
            text: response.data.message,
            icon: "error",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }
});



