var PPO = {
  icon: "pumpkin.png",
  width: 50,
  height: 50
};

$(document).ready(function(){
  var map = new Microsoft.Maps.Map(document.getElementById('myMap'), 
    {
      credentials: 'ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX',
      enableSearchLogo:false,
      enableClickableLogo:false,
      showDashboard:false     
  });

  map.entities.push(new Microsoft.Maps.Pushpin(map.getCenter(), PPO));

});


function boundingBox(){
  var minLat = 42.384609;
  var maxLat = 42.399586;
  var minLon = -72.541252;
  var maxLon = -72.516357;

}