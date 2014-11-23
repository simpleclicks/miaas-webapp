'use strict';

miaasApp.factory('Request', function ($resource) {
        return $resource('app/rest/requests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });

miaasApp.factory('UserRequest',function ($http, $resource, $rootScope, Session) {
    return {
      getRequestsForUser : function(){
          var promise = $http.get('app/rest/userrequests/'+ Session.login).then(function (response) {
              return response.data;
          });
          return promise;
      }
    };
//    var userid = $rootScope.account.login;
//    return $resource('app/rest/userrequests/' + userid, {}, {
//        'query': { method: 'GET', isArray: true}
//    });
});

miaasApp.factory('RequestDevices', function ($http) {
    return {
        getDevicesForReq: function(reqid) {
            var promise = $http.get('app/rest/requestdevices/'+reqid).then(function (response) {
                return response.data;
            });
            return promise;
        }
    };
});