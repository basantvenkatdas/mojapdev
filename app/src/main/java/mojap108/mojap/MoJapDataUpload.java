package mojap108.mojap;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gollaba on 7/10/16.
 */
public class MoJapDataUpload {

    private Context mContext;
    private int UPLOAD_INTERVAL = 60 * 60 * 1000;
    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.d("MojapDataUpload", "timertask start run");
            uploadCurrentBeadCount();
        }
    };

    private void uploadCurrentBeadCount() {

        String url = Constants.BASE_URL + Constants.CREATE_ACTIVITY;
        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject createActivityRequestBody = new JSONObject();
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        try {
            createActivityRequestBody.put(Constants.AUTH_ID, mData.getAuthId());
            createActivityRequestBody.put(Constants.DATETIME, new Date().toString());
            createActivityRequestBody.put(Constants.BEEDCOUNT, BeadData.getInstance(mContext, new Date()).getTodayBeadCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, createActivityRequestBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MojapDataUpload", "activity successfully uploaded");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MojapDataUpload", "activity upload error");
                    }
                });

        mQueue.add(jsObjRequest);
    }

    public MoJapDataUpload(Context context) {
        mContext = context;
    }

    public void startActivityUpload() {
        uploadCurrentBeadCount();
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        Date todayDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(todayDate);
        int min = cal1.get(Calendar.MINUTE);
        timer.schedule(timerTask, (60-min) * 60 * 1000, UPLOAD_INTERVAL);
    }

    public void stopActivityUpload() {
        uploadCurrentBeadCount();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
