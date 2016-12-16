'use strict';

angular.module('mycaseApp')
    .factory('Contract_party', function ($resource, DateUtils) {
        return $resource('api/contract_partys/:id', {}, {
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
