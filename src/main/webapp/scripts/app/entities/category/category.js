'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('category', {
                parent: 'entity',
                url: '/categorys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.category.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/category/categorys.html',
                        controller: 'CategoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('category');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('category.detail', {
                parent: 'entity',
                url: '/category/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.category.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/category/category-detail.html',
                        controller: 'CategoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('category');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Category', function($stateParams, Category) {
                        return Category.get({id : $stateParams.id});
                    }]
                }
            })
            .state('category.new', {
                parent: 'category',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/category/category-dialog.html',
                        controller: 'CategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('category', null, { reload: true });
                    }, function() {
                        $state.go('category');
                    })
                }]
            })
            .state('category.edit', {
                parent: 'category',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/category/category-dialog.html',
                        controller: 'CategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Category', function(Category) {
                                return Category.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('category', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
