'use strict';

miaasApp.factory('Request', function ($resource) {
        return $resource('app/rest/requests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
