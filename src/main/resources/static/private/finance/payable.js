var mainApp = angular.module("payableData", []);
mainApp
	.controller(
		'payableDataController',
		function($scope, $http) {
			$scope.balanceSheet = [];
			$scope.balStId = [];

			$scope.refresh = function() {
				location.reload();
			}

			_autoBalanceSheet();
			function _autoBalanceSheet() {
				$http({
					method: 'GET',
					url: 'fetchBalanceSheet',
				}).then(function successCallback(response) {
					$scope.balanceSheet = response.data;
					$scope.balStId = JSON.parse(response.data);
					console.log($scope.balStId);
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			};
		
			_autoCluster();
			function _autoCluster() {
				$http({
					method: 'GET',
					url: 'fetchCluster',
				}).then(function successCallback(response) {
					$scope.clusters = response.data;
					console.log($scope.clusters.id);
				}, function errorCallback(response) {
					console.log(response.statusText);
				});
			}
			;
			$scope.markPaidData = function(str) {
				$scope.multiCheck = [];
				var a = 0;
				for (var i = 0; i < $scope.balanceSheet.length; i++) {
					if ($scope.balanceSheet[i].selected) {
						$scope.multiCheck[a] = $scope.balanceSheet[i].id;
					}
					a++;
				}
				console.log($scope.multiCheck);

				$http({
					method: 'PUT',
					url: 'markListOfPaidSheet',
					params: {
						"ids": $scope.multiCheck,
					},
					headers: {
						'Content-Type': 'application/json'
					},
					transformResponse: angular.identity
				})
					.then(
						function successCallback(response) {
							Swal.fire({
								position: 'center',
								icon: 'success',
								title: 'Approved',
								showConfirmButton: true,
							}).then(function() {
								location.reload();
							});
						}, function errorCallback(response) {
							Swal.fire({
								icon: 'error',
								title: 'Oops!! Empty Selection...',
								text: 'Please mark any Data!',
							})
							console.log(response.statusText);
						});
			}
			$scope.selected = [];
			$scope.exist = function(item) {
				return $scope.selected.indexOf(item) > -1;

			}
			var allowanceReport = {
				headers: true,
				columns: [{
					columnid: 'name',
					title: 'Name',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'employeeId',
					title: 'Employee Id',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'advanceBalance',
					title: 'Advance Amount',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'advanceDate',
					title: 'Advance Date',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'payableAmount',
					title: 'Commute Amount',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'approvedDt',
					title: 'Approved Date',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'clusterName',
					title: 'Approved Cluster',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'bankName',
					title: 'Bank Name',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'bankType',
					title: 'Bank Type',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'bankAccountNo',
					title: 'A/C No',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'ifscCode',
					title: 'IFSC Code',
					style: {
						bold: true,
						align: 'center'
					}
				}, {
					columnid: 'balance',
					title: 'Pay',
					style: {
						bold: true,
						align: 'center'
					}
				}],
			};
			$scope.exportData = function() {
				console.log($scope.balanceSheet);
				alasql(
					'SELECT * INTO XLS("allowanceReport.xls",?) FROM ?',
					[allowanceReport, $scope.balanceSheet]);

			};
			$scope.printData = function() {
				printData();

			};

			$scope.printData = function() {
				printData();

			};
			function printData() {

				var sTable = document
					.getElementById('balanceReport').innerHTML;

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
				win.document.write('<h1>Allowance Report</h1>');
				// <title> FOR PDF HEADER.
				win.document.write(style); // ADD STYLE INSIDE THE HEAD TAG.
				win.document.write('</head>');
				win.document.write('<body>');

				win.document.write(sTable); // THE TABLE CONTENTS INSIDE THE BODY TAG.
				win.document.write('</body></html>');

				win.document.close(); // CLOSE THE CURRENT WINDOW.

				win.print(); // PRINT THE CONTENTS.

			}
		});