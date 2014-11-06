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

    angular.element($window.document.getElementsByClassName("gauge")).ready(function(){

    });

    $scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
        var gauges = $window.document.getElementsByClassName("gauge");
        //console.log(gauges);
        var guageVars = [];
        for(var i=0; i<gauges.length; i++)
        {
            console.log(gauges[i].id);
            guageVars[i] = new JustGage({
                id: gauges[i].id,
                value: 20,
                min: 0,
                max: 100,
                title: "CPU Usage"
            });
        }
    });

});
