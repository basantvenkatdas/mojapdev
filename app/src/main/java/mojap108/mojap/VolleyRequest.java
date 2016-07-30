package mojap108.mojap;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by gollaba on 7/17/16.
 */
public class VolleyRequest {

    private static VolleyRequest mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized VolleyRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequest(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
