var dbClient = require('./mongodbsetup');

// returns all the location data in the database as a JSON object
function getAllData(callback) {
	dbClient(function (db) {
    var candies = db.collection('candies');
    var users = db.collection('candies');
    var locations = db.collection('candies');
    var candyCounts = db.collection('candies');
    
    var names = ["candies", "users", "locations", "candyCounts"];
    var collections = [];

    for (var i = 0; i < names.length; i++) {
      collections.push(db.collection(names[i]));
    };

    var allresults = {};

    function recurser(i, results) {
      if (i >= 0) {
        allresults[names[i]] = results;
      }

      if (i >= names.length - 1) {
        callback(allresults);
        return;
      }

      collections[i + 1].find().toArray(function(err, results) {
        recurser(i + 1, results);
      });
    }

    recurser(-1, null);
	});
}

module.exports = getAllData;
