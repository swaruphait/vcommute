var mainApp = angular.module('dashboardData', []);
mainApp.controller('dashboardController', function ($scope, $http) {
    $scope.barHod = [];
    $scope.lineDataHod = [];
    $scope.lineWeekDataHod = [];
    $scope.lineAttendanceDataHod = [];
    $scope.lineUsesDataHod = [];
    $scope.doughnutData = [];
    $scope.monthlyvisitHod = {
      premonth: "",
      pastmonth: "",
      preweek: "",
      pastweek: ""
    };
    $scope.weeklyvisit = "";

    // *****************Dashbord Bar Graph********************
    
     _autoBarChartHod();
    function _autoBarChartHod() {
      $http({
        method: 'GET',
        url: 'hodBarchartdata',
      }).then(function successCallback(response) {
        $scope.barHod = response.data;
        //	console.log($scope.bar);
        const ctx = document.getElementById('barChartHod');
        var myChart =  new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                    datasets: [{
                        data: $scope.barHod,
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
// *****************Commute Activity Line Graph********************

_monthlyUsesChartHod();
function _monthlyUsesChartHod() {
  $http({
    method: 'GET',
    url: 'hodMntUsesApplication',

  }).then(function successCallback(response) {
    $scope.lineUsesDataHod = response.data;
    const ctx2 = document.getElementById('lineUsesChartHod');
    var myChart = 	new Chart(ctx2, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                data:  $scope.lineUsesDataHod,
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
_monthlyAttendanceChartHod();
  function _monthlyAttendanceChartHod() {
    $http({
      method: 'GET',
      url: 'hodMntAttndchartdata',

    }).then(function successCallback(response) {
      $scope.lineAttendanceDataHod = response.data;
      const ctx3 = document.getElementById('lineAttenChartHod');

      new Chart(ctx3, {
          type: 'line',
          data: {
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
              datasets: [{
                  data:   $scope.lineAttendanceDataHod,
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

//   ****************Monthly visit*******************
_monthlyVisitChartHod();
function _monthlyVisitChartHod() {
  $http({
    method: 'GET',
    url: 'hodBarchartdata',

  }).then(function successCallback(response) {
    $scope.lineDataHod = response.data;
    //console.log($scope.lineData);
    const ctx4 = document.getElementById('lineChartHod');

		new Chart(ctx4, {
			type: 'line',
			data: {
				labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
				datasets: [{
					data: $scope.lineDataHod,
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
    _weekVisitChartHod();
  function _weekVisitChartHod() {
    $http({
      method: 'GET',
      url: 'hodWeekchartdata',

    }).then(function successCallback(response) {
      $scope.lineWeekDataHod = response.data;
      const ctx5 = document.getElementById('weekChartHod');

      new Chart(ctx5, {
          type: 'line',
          data: {
              labels: ['MON', 'TUS', 'WED', 'THS', 'FRI', 'SAT', 'SUN'],
              datasets: [{
                  data:   $scope.lineWeekDataHod,
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
      url: 'adminUserDistributiondata',
    }).then(function successCallback(response) {
      $scope.doughnutData = response.data;
      $scope.xValues = [];
      $scope.yValues = [];
      const ctx6 = document.getElementById('doughnutChart');
      angular.forEach($scope.doughnutData, function (value, key) {
        $scope.xValues.push(key);
        $scope.yValues.push(value);
      });
      console.log("Label: "+$scope.xValues);
      console.log("data: "+$scope.yValues);
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
  };
});