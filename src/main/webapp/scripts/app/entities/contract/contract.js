'use strict';

angular.module('mycaseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('contract', {
                parent: 'entity',
                url: '/contracts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.contract.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contract/contracts.html',
                        controller: 'ContractController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('contract.detail', {
                parent: 'entity',
                url: '/contract/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mycaseApp.contract.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contract/contract-detail.html',
                        controller: 'ContractDetailController'
                    },
                    'info@contract.detail':{
                        templateUrl: 'scripts/app/entities/contract/contract-detail-info.html',
                        controller: 'ContractDetailController'
                    },
                    'project@contract.detail':{
                        templateUrl: 'scripts/app/entities/contract/contract-detail-projects.html',
                        controller: 'ContractDetailController'
                    },
                    'notes@contract.detail':{
                        templateUrl: 'scripts/app/entities/contract/contract-detail-notes.html',
                        controller: 'ContractDetailActivitiesController'
                    },
                    'activities@contract.detail':{
                        templateUrl: 'scripts/app/entities/contract/contract-detail-activities.html',
                        controller: 'ContractDetailActivitiesController'
                    },
                    'attachments@contract.detail':{
                        templateUrl: 'scripts/app/entities/contract/contract-detail-attachments.html',
                        controller: 'ContractDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Contract', function($stateParams, Contract) {
                        return Contract.get($stateParams.id, {});
                    }]
                }
            })
            .state('contract.detail.nextStep', {
                parent: 'contract.detail',
                url: '/contract/{id}',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, reviewIdentifier: null, contractIdentifier: null, contractingMethod: null, amount: null, amountWritten: null, currency: null, amountCurrentYear: null, submitDate: null, startDate: null, endDate: null, expireDate: null, isMultiYear: null, status: null, state: null, approveDate: null, signDate: null, archiveDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('contract', null, { reload: true });
                    }, function() {
                        $state.go('contract');
                    })
                }]
            })
            .state('contract.new', {
                parent: 'contract',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, reviewIdentifier: null, contractIdentifier: null, contractingMethod: null, amount: null, amountWritten: null, currency: null, amountCurrentYear: null, submitDate: null, startDate: null, endDate: null, expireDate: null, isMultiYear: null, status: null, state: null, approveDate: null, signDate: null, archiveDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('contract', null, { reload: true });
                    }, function() {
                        $state.go('contract');
                    })
                }]
            })
            .state('contract.edit', {
                parent: 'contract',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Contract', function(Contract) {
                                return Contract.get($stateParams.id,{});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('contract', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
