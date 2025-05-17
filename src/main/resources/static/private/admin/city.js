var mainApp = angular.module("cityData", []);
mainApp.controller('cityDataController', function ($scope, $http) {
	_autoLocation();
	function _autoLocation() {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchAllCity'
		}).then(function successCallback(response) {
			$scope.locations = response.data;
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};

});