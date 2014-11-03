'use strict';

miaasApp.controller('RequestController', function ($rootScope, $scope, resolvedRequest, Request, UserRequest) {

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

        $scope.clear = function () {
            $scope.request = {requestType: null, requestStartDate: null, requestEndDate: null, resourceQuantity: null, resourceType: null, resourceVersion: null, resourceMemory: null, resourceBackup: null, id: null};
        };
    });
