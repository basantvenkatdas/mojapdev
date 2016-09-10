package mojap108.mojap;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;

/**
 * Created by gollaba on 7/22/16.
 */
public class AppData {

    static AppData instance = null;
    public static final String PREFS = "MoJap_preferences" ;
    SharedPreferences sharedPref;
    private Context mContext;

    private AppData(Context context) {
        mContext = context;
        sharedPref = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static AppData getInstance(Context context) {
        if(instance == null) {
            instance = new AppData(context.getApplicationContext());
        }
        return instance;
    }

    public void storeDigitsData(String phoneNo, String authId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PHONE_NO, phoneNo);
        editor.putString(Constants.AUTH_ID, authId);
        editor.commit();
    }

    public DigitsData getDigitsData() {
        DigitsData gData = new DigitsData();
        gData.setPhoneNo(sharedPref.getString(Constants.PHONE_NO, null));
        gData.setAuthId(sharedPref.getString(Constants.AUTH_ID, null));
        return gData;
    }

    public void storeUserData(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.commit();
    }

    public LoginData getUserData() {
        LoginData gData = new LoginData();
        gData.setPhoneNo(sharedPref.getString(Constants.PHONE_NO, null));
        gData.setAuthId(sharedPref.getString(Constants.AUTH_ID, null));
        gData.setId(sharedPref.getString(Constants.USER_ID, null));
        return gData;
    }

    public void setCoachMarkSeen() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.COACHMARK_SHOWN, true);
        editor.commit();
    }

    public boolean isCoachMarkSeen() {
        return sharedPref.getBoolean(Constants.COACHMARK_SHOWN, false);
    }

    public void setMantraText(String val) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.MANTRA_SET, val);
        editor.commit();
    }

    public String getMantraText() {
        return sharedPref.getString(Constants.MANTRA_SET, Constants.DEFAULT_MANTRA_TEXT);
    }
}
