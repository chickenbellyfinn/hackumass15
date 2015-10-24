// Get MongoDB client
var dbClient = require('mongodb').MongoClient;
var db;

// Connect to MongoDB
function getConnection(callback) {
	if (db) {
		callback(db);
	}
	else {
		dbClient.connect("mongodb://halloweenapp.cloudapp.net:27017/halloweenapp", function(err, dbconn) {
		  if (!err) {
		    console.log("Connection made");
		    db = dbconn;
		  	callback(db);
		  }
		});
	}
}

module.exports = getConnection;
