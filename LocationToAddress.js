var http = require('http');
//var latitude = process.argv[2];
//var longitude = process.argv[3];

var options = {
	host: "dev.virtualearth.net",
	path: "/REST/v1/Locations/" + latitude + "," + longitude + "?&key=ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX"
}
//getAddress(latitude, longitude, function(address){
	//console.log(address);
//});


// callback = function(response) {
//   	var str = '';

//   	//another chunk of data has been recieved, so append it to `str`
//   	response.on('data', function (chunk) {
//     	str += chunk;
//   	});

//   	//the whole response has been recieved, so we just print it out here
//   	response.on('end', function () {
//     	console.log(str);
//     	console.log("");
//     	var address = JSON.parse(str).resourceSets[0].resources[0].name;
//   		console.log(address);
//   	});

// }

// http.request(options, callback).end();

export function getAddress(latitude, longitude, callback){
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
		    	var address = JSON.parse(str).resourceSets[0].resources[0].name;
		  		//console.log(address);
		  		callback(address);
	  	});

	}).end();
}