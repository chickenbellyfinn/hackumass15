package cleaner.board.white.halloweenapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleaner.board.white.halloweenapp.R;

public class LoginActivity extends Activity {

    public static final String PREF_USER = "user";

    @Bind(R.id.user) TextView user;
    @Bind(R.id.button) Button button;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darkpurp));
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.contains(PREF_USER)){
            nextActivity();
        }
    }

    @OnClick(R.id.button)
    public void submit(){
        String username = user.getText().toString();

        if(!username.isEmpty()){
            prefs.edit().putString(PREF_USER, username).commit();
            nextActivity();
        }
    }

    private void nextActivity(){
        startActivity(new Intent(this, SelectFavoritesActivity.class));

    }
}
