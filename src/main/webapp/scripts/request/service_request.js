'use strict';

miaasApp.factory('Request', function ($resource) {
        return $resource('app/rest/requests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });

miaasApp.factory('UserRequest',function ($resource, $rootScope) {
    var userid = $rootScope.account.login;
    return $resource('app/rest/userrequests/' + userid, {}, {
        'query': { method: 'GET', isArray: true}
    });
});
