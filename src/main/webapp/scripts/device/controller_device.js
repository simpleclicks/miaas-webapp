'use strict';

miaasApp.controller('DevicesController', function ($rootScope, $scope, $location, resolvedRequest, resolvedStat, Request, UserRequest, UserStatistics, RequestDevices, Emulator, DeviceDetails, Session) {

    $scope.requests = resolvedRequest;
    $scope.userStats = resolvedStat;
    $scope.users = $rootScope.account.login;
    $scope.activeDevices = [];

    $scope.refreshDeviceList = function (){
        for(var i = 0 ; i < $scope.requests.length; i++){
            if ($scope.requests[i].requestStatus === 'Active'){
                RequestDevices.getDevicesForReq($scope.requests[i].id).then(function (data) {
                    for(var j = 0; j< data.length; j++){
                        if(data[j].deviceType == "Samsung Galaxy S5"){
                            data[j].manufacturer = "Samsung";
                            data[j].gprs = "Yes";
                            data[j].wifi = "Yes";
                            data[j].sensors = "Accelerometer, gyro, proximity, compass, barometer, gesture, heart rate";
                        }
                        else if(data[j].deviceType == "Nexus 5"){
                            data[j].manufacturer = "LG";
                            data[j].gprs = "Yes";
                            data[j].wifi = "Yes";
                            data[j].sensors = "Accelerometer, gyro, proximity, compass, barometer";
                        }
                        else if(data[j].deviceType == "Nexus 7"){
                            data[j].manufacturer = "ASUS";
                            data[j].gprs = "Yes";
                            data[j].wifi = "Yes";
                            data[j].sensors = "Accelerometer, gyro, proximity, compass";
                        }
                        else if(data[j].deviceType == "Nexus One"){
                            data[j].manufacturer = "HTC";
                            data[j].gprs = "Yes";
                            data[j].wifi = "Yes";
                            data[j].sensors = "Accelerometer, proximity, compass";
                        }
                        else if(data[j].deviceType == "Moto X"){
                            data[j].manufacturer = "Motorola";
                            data[j].gprs = "Yes";
                            data[j].wifi = "Yes";
                            data[j].sensors = "Accelerometer, gyro, proximity, compass, barometer, temperature";
                        }
                        $scope.activeDevices = $scope.activeDevices.concat(data[j]);
                    }
                });
            }
        };
    };

    $scope.refreshDeviceList();

    $scope.deviceOnOff = function (deviceId, $event){
        $event.target.checked ? $scope.startDevice(deviceId) : $scope.stopDevice(deviceId);
    };

    $scope.deviceInfo = function(selectedDevice){
        DeviceDetails.set(selectedDevice);
        $location.path('/devdetails');
    };

    $(document).ready(function () {
        $('.grid-nav li a').on('click', function (event) {
            event.preventDefault();
            $('.grid-container').fadeOut(500, function () {
                $('#' + gridID).fadeIn(500);
            });
            var gridID = $(this).attr("data-id");

            $('.grid-nav li a').removeClass("active");
            $(this).addClass("active");
        });
    });


    $scope.deviceAction = function (devId){
        $('#deviceActionModal').modal('show');
    }

    $scope.delete = function (id) {
        Request.delete({id: id},
            function () {
                $scope.requests = Request.query();
            });
    };

    $scope.deviceAppAction = function(devId){
        $scope.selectedDevice = devId;
        console.log($scope.selectedDevice);
    }

    $scope.deviceView = function (req) {
        $scope.selectedReqId = req.id;

        RequestDevices.getDevicesForReq(req.id).then(function (data) {
            $scope.requestDevices = data;
        });
    };

    $scope.startDevice = function (devId) {
        Emulator.startDeviceEmulator(devId).then(function (data) {
            $scope.activeDevices = [];
            $scope.refreshDeviceList();
        });
    };

    $scope.stopDevice = function (devId) {
        Emulator.stopDeviceEmulator(devId).then(function (data) {
            $scope.activeDevices = [];
            $scope.refreshDeviceList();
        });
    };

    $scope.launchApp = function(appName){
        RequestDevices.launchDeviceApp($scope.selectedDevice,appName);
    }

    $scope.clear = function () {
        $scope.request = {requestType: null, requestStartDate: null, requestEndDate: null, resourceQuantity: null, resourceType: null, resourceVersion: null, resourceMemory: null, resourceBackup: null, id: null};
    };

    $scope.oneAtATime = true;

    $scope.status = {
        isFirstOpen: true,
        isFirstDisabled: false
    };

});
