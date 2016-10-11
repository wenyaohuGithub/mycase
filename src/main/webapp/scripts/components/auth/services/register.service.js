'use strict';

angular.module('mycaseApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


