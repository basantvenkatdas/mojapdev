package mojap108.mojap;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by gollaba on 6/20/16.
 */
public class MoJapApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QnBq4uNRlg7LKgyBlKdf79kWn";
    private static final String TWITTER_SECRET = "wKrzY4R1bnGGMvIrBQU5rPE4UCzLBNp4JdEcp3VECEJ4VeB4Bf";


    private AuthCallback authCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Digits.Builder digitsBuilder = new Digits.Builder().withTheme(R.style.CustomDigitsTheme);
        Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build());
        Fabric.with(this, new Crashlytics());
        // Digits.getSessionManager().clearActiveSession();
        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                storeDigitsData(session);
                startMainActivity();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("TAG","digits failure");
                // Do something on failure
            }
        };
    }

    private void storeDigitsData(DigitsSession session) {
        AppData.getInstance(this).storeDigitsData(session.getPhoneNumber(), String.valueOf(session.getId()));
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MoJapMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public AuthCallback getAuthCallback() {
        return authCallback;
    }
}
