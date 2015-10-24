var PORT = 8080;

var express = require('express');
var bodyParser = require('body-parser');
var submitCandy = require('./submitCandy');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.use(function (req, res, next) {
  if (req.method === 'POST') {
    console.log('API request for ' + req.path);
  }
  next();
});

app.post('/submit_candy', function (req, res) {
  submitCandy(req.body);
  res.end();
});

var server = app.listen(PORT, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Halloween app listening at http://%s:%s', host, port);
});
