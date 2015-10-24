package cleaner.board.white.halloweenapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String PREF_CANDY_DATA = "candy";

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Bind(R.id.list) ListView list;
    @Bind(R.id.addButton) FloatingActionButton addButton;
    @Bind(R.id.what) TextView nocandy;

    private String username;

    private CandyAdapter adapter;
    private FitbitHelper fitbit;

    private OkHttpClient client;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString(LoginActivity.PREF_USER, "beckles");

        adapter = new CandyAdapter(this);
        loadCandies();
        list.setAdapter(adapter);

        fitbit = new FitbitHelper(this);

        if(!adapter.isEmpty()){
            nocandy.setVisibility(View.GONE);
        }

        client = new OkHttpClient();

    }

    @Override
    public void onResume(){
        super.onResume();
        if(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_VIEW)){

            fitbit.authCallback(getIntent().getDataString());
        } else {
            if(!fitbit.isAuthenticated()) {
                fitbit.authenticate();
            }
        }
    }

    @OnClick(R.id.addButton)
    public void addCandy(){
        Intent intent = new Intent(this, CandySearchActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int req, int res , Intent data){

        if(res == Activity.RESULT_OK) {
            Candy candy = new Gson().fromJson(data.getStringExtra("candy"), Candy.class);
            postCandy(candy);

            boolean added = false;
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).name.equals(candy.name)) {
                    adapter.getItem(i).count++;
                    added = true;
                    break;
                }
            }

            if (!added) {
                candy.count++;
                adapter.add(candy);
            }

            saveCandies();


        }
    }

    private void postCandy(final Candy candy) {

        new Thread(){
            public void run(){

                try {
                    JSONObject json = new JSONObject();
                    json.put("user", username);
                    json.put("lat", 42.3911608+ (Math.random()-0.5)/1000.0);
                    json.put("lon", -72.5289008 + (Math.random() - 0.5)/1000.0);

                    JSONObject candyJSON = new JSONObject();
                    candyJSON.put("name", candy.name);
                    candyJSON.put("calories", candy.calories);

                    json.put("candy", candyJSON);

                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .url("http://halloweenapp.cloudapp.net:8080/submit_candy")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, response.body().string());
                } catch (Exception e){}
            }
        }.start();

    }

    private void saveCandies(){
        Candy[] candies = new Candy[adapter.getCount()];
        for(int i = 0; i < adapter.getCount(); i++){
            candies[i] = adapter.getItem(i);
        }
        prefs.edit().putString(PREF_CANDY_DATA, new Gson().toJson(candies)).commit();
    }

    private void loadCandies(){
        String data = prefs.getString(PREF_CANDY_DATA, "[]");
        Candy[] candies = new Gson().fromJson(data, new Candy[]{}.getClass());

        adapter.clear();
        for(Candy candy:candies){
            adapter.add(candy);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
