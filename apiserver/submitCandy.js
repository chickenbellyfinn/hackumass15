var locToAddr = require('./LocationToAddress');
var dbClient = require('./mongodbsetup');

// req: JSON object containing raw location data, candy information, and user information
// returns: nothing
function submitCandy(req, callback) {
	var user = req.user;
	var lat = req.lat;
	var lon = req.lon;
	var candyName = req.candy.name;
	var candyCalories = req.candy.calories;

	locToAddr.getAddress(lat, lon, function(address) {
		dbClient(function (db) {
			getLocation(db, address, lat, lon, function (location) {
				getCandy(db, candyName, candyCalories, function (candy) {
					getCandyCount(db, location._id, candy._id, function (candyCount) {
						var candyCounts = db.collection('candyCounts');
						candyCounts.update({ '_id': candyCount._id }, { $inc: { 'count': 1 } });
					});
				});
			});
		});

		callback(address);
	});
}

function getLocation(db, address, lat, lon, callback) {
	var locations = db.collection('locations');
	locations.findOne({ 'addr': address }, function (err, location) {
		if (!location) {
			locations.insert({ 'lat': lat, 'lon': lon, 'addr': address }, function (err, location) {
				callback(location);
			});
		}
		else {
			callback(location);
		}
	});
}

function getCandy(db, name, calories, callback) {
	var candies = db.collection('candies');
	candies.findOne({ 'name': name }, function (err, candy) {
		if (!candy) {
			candies.insert({ 'name': name, 'calories': calories }, function (err, candy) {
				callback(candy);
			});
		}
		else {
			callback(candy);
		}
	});
}

function getCandyCount(db, locID, candyID, callback) {
	var candyCounts = db.collection('candyCounts');
	candyCounts.findOne({ 'loc': locID, 'candy': candyID }, function (err, candyCount) {
		if (!candyCount) {
			candyCounts.insert({ 'loc': locID, 'candy': candyID, 'count': 0 }, function (err, candyCount) {
				callback(candyCount);
			});
		}
		else {
			callback(candyCount);
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
