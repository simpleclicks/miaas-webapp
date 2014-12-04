/**
 * Created by sheetalparanjpe on 12/3/14.
 */

'use strict';

miaasApp.controller('DevDetailsController', function ($rootScope, $scope, DeviceDetails) {
    $scope.app = {};
    var device = DeviceDetails.get();
    if(device.deviceVersion == 19) {
        device.deviceOS = "Android 4.4";
        device.deviceOSName = "KitKat";
    }
    else if(device.deviceVersion == 18) {
        device.deviceOS = "Android 4.3";
        device.deviceOSName = "JellyBean";
    }
    else if(device.deviceVersion == 21) {
        device.deviceOS = "Android 5.0";
        device.deviceOSName = "Lollipop";
    }
    $scope.selectedDevice = device;
    console.log($scope.selectedDevice);


    $scope.uploader = new Flow({
        target : "http://" + $scope.selectedDevice.amazonInstance.publicDnsName + "/simpleapp/webapi/androidcontrol/install/appname/id",
        query : {
            appName : $scope.app.name,
            appDesc : "desc"
        },
        fileParameterName : "apkfile",
        testMethod : "POST"
    });


    $scope.uploadFiles = function() {
        $scope.uploader.upload();
    }


});

