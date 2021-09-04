package me.shagor.ctnewsbd;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by shagor on 7/7/2017.
 */

public class MySingleTon {
    // Define Variable
    public static final String TAG = MySingleTon.class.getSimpleName();
    private static MySingleTon mySingleTon;
    private RequestQueue requestQueue;
    private static Context context;
    private ImageLoader mImageLoader;
    private static MySingleTon mInstance;
    //create Constractor
    public MySingleTon(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    // Create Method

    public RequestQueue getRequestQueue() {
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized MySingleTon getMySingleTop (Context context)
    {
        if (mySingleTon == null)
        {
            mySingleTon = new MySingleTon(context);
        }
        return  mySingleTon;
    }

    public <T> void  addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }


}