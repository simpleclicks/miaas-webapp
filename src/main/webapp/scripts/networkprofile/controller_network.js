'use strict';

miaasApp.controller('NetworkController', function ($scope, Network, AmazonInstance,resolvedAmazonInstance, resolvedStats, resolvedRequest, $window) {

    $scope.amazonInstance = resolvedAmazonInstance;

    angular.element(document).ready(function () {
        var gauges = $window.document.getElementsByClassName("gauge");
        //console.log(gauges);
        for(var i=0; i<gauges.length; i++)
        {
            alert(gauges[i].innerHTML);
        }
    });

    $('#myTab a[data-target="#ec2"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $('#myTab a[data-target="#requestStats"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    angular.element($window.document.getElementsByClassName("gauge")).ready(function(){

    });

    $scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
        var gauges = $window.document.getElementsByClassName("gauge");
        console.log(gauges);
        console.log(resolvedAmazonInstance);
        var guageVars = [];
        for(var i=0; i<gauges.length; i++)
        {
            console.log(gauges[i].id);
            guageVars[i] = new JustGage({
                id: gauges[i].id,
                value: gauges[i].attributes['data-cpuUtil'].value,
                min: 0,
                max: 100,
                title: "CPU Usage"
            });
        }
        var gaugesRscrs = $window.document.getElementsByClassName("gaugeResrcs");
        console.log(gaugesRscrs);
        //console.log(resolvedAmazonInstance);
        var gaugesRscrsVars = [];
        for(var i=0; i<gaugesRscrs.length; i++)
        {
            console.log(gaugesRscrs[i].id);
            gaugesRscrsVars[i] = new JustGage({
                id: gaugesRscrs[i].id,
                value: gaugesRscrs[i].attributes['data-avlblRescrc'].value,
                min: 0,
                max: 20,
                title: "Available Resources"
            });
        }

    });

    $scope.adminStats = resolvedStats;

    $scope.requests = resolvedRequest;

    $scope.memPieData = [];

    $scope.apiPieData = [];

    $scope.reqData = [];

    $scope.showMemPie = function(){
        var k = 0;
        for(var m in $scope.adminStats.memory){
            $scope.memPieData[k] = {
                key : m + " mb",
                y : parseInt($scope.adminStats.memory[m])
            };
            k++;
        }

        return $scope.memPieData;
    };

    $scope.showApiPie = function(){
        var j = 0;

        for(var a in $scope.adminStats.api){
            $scope.apiPieData[j] = {
                key : "API " + a,
                y : parseInt($scope.adminStats.api[a])
            };
            j++;
        }

        return $scope.apiPieData;
    };

    function compare(a,b) {
        if (a.requestProcessTime < b.requestProcessTime)
            return -1;
        if (a.requestProcessTime > b.requestProcessTime)
            return 1;
        return 0;
    }

    //objs.sort(compare);

    $scope.showRequestTime = function(){
        var g = 0;
        var vals = [];
        $scope.requests.sort(compare);
        for(var r = 0; r < $scope.requests.length; r++){
            vals[g] = [$scope.requests[r].requestProcessTime, $scope.requests[r].requestPrice];
            g++;
        }
        $scope.reqData = [
            {
                "key" : "Series 1",
                "values" : vals
            }
        ];
        //return $scope.reqData;
    };
    $scope.showRequestTime();

    $scope.xAxisTickFormat = function(){
        return function(d){
            var date = new Date(d);
            var y = date.getDate() + " " + date.getFullYear() + " " + date.getHours()+ ":" + date.getMinutes() + ":" + date.getSeconds();
            //console.log(date.getMonth() + " " + date.getDay() + " " +date.getYear() + " " + date.getHours() + " " + date.getMinutes() + " " + date.getSeconds());
            return y;
        };
    };

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

});
