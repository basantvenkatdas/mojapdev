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
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gollaba on 7/10/16.
 */
public class MoJapDataUpload {

    private Context mContext;
    private int UPLOAD_INTERVAL = 60 * 60 * 1000;
    private Timer timer;
    private TimerTask timerTask = null;
    private long offsetHours;

    private TimerTask initTimeTask() {
        return new TimerTask() {
            @Override
            public void run() {
                Log.d("MojapDataUpload", "timertask start run");
                uploadCurrentBeadCount();
            }
        };
    }

    private void calculateTimeZonOffset() {
        Calendar cal1 = Calendar.getInstance();
        TimeZone tz = cal1.getTimeZone();
        long msFromEpochGmt = cal1.getTimeInMillis();
        offsetHours = tz.getOffset(msFromEpochGmt)/(3600 * 1000);

      /*  Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        offsetHours =  TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS);*/
    }

    private void uploadCurrentBeadCount() {

        String url = Constants.BASE_URL + Constants.CREATE_ACTIVITY+"?offset="+offsetHours;
        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject createActivityRequestBody = new JSONObject();
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        try {
            createActivityRequestBody.put(Constants.AUTH_ID, mData.getAuthId());
            createActivityRequestBody.put(Constants.DATETIME, new Date().toString());
            createActivityRequestBody.put(Constants.BEADCOUNTFORDAY, BeadData.getInstance(mContext, new Date()).getTodayBeadCount());
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
        calculateTimeZonOffset();
        uploadCurrentBeadCount();
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        Date todayDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(todayDate);
        int min = cal1.get(Calendar.MINUTE);
        timerTask = initTimeTask();
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
