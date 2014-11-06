'use strict';

miaasApp.controller('NetworkController', function ($scope, Network, resolvedAmazonInstance) {

    var g1 = new JustGage({
        id: "gauge1",
        value: 67,
        min: 0,
        max: 100,
        title: "Visitors"
    });

    var g2 = new JustGage({
        id: "gauge2",
        value: 67,
        min: 0,
        max: 100,
        title: "Visitors"
    });

    var g3 = new JustGage({
        id: "gauge3",
        value: 67,
        min: 0,
        max: 100,
        title: "Visitors"
    });

    var g4 = new JustGage({
        id: "gauge4",
        value: 67,
        min: 0,
        max: 100,
        title: "Visitors"
    });
});
