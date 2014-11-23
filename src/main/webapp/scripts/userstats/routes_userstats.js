/**
 * Created by sheetalparanjpe on 11/22/14.
 */
miaasApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
        $routeProvider
            .when('/userstats', {
                templateUrl: 'views/userStatistics.html',
                controller: 'UserStatisticsController',
                resolve:{
                    resolvedStats: ['UserStatistics', function (UserStatistics) {
                        return UserStatistics.getStatsForUser();
                    }]
                },
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
    });