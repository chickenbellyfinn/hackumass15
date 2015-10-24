import java.net.MalformedURLException;

import com.google.gson.Gson;
import org.json.simple.*;
import org.json.simple.parser.*;


public class LocationToAddressDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String latitude = "42.329647";
		String longitude = "-71.614029";
		LocationToAddress lta = new LocationToAddress(latitude,longitude);
		String address = "";
		try {
			address = lta.sendHTTPRequest();
		} catch (MalformedURLException e) {
			System.out.println("Connection could not be established");
		}
		System.out.println(address);
		//Object obj = JSONValue.parse(address);
		//JSONArray arr = (JSONArray) address;
		//JSONObject obj2 = (JSONObject)arr.get(3);
		//System.out.println(obj2.get(2));
		String res = lta.getAddress(address);
		
		
		
	}

}
