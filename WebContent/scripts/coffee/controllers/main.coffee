"use strict"

###
@ngdoc function
@name javaWebStackApp.controller:MainCtrl
@description
# MainCtrl
Controller of the javaWebStackApp
###
angular.module("javaWebStackApp").controller "MainCtrl", ($scope) ->
  $scope.awesomeThings = [
    "HTML5 Boilerplate"
    "AngularJS"
    "Karma"
  ]
