var http = require('http');

//Get directions from loc1 to loc2. Both loc1 and loc2 are addresses.
function getDirections(loc1, loc2, callback) {
	var address1 = encodeURIComponent(loc1);
	var address2 = encodeURIComponent(loc2);
	var options = {
		host: "dev.virtualearth.net",
		path: "/REST/V1/Routes/Walking?wp.0="+address1+"&wp.1="+address2+"&key=ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX"
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
		    	var dir = JSON.parse(str).resourceSets[0].resources[0].routeLegs[0].itineraryItems; //[0].instruction.text;
		    	var dir_array = [];
		    	//var dir_text = dir[0].instruction[0].text;
		    	//console.log(dir);
		    	for(i=0;i<dir.length;i++) {
		    		dir_array[i] = dir[i].instruction.text;
		    	}
		  		callback(dir_array);
	  	});

	}).end();
}

module.exports.getDirections = getDirections;

//getDirections("154 Hicks Way, Amherst, MA, 01003","340 Thatcher Rd Amherst MA 01003", function(dir_array) { console.log(dir_array) });