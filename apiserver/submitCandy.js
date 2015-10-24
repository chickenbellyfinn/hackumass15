var locToAddr = require('./LocationToAddress');
var dbClient = require('./mongodbsetup');
var _ = require('lodash');

// req: JSON object containing raw location data, candy information, and user information
// returns: the address at the location given
function submitCandy(req, callback) {
	var user = req.user;
	var lat = req.lat;
	var lon = req.lon;
	var keyword = req.keyword;
	var candyName = req.candy.name;
	var candyCalories = req.candy.calories;

	locToAddr.getAddress(lat, lon, function(address) {
		dbClient(function (db) {
			var locations = db.collection('locations');
			var users = db.collection('users');
			var candies = db.collection('candies');
			var candyCounts = db.collection('candyCounts');

			getEntity(locations, { 'addr': address }, { 'lat': lat, 'lon': lon }, function (location) {
				getEntity(candies, { 'name': candyName }, { 'calories': candyCalories, 'keyword': keyword }, function (candy) {
					getEntity(candyCounts, { 'loc': location._id, 'candy': candy._id }, { 'count': 0 }, function (candyCount) {
						candyCounts.update({ '_id': candyCount._id }, { $inc: { 'count': 1 } });
					});
				});

				getEntity(users, { 'name': user }, { 'lastLocation': location._id }, function (user) {
					users.update({ '_id': user._id }, { $set: { 'lastLocation': location._id } });
				});
			});
		});

		callback(address);
	});
}

function getEntity(collection, key, data, callback) {
	collection.findOne(key, function (err, entity) {
		if (!entity) {
			collection.insert(_.merge(key, data), function (err, result) {
				callback(result.ops[0]);
			});
		}
		else {
			callback(entity);
		}
	});
}

module.exports = submitCandy;


// submitCandy(
// {
// 	user: dbjsxn,
// 	lat: 42.3911608,
// 	lon: -72.5289008,
// 	candy: {
// 		name: "Snickers",
// 		calories: 400
// 	}
// });
