var dbClient = require('./mongodbsetup');
var getDirections = require('./Directions');
var _ = require('lodash');
var ObjectId = require('mongodb').ObjectID;

// req: JSON object containing house request parameters
// returns: string containing address of the next best house
function getBestHouse(req, callback) {
	var user = req.user;
	var keywords = req.keywords;

	if (!keywords.length) {
		getRandomHouse(function (rand) {
			callback(rand.addr);
		});
	}

	dbClient(function(db) {
		var regexp = RegExp(keywords.join('|'), 'i');
		db.collection("candies").find({'name': {$regex: regexp}}).toArray(function(err, candiesRes) {
			if (err || !candiesRes || !candiesRes.length) {
				getRandomHouse(function (rand) {
					callback(rand.addr);
				});
			}
			db.collection("candyCounts").find({'candy': {$in: _.pluck(candiesRes, "_id")}}).toArray(function (err, candyCountRes) {
				if (err || !candyCountRes || !candyCountRes.length) {
					getRandomHouse(function (rand) {
						callback(rand.addr);
					});
				}
				var countList = _.countBy(candyCountRes, 'loc');
				var bestCount = _.max(countList);
				var bestLocation = ObjectId(_.findKey(countList, function(count) {
					return count === bestCount;
				}));
				var houseCandyID = _.filter(candyCountRes, function (c) {
					return c.loc.equals(bestLocation);
				})[0].candy;

				db.collection('candies').findOne({'_id': houseCandyID}, function (err, houseCandy) {
					db.collection("locations").findOne({'_id': bestLocation}, function(err, entity) {
						var add2 = entity.addr;
						var add1;
						db.collection("users").findOne({'name': user}, function(err, results) {
							var id = results.lastLocation;
							db.collection("locations").findOne({'_id': id}, function(err, loc) {
								add1 = loc.addr;
								getDirections(add1, add2, function (directions) {
									callback({
										directions: directions,
										addr: entity.addr,
										candy: houseCandy.name
									});
								});
							});
						});
					});
				});
			});
		});
	});
}

function getRandomHouse(callback) {
	db.collection('locations').find({}).toArray(function (err, locations) {
		callback(locations[Math.floor(Math.random() * locations.length)]);
	});
}

module.exports = getBestHouse;
