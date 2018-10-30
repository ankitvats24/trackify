var app = angular.module('trackifyApp', ['ngMaterial','md.data.table']);
app.controller('liveStatusCtrl', function($scope, $http, $interval, $filter) {

	$scope.deviceDetails={};
	$interval(checkDeviceStatus, 5000);

	function checkDeviceStatus(){
		$http.get("/deviceLiveStatus")
		.then(function (response) {
			if(null==response.data || undefined == response.data){
				$window.alert("No data found!")
			}else{
				$scope.allDeviceDetails = response.data;
				$scope.upDeviceDetails = $filter('filter')($scope.allDeviceDetails, {status: true});
				console.log($scope.upDeviceDetails);
				$scope.downDeviceDetails = $filter('filter')($scope.allDeviceDetails, {status: false});
				
			}

			});

	}
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

