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

app.controller('ServerDataController', function ($scope) {
  var map = new Microsoft.Maps.Map($('#map')[0], {
      credentials: 'ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX',
      enableSearchLogo: false,
      enableClickableLogo: false,
      showDashboard: false
  });
  map.setView({ zoom: 3 });

  var locationHistory = [];

  var $loginModal = $('#loginModal');
  var $loginButton = $('#loginButton');
  var $logoutButton = $('#logoutButton');
  var $userNameForm = $('#userNameForm');
  var $userNameInput = $('#userNameInput');

  $('.server-data').hide();

  function getLocation(username, success, fail) {
    if (username === 'admin') {
      $('.map-ui').hide();
      $('.server-data').show();
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
      fail && fail();
    }
    else {
      apiGet('user_loc', { user: username })
        .success(function (loc) {
          if (!locationHistory.length || locationHistory[locationHistory.length - 1]._id !== loc._id) {
            console.log('new location');
            console.log('location: ', loc);
            console.log('history: ', locationHistory);
            locationHistory.push(loc);

            var mloc = new Microsoft.Maps.Location(loc.lat, loc.lon);

            map.entities.clear();

            map.entities.push(new Microsoft.Maps.Pushpin(mloc, {
              icon: 'pumpkin.png',
              width: 40,
              height: 40
            }));

            map.entities.push(new Microsoft.Maps.Polyline(_.map(locationHistory, function (l) {
              return new Microsoft.Maps.Location(l.lat, l.lon);
            }), null));

            map.setView({zoom: 16, center: mloc});
          }
          else {
            console.log('hasnt moved');
          }

          success && success();
        })
        .fail(function (err) {
          promptUsername();
          fail && fail();
        });
    }
  }

  function promptUsername() {
    $userNameForm.submit(function () {
      $loginButton.click();
      return false;
    });

    $loginButton.click(function () {
      document.location = '?username=' + encodeURIComponent($userNameInput.val());
      $loginModal.modal('hide');
    });

    function centerModal() {
        $(this).css('display', 'block');
        var $dialog  = $(this).find(".modal-dialog"),
        offset       = ($(window).height() - $dialog.height()) / 2,
        bottomMargin = parseInt($dialog.css('marginBottom'), 10);

        // Make sure you don't hide the top part of the modal w/ a negative margin if it's longer than the screen height, and keep the margin equal to the bottom margin of the modal
        if(offset < bottomMargin) offset = bottomMargin;
        $dialog.css("margin-top", offset);
    }

    $(document).on('show.bs.modal', '.modal', centerModal);
    $(window).on("resize", function () {
        $('.modal:visible').each(centerModal);
    });

    $loginModal.modal('show');
    $userNameInput.focus();
  }

  $loginModal.modal({ show: false });

  $logoutButton.click(function () {
    window.location = '/';
  });

  var username = getUrlParameter('username');
  if (!username) {
    promptUsername();
  }
  else {
    function update() {
      getLocation(username, function () {
        setTimeout(update, 60000);
      });
    }
    update();
  }
});
