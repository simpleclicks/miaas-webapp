'use strict';

miaasApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/request', {
                    templateUrl: 'views/requests.html',
                    controller: 'RequestController',
                    resolve:{
                        resolvedRequest: ['Request', function (Request) {
                            return Request.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
