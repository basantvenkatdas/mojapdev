package mojap108.mojap;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by gollaba on 7/14/16.
 */
public class ResetRequest {

    String url = Constants.BASE_URL + Constants.POST_RESET;
    private Context mContext = null;
    private Handler mhandler = null;
    private String timeZoneName;
    private int offsetHours;


    public ResetRequest(Context context, Handler mHandler) {
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
    }


    public void sendResetRequest() {
        url = url +"?offset="+timeZoneName;//offsetHours;
        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject loginPostBody = new JSONObject();
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        try {
            loginPostBody.put(Constants.AUTH_ID, mData.getAuthId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, loginPostBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResetRequest", "posted the reset"+response.toString());
                        sendResponse(null);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendResponse(null);

                    }
                });

        mQueue.add(jsObjRequest);
    }

    private void sendResponse(LoginData mData){
    }


}
