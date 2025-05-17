var mainApp = angular.module("paidReportData", []);
mainApp.controller('paidReportController', function ($scope, $http) {

    var today = new Date();
    $scope.balanceSheet = [];
    $scope.balStId = [];
    $scope.paidSheet = [];
    paidSheetBak = [];
    $scope.balStId = [];
    $scope.nameList = [];
    $scope.date = {
        startdate: "",
        enddate: ""
    };


    $scope.stdt = new Date(today.getFullYear(), today.getMonth() - 1, 1);;
    $scope.eddt = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    console.log( $scope.stdt );
    console.log($scope.eddt );

    $scope.refresh = function () {
        location.reload();
    }


    _autoPaidSheet();
    function _autoPaidSheet() {
        $scope.nameList = [];
        $http({
            method: 'GET',
            url: 'fetchPaiidSheet',
            params: {
				"stdate": $scope.stdt,
				"enddate": $scope.eddt,
			},
			headers: {
				'Content-Type': 'application/json'
			}

        }).then(function successCallback(response) {
            $scope.paidSheet = response.data;
            angular.forEach($scope.paidSheet, function (val, key) {
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

            angular.forEach($scope.paidSheet, function (item, key) {
                var date = new Date(item.approvedDt);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });

            $scope.paidSheetBak = $scope.paidSheet;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


    $scope.getDataNameWise = function (userid) {
        $scope.uniqueMonths = [];
        $scope.paidSheet = $scope.paidSheetBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.paidSheet, function (item, key) {
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

    $scope.getRange = function(total) {
		return Array.from({ length: total }, (_, index) => index + 1);
	};


    $scope.filterByMonth = function (month) {
        $scope.travelData = $scope.travelDataBak.filter(function (item) {
            var date = new Date(item.startDate);
            return date.getMonth() + 1 === month;
        });
    };


    $scope.fetchTravelData = function (str) {
        $http({
            method: 'GET',
            url: 'fetchPaiidSheet',
            params: {
                "stdate": $scope.date.startdate,
                "enddate": $scope.date.enddate

            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(function successCallback(response) {
            $scope.paidSheet = JSON.parse(response.data);
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Data Fetched',
                showConfirmButton: true,
            }).then(function () {

            });

        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.resetFilter = function () {
        $http({
            method: 'GET',
            url: 'fetchPaiidSheet',

        }).then(function successCallback(response) {
            $scope.paidSheet = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.searchPaidSheet = function (name) {
        console.log("Length: " + name.length > 0)
        $http({
            method: 'GET',
            url: 'searchPaidSheet',
            params: {
                "name": name
            }
        }).then(function successCallback(response) {

            $scope.paidSheet = response.data;
            console.log($scope.locations);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };
    var paidReport = {
        headers: true,
        columns: [{
            columnid: 'name',
            title: 'Name',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'employeeId',
            title: 'Employee Id',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'advanceBalance',
            title: 'Advance Amount',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'addedAdavanceDt',
            title: 'Advance Date',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'allowanceAmount',
            title: 'Allowance Amount',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'approvedDt',
            title: 'Approved Date',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'clusterName',
            title: 'Approved Cluster',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'bankName',
            title: 'Bank Name',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'bankType',
            title: 'Bank Type',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'bankAccountNo',
            title: 'A/C No',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'ifscCode',
            title: 'IFSC Code',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }, {
            columnid: 'payableAmount',
            title: 'Pay',
            style: {
                bold: true,
                align: 'center',
                background: 'yellow'
            }
        }],
    };
    $scope.exportData = function () {
        console.log($scope.paidSheet);
        alasql(
            'SELECT * INTO XLS("paidReport.xls",?) FROM ?',
            [paidReport, $scope.paidSheet]);

    };
    $scope.printData = function () {
        printData();

    };
    function printData() {
        var sTable = document.getElementById('paidReport').innerHTML;
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
    $scope.markPaymentImportedList = function () {
        $scope.multiPaid = [];
        var a = 0;
        for (var i = 0; i < $scope.paidSheet.length; i++) {
            $scope.multiPaid[a] = $scope.paidSheet[i].id;
            console.log($scope.multiPaid);
            a++;
        }
        ($scope.multiaddPayable)
        $http({
            method: 'PUT',
            url: 'markPaymentImportedList',
            params: {
                "ids": $scope.multiPaid
            },
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Mark Paid Successfully',
                showConfirmButton: true,
            }).then(function () {
                location.reload();
            });
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };


});