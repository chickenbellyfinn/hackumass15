package cleaner.board.white.halloweenapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cleaner.board.white.halloweenapp.model.Candy;
import cleaner.board.white.halloweenapp.model.CandyFilter;
import cleaner.board.white.halloweenapp.fitbit.FitbitHelper;
import cleaner.board.white.halloweenapp.view.FoodAdapter;
import cleaner.board.white.halloweenapp.R;

public class CandySearchActivity extends AppCompatActivity implements FitbitHelper.ResponseHandler {

    private final int MAX_RESULTS = 10;

    @Bind(R.id.term)  EditText searchText;
    @Bind(R.id.button) Button searchButton;
    @Bind(R.id.listView) ListView list;
    @Bind(R.id.ohno) TextView notfound;

    private FoodAdapter adapter;

    private FitbitHelper fitbit;

    private String lastTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candy_search);
        ButterKnife.bind(this);

        adapter = new FoodAdapter(this);
        list.setAdapter(adapter);

        fitbit = new FitbitHelper(this);
        notfound.setVisibility(View.GONE);
    }

    @OnClick(R.id.button)
    public void doSearch(){
        String term = searchText.getText().toString();

        if(!term.isEmpty() && !term.equals(lastTerm)){
            lastTerm = term;
            fitbit.search(term, this);
        }

    }

    @OnItemClick (R.id.listView)
    public void candySelected(int pos){
        Candy candy = adapter.getItem(pos);
        candy.keyword = CandyFilter.keyword(candy.name);
        setResult(Activity.RESULT_OK, new Intent().putExtra("candy", new Gson().toJson(candy)));
        finish();
    }


    @Override
    public void onResponse(JSONObject response) {
        if(response != null){
            try {
                if(response.getString("searchTerm").equals(lastTerm)) {
                    final ArrayList<Candy> foodList = new ArrayList<Candy>();
                    JSONArray foods = response.getJSONArray("foods");

                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject foodItem = foods.getJSONObject(i);
                        String name = foodItem.getString("name");
                        int calories = foodItem.getInt("calories");
                        if(CandyFilter.couldBeCandy(name)) {
                            foodList.add(new Candy(name, calories, 0));
                        }
                    }

                    updateFoodList(foodList);
                }
            } catch (Exception e){
                e.printStackTrace();
                updateFoodList(new ArrayList<Candy>());
            }

        }
    }

    private void updateFoodList(final List<Candy> foodList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(foodList);
                adapter.notifyDataSetChanged();
                if(adapter.getCount() == 0){
                    notfound.setVisibility(View.VISIBLE);
                } else {
                    notfound.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_candy_search, menu);
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
