package nl.wildenberg.maurice_537811.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mdwil on 16-10-2017.
 */

public class AuthToken {
    private static String authToken = "";
    private static AuthToken instance = null;
    private static SharedPreferences preferences;

    protected AuthToken() {
        // Exists only to defeat instantiation.
    }
    public static AuthToken getInstance(Context context) {
        if(instance == null) {
            preferences = context.getSharedPreferences("NewsReader", MODE_PRIVATE);
            instance = new AuthToken();
        }
        return instance;
    }

    public void SetAuthToken(String authToken){
        preferences.edit().putString("guestToken", authToken).apply();
    }

    public void DeleteAuthToken(Context context){
        preferences.edit().putString("guestToken", null).apply();
    }
    public String GetAuthToken()
    {
        String authToken = preferences.getString("guestToken", "");

        return authToken;
    }

    public boolean CheckLoggedIn() {
        String authToken = preferences.getString("guestToken", null);

        return (authToken != null) ? true : false;
    }
}
