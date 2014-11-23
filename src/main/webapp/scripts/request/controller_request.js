'use strict';

miaasApp.controller('RequestController', function ($rootScope, $scope, resolvedRequest, Request, UserRequest, RequestDevices, Emulator, Session) {
    var resourcePricePerDay = 5;
    $scope.requestTotalPrice = 0;
    $scope.requests = resolvedRequest;
    //$scope.users = resolvedUser;
    $scope.users = $rootScope.account.login;

    $('#myTab a[data-target="#request"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $('#myTab a[data-target="#billing"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        //$scope.requestTotalPrice = calculateBill();
        $scope.showPie();
    });

    $scope.exampleData = [];

    $scope.showPie = function(){
        var j = 0;
        for(var i=0;i<$scope.requests.length;i++){
            $scope.exampleData[j] = {
                key : "Request " + $scope.requests[i].id,
                y : $scope.requests[i].requestPrice
            };
            j++;
        }
        return $scope.exampleData;
    };

    $scope.calculateBill = function(){
        var price = 0;
        for(var i=0;i<$scope.requests.length;i++){
            price += resolvedRequest[i].requestPrice;
        }
        console.log(price);
        return price;
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


//    $scope.exampleData = [
//        { key: "One", y: 5 },
//        { key: "Two", y: 2 },
//        { key: "Three", y: 9 },
//        { key: "Four", y: 7 },
//        { key: "Five", y: 4 },
//        { key: "Six", y: 3 },
//        { key: "Seven", y: 9 }
//    ];

    $scope.xFunction = function(){
        return function(d) {
            return d.key;
        };
    };

    $scope.yFunction = function(){
        return function(d){
            return d.y;
        };
    };

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

    $scope.create = function () {
        var userobj = {
            login: $scope.users
        };
        $scope.request.user = userobj;
        console.log($scope.request);
        $scope.request['requestPrice'] = $scope.calculatePrice($scope.request.requestStartDate, $scope.request.requestEndDate, resourcePricePerDay, $scope.request.resourceQuantity);
        console.log(JSON.stringify($scope.request));
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

    $scope.calculatePrice = function (startDate, endDate, rate, numResources) {
        var oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
        var firstDate = new Date(endDate);
        var secondDate = new Date(startDate);
        var timeDiff = (firstDate - secondDate) / oneDay;
        var price = timeDiff * rate * numResources;
        console.log(price);
        return price;
    };

    $scope.deviceView = function (req) {
        $scope.selectedReqId = req.id;
        //$scope.requestDevices = RequestDevices.getDevicesForReq(req.id);
        //console.log($scope.requestDevices);

        RequestDevices.getDevicesForReq(req.id).then(function (data) {
            $scope.requestDevices = data;
        });
    };

    $scope.startDevice = function (devId) {
        Emulator.startDeviceEmulator(devId).then(function (data) {
            console.log(data);
        });
    };

    $scope.clear = function () {
        $scope.request = {requestType: null, requestStartDate: null, requestEndDate: null, resourceQuantity: null, resourceType: null, resourceVersion: null, resourceMemory: null, resourceBackup: null, id: null};
    };

    $scope.oneAtATime = true;

    $scope.status = {
        isFirstOpen: true,
        isFirstDisabled: false
    };

});
