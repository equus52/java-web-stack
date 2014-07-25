"use strict"

###
@ngdoc function
@name javaWebStackApp.controller:AboutCtrl
@description
# AboutCtrl
Controller of the javaWebStackApp
###
angular.module("javaWebStackApp").controller "AboutCtrl", ($scope) ->
  $scope.awesomeThings = [
    "HTML5 Boilerplate"
    "AngularJS"
    "Karma"
  ]
