package mojap108.mojap;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by gollaba on 7/14/16.
 */
public class Login {

    String url = Constants.BASE_URL + Constants.LOGIN_API;
    private Context mContext = null;
    private Handler mhandler = null;
    private String timeZoneName;
    private int offsetHours;

    public Login(Context context, Handler mHandler) {
        mContext = context;
        this.mhandler = mHandler;
        calculateTimeZonOffset();
    }

    private void calculateTimeZonOffset() {
        Calendar cal1 = Calendar.getInstance();
        TimeZone tz = cal1.getTimeZone();
        timeZoneName = tz.getID();
        long msFromEpochGmt = cal1.getTimeInMillis();
        offsetHours = tz.getOffset(msFromEpochGmt)/(3600 * 1000);

      /*  Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        offsetHours =  TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS);*/
    }

    public void doLogin() {

        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        url = url + "?offset="+timeZoneName;

        JSONObject loginPostBody = new JSONObject();
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        try {
            loginPostBody.put(Constants.PHONE_NO, mData.getPhoneNo());
            loginPostBody.put(Constants.AUTH_ID, mData.getAuthId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, loginPostBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        LoginData mData = parseLoginResponse(response);
                        sendResponse(mData);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendResponse(null);

                    }
                });

        mQueue.add(jsObjRequest);
    }

    private LoginData parseLoginResponse(JSONObject response) {
        ObjectMapper mapper = new ObjectMapper();
        LoginData mData = null;
        try {
            Log.d("Login", "the login response="+response.toString());
            mData = mapper.readValue(response.toString(), LoginData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  mData;
    }

    private void sendResponse(LoginData mData){
        Message mMessage = mhandler.obtainMessage();
        mMessage.what = Constants.LOGIN_RESPONSE;
        mMessage.obj = mData;
        mhandler.sendMessage(mMessage);
    }


}
