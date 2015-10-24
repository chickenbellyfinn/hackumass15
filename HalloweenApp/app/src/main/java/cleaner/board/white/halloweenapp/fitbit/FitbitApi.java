package cleaner.board.white.halloweenapp.fitbit;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;

/**
 * Created by Akshay on 10/12/2015.
 */
public class FitbitApi extends DefaultApi20 {

    public static final String API_SECRET = "87beccc1aff26139d01a3d5b5739a4ad";
    public static final String CLIENT_ID = "229RNB";

    private static final String AUTH_URL = "https://www.fitbit.com/oauth2/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.fitbit.com/oauth2/token";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return AUTH_URL;
    }
}
