'use strict';

miaasApp.controller('NetworkController', function ($scope, Network, AmazonInstance,resolvedAmazonInstance, $window) {

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

});
