'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('route_condition', {
                parent: 'entity',
                url: '/route_conditions',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.route_condition.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route_condition/route_conditions.html',
                        controller: 'Route_conditionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route_condition');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('route_condition.detail', {
                parent: 'entity',
                url: '/route_condition/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.route_condition.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route_condition/route_condition-detail.html',
                        controller: 'Route_conditionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route_condition');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Route_condition', function($stateParams, Route_condition) {
                        return Route_condition.get({id : $stateParams.id});
                    }]
                }
            })
            .state('route_condition.new', {
                parent: 'route_condition',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/route_condition/route_condition-dialog.html',
                        controller: 'Route_conditionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {previous_step_result: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('route_condition', null, { reload: true });
                    }, function() {
                        $state.go('route_condition');
                    })
                }]
            })
            .state('route_condition.edit', {
                parent: 'route_condition',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/route_condition/route_condition-dialog.html',
                        controller: 'Route_conditionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Route_condition', function(Route_condition) {
                                return Route_condition.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('route_condition', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
