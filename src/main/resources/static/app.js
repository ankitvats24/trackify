var app = angular.module('trackifyApp', ['ngMaterial','md.data.table']);
app.controller('liveStatusCtrl', function($scope, $http, $interval, $filter, $window) {

	$scope.startDate = new Date();
	$scope.endDate = new Date();
	$scope.maxDate = new Date();
	$scope.currentTimeMili=0;

	$scope.deviceDetails={};
	$interval(checkDeviceStatus, 5000);
	$scope.orderBy='status';
	$scope.reverse=true;
	function checkDeviceStatus(){
		$http.get("/deviceLiveStatus")
		.then(function (response) {
			$scope.currentTimeMili = new Date().getTime();
			console.log($scope.currentTimeMili);
			if(null==response.data || undefined == response.data){
				$window.alert("No data found!")
			}else{
				$scope.allDeviceDetails = response.data;
				console.log($scope.allDeviceDetails);
				$scope.upDeviceDetails = $filter('filter')($scope.allDeviceDetails, {status: true});
				$scope.downDeviceDetails = $filter('filter')($scope.allDeviceDetails, {status: false});

			}

		});

	}

	$scope.downloadReport = function () {  

		var start = $filter('date')($scope.startDate, "yyyy-MM-dd");
		var end = $filter('date')($scope.endDate, "yyyy-MM-dd");

		if(this.startDate<=this.endDate){
			$window.open("/getReport?start="+start+"&end="+end,'_blank');
			return;
		} else {
			$window.alert("Start date should be earlier than or equal to end date and today !");
			return;
		}

	}; 
});
app.controller('statusCtrl', function($scope, $http, $interval, $window) {

	$scope.deviceId="";

	$scope.getDeviceStatus = function getDeviceStatus(deviceId){
		console.log($scope.deviceId);
		$http.get("/deviceLog", {
			params: { "deviceId": $scope.deviceId }
		}).then(function (response) {
			if(null==response.data || undefined == response.data){
				$window.alert("No data found!")
			}else{
				$scope.deviceDetails = response.data;	
			}
		});

	}

});
app.filter('secondsToTime', function() {

	function padTime(t) {
		return t < 10 ? "0"+t : t;
	}

	return function(_seconds) {
		if (typeof _seconds !== "number" || _seconds < 0)
			return "";

		var hours = Math.floor(_seconds / 3600),
		minutes = Math.floor((_seconds % 3600) / 60),
		seconds = Math.floor(_seconds % 60);

		return padTime(hours) + ":" + padTime(minutes) + ":" + padTime(seconds);
	};
});

