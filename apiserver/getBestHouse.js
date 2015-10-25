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
		getRandomHouse(db, user, callback);
		return;
	}

	dbClient(function(db) {
		var regexp = RegExp(keywords.join('|'), 'i');
		db.collection("candies").find({'name': {$regex: regexp}}).toArray(function(err, candiesRes) {
			if (err || !candiesRes || !candiesRes.length) {
				getRandomHouse(db, user, callback);
				return;
			}
			db.collection("candyCounts").find({'candy': {$in: _.pluck(candiesRes, "_id")}}).toArray(function (err, candyCountRes) {
				if (err || !candyCountRes || !candyCountRes.length) {
					getRandomHouse(db, user, callback);
					return;
				}
				console.log(candyCountRes);
				var countList = _.countBy(candyCountRes, 'loc');
				var bestCount = _.max(countList);
				var bestLocation = ObjectId(_.findKey(countList, function(count) {
					return count === bestCount;
				}));
				console.log(bestLocation);
				var houseCandyIDs = _.filter(candyCountRes, function (c) {
					return c.loc.equals(bestLocation);
				});
				console.log(houseCandyIDs);
				var houseCandyID = houseCandyIDs[0].candy;

				db.collection("locations").findOne({'_id': bestLocation}, function(err, entity) {
					sendDirections(db, entity.addr, user, houseCandyID, callback);
				});
			});
		});
	});
}

function getRandomHouse(db, user, callback) {
	db.collection('locations').find({}).toArray(function (err, locations) {
		var randLoc = locations[Math.floor(Math.random() * locations.length)];
		db.collection('candyCounts').find({'loc': randLoc._id}).toArray(function (err, candyCounts) {
			var randCandy = candyCounts[Math.floor(Math.random() * candyCounts.length)];
			sendDirections(db, randLoc.addr, user, randCandy.candy, callback);
		});
	});
}

function sendDirections(db, toAddr, user, candyID, callback) {
	var add2 = toAddr;
	var add1;
	db.collection("users").findOne({'name': user}, function(err, results) {
		if (err || !results) {
			return;
		}
		var id = results.lastLocation;
		db.collection("locations").findOne({'_id': id}, function(err, loc) {
			add1 = loc.addr;
			getDirections(add1, add2, function (directions) {
				db.collection('candies').findOne({'_id': candyID}, function (err, houseCandy) {
					callback({
						directions: directions,
						addr: toAddr,
						candy: houseCandy.name
					});
				});
			});
		});
	});
}

module.exports = getBestHouse;
