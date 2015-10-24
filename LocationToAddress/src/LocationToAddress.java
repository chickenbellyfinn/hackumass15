import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

public class LocationToAddress {
	
	/*Our Bing key*/
	String bingKey = "ApZUbPx7guIK9fElyBl_r4N8BX5FmED1Kq3Z-gDSjK-sv7KKC25FpXRBi9TEGdQX";
	
	String latitude;
	String longitude;
	
	public LocationToAddress(String lat, String lon) {
		this.latitude = lat;
		this.longitude = lon;
	}
	
	/*Sends the HTTP request to Bing to obtain Address given the latitude and longitude*/ 
	public String sendHTTPRequest() throws MalformedURLException {
		String url = "http://dev.virtualearth.net/REST/v1/Locations/" + latitude + ',' + longitude + "?&key=" + bingKey;
		URL obj = new URL(url);
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				System.out.println("Successful Request");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine = "";
				StringBuffer response = new StringBuffer();
				
				while((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
				br.close();
				return response.toString();
			}
			else{
				System.out.println("Unsuccessful Request");
				return null;
			}
		} catch (IOException e) {
			System.out.println("Unable to open connection");
			return null;
		}
		
	}
	
	public String getAddress(String r) {
		String jsonText = r;
		JSONParser parser = new JSONParser();
		  ContainerFactory containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		 };
		 
		 try{
			 System.out.println("entered try");
			 JSONObject jsonObj = (JSONObject) new JSONParser().parse(r);
			 JSONObject obj2 = (JSONObject)(((JSONArray) jsonObj.get("resourceSets")).get(0));
			 JSONObject obj3 = (JSONObject)(((JSONArray) obj2.get("resources")).get(0));
			 String result = (String) obj3.get("name");
			 System.out.println(result);
			 return result;
//			    Map json = (Map)parser.parse(jsonText, containerFactory);
//			    
//			    Iterator<String,String> iter = (Iterator<String,String>) json.entrySet().iterator();
//			    System.out.println("==iterate result==");
//			    while(((java.util.Iterator) iter).hasNext()){
//			      Map.Entry entry = iter.next();
//			      System.out.println(entry.getKey() + "=>" + entry.getValue());
//			    }
//			                        
//			    System.out.println("==toJSONString()==");
//			    System.out.println(JSONValue.toJSONString(json));
			  }
			  catch(Exception pe){
			    pe.printStackTrace();
			    return null;
			  } 
		  
	}

}
