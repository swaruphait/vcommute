var app = angular.module("AdminManagment", []);
app.controller("dashboardController", function ($scope, $http, $timeout) {
	$scope.barAdmin = [];
	$scope.barHod = [];
	$scope.lineDataAdmin = [];
	$scope.lineDataHod = [];
	$scope.lineWeekDataAdmin = [];
	$scope.lineWeekDataHod = [];
	$scope.lineAttendanceData = [];
	$scope.lineAttendanceDataHod = [];
	$scope.lineUsesDataAdmin = [];
	$scope.lineUsesDataHod = [];
	$scope.doughnutData = [];
	$scope.monthlyvisitAdmin = {
		premonth: "",
		pastmonth: "",
		preweek: "",
		pastweek: ""
	};
	$scope.monthlyvisitHod = {
		premonth: "",
		pastmonth: "",
		preweek: "",
		pastweek: ""
	};
	$scope.weeklyvisit = "";

	_autoMonthlyVisitAdmin();
	function _autoMonthlyVisitAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/monthlyvisit',
		}).then(function successCallback(response) {
			$scope.monthlyvisitAdmin = response.data;
		}, function errorCallback(response) {
		});
	};

	_autoCommuteDataUpdated();
	function _autoCommuteDataUpdated() {
		$http({
			method: 'GET',
			url: 'commute/autoupdateDataBygoogleapi'
		}).then(function successCallback(response) {

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	_autoBarChartAdmin();
	function _autoBarChartAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/adminBarchartdata',
		}).then(function successCallback(response) {
			$scope.barAdmin = response.data;
			//	console.log($scope.bar);
			const ctx = document.getElementById('barChart');
			var myChart = new Chart(ctx, {
				type: 'bar',
				data: {
					labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
					datasets: [{
						data: $scope.barAdmin,
						borderWidth: 0,
						hoverBorderWidth: 0,
						borderRadius: Number.MAX_VALUE,
						maxBarThickness: 20,
						backgroundColor: 'rgba(243, 243, 243, 1)',
						hoverBackgroundColor: 'rgba(204, 233, 255, 1)'
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							///display: false, // Hide Y axis labels
							border: {
								display: false // base line
							},
							grid: {
								color: 'rgba(160, 158, 158, 0.5)'

							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label

							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false, //to hide vartical lines
								drawTicks: false
							},
							ticks: {
								color: 'rgba(237, 237, 237, 1)'
							}
						}
					},
					plugins: {
						legend: {
							display: false //to hide top label
						}
					}
				}
			});

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	_monthlyUsesChartAdmin();
	function _monthlyUsesChartAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/adminMntUsesApplication',

		}).then(function successCallback(response) {
			$scope.lineUsesDataAdmin = response.data;
			const ctx2 = document.getElementById('lineUsesChart');
			var myChart = new Chart(ctx2, {
				type: 'line',
				data: {
					labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
					datasets: [{
						data: $scope.lineUsesDataAdmin,
						borderWidth: 1,
						//tension: 0.4, // making the line curve
						backgroundColor: '#217ABF',
						hoverBackgroundColor: '#217ABF',
						borderColor: '#217ABF',
						hoverBorderColor: '#217ABF'
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label
							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								display: false //this will remove only the label
							}
						}
					},
					plugins: {
						legend: {
							display: false //to hide top label
						}
					}
				}
			});

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};
	// *****************Attendance Activity Line Graph********************
	//monthly attendance
	_monthlyAttendanceChartAdmin();
	function _monthlyAttendanceChartAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/adminMntAttndchartdata',

		}).then(function successCallback(response) {
			$scope.lineAttendanceData = response.data;
			const ctx3 = document.getElementById('lineAttenChart');

			new Chart(ctx3, {
				type: 'line',
				data: {
					labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
					datasets: [{
						data: $scope.lineAttendanceData,
						borderWidth: 1,
						//tension: 0.4, // making the line curve
						backgroundColor: '#217ABF',
						hoverBackgroundColor: '#217ABF',
						borderColor: '#217ABF',
						hoverBorderColor: '#217ABF'
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label
							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								display: false //this will remove only the label
							}
						}
					},
					plugins: {
						legend: {
							display: false //to hide top label
						}
					}
				}
			});

			const ctx4 = docum
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};
	_monthlyVisitChartAdmin();
	function _monthlyVisitChartAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/adminBarchartdata',

		}).then(function successCallback(response) {
			$scope.lineDataAdmin = response.data;
			//console.log($scope.lineData);
			const ctx4 = document.getElementById('lineChart');

			new Chart(ctx4, {
				type: 'line',
				data: {
					labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
					datasets: [{
						data: $scope.lineDataAdmin,
						borderWidth: 1,
						tension: 0.4, // making the line curve
						backgroundColor: '#217ABF',
						hoverBackgroundColor: '#217ABF',
						borderColor: '#217ABF',
						hoverBorderColor: '#217ABF'
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label
							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								display: false //this will remove only the label
							}
						}
					},
					plugins: {
						legend: {
							display: false //to hide top label
						}
					}
				}
			});

		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

	// ******************Weekly Visit Line Chart**********************
	_weekVisitChartAdmin();
	function _weekVisitChartAdmin() {
		$http({
			method: 'GET',
			url: 'dashboard/adminWeekchartdata',

		}).then(function successCallback(response) {
			$scope.lineWeekDataAdmin = response.data;
			const ctx5 = document.getElementById('weekChart');

			new Chart(ctx5, {
				type: 'line',
				data: {
					labels: ['MON', 'TUS', 'WED', 'THS', 'FRI', 'SAT', 'SUN'],
					datasets: [{
						data: $scope.lineWeekDataAdmin,
						borderWidth: 1,
						tension: 0.4, // making the line curve
						backgroundColor: '#217ABF',
						hoverBackgroundColor: '#217ABF',
						borderColor: '#217ABF',
						hoverBorderColor: '#217ABF'
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label
							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								display: false //this will remove only the label
							}
						}
					},
					plugins: {
						legend: {
							display: false //to hide top label
						}
					}
				}
			});
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};
	// ******************User Distribution Doughnut Chart*********************
	_doughnutChart();
	function _doughnutChart() {
		$http({
			method: 'GET',
			url: 'dashboard/adminUserDistributiondata',
		}).then(function successCallback(response) {
			$scope.doughnutData = response.data;
			$scope.xValues = [];
			$scope.yValues = [];
	
			// Parsing the response data
			angular.forEach($scope.doughnutData, function (item) {
				$scope.xValues.push(item.access);
				$scope.yValues.push(item.count);
			});
	
			console.log("Label: " + $scope.xValues);
			console.log("Data: " + $scope.yValues);
	
			const ctx6 = document.getElementById('doughnutChart');
			new Chart(ctx6, {
				type: 'doughnut',
				data: {
					labels: $scope.xValues,
					datasets: [{
						data: $scope.yValues,
						backgroundColor: [
							'#45A9F1',
							'#F1C145',
							'#C8CC09',
							'#D8684F',
							'#A06AF9',
							'#1FBBA8',
							'#1B72B0',
						],
						borderWidth: 0,
						maxBarThickness: 25
					}]
				},
				options: {
					maintainAspectRatio: false,
					scales: {
						y: {
							beginAtZero: true,
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								maxTicksLimit: 8,
								display: false //this will remove only the label
							}
						},
						x: {
							border: {
								display: false // base line
							},
							grid: {
								display: false
							},
							ticks: {
								display: false //this will remove only the label
							}
						}
					},
					plugins: {
						legend: {
							position: 'right',
							labels: {
								boxWidth: 15,
								padding: 15
							}
						}
					}
				}
			});
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

});
