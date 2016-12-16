'use strict';

angular.module('mycaseApp')
    .factory('Business_entity', function ($resource, DateUtils) {
        return $resource('api/business_entitys/:id', {}, {
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
