package cleaner.board.white.halloweenapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String PREF_CANDY_DATA = "candy";

    @Bind(R.id.list) ListView list;
    @Bind(R.id.addButton) FloatingActionButton addButton;
    @Bind(R.id.what) TextView nocandy;

    private CandyAdapter adapter;
    private FitbitHelper fitbit;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        adapter = new CandyAdapter(this);
        loadCandies();
        list.setAdapter(adapter);

        fitbit = new FitbitHelper(this);

        if(getIntent().getAction().equals(Intent.ACTION_VIEW)){
            fitbit.authCallback(getIntent().getDataString());
        } else {
            if(!fitbit.isAuthenticated()) {
                fitbit.authenticate();
            }
        }

        if(!adapter.isEmpty()){
            nocandy.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.addButton)
    public void addCandy(){
        Intent intent = new Intent(this, CandySearchActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int req, int res , Intent data){
        Candy candy = new Gson().fromJson(data.getStringExtra("candy"), Candy.class);

        boolean added = false;
        for(int i = 0; i < adapter.getCount(); i++){
            if(adapter.getItem(i).name.equals(candy.name)){
                adapter.getItem(i).count++;
                added = true;
                break;
            }
        }

        if(!added){
            candy.count++;
            adapter.add(candy);
        }

        saveCandies();
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
