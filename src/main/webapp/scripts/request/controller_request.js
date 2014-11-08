'use strict';

miaasApp.controller('RequestController', function ($rootScope, $scope, resolvedRequest, Request, UserRequest, RequestDevices) {

        $scope.preDefRequestType = [
            { value: 'Android', text: 'Android' },
            { value: 'iOS', text: 'iOS' },
            { value: 'Windows', text: 'Windows Phone' }
        ];

        $scope.preDefResourceVersion = [
            { value: '15', text: 'api15' },
            { value: '16', text: 'api16' },
            { value: '17', text: 'api17' },
            { value: '18', text: 'api18' },
            { value: '19', text: 'api19' },
            { value: '20', text: 'api20' }
        ];

        $scope.preDefResourceType = [
            { value: 'Device', text: 'Device' },
            { value: 'Emulator', text: 'Emulator' }
        ];

        $scope.preDefResourceMemory = [
            { value: '512', text: '512 mb' },
            { value: '1024', text: '1024 mb' }
        ];

        $scope.requests = resolvedRequest;
        //$scope.users = resolvedUser;
        $scope.users = $rootScope.account.login;
        $scope.create = function () {
            var userobj = {
                login : $scope.users
            };
            $scope.request.user = userobj;
            console.log($scope.request);
            Request.save($scope.request,
                function () {
                    $scope.requests = UserRequest.query();
                    $('#saveRequestModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.request = Request.get({id: id});
            $('#saveRequestModal').modal('show');
        };

        $scope.delete = function (id) {
            Request.delete({id: id},
                function () {
                    $scope.requests = Request.query();
                });
        };



        $scope.deviceView = function(req){
            $scope.selectedReqId = req.id;
            //$scope.requestDevices = RequestDevices.getDevicesForReq(req.id);
            //console.log($scope.requestDevices);

            RequestDevices.getDevicesForReq(req.id).then(function(data) {
                $scope.requestDevices = data;
            });
        }

        $scope.clear = function () {
            $scope.request = {requestType: null, requestStartDate: null, requestEndDate: null, resourceQuantity: null, resourceType: null, resourceVersion: null, resourceMemory: null, resourceBackup: null, id: null};
        };
    });
