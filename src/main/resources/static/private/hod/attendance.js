var app = angular.module("HodManagment", []);

app.controller("AttendanceReportController", function ($scope, $http, $timeout) {
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
    $scope.attendanceData = [];
    $scope.attendanceDataBak = [];
    $scope.nameList = [];

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

    var AttendanceReport = {
        headers: true,
        columns: [{
            columnid: 'name',
            title: 'Name'
        }, {
            columnid: 'customerName',
            title: 'customer Name'
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
        },
        ],
    };

    _autoAttendanceData();
    function _autoAttendanceData() {
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'attendance/fetchAttendLevelAuthority',
            params: {
                "stdate": $scope.stdt,
                "enddate": $scope.eddt
            }
        }).then(function successCallback(response) {
            $scope.attendanceData = response.data.data;
            document.getElementById("loader").style.display = "none";
            angular.forEach($scope.attendanceData, function (val, key) {
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
            $scope.attendanceDataBak = $scope.attendanceData;
        }, function errorCallback(response) {
            console.log(response.statusText);
            document.getElementById("loader").style.display = "none";
        });
    };


    $scope.getDataNameWise = function (userid, month) {
        console.log(month);
        $scope.uniqueMonths = [];
        $scope.attendanceData = $scope.attendanceDataBak.filter(function (item) {
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

    $scope.filterByAttendStatus = function(status) {
        $scope.attendanceData = angular.copy($scope.attendanceDataBak);
        if (status === 'C') {
            $scope.attendanceData = $scope.attendanceData.filter(function(item) {
                return item.status === true;
            });
        } else if (status === 'U') {
            $scope.attendanceData = $scope.attendanceData.filter(function(item) {
                return item.status === false;
            });
        }
    };


    $scope.fetchAttendanceData = function (str) {
        $http({
            method: 'GET',
            url: 'attendance/fetchAttendLevelAuthority',
            params: {
                "stdate": $scope.date.startdate,
                "enddate": $scope.date.enddate,
            }
        }).then(function successCallback(response) {
            $scope.attendanceData = response.data.data;
            $scope.nameList=[];
            angular.forEach($scope.attendanceData, function (val, key) {
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

            $scope.attendanceDataBak = $scope.attendanceData;
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
        console.log($scope.attendanceData);
        alasql(
            'SELECT * INTO XLS("AttendanceReport.xls",?) FROM ?',
            [AttendanceReport, $scope.attendanceData]);

    };

    $scope.printData = function () {
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
        var win = window.open('', '',
            'height=700,width=700');

        win.document.write('<html><head>');
        win.document.write('<h1>Status Report</h1>');
        win.document.write(style);
        win.document.write('</head>');
        win.document.write('<body>');
        win.document.write(sTable);
        win.document.write('</body></html>');
        win.document.close();
        win.print();
    }

});
