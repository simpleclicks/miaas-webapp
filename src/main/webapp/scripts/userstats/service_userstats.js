/**
 * Created by sheetalparanjpe on 11/22/14.
 */
miaasApp.factory('UserStatistics', function ($resource,$http,Session) {
    return {
        getStatsForUser: function() {
            var promise = $http.get('app/rest/userStatistics/'+Session.login).then(function (response) {
                return response.data;
            });
            return promise;
        }
    };
});

