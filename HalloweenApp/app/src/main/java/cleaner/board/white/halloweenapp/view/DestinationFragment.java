package cleaner.board.white.halloweenapp.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.activity.LoginActivity;
import cleaner.board.white.halloweenapp.activity.SelectFavoritesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DestinationFragment extends Fragment {

    private static final String TAG = DestinationFragment.class.getSimpleName();

    private static final String PREF_USER = "username";

    @Bind(R.id.find) Button findCandy;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.candy) TextView candy;
    @Bind(R.id.list) ListView list;
    @Bind(R.id.cardd) View card;
    @Bind(R.id.hit) View hit;
    @Bind(R.id.textView3) TextView view2;

    private ArrayAdapter<String> adapter;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private SharedPreferences prefs;
    private String reqString;

    String addressStr;
    String candyStr;


    public DestinationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_destination, container, false);

        ButterKnife.bind(this, view);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            String user = prefs.getString(LoginActivity.PREF_USER, "chicken");
            JSONArray keys =  (new JSONArray(prefs.getString(SelectFavoritesActivity.PREF_FAV, "['snickers']")));

            JSONObject req = new JSONObject();
            req.put("user", user);
            req.put("keywords", keys);
            reqString = req.toString();

        } catch(Exception e){
            reqString = "";
        }

        return view;
    }

    @OnClick(R.id.find)
    public void findBestCandy(){
        new Thread() {
            public void run() {

                try {

                    RequestBody body = RequestBody.create(JSON, reqString);
                    Request request = new Request.Builder()
                            .url("http://halloweenapp.cloudapp.net:8080/best_house")
                            .post(body)
                            .build();
                    Response response = new OkHttpClient().newCall(request).execute();
                    //Log.d(TAG,  response.body().string());

                    JSONObject res = new JSONObject(response.body().string());
                    JSONArray directions = res.getJSONArray("directions");
                    addressStr = res.getString("addr");
                    candyStr = "They have " + res.getString("candy") + "!";

                    List<String> directionList = new ArrayList<String>();
                    for(int i = 0; i < directions.length(); i++){
                        directionList.add(directions.getString(i));
                    }

                    updateUI(directionList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void updateUI(final List<String> dir){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(dir);
                adapter.notifyDataSetChanged();
                address.setText(addressStr);
                candy.setText(candyStr);
                card.setVisibility(View.VISIBLE);
                hit.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);

            }
        });
    }



}
