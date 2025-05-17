var mainApp = angular.module("modeData", []);
	mainApp.controller('modeDataController', function($scope, $http) {
	
		_autoTraveldata();
		function _autoTraveldata() {
			$http({
				method : 'GET',
				url : 'fetchLevelDescriptions',

			}).then(function successCallback(response) {
				$scope.travel = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		};

		_autoLocation();
		function _autoLocation() {
			$http({
				method : 'GET',
				url : 'fetchAllCity',

			}).then(function successCallback(response) {
				$scope.locations = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		};

		$scope.refresh = function() {
			location.reload();
		}

		_autoMode();
		function _autoMode() {
			document.getElementById("loader").style.display = "block";
			$http({
				method : 'GET',
				url : 'fetchMode',

			}).then(function successCallback(response) {
				$scope.modetrans = response.data ;
				document.getElementById("loader").style.display = "none";
			}, function errorCallback(response) {
				console.log(response.statusText);
				document.getElementById("loader").style.display = "none";
			});
		};

		$scope.modeedit = function(str) {
			$http({
				method : 'GET',
				url : 'editMode',
				params : {
					"id" : str
				}

			}).then(function successCallback(response) {
				$scope.form = response.data;
			}, function errorCallback(response) {
				console.log(response.statusText);
			});
		};

		$scope.modedelete = function(str) {
			$http({
				method : 'GET',
				url : 'deleteMode',
				params : {
					"id" : str
				},
				headers : {
					'Content-Type' : 'application/json'
				},
				transformResponse : angular.identity

			}).then(function successCallback(response) {
				Swal.fire({
					position : 'center',
					icon : 'success',
					title : response.data,
					showConfirmButton : true,
				}).then(function() {
					location.reload();
				});
			}, function errorCallback(response) {
				Swal.fire({
					position : 'center',
					icon : 'error',
					title : response.data,
					showConfirmButton : true,
				})
				console.log(response.statusText);
			});
		};

		$scope.submitmodetran = function() {
			$http({
				method : "POST",
				url : 'addMode',
				data : angular.toJson($scope.form),
				headers : {
					'Content-Type' : 'application/json'
				},
				transformResponse : angular.identity
			}).then(function successCallback(response) {
				Swal.fire({
					position : 'center',
					icon : 'success',
					title : response.data,
					showConfirmButton : true,
				}).then(function() {
					location.reload();
				});
			}, function errorCallback(response) {
				console.log(response.data);
          	Swal.fire({
					position: 'center',
					icon: 'error',
					title: response.data,
					showConfirmButton: true,
				}).then(function () {   
				});
			});
		};

	});