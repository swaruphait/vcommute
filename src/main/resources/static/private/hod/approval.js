var mainApp = angular.module("approvalData", []);

// DIRECTIVE - FILE MODEL
mainApp.directive('fileModel', ['$parse', function($parse) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};

}]);

mainApp.controller('approvalDataController', function($scope, $http) {

	$scope.imageIsViewed = false;
	$scope.month_id = {};
	
	$scope.refresh = function() {
		location.reload();
	}

	_autoGoogleApi();
	function _autoGoogleApi() {
		$http({
			method : 'GET',
			url : 'autoupdateDataBygoogleapi',

		}).then(function successCallback(response) {
			location.reload();
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

		_autotravelData();
		function _autotravelData() {
			$scope.nameList = [];
			document.getElementById("loader").style.display = "block";
			$http({
				method: 'GET',
				url: 'fetchApprovalData',
				headers: {
					'Content-Type': 'application/json'
				},
				transformResponse: angular.identity
			}).then(function successCallback(response) {
				// console.log(JSON.parse(response.data));
				$scope.travelData = JSON.parse(response.data);
				angular.forEach($scope.travelData, function (val, key) {
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
				document.getElementById("loader").style.display = "none";
	
			}, function errorCallback(response) {
				console.log(response.statusText);
				document.getElementById("loader").style.display = "none";
			});
		};
		

		$scope.getDataNameWise = function (userid,month) {
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

			if(month !== undefined){
				$scope.travelData = $scope.travelData.filter(function (item) {
					var date = new Date(item.startDate);
					return date.getMonth() + 1 === month;
				});
			}
	
		}

	$scope.getRange = function(total) {
		return Array.from({ length: total }, (_, index) => index + 1);
	};

	_autoFetchApprovalData();
	function _autoFetchApprovalData() {
		$http({
			method: 'GET',
			url: 'autoupdateDataBygoogleapi'
		}).then(function successCallback(response) {
			$scope.autoFetchAData();
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};


	$scope.redirectPage = function(pageNumber) {
		console.log("Page Number: " + pageNumber);
		$http({
			method: 'GET',
			url: 'travelDataApproveListPage',
			params: {
				"pageNumber": pageNumber,
			}

		}).then(function successCallback(response) {
			$scope.travelData = response.data.content;
			$scope.currentPage = response.data.number;
			console.log($scope.travelData);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}


	$scope.searchTravelData = function(name) {
		console.log(name)
		$http({
			method: 'GET',
			url: 'searchTravelData',
			params: {
				"name": name
			}

		}).then(function successCallback(response) {
			if (name.length > 0) {
				$scope.pageView = false;
			} else {
				$scope.pageView = true;
				$scope.redirectPage(0);
			}
			$scope.travelData = response.data;
			console.log($scope.travelData);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};


	$scope.fetchTravelDataByDate = function(str) {
		$scope.nameList = [];
		$scope.month_id = {};
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchCommuteDatabyDate',
			params: {
				"stdate": $scope.date.startdate,
				"enddate": $scope.date.enddate

			},
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(function successCallback(response) {
			$scope.pageView = false;
			$scope.travelData = JSON.parse(response.data);
			angular.forEach($scope.travelData, function (val, key) {
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
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			document.getElementById("loader").style.display = "none";
			console.log(response.statusText);
		});
	};

	$scope.filterByMonth = function (month,userid) {
		console.log(month);
		console.log(userid);
        $scope.travelData = $scope.travelDataBak.filter(function (item) {
            var date = new Date(item.startDate);
            return date.getMonth() + 1 === month;
        });

		if(userid !== undefined){
			$scope.travelData = $scope.travelData.filter(function (item) {
				return item.userId === userid;
			});
		}
    };



	// $scope.fetchTravelHodDataByDate = function(str) {
	// 	document.getElementById("loader").style.display = "block";
	// 	$http({
	// 		method: 'GET',
	// 		url: 'fetchCommuteDatabyDate',
	// 		params: {
	// 			"stdate": $scope.date.startdate,
	// 			"enddate": $scope.date.enddate

	// 		},
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity
	// 	}).then(function successCallback(response) {
	// 		$scope.pageView = false;
	// 		$scope.travelData = JSON.parse(response.data);
	// 		Swal.fire({
	// 			position: 'center',
	// 			icon: 'success',
	// 			title: 'Data Fetched',
	// 			showConfirmButton: true,
	// 		}).then(function() {
	// 			document.getElementById("loader").style.display = "none";
	// 			$scope.refresh();
	// 		});
	// 	}, function errorCallback(response) {
	// 		document.getElementById("loader").style.display = "none";
	// 		console.log(response.statusText);
	// 	});
	// };


	$scope.setImageAsViewed = function() {
		$scope.imageIsViewed = true;
	};
	$scope.DisapproveData = function(str, price) {
		Swal.fire({
			input: 'textarea',
			inputLabel: 'Reason:',
			inputPlaceholder: 'Type your reason here...',
			inputAttributes: {
				'aria-label': 'Type your reason here'
			},
			showCancelButton: true,
			allowOutsideClick: false,
			preConfirm: function(text) {
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

						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Disapproved',
							showConfirmButton: true,
						}).then(function() {
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

	$scope.uploadImage = function(file, id) {
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
					title: 'Upload Failed',
					showConfirmButton: true,
				}).then(function() {
					location.reload();
				});
				$('#fileUpladModal').modal('hide');
			}, function errorCallback(response) {
				Swal.fire({
					position: 'center',
					icon: 'success',
					title: 'Upload Succesfull',
					showConfirmButton: true,
				}).then(function() {
					location.reload();
					$('#fileUpladModal').modal('hide');
				});
			})
	};


	$scope.ApproveData = function(str, price) {

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
					method: 'GET',
					url: 'approvalTravelData',
					params: {
						"id": str,
						"price": price,

					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity

				}).then(function successCallback(response) {
					document.getElementById("loader").style.display = "block";
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Approved',
						showConfirmButton: true,
					}).then(function() {
						document.getElementById("loader").style.display = "none";
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


	$scope.GetValueForDisApprove = function(str) {
		$scope.multiCheck = [];
		var a = 0;
		for (var i = 0; i < $scope.travelData.length; i++) {
			if ($scope.travelData[i].selected) {
				$scope.multiCheck[a] = $scope.travelData[i];
				// alert($scope.multiCheck);
			}
			a++;
		}

		if ($scope.multiCheck.length === 0) {
			Swal.fire({
				icon: 'error',
				title: 'No selection',
				text: 'Please select at least one record to approve.'
			});
			return; // Exit the function
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
			preConfirm: function(text) {
				if (text) {
					document.getElementById("loader").style.display = "block";
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
						document.getElementById("loader").style.display = "none";
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Disapproved',
							showConfirmButton: true,
						}).then(function() {
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

	// $scope.GetValueForApprove = function(str) {
	// 	$scope.multiCheck = [];
	// 	var a = 0;
	// 	for (var i = 0; i < $scope.travelData.length; i++) {
	// 		if ($scope.travelData[i].selected) {
	// 			$scope.multiCheck[a] = $scope.travelData[i]
	// 		}
	// 		a++;
	// 	}
	// 	Swal.fire({
	// 		title: 'Are you sure?',
	// 		text: "You want to Approved data!",
	// 		icon: 'warning',
	// 		showCancelButton: true,
	// 		confirmButtonColor: '#00B571',
	// 		cancelButtonColor: '#d33',
	// 		confirmButtonText: 'Yes, Approve it!'
	// 	}).then((result) => {
	// 		if (result.isConfirmed) {
	// 			$http({
	// 				method: 'POST',
	// 				url: 'approvalTravelDataList',
	// 				data: angular.toJson($scope.multiCheck),
	// 				headers: {
	// 					'Content-Type': 'application/json'
	// 				},
	// 				transformResponse: angular.identity
	// 			}).then(function successCallback(response) {
	// 				Swal.fire({
	// 					position: 'center',
	// 					icon: 'success',
	// 					title: 'Approved',
	// 					showConfirmButton: true,
	// 				}).then(function() {
	// 					location.reload();
	// 				});
	// 			}, function errorCallback(response) {
	// 				alert("failed");
	// 				console.log(response.statusText);
	// 			});
	// 		} else {
	// 			location.reload();
	// 		}
	// 	})
	// }

	$scope.GetValueForApprove = function(str) {
		$scope.multiCheck = [];
		var a = 0;
		for (var i = 0; i < $scope.travelData.length; i++) {
			if ($scope.travelData[i].selected) {
				$scope.multiCheck[a] = $scope.travelData[i];
				a++;
			}
		}
	
		// If no items selected, show an error and stop execution
		if ($scope.multiCheck.length === 0) {
			Swal.fire({
				icon: 'error',
				title: 'No selection',
				text: 'Please select at least one record to approve.'
			});
			return; // Exit the function
		}
	
		Swal.fire({
			title: 'Are you sure?',
			text: "You want to Approved data!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#00B571',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, Approve it!'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'POST',
					url: 'approvalTravelDataList',
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
					}).then(function() {
						location.reload();
					});
				}, function errorCallback(response) {
					alert("failed");
					console.log(response.statusText);
				});
			} else {
				location.reload();
			}
		});
	};
	
	$scope.selected = [];
	$scope.exist = function(item) {
		return $scope.selected.indexOf(item) > -1;

	}
	$scope.toggleSelection = function(item) {
		var idx = $scope.selected.indexOf(item);
		if (idx > -1) {
			$scope.selected.splice(idx, 1);
		} else {
			$scope.selected.push(item);
		}
	}
	$scope.form = {};
	$scope.upladFileModal = function(id) {
		// console.log("Hi"+ id);
		$('#fileUpladModal').modal('show');
		$scope.form.id = id;
	}
	$scope.setImageAsViewed = function(index, image) {
		// console.log("Hi"+ index);
		$('#imageViewModal').modal('show');
		$scope.view_image = image;
	}

});