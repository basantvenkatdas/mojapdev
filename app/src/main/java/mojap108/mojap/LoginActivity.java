package mojap108.mojap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.internal.LogoImageView;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DigitsData mData = AppData.getInstance(this).getDigitsData();
        if(mData != null && mData.getAuthId() != null) {
            showMoJapActivity();
            return;
        }
        setContentView(R.layout.activity_login);
        LogoImageView img = (LogoImageView)findViewById(R.id.auth_logo);
        int dimPixels = (int)getResources().getDisplayMetrics().density * 200;
        int marginTopPixels = (int)getResources().getDisplayMetrics().density * 30;
        RelativeLayout.LayoutParams lpm =   new RelativeLayout.LayoutParams(dimPixels,dimPixels);
        lpm.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lpm.setMargins(0,marginTopPixels, 0, 0);
        img.setLayoutParams(lpm);
        img.setImageDrawable(getResources().getDrawable(R.drawable.app_icon));
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        digitsButton.setTextColor(getResources().getColor(android.R.color.black));
        digitsButton.setText("Signup/Login with Phone Number");
        digitsButton.setAuthTheme(R.style.CustomDigitsTheme);
        digitsButton.setCallback(((MoJapApplication) getApplication()).getAuthCallback());
        /*digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user12 model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
                showMoJapActivity();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });*/

    }

    private void showMoJapActivity() {
        Intent intent = new Intent(this, MoJapMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    protected void onStop() {
        finish();
        super.onStop();
    }
}
