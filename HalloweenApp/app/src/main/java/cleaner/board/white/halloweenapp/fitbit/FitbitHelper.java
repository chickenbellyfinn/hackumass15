package cleaner.board.white.halloweenapp.fitbit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Created by Akshay on 10/12/2015.
 */
public class FitbitHelper {

    public interface ResponseHandler{
        public void onResponse(JSONObject response);
    }

    private static final String INVALID_TOKEN = "invalid_token";

    private static final String TAG = FitbitHelper.class.getSimpleName();

    private static final String PREF_FITBIT_TOKEN = "fitbit_token";
    private static final String PREF_FITBIT_TOKEN_SECRET = "fitbit_token_secret";

    private static final String SEARCH_URL = "https://api.fitbit.com/1/foods/search.json";

    private Context context;
    private SharedPreferences prefs;

    private OAuthService service;
    private Token accessToken;

    public FitbitHelper(Context context){
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        service = new ServiceBuilder()
                .provider(FitbitApi.class)
                .apiKey(FitbitApi.CLIENT_ID)
                .apiSecret(FitbitApi.API_SECRET)
                .callback("relax://callback")
                .build();

        if(isAuthenticated()) {
            getToken();
        }
    }

    public void authenticate(){
        String authUrl = service.getAuthorizationUrl(null) + getAuthUrlParams();
        Log.d(TAG, authUrl);

        Intent authIntent = new Intent(Intent.ACTION_VIEW);
        authIntent.setData(Uri.parse(authUrl));
        context.startActivity(authIntent);
    }

    public void search(final String term, final ResponseHandler handler){
        if(!isAuthenticated()){
            Log.e(TAG, "No Access Token!");
        }
        new Thread(){
            @Override
            public void run(){
                String url = Uri.parse(SEARCH_URL)
                        .buildUpon()
                        .appendQueryParameter("query", term)
                        .build().toString();
                OAuthRequest request = new OAuthRequest(Verb.GET, url);
                request.addHeader("Authorization", "Bearer "+getToken().getToken());
                //service.signRequest(getToken(), request);
                Response response = request.send();
                JSONObject json = null;

                if(response.getBody().contains(INVALID_TOKEN)){
                    invalidateToken();
                }

                try {
                    json = new JSONObject(response.getBody());
                    json.put("searchTerm", term);
                } catch (Exception e){
                    e.printStackTrace();
                }
                handler.onResponse(json);
            }
        }.start();

    }

    public boolean isAuthenticated(){
        return prefs.contains(PREF_FITBIT_TOKEN);
    }

    private Token getToken(){
        if(accessToken == null){
            accessToken = new Token(prefs.getString(PREF_FITBIT_TOKEN, null), prefs.getString(PREF_FITBIT_TOKEN_SECRET, null));
        }
        return accessToken;
    }

    private void invalidateToken(){
        prefs.edit().remove(PREF_FITBIT_TOKEN).remove(PREF_FITBIT_TOKEN_SECRET).commit();
    }

    public void authCallback(final String callbackUrl){
        new Thread(){
            @Override
            public void run(){
                try {
                    String code = callbackUrl.substring(callbackUrl.indexOf("code=") + 5); //"code=".length()
                    Token access = getAccessToken(code);//service.getAccessToken(new Token(FitbitApi.API_KEY, FitbitApi.API_SECRET), new Verifier(token));
                    Log.d(TAG, "code=" + code);
                    prefs.edit()
                            .putString(PREF_FITBIT_TOKEN, access.getToken())
                            .putString(PREF_FITBIT_TOKEN_SECRET, access.getSecret())
                            .commit();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(TAG, "authCallback "+callbackUrl);
            }
        }.start();

    }

    private String getAuthUrlParams(){
        return String.format("?client_id=%s&response_type=code&scope=activity nutrition&expires_in=%d",
                FitbitApi.CLIENT_ID, 2592000);
    }

    /**
     * Scribe doesn't sign token request..so custom request
     * @return
     */
    private Token getAccessToken(String code) throws JSONException {
        FitbitApi api = new FitbitApi();

        String authHeader = Base64.encodeToString((FitbitApi.CLIENT_ID+":"+FitbitApi.API_SECRET).getBytes(), 0);
        OAuthRequest request = new OAuthRequest(Verb.POST, api.getAccessTokenEndpoint());
        request.addQuerystringParameter("client_id", FitbitApi.CLIENT_ID);
        request.addQuerystringParameter("grant_type", "authorization_code");
        request.addQuerystringParameter("redirect_uri", "halloweenapp://");
        request.addQuerystringParameter("code", code);
        request.addHeader("Authorization", "Basic " + authHeader);
        Response response = request.send();
        JSONObject tokenResponse = new JSONObject(response.getBody());
        return new Token(tokenResponse.getString("access_token"), tokenResponse.getString("refresh_token"));
    }


}
