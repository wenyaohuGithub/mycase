'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('route', {
                parent: 'entity',
                url: '/routes',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.route.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route/routes.html',
                        controller: 'RouteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('route.detail', {
                parent: 'entity',
                url: '/route/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.route.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route/route-detail.html',
                        controller: 'RouteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Route', function($stateParams, Route) {
                        return Route.get({id : $stateParams.id});
                    }]
                }
            })
            .state('route.new', {
                parent: 'route',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/route/route-dialog.html',
                        controller: 'RouteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('route', null, { reload: true });
                    }, function() {
                        $state.go('route');
                    })
                }]
            })
            .state('route.edit', {
                parent: 'route',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/route/route-dialog.html',
                        controller: 'RouteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Route', function(Route) {
                                return Route.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('route', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
