'use strict';

miaasApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/amazoninstance', {
                    templateUrl: 'views/amazoninstances.html',
                    controller: 'AmazonInstanceController',
                    resolve:{
                        resolvedAmazonInstance: ['AmazonInstance', function (AmazonInstance) {
                            return AmazonInstance.query();
                        }],
                        resolvedDevice: ['Device', function (Device) {
                            return Device.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
