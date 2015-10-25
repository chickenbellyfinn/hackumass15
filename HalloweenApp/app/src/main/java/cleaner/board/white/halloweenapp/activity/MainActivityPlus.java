package cleaner.board.white.halloweenapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.fitbit.FitbitHelper;
import cleaner.board.white.halloweenapp.model.Address;
import cleaner.board.white.halloweenapp.model.Candy;
import cleaner.board.white.halloweenapp.view.AddressFragment;
import cleaner.board.white.halloweenapp.view.CandyListFragment;
import cleaner.board.white.halloweenapp.view.DestinationFragment;

public class MainActivityPlus extends AppCompatActivity implements ActionBar.TabListener {

    private static final String TAG = MainActivityPlus.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Bind(R.id.addButton) FloatingActionButton addButton;

    private String username;
    private FitbitHelper fitbit;
    private OkHttpClient client;
    private SharedPreferences prefs;

    SectionsPagerAdapter mSectionsPagerAdapter;

    AddressFragment addressFragment;
    CandyListFragment candyListFragment;
    DestinationFragment destinationFragment;

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_plus);

        ButterKnife.bind(this);

        addressFragment = new AddressFragment();
        candyListFragment = new CandyListFragment();
        destinationFragment = new DestinationFragment();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString(LoginActivity.PREF_USER, "beckles");

        fitbit = new FitbitHelper(this);
        client = new OkHttpClient();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    @Override
    public void onResume(){
        super.onResume();
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
            Candy.add(candy);
            updateFragments();
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
                    candyJSON.put("keyword", candy.keyword);
                    candyJSON.put("calories", candy.calories);

                    json.put("candy", candyJSON);

                    String jsonString = json.toString();
                    RequestBody body = RequestBody.create(JSON, jsonString);
                    Log.d(TAG, jsonString);
                    Request request = new Request.Builder()
                            .url("http://halloweenapp.cloudapp.net:8080/submit_candy")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Address.get(response.body().string()).addCandy(candy);
                    updateFragments();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void updateFragments(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addressFragment.update();
                candyListFragment.update();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main_activity_plus, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return destinationFragment;
                case 1:
                    return candyListFragment;
                case 2:
                    return addressFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity_plus, container, false);
            return rootView;
        }
    }

}
