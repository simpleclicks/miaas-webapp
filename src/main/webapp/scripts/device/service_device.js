'use strict';

miaasApp.factory('Device', function ($resource) {
        return $resource('app/rest/devices/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });

miaasApp.factory('Emulator', function ($http) {

    return {
        startDeviceEmulator: function(devid) {
            var promise = $http.get('app/rest/start/device/'+devid).then(function (response) {
                return response.data;
            });
            return promise;
        }
    };
});
