var app = angular.module("HodManagment", []);

app.controller("LeaveReportController", function ($scope, $http, $timeout) {
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

    $scope.leaveData = [];
    $scope.leaveDataBak = [];
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

    var LeaveReport = {
        headers: true,
        columns: [{
            columnid: 'name',
            title: 'Name'
        }, {
            columnid: 'leaveStartDate',
            title: 'Leave Start Date'
        }, {
            columnid: 'leaveEndDate',
            title: 'Leave End Date'
        }, {
            columnid: 'noOfDaysLeave',
            title: 'No Of Days Leave'
        }, {
            columnid: 'activityBy',
            title: 'Action Taken'
        }, {
            columnid: 'status',
            title: 'Status'
        }
        ],
    };


    

    _autoLeaveData();
    function _autoLeaveData() {
        $scope.nameList = [];
        $scope.uniqueMonths = [];
        document.getElementById("loader").style.display = "block";
        $http({
            method: 'GET',
            url: 'leave_apvl/fetchLeaveAuthorityData',
            params: {
                "startDate": $scope.stdt,
                "endDate": $scope.eddt
            }
        }).then(function successCallback(response) {
            $scope.leaveData = response.data.data;
            document.getElementById("loader").style.display = "none";
            angular.forEach($scope.leaveData, function (val, key) {
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

            angular.forEach($scope.leaveData, function (item, key) {
                var date = new Date(item.leaveStartDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });
            $scope.leaveDataBak = $scope.leaveData;
        }, function errorCallback(response) {
            console.log(response.statusText);
            document.getElementById("loader").style.display = "none";
        });
    };


    $scope.getDataNameWise = function (userid, month) {
        console.log(month);
        $scope.uniqueMonths = [];
        $scope.leaveData = $scope.leaveDataBak.filter(function (item) {
            return item.userId === userid;
        });

        angular.forEach($scope.leaveData, function (item, key) {
            var date = new Date(item.leaveStartDate);
            var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
            var monthNumber = date.getMonth() + 1;
            var monthYear = `${monthName}${date.getFullYear()}`;
            var monthObj = { month: monthNumber, name: monthYear };

            if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                $scope.uniqueMonths.push(monthObj);
            }
        });

        if (month !== undefined) {
            $scope.leaveData = $scope.leaveData.filter(function (item) {
                var date = new Date(item.leaveStartDate);
                return date.getMonth() + 1 === month;
            });
        }
    }

    $scope.getRange = function (total) {
        return Array.from({ length: total }, (_, index) => index + 1);
    };

    $scope.filterByLeaveStatus = function(status) {
        $scope.leaveData = angular.copy($scope.leaveDataBak);
        if (status === 'C') {
            $scope.leaveData = $scope.leaveData.filter(function(item) {
                return item.status === 'C';
            });
        } else if (status === 'A') {
            $scope.leaveData = $scope.leaveData.filter(function(item) {
                return item.status === 'A';
            });
        } else if (status === 'R') {
            $scope.leaveData = $scope.leaveData.filter(function(item) {
                return item.status === 'R';
            });
        }
    };


    $scope.fetchAttendanceData = function (str) {
        $http({
            method: 'GET',
            url: 'leave_apvl/fetchLeaveAuthorityData',
            params: {
                "stdate": $scope.date.startdate,
                "enddate": $scope.date.enddate,
            }
        }).then(function successCallback(response) {
            $scope.leaveData = response.data.data;
            $scope.nameList=[];
            angular.forEach($scope.leaveData, function (val, key) {
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

            angular.forEach($scope.leaveData, function (item, key) {
                var date = new Date(item.leaveStartDate);
                var monthName = date.toLocaleString('default', { month: 'short' }).toUpperCase();
                var monthNumber = date.getMonth() + 1;
                var monthYear = `${monthName}${date.getFullYear()}`;
                var monthObj = { month: monthNumber, name: monthYear };

                if (!($scope.uniqueMonths.some(e => e.month === monthObj.month && e.name === monthObj.name))) {
                    $scope.uniqueMonths.push(monthObj);
                }
            });

            $scope.leaveDataBak = $scope.leaveData;
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
        console.log($scope.leaveData);
        alasql(
            'SELECT * INTO XLS("LeaveReport.xls",?) FROM ?',
            [LeaveReport, $scope.leaveData]);

    };

    $scope.printData = function () {
        printData();

    };

    function printData() {
    const sTable = document.getElementById('customers').outerHTML;

    const printWindow = window.open('', '', 'height=700,width=700');

    // Create HTML document structure
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
    printWindow.print();
    // Wait for DOM render before printing
    printWindow.onload = function () {
        printWindow.print();
        printWindow.close(); // optional, closes the window after printing
    };
}

    
});
