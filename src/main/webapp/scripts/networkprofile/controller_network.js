'use strict';

miaasApp.controller('NetworkController', function ($scope, Network, resolvedAmazonInstance) {

    $scope.devices = resolvedDevice;
    $scope.amazonInstances = resolvedAmazonInstance;

    $scope.create = function () {
        Device.save($scope.device,
            function () {
                $scope.devices = Device.query();
                $('#saveDeviceModal').modal('hide');
                $scope.clear();
            });
    };

    $scope.update = function (id) {
        $scope.device = Device.get({id: id});
        $('#saveDeviceModal').modal('show');
    };

    $scope.delete = function (id) {
        Device.delete({id: id},
            function () {
                $scope.devices = Device.query();
            });
    };

    $scope.clear = function () {
        $scope.device = {deviceId: null, deviceImageName: null, deviceStatus: null, deviceType: null, deviceVersion: null, deviceMemory: null, id: null};
    };
});
