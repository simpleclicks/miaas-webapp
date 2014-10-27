'use strict';

miaasApp.controller('AmazonInstanceController', function ($scope, resolvedAmazonInstance, AmazonInstance, resolvedDevice) {

        $scope.amazoninstances = resolvedAmazonInstance;
        $scope.devices = resolvedDevice;

        $scope.create = function () {
            AmazonInstance.save($scope.amazoninstance,
                function () {
                    $scope.amazoninstances = AmazonInstance.query();
                    $('#saveAmazonInstanceModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.amazoninstance = AmazonInstance.get({id: id});
            $('#saveAmazonInstanceModal').modal('show');
        };

        $scope.delete = function (id) {
            AmazonInstance.delete({id: id},
                function () {
                    $scope.amazoninstances = AmazonInstance.query();
                });
        };

        $scope.clear = function () {
            $scope.amazoninstance = {instanceId: null, instanceImageId: null, instanceType: null, instanceRegion: null, instanceStatus: null, id: null};
        };
    });
