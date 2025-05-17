var mainApp = angular.module("reportData", []);
mainApp.controller('reportDataController', function($scope, $http) {
	var today = new Date();
	$scope.date = {
		startdate: "",
		enddate: "",
		active: 'true',
		location: ""
	};

  $scope.stdt =  new Date(today.getFullYear(), today.getMonth() - 1, 1);;
  $scope.eddt = new Date(today.getFullYear(), today.getMonth() + 1, 0);
  console.log("Start Date: "+$scope.stdt);
  console.log("End Date: "+$scope.eddt);
	var TravelReport = {
		headers: true,
		columns: [{
			columnid: 'username',
			title: 'Name'
		}, {
			columnid: 'customerName',
			title: 'customer Name'
		}, {
			columnid: 'customerLocation',
			title: 'customer Location'
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
			columnid: 'totalDistance',
			title: 'Distance'
		}, {
			columnid: 'totalTime',
			title: 'Travel Time'
		}, {
			columnid: 'totalEstimatePrice',
			title: 'Estimated Price'
		}, {
			columnid: 'totalActualPrice',
			title: 'Actual Allowance'
		}
		],
	};

	_autotravelData();
	function _autotravelData() {
		$scope.nameList = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'reportGenAutority',
			params: {
				"stdate": $scope.stdt,
				"enddate": $scope.eddt,
				"active": 'ALL'

			},

			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
			//console.log(response.data);
			//alert(response.data)
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
			$scope.travelDataBak = $scope.travelData;
			document.getElementById("loader").style.display = "none";
		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	}
	;


	$scope.getDataNameWise = function (userid) {
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

    }

$scope.searchReportHod = function(name) {
		var startDate = $scope.date.startdate || $scope.stdt;
		var endDate = $scope.date.enddate || $scope.eddt;
		var status = $scope.date.status ? 'ALL' : $scope.date.active;

		console.log("startDate: " + startDate);
		console.log("endDate: " + endDate);
		console.log("status: " + status);

		$http({
			method: 'GET',
			url: 'searchReportHod',
			params: {
				"name": name,
				"stdate": startDate,
				"enddate": endDate,
				"active": status

			}
		}).then(function successCallback(response) {
			$scope.travelData = response.data;
			console.log($scope.travelData);
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	$scope.fetchHodTravelDataReport = function(str) {
		$scope.nameList = [];
		document.getElementById("loader").style.display = "block";
		$http({
			method: 'GET',
			url: 'reportGenAutority',
			params: {
				"stdate": $scope.date.startdate,
				"enddate": $scope.date.enddate,
				"active": $scope.date.active

			},

			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity

		}).then(function successCallback(response) {
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
			$scope.travelDataBak = $scope.travelData;
			document.getElementById("loader").style.display = "none";
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Data Fetched',
				showConfirmButton: true,
			}).then(function() {

			});

		}, function errorCallback(response) {
			console.log(response.statusText);
			document.getElementById("loader").style.display = "none";
		});
	};

	$scope.exportData = function() {
		console.log($scope.travelData);
		alasql(
			'SELECT * INTO XLS("TravelData.xls",?) FROM ?',
			[TravelReport, $scope.travelData]);

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
		win.document.write('<h1>Status Report</h1>'); // <title> FOR PDF HEADER.
		win.document.write(style); // ADD STYLE INSIDE THE HEAD TAG.
		win.document.write('</head>');
		win.document.write('<body>');
		win.document.write(sTable); // THE TABLE CONTENTS INSIDE THE BODY TAG.
		win.document.write('</body></html>');

		win.document.close(); // CLOSE THE CURRENT WINDOW.

		win.print(); // PRINT THE CONTENTS.

	}
});