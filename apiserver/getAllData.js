var dbClient = require('./mongodbsetup');

// returns all the location data in the database as a JSON object
function getAllData(callback) {
	dbClient(function (db) {
    db.collection('candies', function (err, candies) {
      if (err) console.log('Error: ' + err);
      else {
        candies.find().toArray(function (err, results) {
          if (err) console.log('Error: ' + err);
          else {
            callback(results);
          }
        });
      }
    });
	});
}

module.exports = getAllData;
