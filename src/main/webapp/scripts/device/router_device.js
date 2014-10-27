'use strict';

miaasApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/device', {
                    templateUrl: 'views/devices.html',
                    controller: 'DeviceController',
                    resolve:{
                        resolvedDevice: ['Device', function (Device) {
                            return Device.query();
                        }],
                        resolvedAmazonInstance: ['AmazonInstance', function (AmazonInstance) {
                            return AmazonInstance.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
