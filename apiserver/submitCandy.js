var locToAddr = require('./LocationToAddress');
var dbClient = require('./mongodbsetup');

// req: JSON object containing raw location data, candy information, and user information
// returns: nothing
function submitCandy(req, callback) {
	var user = req.user;
	var lat = req.lat;
	var lon = req.lon;
	var candyName = req.candy[name];
	var candyCalories = req.candy[calories];

	getAddress(lat, lon, function(address) {
		dbClient(function (db) {
			var locResults = db.globaloc.findOne({'loc.addr': address});
			var results = db.candycol.findOne({name: candyName});
			if (!results) {
				db.candycol.insert({name: candyName, calories: candyCalories}, function(err, docInserted) {
					if (locResults) {
						locResults.candy.push({candy: docInserted._id, count: 1});
						db.globalloc.update({_id: locResults._id}, locResults);
					}
				});
			}
			else {
				if (locResults) {
					for (var i = locResults.candy.length - 1; i >= 0; i--) {
						if(locResults.candy[i].name === candyName) {
							locResults.candy[i].count++;
							break;
						}
					};
					db.globalloc.update({_id: locResults._id}, locResults);
				}
			}
		});

		callback(address);
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
