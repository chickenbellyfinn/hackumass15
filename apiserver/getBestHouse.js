var dbClient = require('./mongodbsetup');
var _ = require('lodash');

// req: JSON object containing house request parameters
// returns: string containing address of the next best house
function getBestHouse(req, callback) {
	var user = req.user;
	var keywords = req.keywords;

	dbClient(function(db) {
		var regexp = RegExp(_.join(keywords, '|'));
		db.collection("candies").find({'name': {$regex: regexp}).toArray(function(err, candiesRes) {
			db.collection("candyCounts").find({'candy': {$in: _.pluck(candiesRes, "_id")}}).toArray(function (err, candyCountRes) {
				var countList = _.countBy(candyCountRes, 'loc');
				var bestCount = _.max(countList);
				var bestLocation = _.find(countList, function(count) {
					return count === bestCount;
				});

				db.collection("locations").findOne({'_id': bestLocation}, function(err, entity) {
					var add2 = entity.addr;
					var add1;
					db.collection("users").find({user}, function(results) {
						var id = results.lastLocation;
						db.collection("locations").find({id}, function(loc) {
							add1 = loc.addr;
							getDirections(add1, add2, callback);
						});
					});
				});
			});
		});
	});
}

module.exports = getBestHouse;
