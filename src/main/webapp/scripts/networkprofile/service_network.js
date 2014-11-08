/**
 * Created by sheetalparanjpe on 11/6/14.
 */

miaasApp.factory('Device', function ($resource) {
    return $resource('app/rest/devices/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': { method: 'GET'}
    });
});
