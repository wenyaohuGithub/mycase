'use strict';

angular.module('mycaseApp')
    .factory('Route_condition', function ($resource, DateUtils) {
        return $resource('api/route_conditions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
