package cleaner.board.white.halloweenapp.model;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Akshay on 10/24/2015.
 */
public class Address extends SugarRecord<Address> {

    private static final String PREF_ADDRESS = "adresses";


    public String address;
    private String candyList = "[]";



    public static Address get(String address) {

        Iterator<Address> addrIt = SugarRecord.findAll(Address.class);
        Address addr;
        while (addrIt.hasNext()){
            addr = addrIt.next();
            if(addr.address.equals(address)){
                return addr;
            }
        }

        addr = new Address(address);
        addr.save();
        return addr;
    }


    public static List<Address> all(){
        List<Address> addressList = new ArrayList<Address>();
        Iterator<Address> addrIt = SugarRecord.findAll(Address.class);
        while (addrIt.hasNext()){
            addressList.add(addrIt.next());
        }
        return addressList;
    }

    public Address(){}

    public Address(String address){
        this.address = address;
    }

    public String getName(int i){
        try {

            JSONArray candies = new JSONArray(candyList);
            return ((JSONObject)candies.get(i)).getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public int getCount(int i){
        try {

            JSONArray candies = new JSONArray(candyList);
            return ((JSONObject)candies.get(i)).getInt("count");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int len(){

        try {

            JSONArray candies = new JSONArray(candyList);
            return candies.length();
        } catch (Exception e){}
        return 0;
    }

    public void addCandy(Candy candy){

        try {

            JSONArray candies = new JSONArray(candyList);

            boolean added = false;
            for(int i = 0; i < candies.length(); i++){
                JSONObject jc = (JSONObject)candies.get(i);
                if(jc.get("name").toString().equals(candy.name)){
                    jc.put("count", ((Integer)jc.get("count")) + 1);
                    added = true;
                    break;

                }
            }

            if(!added){
                JSONObject newObj = new JSONObject();
                newObj.put("name", candy.name);
                newObj.put("count", 1);
                candies.put(newObj);
            }

            candyList = candies.toString();
            save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
