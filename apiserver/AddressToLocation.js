var http = require('http');
//var latitude = process.argv[2];
//var longitude = process.argv[3];

function getLocation(state, zipcode, town, add_line, callback){
	var address_line = encodeURIComponent(add_line);
	var options = {
		host: "dev.virtualearth.net",
		path: "/REST/v1/Locations/US/" + state + "/" + zipcode + "/" + town + "/" + address_line + "?&key=ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX"
	};
	http.request(options, 
		function(response) {
		  	var str = '';

		  	//another chunk of data has been recieved, so append it to `str`
		  	response.on('data', function (chunk) {
		    	str += chunk;
		  	});

		  	//the whole response has been recieved, so we just print it out here
		  	response.on('end', function () {
		    	//console.log(str);
		    	//console.log("");
		    	var lat_and_long = JSON.parse(str).resourceSets[0].resources[0].point.coordinates;
		  		//console.log(lat_and_long.toString());
		  		callback(lat_and_long);
	  	});

	}).end();
}

module.exports.getLocation = getLocation;

//getLocation("Massachusetts","01003","Amherst","340 Thatcher Road", function(lat_and_long) { console.log(lat_and_long.toString()) });