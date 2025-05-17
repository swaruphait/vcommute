var app = angular.module("AdminManagment", []);

app.controller("ReportCommuteController", function ($scope, $http, $timeout) {
	var today = new Date();
	$scope.form = {};
	$scope.views = {};
	$scope.views.list = true;
	$scope.date = {
		startdate: "",
		enddate: "",
		active: 'true',
		location: ""
	};

	$scope.travelDataBck = []
	$scope.stdt = new Date(today.getFullYear(), today.getMonth() - 6, 1);
	$scope.eddt = new Date(today.getFullYear(), today.getMonth() + 1, 0);

	flatpickr("#holidayDate", {
		enableTime: false,
		noCalendar: false,
		dateFormat: "Y-m-d",
		allowInput: true,
		onChange: function (selectedDates, startDateStr) {
			$scope.$apply(function () {
				$scope.form.holidayDate = startDateStr;
			});
		}
	});

	var TravelReport = {
		headers: true,
		columns: [{
			columnid: 'name',
			title: 'Name'
		}, {
			columnid: 'customerName',
			title: 'customer Name'
		}, {
			columnid: 'customerLocation',
			title: 'customer Location'
		}, {
			columnid: 'modeName',
			title: 'Mode of Transportation'
		}, {
			columnid: 'startDate',
			title: 'Start Date'
		}, {
			columnid: 'startTime',
			title: 'Start Time'
		}, {
			columnid: 'startLocation',
			title: 'Start Location'
		}, {
			columnid: 'endDate',
			title: 'End Date'
		}, {
			columnid: 'endTime',
			title: 'End Time'
		}, {
			columnid: 'endLocation',
			title: 'End Location'
		}, {
			columnid: 'distance',
			title: 'Distance'
		}, {
			columnid: 'time',
			title: 'Travel Time'
		}, {
			columnid: 'travelPrice',
			title: 'Travel Allowance'
		}, {
			columnid: 'actualPrice',
			title: 'Actual Allowance'
		}
		],
	};

	_autotravelData();
	function _autotravelData() {
		$scope.nameList = [];
		$scope.uniqueMonths = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'commute/fetchCommuteData',
			params: {
				"startDate": $scope.stdt,
				"endDate": $scope.eddt
			}
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

	$scope.filterByAttendStatus = function (status) {
		$scope.travelData = angular.copy($scope.travelDataBak);
		if (status === 'C') {
			$scope.travelData = $scope.travelData.filter(function (item) {
				return item.status === true;
			});
		} else if (status === 'U') {
			$scope.travelData = $scope.travelData.filter(function (item) {
				return item.status === false;
			});
		}
	};

	$scope.fetchTravelData = function (str) {
		$http({
			method: 'GET',
			url: 'commute/fetchCommuteData',
			params: {
				"startDate": $scope.date.startdate,
				"endDate": $scope.date.enddate
			}
		}).then(function successCallback(response) {
			$scope.travelData = response.data.data;
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
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Customer Fetched',
				showConfirmButton: true,
			}).then(function () {

			});

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	$scope.exportData = function () {
		console.log($scope.travelData);
		alasql(
			'SELECT * INTO XLS("TravelData.xls",?) FROM ?',
			[TravelReport, $scope.travelData]);
	};

	$scope.printData = function () {
		printData();
	};

	function printData() {
		const sTable = document.getElementById('customers').outerHTML;

		const printWindow = window.open('', '', 'height=700,width=700');
		const doc = printWindow.document;

		const html = doc.createElement('html');
		const head = doc.createElement('head');
		const body = doc.createElement('body');

		const title = doc.createElement('title');
		title.innerText = 'Status Report';

		const style = doc.createElement('style');
		style.innerHTML = `
			table { width: 100%; font: 17px Calibri; border-collapse: collapse; }
			table, th, td { border: 1px solid #DDD; padding: 2px 3px; text-align: center; }
		`;

		const heading = doc.createElement('h1');
		heading.innerText = 'Status Report';

		const tableContainer = doc.createElement('div');
		tableContainer.innerHTML = sTable;

		head.appendChild(title);
		head.appendChild(style);

		body.appendChild(heading);
		body.appendChild(tableContainer);

		html.appendChild(head);
		html.appendChild(body);

		doc.documentElement.replaceWith(html);

		printWindow.onload = function () {
			printWindow.print();
			printWindow.close();
		};

	}

	$scope.changeView = function (view) {
		if (view == "add" || view == "list" || view == "show") {
			$scope.form = {};
		}
		$scope.views.add = false;
		$scope.views.edit = false;
		$scope.views.list = false;
		$scope.views[view] = true;
	}

});