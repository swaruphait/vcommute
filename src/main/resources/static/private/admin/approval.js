var app = angular.module("AdminManagment", []);

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
app.controller("ApprovalController", function ($scope, $http, $timeout) {
	$scope.travelData = [];
	$scope.disapprovedData = [];
	$scope.logdet = "";
	$scope.imageIsViewed = false;
	$scope.edit = {
		price: []
	};
	$scope.travelDataBak = [];

	_autoFetchApprovalData();
	function _autoFetchApprovalData() {
		$scope.nameList = [];
        $scope.uniqueMonths = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'commute/fetchApprovalList'
		}).then(function successCallback(response) {
			$scope.travelData = response.data.data;
			document.getElementById("loader").style.display = "none";
			angular.forEach($scope.travelData, function (val, key) {
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

            angular.forEach($scope.travelData, function (item, key) {
                var date = new Date(item.startDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });
            $scope.travelDataBak = $scope.travelData;
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};


	$scope.getDataNameWise = function (userid, month) {
        console.log(month);
        $scope.uniqueMonths = [];
        $scope.travelData = $scope.travelDataBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.travelData, function (item, key) {
            var date = new Date(item.startDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

        if (month !== undefined) {
            $scope.travelData = $scope.travelData.filter(function (item) {
                var date = new Date(item.startDate);
                return date.getMonth() + 1 === month;
            });
        }
    }

    $scope.getRange = function (total) {
        return Array.from({ length: total }, (_, index) => index + 1);
    };


	$scope.approveData = function (id, price) {
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to approve data!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, Approve it!'
		}).then((result) => {
			if (result.isConfirmed) {
				document.getElementById("loader").style.display = "block";
	
				$http({
					method: 'PUT',
					url: 'commute/approvedCommuteData',
					params: {
						id: id,
						price: price
					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				}).then(_success, _error);
			}
		});
	};
	

	$scope.listOfApprove = function () {
    $scope.multiCheck = [];
    for (var i = 0; i < $scope.travelData.length; i++) {
        if ($scope.travelData[i].selected) {
            $scope.multiCheck.push($scope.travelData[i]);
        }
    }
    Swal.fire({
        title: 'Are you sure?',
        text: "You want to approve data!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#00B571',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Approve it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $http({
                method: 'POST',
                url: 'commute/approvedDataList',
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
                    location.reload();
                });
            }, function errorCallback(response) {
                alert($scope.multiCheck);
                console.log(response.statusText);
            });
        } else {
            location.reload();
        }
    });
}

	$scope.disapproveData = function (str, price) {
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
						method: 'PUT',
						url: 'commute/disapprovedCommuteData',
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

						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Disapproved',
							showConfirmButton: true,
						}).then(function () {
							document.getElementById("loader").style.display = "none";
							location.reload();
						});
					}, function errorCallback(response) {
						alert("failed");
						document.getElementById("loader").style.display = "none";
						console.log(response.statusText);
					});
				}
			}
		});
		document.getElementById("loader").style.display = "none";
	};


	$scope.listOfDisApprove = function (str) {
		$scope.multiCheck = [];
		var a = 0;
		for (var i = 0; i < $scope.travelData.length; i++) {
			if ($scope.travelData[i].selected) {
				$scope.multiCheck[a] = $scope.travelData[i];
			}
			a++;
		}

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
				if (text) {
					document.getElementById("loader").style.display = "block";
					$http({
						method: 'POST',
						url: 'commute/disapprovedCommuteDataList',
						data: angular.toJson($scope.multiCheck),
						params: {
							"reason": text
						},
						headers: {
							'Content-Type': 'application/json'
						},
						transformResponse: angular.identity

					}).then(function successCallback(response) {
						document.getElementById("loader").style.display = "none";
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Disapproved',
							showConfirmButton: true,
						}).then(function () {
							location.reload();
						});
					}, function errorCallback(response) {
						document.getElementById("loader").style.display = "none";
						alert("failed");
						console.log(response.statusText);
					});
				}
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