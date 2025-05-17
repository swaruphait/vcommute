var app = angular.module("SuperAdminManagment", []);

app.controller("HolidayMasterController", function ($scope, $http, $timeout) {
    $scope.users = [];
    $scope.locations = [];
    $scope.companies = [];
    $scope.holiday = {};
    $scope.supercompany = "";

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

    _autoCompanyListFetch();
    function _autoCompanyListFetch() {
        showHideLoad();
        $http({
            method: 'GET',
            url: 'subcompany/findByAllActive'
 
        }).then(function successCallback(response) {
            $scope.supercompany =  response.data.data[0].superCompanyName;
            $scope.companies = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
 

    _holidayYearList();
    function _holidayYearList() {
        showHideLoad();
        $http({
            method: 'GET',
            url: 'holiday/fetchYearList',
            params: { 'companyId': $scope.holiday.compId },
        }).then(function successCallback(response) {
            console.log(response);
            $scope.yearList = response.data;
            if ($scope.yearList.length > 0) {
                $scope.years = $scope.yearList[0]; // Assign value only if data exists
            }
            showHideLoad(true);

            // Call _autoHoildayListFetch only after years is set
            if ($scope.years) {
                _autoHoildayListFetch();
            }
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    function _autoHoildayListFetch() {
        showHideLoad();
        $http({
            method: 'GET',
            url: 'holiday/findByAll',
            params: {
                'year': $scope.years,
            },
        }).then(function successCallback(response) {
            $scope.holidays = response.data;
            // alert($scope.holidays);
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    $scope.fetchHoliday = function (str) {
        $http({
            method: 'GET',
            params: { 'companyId': str,
                      'year': $scope.years,
             },
            url: 'holiday/findByAll'
        }).then(function successCallback(response) {
            $scope.holidays = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }



    $scope.addHolidayList = function () {
        showHideLoad();
        var method = "POST";
        var url = 'holiday/createHoliday';
        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success,_error)
    };

    function _success(response) {
		$scope.form = {};
		_holidayYearList();
		Swal.fire({
			text: response.data,
			icon: "success",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
	}

	function _error(response) {
		Swal.fire({
			text: response.data,
			icon: "error",
			buttonsStyling: !1,
			confirmButtonText: "Ok, got it!",
			customClass: {
				confirmButton: "btn btn-primary"
			}
		})
	}



    $scope.editHoildays = function (str) {
        $http({
            method: 'GET',
            url: 'holiday/findById',
            params: {
                'id': str,
            },
        }).then(function successCallback(response) {
            $scope.form = response.data;
       
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

    $scope.deleteHoliday = function (str) {
        $http({
            method: 'DELETE',
            url: 'holiday/deleteHoliday',
            params: {
                'id': str,
            },
        }).then(function successCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                _holidayYearList();
                location.reload();
            });
        }, function errorCallback(response) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: response.data,
                showConfirmButton: true,
            }).then(function () {
                _holidayYearList();
                location.reload();
            });
        });;
    };


    $scope.changeView = function (view) {
        if (view == "add" || view == "list" || view == "show") {
            $scope.edits = {};
        }
        $scope.views.add = false;
        $scope.views.edit = false;
        $scope.views.list = false;
        $scope.views[view] = true;
    }


});