'use strict';

angular.module('mycaseApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
