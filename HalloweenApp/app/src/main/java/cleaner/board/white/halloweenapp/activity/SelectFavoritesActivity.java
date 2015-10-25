package cleaner.board.white.halloweenapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.fitbit.FitbitHelper;
import cleaner.board.white.halloweenapp.model.Candy;
import cleaner.board.white.halloweenapp.view.CandyAdapter;

public class SelectFavoritesActivity extends AppCompatActivity {

    public static final String PREF_FAV = "favorites";

    @Bind(R.id.list) ListView list;
    @Bind(R.id.addButton) FloatingActionButton addButton;

    FitbitHelper fitbit;
    CandyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_favorites);
        ButterKnife.bind(this);
        adapter = new CandyAdapter(this);
        adapter.setNumbersEnabled(false);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fitbit = new FitbitHelper(this);

    }

    @Override
    public void onResume(){
        super.onResume();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.contains(PREF_FAV)){
            startActivity(new Intent(this, MainActivityPlus.class));
        }

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
            adapter.add(candy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            saveFavorites();
            startActivity(new Intent(this, MainActivityPlus.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveFavorites(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        JSONArray array = new JSONArray();

        for(int i = 0; i < adapter.getCount(); i++){
            array.put(adapter.getItem(i).keyword);
        }

        prefs.edit().putString(PREF_FAV, array.toString()).commit();
    }
}
