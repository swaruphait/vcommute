var mainApp = angular.module('HodManagmentData', []);

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

mainApp.controller('userManagmentController', function ($scope, $http) {
    $scope.form = {};
    $scope.imageIsViewed = false;



    $scope.upladFileModal = function(id) {
		// console.log("Hi"+ id);
		$('#fileUpladModal').modal('show');
		$scope.form.id = id;
	}
	$scope.setImageAsViewed = function(index, image, id, faceDataApproved, appvBy) {
		// console.log("Hi"+ index);
		$('#imageViewModal').modal('show');
	$scope.view_image = image;
    $scope.form_id = id;
    $scope.appvl = faceDataApproved;
    $scope.appvlBy = appvBy;
    
	}

    
    _autoGrade();
    function _autoGrade() {
        $http({
            method: 'GET',
            url: 'fetchGrade',

        }).then(function successCallback(response) {
            $scope.grades = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
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

    _fetchUsers();
    function _fetchUsers() {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'fetchUserUnderManager',
        }).then(function successCallback(response) {
            document.getElementById("loader").style.display = "none";
            $scope.userDetails = response.data;
            $scope.userDetails.forEach(function (user) {
                $scope.resizeImage(user.images, 80, function (thumbnail) {
                    user.thumbnailImage = thumbnail;
                });
            });
        }, function errorCallback(response) {
            document.getElementById("loader").style.display = "none";
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


    $scope.edit = function (str) {
        $http({
            method: 'GET',
            url: 'fetchUserDeatilById',
            params: { "id": str }
        }).then(function successCallback(response) {
            console.log(response.data);
            $scope.form = response.data;
            $("#exampleModal").modal("show");
        }, function errorCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            })
        });

    };

    $scope.editRegistration = function () {
            var method = "";
            var url = "";
            method = "PUT";
            url = 'userUpdate';
            console.log(angular.toJson($scope.form));
            $http({
                method: method,
                url: url,
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
                //alert("hi");
                console.log(response.data);
            }, function errorCallback(response) {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: response.data,
                    showConfirmButton: true,
                })
            });
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
		var url = "addImages";
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

    // $scope.editRegistration = function () {
    //     console.log($scope.form);
    //         $http({
    //             method: "PUT",
    //             url: 'userUpdate',
    //             data: angular.toJson($scope.form),
    //             headers: {
    //                 'Content-Type': 'application/json'
    //             },
    //             transformResponse: angular.identity
    //         }).then(function successCallback(response) {
    //             Swal.fire({
    //                 position: 'center',
    //                 icon: 'success',
    //                 title: response.data,
    //                 showConfirmButton: true,
    //             }).then(function () {
    //                 location.reload();
    //             });
    //         }, function errorCallback(response) {
    //             Swal.fire({
    //                 position: 'center',
    //                 icon: 'error',
    //                 title: response.data,
    //                 showConfirmButton: true,
    //             })
    //         });
    // };
    $scope.userActivation = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'userActivation',
            params: {
                "id": str
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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            alert("failed");
            console.log(response.statusText);
        });
    };

    $scope.userDeactivation = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'userDeactivation',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            alert("failed");
            console.log(response.statusText);
        });
    };

    $scope.resetEmbaddingId= function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'resetEmbadding',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
            console.log("ID" + response);
        }, function errorCallback(response) {
            console.log(response.statusText);
            console.log("Error" + response);
        });
    };


    $scope.resetDeviceId = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'resetDeviceId',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
            console.log("ID" + response);
        }, function errorCallback(response) {
            console.log(response.statusText);
            console.log("Error" + response);
        });
    };

    $scope.approvedFace = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'faceApproved',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            document.getElementById("loader").style.display = "none";
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });

        });
    };

    $scope.disApprovedFace = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'faceDisApproved',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.statusText);
            console.log("Error" + response);
        });
    };


 $scope.resetImage = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'resetImage',
            params: {
                "id": str
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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
            console.log("ID" + response);
        }, function errorCallback(response) {
            console.log(response.statusText);
            console.log("Error" + response);
        });
    };


    $scope.resetDeviceAllId = function () {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'resetDeviceIdAll',
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(function successCallback(response) {
            document.getElementById("loader").style.display = "none";
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
            console.log("ID" + response);
        }, function errorCallback(response) {
            console.log(response.statusText);
            console.log("Error" + response);
        });
    };


    $scope.resetPassword = function (str) {
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'resetPassword',
            params: {
                "id": str

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
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            alert("failed");
            console.log(response.statusText);
        });
    };
    $scope.refresh = function () {
        location.reload();
    }
});