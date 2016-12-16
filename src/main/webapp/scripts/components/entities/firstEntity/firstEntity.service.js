'use strict';

angular.module('mycaseApp')
    .factory('FirstEntity', function ($resource, DateUtils) {
        return $resource('api/firstEntitys/:id', {}, {
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
