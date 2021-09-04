package me.shagor.ctnewsbd;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by SHAGOR on 5/29/2017.
 */

public class AppController {
    private static AppController mySingleTon;
    private ImageLoader mImageLoader;
    private RequestQueue requestQueue;
    private static Context context;

    // Create Constructor
    public AppController(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    // Create Method
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized AppController getInstance(Context context) {
        if (mySingleTon == null) {
            mySingleTon = new AppController(context);
        }
        return mySingleTon;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public<T> void addToRequestQueue(Request<T>request) {
        requestQueue.add(request);
    }
}
