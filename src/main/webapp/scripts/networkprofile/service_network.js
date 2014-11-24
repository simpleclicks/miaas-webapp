/**
 * Created by sheetalparanjpe on 11/6/14.
 */

miaasApp.factory('Network', function ($resource) {
    return $resource('app/rest/amazoninstances/monitor', {}, {
        'query': { method: 'GET', isArray: true},
        'get': { method: 'GET'}
    });
});

miaasApp.factory('AdminStatistics', function ($resource,$http) {
    return {
        getStatsForAdmin: function() {
            var promise = $http.get('app/rest/getpriceAll').then(function (response) {
                return response.data;
            });
            return promise;
        }
    };
});