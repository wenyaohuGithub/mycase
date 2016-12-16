'use strict';
/*
angular.module('mycaseApp')
    .factory('Contract', function ($resource, DateUtils) {
        return $resource('api/contracts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.submit_date = DateUtils.convertDateTimeFromServer(data.submit_date);
                    data.start_date = DateUtils.convertDateTimeFromServer(data.start_date);
                    data.end_date = DateUtils.convertDateTimeFromServer(data.end_date);
                    data.expire_date = DateUtils.convertDateTimeFromServer(data.expire_date);
                    data.approve_date = DateUtils.convertDateTimeFromServer(data.approve_date);
                    data.sign_date = DateUtils.convertDateTimeFromServer(data.sign_date);
                    data.archive_date = DateUtils.convertDateTimeFromServer(data.archive_date);
                    data.contractParty = data.contractParties[0];
                    return data;
                }
            },
            'update': { method:'PUT'}
        });
    });
*/
angular.module('mycaseApp')
    .factory('Contract', function ($resource, $http, $q, DateUtils, UrlParams) {
        return {
            query: function(params, callback) {
                var urlParamString = UrlParams.parseParam(params);
                $http.get('api/contracts' + urlParamString).success(callback);
            },
            get: function(id, params){
                var urlParamString = UrlParams.parseParam(params);
                var deferred = $q.defer();
                $http.get('api/contracts/'+id+urlParamString).success(function(data) {
                    data = angular.fromJson(data);
                    data.submit_date = DateUtils.convertDateTimeFromServer(data.submit_date);
                    data.start_date = DateUtils.convertDateTimeFromServer(data.start_date);
                    data.end_date = DateUtils.convertDateTimeFromServer(data.end_date);
                    data.expire_date = DateUtils.convertDateTimeFromServer(data.expire_date);
                    data.approve_date = DateUtils.convertDateTimeFromServer(data.approve_date);
                    data.sign_date = DateUtils.convertDateTimeFromServer(data.sign_date);
                    data.archive_date = DateUtils.convertDateTimeFromServer(data.archive_date);
                    data.contractParty = data.contractParties[0];
                    deferred.resolve(data);
                }).error(function(){
                    deferred.reject();
                });
                return deferred.promise;
            },
            save: function(contract, callback) {
                $http.post('api/contracts', contract).success(callback);
            },
            update: function(obj) {
                $http.put('api/contracts', contract).success(callback);
            },
            approve: function(id, callback){
                $http.put('api/contracts/'+id+'/approve').succes(callback);
            },
            reject: function(id, callback){
                $http.put('api/contracts/'+id+'/reject').success(callback);
            },
            addComment: function(id, contract_history, callback){
                $http.post('api/contracts/'+id+'/comment', contract_history).success(callback);
            },
            moveToNextProcess: function(urlParameter){
                return $resource('api/contracts/:id/nextProcess?'+urlParameter, {}, {
                    'moveToNextProcess': {
                        method: 'PUT'
                    }
                })
            }
        }
    });

/*
angular.module('mycaseApp')
    .factory('Process', function ($http, $q, DateUtils, UrlParams) {
        var parseParam = function(params){
            var urlParamString = '';
            angular.forEach(params, function(paramInfo, urlParam){
                if(urlParamString == ''){
                    urlParamString = urlParamString+'?'+urlParam+'='+paramInfo;
                } else {
                    urlParamString = urlParamString+'&'+urlParam+'='+paramInfo;
                }
            });
            return urlParamString;
        };

        return {
            get: function(params, callback){
                var id = params.id;
                var deferred = $q.defer();
                $http.get('api/processes/'+id).success(function(data){
                    deferred.resolve(data);
                }).error(function(){
                    deferred.reject();
                });
                return deferred.promise;
            },
            query: function(params, callback) {
                var urlParamString = UrlParams.parseParam(params);
                $http.get('api/processes'+urlParamString).success(callback);
            },
            update: function(params) {
                var id = params.id;
                $http.put('api/processes/'+id).success(function(data){
                    deferred.resolve(data);
                }).error(function(){
                    deferred.reject();
                });
                return deferred.promise;
            },
            delete: function() {
                var deferred = $q.defer();
                $http.delete('api/processes').success(function(data){
                    deferred.resolve(data);
                }).error(function(){
                    deferred.reject();
                });
                return deferred.promise;
            },
            save: function() {
                var deferred = $q.defer();
                $http.post('api/processes').success(function(data){
                    deferred.resolve(data);
                }).error(function(){
                    deferred.reject();
                });
                return deferred.promise;
            },
            getAvailableProcesses: function(params, callback) {
                var urlParamString = parseParam(params);
                $http.get('api/processes/next'+urlParamString).success(callback);
            }
        }
    });
*/
