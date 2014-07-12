"use strict"

###
@ngdoc overview
@name javaWebStackApp
@description
# javaWebStackApp

Main module of the application.
###
angular.module("javaWebStackApp", [
  "ngAnimate"
  "ngCookies"
  "ngResource"
  "ngRoute"
  "ngSanitize"
  "ngTouch"
]).config ($routeProvider) ->
  $routeProvider.when("/",
    templateUrl: "views/main.html"
    controller: "MainCtrl"
  ).when("/about",
    templateUrl: "views/about.html"
    controller: "AboutCtrl"
  ).otherwise redirectTo: "/"
