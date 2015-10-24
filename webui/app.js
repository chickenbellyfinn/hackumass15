var app = angular.module('halloweenApp', []);

app.controller('ServerDataController', function ($scope) {
  $.getJSON(window.location.protocol + '//' + window.location.hostname + ':' + 8080 + '/all_data')
    .done(function (data) {
      console.log(data);

      _.forEach(data.users, function (user) {
        user.lastLocation = _.find(data.locations, function (l) { return l._id === user.lastLocation; });
      });
      
      _.merge($scope, data);
      $scope.$apply();
    })
    .fail(function () {
      console.log('Error getting server data');
    });
});
