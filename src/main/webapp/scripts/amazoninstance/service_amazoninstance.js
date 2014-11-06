'use strict';

miaasApp.factory('AmazonInstance', function ($resource) {
        return $resource('app/rest/amazoninstances/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
