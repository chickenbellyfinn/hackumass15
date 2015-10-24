
// returns all the location data in the database as a JSON object
function getAllData(callback) {
	dbClient(function (db) {
		var results = db.candycol.find();
		callback(results);
	});
}

module.exports = getAllData;
