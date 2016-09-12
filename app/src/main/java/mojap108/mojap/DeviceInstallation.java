package mojap108.mojap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
public class DeviceInstallation {

    String url = Constants.BASE_URL + Constants.INSTALLATION_API;
    private Context mContext = null;
    private Handler mhandler = null;


    public DeviceInstallation(Context context, Handler mHandler) {
        mContext = context;
        this.mhandler = mHandler;
    }

    public void doDeviceInstallation() {
        String token = null;
            try {
                InstanceID instanceID = InstanceID.getInstance(mContext);
                token = instanceID.getToken(Constants.GCM_SENDERID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        if(token == null) {
            return ;
        }

       // url = url.replace("authId", AppData.getInstance(mContext).getUserData().getAuthId());

        RequestQueue mQueue = Volley.newRequestQueue(mContext.getApplicationContext());


        JSONObject loginPostBody = new JSONObject();
        final DigitsData mData = AppData.getInstance(mContext.getApplicationContext()).getDigitsData();
        try {
            loginPostBody.put(Constants.GCM_TOKEN, token);
            loginPostBody.put(Constants.TOKEN_TYPE, "gcm");
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
            mData = mapper.readValue(response.toString(), LoginData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  mData;
    }

    private void sendResponse(LoginData mData){
       /* Message mMessage = mhandler.obtainMessage();
        mMessage.what = Constants.INSTALLATION_RESPONSE;
        mMessage.obj = mData;
        mhandler.sendMessage(mMessage);*/
    }


}
