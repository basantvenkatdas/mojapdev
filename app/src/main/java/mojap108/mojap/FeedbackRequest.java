package mojap108.mojap;

import android.content.Context;
import android.os.Handler;

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

/**
 * Created by gollaba on 7/14/16.
 */
public class FeedbackRequest {

    String url = Constants.BASE_URL + Constants.INSTALLATION_API;
    private Context mContext = null;
    private Handler mhandler = null;


    public FeedbackRequest(Context context, Handler mHandler) {
        mContext = context;
        this.mhandler = mHandler;
    }

    public void sendFeedback(String feedbackString) {
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        String url = Constants.BASE_URL + Constants.POST_FEEDBACK+"/"+mData.getAuthId();
        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject feedbackPostBody = new JSONObject();
        try {
            feedbackPostBody.put(Constants.FEEDBACK, feedbackString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, feedbackPostBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        mQueue.add(jsObjRequest);
    }
}
