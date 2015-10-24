function apiGet(method, data) {
  return $.ajax({
    type: 'POST',
    url: window.location.protocol + '//' + window.location.hostname + ':' + 8080 + '/' + method,
    data: data,
    dataType: 'json'
  });
}

var app = angular.module('halloweenApp', []);

app.controller('MapController', function ($scope) {
  var map = new Microsoft.Maps.Map($('#map')[0], {
      credentials: 'ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX',
      enableSearchLogo: false,
      enableClickableLogo: false,
      showDashboard: false
  });


  apiGet('user_loc', { user: 'chicken' })
    .success(function (loc) {
      map.entities.push(new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(loc.lat, loc.lon), {
        icon: "pumpkin.png",
        width: 50,
        height: 50
      }));
    });
});

app.controller('ServerDataController', function ($scope) {
  apiGet('all_data')
    .success(function (data) {
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

$(function () {
  $('.server-data').hide();
});
