function apiGet(method, data) {
  return $.ajax({
    type: 'POST',
    url: window.location.protocol + '//' + window.location.hostname + ':' + 8080 + '/' + method,
    data: data,
    dataType: 'json'
  });
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

var app = angular.module('halloweenApp', []);

app.controller('MapController', function ($scope) {
  var map = new Microsoft.Maps.Map($('#map')[0], {
      credentials: 'ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX',
      enableSearchLogo: false,
      enableClickableLogo: false,
      showDashboard: false
  });

  function login(username) {
    apiGet('user_loc', { user: username })
      .success(function (loc) {
        map.entities.push(new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(loc.lat, loc.lon), {
          icon: 'pumpkin.png',
          width: 50,
          height: 50
        }));
      });
  }

  var $loginModal = $('#loginModal');
  var $loginButton = $('#loginButton');
  var $userNameInput = $('#userNameInput');

  $loginModal.modal({ show: false });

  $loginButton.click(function () {
    login($userNameInput.val());
    $loginModal.modal('hide');
  });

  $loginModal.modal('show');
});

app.controller('ServerDataController', function ($scope) {
  // apiGet('all_data')
  //   .success(function (data) {
  //     console.log(data);

  //     _.forEach(data.users, function (user) {
  //       user.lastLocation = _.find(data.locations, function (l) { return l._id === user.lastLocation; });
  //     });

  //     _.merge($scope, data);
  //     $scope.$apply();
  //   })
  //   .fail(function () {
  //     console.log('Error getting server data');
  //   });
});

$(function () {
  $('.server-data').hide();
});
