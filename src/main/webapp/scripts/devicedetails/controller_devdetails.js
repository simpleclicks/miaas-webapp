/**
 * Created by sheetalparanjpe on 12/3/14.
 */

'use strict';

miaasApp.controller('DevDetailsController', function ($rootScope, $scope, DeviceDetails,fileUpload) {

    $scope.app = {};
    var device = DeviceDetails.get();
    if (device.deviceVersion == 19) {
        device.deviceOS = "Android 4.4";
        device.deviceOSName = "KitKat";
    }
    else if (device.deviceVersion == 18) {
        device.deviceOS = "Android 4.3";
        device.deviceOSName = "JellyBean";
    }
    else if (device.deviceVersion == 21) {
        device.deviceOS = "Android 5.0";
        device.deviceOSName = "Lollipop";
    }
    $scope.selectedDevice = device;
    console.log($scope.selectedDevice);

    $scope.getTartgetUrl = function(){
        return "http://" + $scope.selectedDevice.amazonInstance.publicDnsName +
            "/simpleapp/webapi/androidcontrol/install/" +
            $scope.app.name + "/" + $scope.selectedDevice.deviceId;
    };

    $scope.uploader = new Flow({
        target: "http://" + $scope.selectedDevice.amazonInstance.publicDnsName +
            "/simpleapp/webapi/androidcontrol/install/" +
            $scope.app.name + "/" + $scope.selectedDevice.deviceId,
        fileParameterName: "apkfile",
        testMethod: "POST",
        chunkSize:100*1024*1024
    });

    $scope.client = new XMLHttpRequest();

    $scope.upload = function()
    {
        var file = document.getElementById("uploadfile");

        /* Create a FormData instance */
        var formData = new FormData();
        /* Add the file */
        formData.append("apkfile", file.files[0]);

        $scope.client.open("post", $scope.getTartgetUrl(), true);
        $scope.client.setRequestHeader("Content-Type", "multipart/form-data");
        $scope.client.send(formData);  /* Send to server */
    }

    /* Check the response status */
    $scope.client.onreadystatechange = function()
    {
        if ($scope.client.readyState == 4 && $scope.client.status == 200)
        {
            alert($scope.client.statusText);
        }
    }

    $scope.myFile = "";
    $scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' + JSON.stringify(file));
        var uploadUrl = "/fileUpload";
        fileUpload.uploadFileToUrl(file,uploadUrl)
    };


});

