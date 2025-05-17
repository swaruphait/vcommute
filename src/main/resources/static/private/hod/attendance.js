var mainApp = angular.module("approvalData", []);

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

mainApp.controller('approvalDataController', function ($scope, $http) {

	$scope.imageIsViewed = false;
	$scope.month_id = {};
	$scope.refresh = function () {
		location.reload();
	};
	$scope.customers = []; // Initialize customers array


	$scope.form = {
		userId: null,
		startDate: null,
		startTime: null,
		startCustId: null,
		endDate: null,
		endTime: null,
		endCustId: null
	};

	var AttendanceReport = {
		headers: true,
		columns: [{
			columnid: 'username',
			title: 'Name'
		}, {
			columnid: 'startCustName',
			title: 'Start Customer'
		},{
			columnid: 'startDate',
			title: 'Start Date'
		}, {
			columnid: 'startTime',
			title: 'Start Time'
		}, {
			columnid: 'endCustName',
			title: 'Start Location'
		}, {
			columnid: 'customerLocation',
			title: 'End Customer'
		}, {
			columnid: 'endDate',
			title: 'End Date'
		}, {
			columnid: 'endTime',
			title: 'End Time'
		}, {
			columnid: 'endLocation',
			title: 'End Location'
		}
		],
	};

	// function initFlatpickr(selector, format, modelKey) {
	// 	flatpickr(selector, {
	// 		enableTime: false,
	// 		noCalendar: false,
	// 		dateFormat: format, 
	// 		allowInput: true,
	// 		onChange: function (selectedDates, dateStr) {
	// 			if (!$scope.$$phase) {
	// 				$scope.$apply(function () {
	// 					$scope.form[modelKey] = dateStr;
	// 				});
	// 			} else {
	// 				$scope.form[modelKey] = dateStr;
	// 			}
	// 		}
	// 	});
	// }
	
	// // Initialize Flatpickr for different date fields
	// initFlatpickr("#startDate", "Y-m-d", "startDate");
	// initFlatpickr("#endDate", "Y-m-d", "endDate");

	flatpickr("#startDate", {
		enableTime: false,
		noCalendar: false, // Disable calendar (only time)
		dateFormat: "Y-m-d", // Format as HH:mm:ss
		allowInput: true, // Allow manual input
		onChange: function (selectedDates, startDateStr) {
			// Update the AngularJS model when the time changes
			$scope.$apply(function () {
				$scope.form.startDate = startDateStr;
			});
		}
	});

	flatpickr("#endDate", {
		enableTime: false,
		noCalendar: false, // Disable calendar (only time)
		dateFormat: "Y-m-d", // Format as HH:mm:ss
		allowInput: true, // Allow manual input
		onChange: function (selectedDates, ebnDateStr) {
			// Update the AngularJS model when the time changes
			$scope.$apply(function () {
				$scope.form.endDate = ebnDateStr;
			});
		}
	});


	flatpickr("#startTime", {
		enableTime: true, // Enable time selection
		noCalendar: true, // Disable calendar (only time)
		dateFormat: "H:i:S", // Format as HH:mm:ss
		time_24hr: true, // Use 24-hour format
		minuteIncrement: 1, // Increment time by 1 minute
		secondIncrement: 1, // Increment time by 1 second
		allowInput: true, // Allow manual input
		onChange: function (selectedDates, startDateStr) {
			// Update the AngularJS model when the time changes
			$scope.$apply(function () {
				$scope.form.startTime = startDateStr;
			});
		}
	});

	flatpickr("#endTime", {
		enableTime: true, // Enable time selection
		noCalendar: true, // Disable calendar (only time)
		dateFormat: "H:i:S", // Format as HH:mm:ss
		time_24hr: true, // Use 24-hour format
		minuteIncrement: 1, // Increment time by 1 minute
		secondIncrement: 1, // Increment time by 1 second
		allowInput: true, // Allow manual input
		onChange: function (selectedDates, enddateStr) {
			// Update the AngularJS model when the time changes
			$scope.$apply(function () {
				$scope.form.endTime = enddateStr;
			});
		}
	});

	_autoAttendanceData();
	function _autoAttendanceData() {
		$scope.nameList = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchAttendApprovalDataAuthorityWise'
		}).then(function successCallback(response) {
			// console.log(JSON.parse(response.data));
			$scope.attendanceData = response.data;
			angular.forEach($scope.attendanceData, function (val, key) {
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

			angular.forEach($scope.attendanceData, function (item, key) {
				var date = new Date(item.startDate);
				var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
				var monthNumber = date.getMonth() + 1;
				var monthYear = `${monthName}${date.getFullYear()}`;
				var monthObj = { month: monthNumber, name: monthYear };

				if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
					$scope.uniqueMonths.push(monthObj);
				}
			});

			$scope.travelDataBak = $scope.attendanceData;
			document.getElementById("loader").style.display = "none";

		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};


	$scope.getDataNameWise = function (userid, month) {
		console.log(month);
		$scope.uniqueMonths = [];
		$scope.attendanceData = $scope.travelDataBak.filter(function (item) {
			return item.userId === userid;
		});

		angular.forEach($scope.attendanceData, function (item, key) {
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
			$scope.attendanceData = $scope.attendanceData.filter(function (item) {
				var date = new Date(item.startDate);
				return date.getMonth() + 1 === month;
			});
		}

	}

	$scope.getRange = function (total) {
		return Array.from({ length: total }, (_, index) => index + 1);
	};

	_autoFetchApprovalData();
	function _autoFetchApprovalData() {
		$http({
			method: 'GET',
			url: 'fetchUserListByAuthority'
		}).then(function successCallback(response) {
			$scope.users = response.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};



	$scope.openEntryAttendance = function () {
		$scope.form = {};
		$scope.fetchAllCustomer();
		$("#attendanceEntry").modal("show");

	}

	$scope.openEditAttendance = function () {
		$scope.form = {};
		$("#editAttendance").modal("show");

	}

	// Fetch customer data from the server
	$scope.fetchAllCustomer = function () {
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'fetchCustomer',
		}).then(function successCallback(response) {
			$scope.customers = response.data;
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			document.getElementById("loader").style.display = "none";
			console.log(response.statusText);
		});
	};

	$scope.updateStartCustomer = function(data, type) {
		let selectedCustomer = $scope.customers.find(cust => cust.name === data);
		if (selectedCustomer) {
			$scope.form[type] = selectedCustomer.id; // Dynamically update form field
		} else {
			$scope.form[type] = null;
		}
	};
	
	
	$scope.attendFindById = function (str) {
		$http({
			method: 'GET',
			url: 'findAttendById',
			params: { "id": str }
		}).then(function successCallback(response) {
			$scope.form = response.data;
			$scope.fetchAllCustomer();
			// Convert startDate to a valid YYYY-MM-DD format
			// if ($scope.form.startDate) {
			// 	$scope.form.startDate = new Date($scope.form.startDate);
			// }
	        
		}, function errorCallback(response) {
			console.error("Error:", response.statusText);
		});
	};
	



	$scope.entryAttendance = function () {
		// Ensure only time is sent, not the full datetime string

		console.log($scope.form);
		$http({
			method: "POST",
			url: 'addAttendanceByAuthority',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(function successCallback(response) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Success',
				showConfirmButton: true,
			}).then(function () {
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



	$scope.editAttendance = function () {
		console.log($scope.form);
		$http({
			method: "PUT",
			url: 'editAttendance',
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(function successCallback(response) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Success',
				showConfirmButton: true,
			}).then(function () {
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


	$scope.filterByMonth = function (month, userid) {
		console.log(month);
		console.log(userid);
		$scope.attendanceData = $scope.travelDataBak.filter(function (item) {
			var date = new Date(item.startDate);
			return date.getMonth() + 1 === month;
		});

		if (userid !== undefined) {
			$scope.attendanceData = $scope.attendanceData.filter(function (item) {
				return item.userId === userid;
			});
		}
	};

	$scope.exportData = function() {
		console.log($scope.attendanceData);
		alasql(
			'SELECT * INTO XLS("AttendanceData.xls",?) FROM ?',
			[AttendanceReport, $scope.attendanceData]);

	};


	$scope.printData = function() {
		printData();

	};
	function printData() {

		var sTable = document.getElementById('customers').innerHTML;

		var style = "<style>";
		style = style
			+ "table {width: 100%;font: 17px Calibri;}";
		style = style
			+ "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
		style = style
			+ "padding: 2px 3px;text-align: center;}";
		style = style + "</style>";

		// CREATE A WINDOW OBJECT.
		var win = window.open('', '',
			'height=700,width=700');

		win.document.write('<html><head>');
		win.document.write('<h1>Attendance Report</h1>'); // <title> FOR PDF HEADER.
		win.document.write(style); // ADD STYLE INSIDE THE HEAD TAG.
		win.document.write('</head>');
		win.document.write('<body>');
		win.document.write(sTable); // THE TABLE CONTENTS INSIDE THE BODY TAG.
		win.document.write('</body></html>');

		win.document.close(); // CLOSE THE CURRENT WINDOW.

		win.print(); // PRINT THE CONTENTS.

	}

});