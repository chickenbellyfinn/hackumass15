var PORT = 8080;

var express = require('express');
var bodyParser = require('body-parser');
var submitCandy = require('./submitCandy');
var getBestHouse = require('./getBestHouse');
var getAllData = require('./getAllData');
var getUserLocation = require('./getUserLocation');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

app.use(function (req, res, next) {
  console.log('API request: ' + req.method + ' ' + req.path);
  res.set('Access-Control-Allow-Origin', 'http://halloweenapp.cloudapp.net');
  next();
});

app.post('/submit_candy', function (req, res) {
  console.log(req.body);
  submitCandy(req.body, function (address) {
    console.log(address);
    res.send(address);
  });
});

app.post('/best_house', function (req, res) {
  console.log(req.body);
  res.type('application/json');
  getBestHouse(req.body, function (bestHouse) {
    console.log(bestHouse);
    res.send(bestHouse);
  });
});

app.post('/all_data', function (req, res) {
  res.type('application/json');
  getAllData(function (data) {
    console.log(data);
    res.send(data);
  });
});

app.post('/user_loc', function (req, res) {
  res.type('application/json');
  getUserLocation(req.body, function (location) {
    console.log(location);
    res.send(location);
  });
});

var server = app.listen(PORT, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Halloween app listening at http://%s:%s', host, port);
});
