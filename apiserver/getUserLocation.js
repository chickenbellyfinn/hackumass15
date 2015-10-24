var dbClient = require('./mongodbsetup');

function getUserLocation(req, callback) {
  dbClient(function (db) {
    db.collection('users').findOne({'name': req.user}, function(err, user) {
      if (err || !user) {
        callback(null);
        return;
      }
      db.collection('locations').findOne({'_id': user.lastLocation}, function(err, location) {
        callback(location);
      });
    });
  });
}

module.exports = getUserLocation;
