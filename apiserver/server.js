var PORT = 8080;

var express = require('express');

var app = express();

app.get('/', function (req, res) {
  res.send('cool beans');
});

var server = app.listen(PORT, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Halloween app listening at http://%s:%s', host, port);
});
