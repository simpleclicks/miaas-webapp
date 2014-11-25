'use strict';

miaasApp.controller('RequestController', function ($rootScope, $scope, resolvedRequest, resolvedStat, Request, UserRequest, UserStatistics, RequestDevices, Emulator, Session) {
    var resourcePricePerDay = 5;
    $scope.requestTotalPrice = 0;
    $scope.requests = resolvedRequest;
    $scope.userStats = resolvedStat;
    $scope.users = $rootScope.account.login;
    $scope.activeDevices = [];

    $scope.refreshDeviceList = function (){
        for(var i = 0 ; i < $scope.requests.length; i++){
            if ($scope.requests[i].requestStatus === 'Active'){
                RequestDevices.getDevicesForReq($scope.requests[i].id).then(function (data) {
                    $scope.activeDevices = $scope.activeDevices.concat(data);
                });
            }
        };
    };

    $scope.refreshDeviceList();

    $scope.deviceOnOff = function (deviceId, $event){
        $event.target.checked ? $scope.startDevice(deviceId) : $scope.stopDevice(deviceId);
        $scope.activeDevices = [];
        $scope.refreshDeviceList();
    };

    console.log($scope.userStats);

    $('#myTab a[data-target="#request"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $('#myTab a[data-target="#billing"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $('#myTab a[data-target="#devices"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $scope.exampleData = [];

    $scope.memPieData = [];

    $scope.apiPieData = [];

    $scope.showMemPie = function(){
        var k = 0;
        for(var m in $scope.userStats.memory){
            $scope.memPieData[k] = {
                key : m + " mb",
                y : parseInt($scope.userStats.memory[m])
            };
            k++;
        }

        return $scope.memPieData;
    };

    $scope.showApiPie = function(){
        var j = 0;

        for(var a in $scope.userStats.api){
            $scope.apiPieData[j] = {
                key : "API " + a,
                y : parseInt($scope.userStats.api[a])
            };
            j++;
        }

        return $scope.apiPieData;
    };

    $scope.calculateBill = function(){
        var price = 0;
        for(var i=0;i<$scope.requests.length;i++){
            price += $scope.requests[i].requestPrice;
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

    $scope.x1Function = function(){
        return function(d) {
            return d.key;
        };
    };

    $scope.y1Function = function(){
        return function(d){
            return d.y;
        };
    };

    $scope.preDefRequestType = [
        { value: 'Nexus 7', text: 'Nexus 7' },
        { value: 'Nexus One', text: 'Nexus One' },
        { value: 'Moto X', text: 'Moto X' },
        { value: 'Samsung Galaxy S5', text: 'Samsung Galaxy S5' },
        { value: 'HTC Hero', text: 'HTC Hero'}
    ];

    $scope.preDefResourceVersion = [
        { value: '18', text: 'Jellybean 4.3.x' },
        { value: '19', text: 'KitKat 4.4.x' },
        { value: '21', text: 'Lollipop 5.0' }
    ];

    $scope.preDefResourceMemory = [
        { value: '512', text: '512 mb' },
        { value: '1024', text: '1024 mb' }
    ];

    $scope.preDefResourceType = [
        { value: 'Device', text: 'Device' },
        { value: 'Emulator', text: 'Emulator' }
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
                UserRequest.getRequestsForUser().then(function(data) {
                    $scope.requests = data;
                });
                UserStatistics.getStatsForUser().then(function(data){
                    $scope.calculateBill();
                    $scope.userStats = data;
                })
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

    $scope.randomStacked = function() {
        $scope.requestTotalPrice = $scope.calculateBill();
        $scope.stacked = [];
        var types = ['success', 'info', 'warning', 'danger'];

        for (var i = 0, n = $scope.requests.length; i < n; i++) {
            var index = Math.floor((Math.random() * 4));
            var price = Math.floor(($scope.requests[i].requestPrice / $scope.requestTotalPrice)*100);
            $scope.stacked.push({
                value: price,
                displayText: $scope.requests[i].requestPrice,
                type: types[index]
            });
        }
        console.log($scope.stacked);
    };

    $scope.setWidth = function(bar){
        return { width: bar.barWidth +'%' };
    };

    $scope.randomStacked();

    $scope.deviceView = function (req) {
        $scope.selectedReqId = req.id;

        RequestDevices.getDevicesForReq(req.id).then(function (data) {
            $scope.requestDevices = data;
        });
    };

    $scope.startDevice = function (devId) {
        Emulator.startDeviceEmulator(devId).then(function (data) {
            console.log(data);
        });
    };

    $scope.stopDevice = function (devId) {
        Emulator.stopDeviceEmulator(devId).then(function (data) {
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
