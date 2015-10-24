var app = angular.module('halloweenApp', []);

app.controller('ServerDataController', function ($scope) {
  $.getJSON(window.location.protocol + '//' + window.location.hostname + ':' + 8080 + '/all_data')
    .done(function (data) {
      console.log(data);
      _.merge($scope, data);
      $scope.$apply();
    })
    .fail(function () {
      console.log('Error getting server data');
    });
});
