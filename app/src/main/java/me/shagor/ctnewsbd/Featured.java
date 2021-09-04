package me.shagor.ctnewsbd;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.shagor.ctnewsbd.R.id.slider;

/**
  Created by shagor on 7/16/2017.
 */

public class Featured extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private static final String TAG = Category.class.getSimpleName();
    HashMap<String, List<String>> Hash_file_maps1 ;
    private static final String url = Config.featured;
    private static final String url2 = Config.featured_galley;
    private String[] category;
    SliderLayout sliderLayout;
    private List<FeathareConfig> featuareList = new ArrayList<FeathareConfig>();
    private SwipeRefreshLayout swipeRefreshLayout1;
    private  boolean refreshfeatured=false;
    private FeaturedAdapter fadapter;
    Boolean internetcheck;
    private ProgressDialog pDialog;
    ListView feature_list;
    Functions func;
    private Context context;
    TextView first_text,first_title,first_date;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    AdView adView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.featured,container,false);

        //Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mNetworkImageView = (NetworkImageView) view
                .findViewById(R.id.first_image);
       //  first_text = (TextView) view.findViewById(R.id.first_text);
         first_title = (TextView) view.findViewById(R.id.first_title);
       //  first_date = (TextView) view.findViewById(R.id.first_date);

        adView.loadAd(adRequest);

        func = new Functions();

        category = new String[]{"National","POLITICS","INTERNATIONAL","SPORTS","ENTERTAINMENT","Opinion"};

        fadapter = new FeaturedAdapter(getContext(),featuareList);
        feature_list = (ListView) view.findViewById(R.id.feature_list);
        feature_list.setAdapter(fadapter);
        swipeRefreshLayout1 = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);
       String sliderurl=Config.englishcategory+"&limit=10";
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        mImageLoader = AppController.getInstance(getContext())
                .getImageLoader();
        fetchData(sliderurl);
        //String category_url=url+"&limit=5";
        //String category_url=url+"&limit=5";
        fetchFeaturedData(url2);
        swipeRefreshLayout1.setColorSchemeResources(R.color.logo_color,
                android.R.color.holo_green_light,
                R.color.logo_color,
                android.R.color.holo_red_light);
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout1.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout1.setRefreshing(false);
                        fetchData(url);
                    }
                },200);
            }
        });

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();


    }
    private void fetchData(String url) {

        internetcheck=func.isNetworkAvailable(getContext());

        if(internetcheck) {
            if(refreshfeatured==true)
            swipeRefreshLayout1.setRefreshing(true);
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            // Log.d(TAG, response.toString());
                            //    hidePDialog();
                            if (response.length() > 0) {
                                String tittle1;
                                // Parsing json


                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                            Hash_file_maps1 = new HashMap<String, List<String>>();
                                            List<String> fieldData= new ArrayList<>();
                                            fieldData.add(obj.getString("postId"));
                                            fieldData.add(obj.getString("image"));
                                            sliderLayout = (SliderLayout) view.findViewById(slider);
                                            Hash_file_maps1.put(obj.getString("title"), fieldData);
                                            for (String name : Hash_file_maps1.keySet()) {

                                                List<String> listdata = Hash_file_maps1.get(name);
                                                String post_id=listdata.get(0);
                                                String image=listdata.get(1);
                                                TextSliderView textSliderView = new TextSliderView(getContext());
                                                textSliderView
                                                        .description(name)
                                                        .image(image)
                                                        //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                                        .setOnSliderClickListener(Featured.this);
                                                textSliderView.bundle(new Bundle());
                                                textSliderView.getBundle()
                                                        .putString("post_Id", post_id.trim());
                                                sliderLayout.addSlider(textSliderView);
                                            }
                                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                            sliderLayout.setDuration(2000);
                                            sliderLayout.addOnPageChangeListener(Featured.this);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                refreshfeatured=true;
                            }
                            // notifying list adapter about data changes
                            swipeRefreshLayout1.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   hidePDialog();
                    // stopping swipe refresh
                    swipeRefreshLayout1.setRefreshing(false);
                }
            });

            // Adding request to request queue
            AppController.getInstance(getContext()).addToRequestQueue(movieReq);
        }
        else {

            Toast.makeText(getContext(),"Please check your internet connection.",Toast.LENGTH_LONG).show();
        }
    }
    private void fetchFeaturedData(String url) {

        internetcheck=func.isNetworkAvailable(getContext());

        if(internetcheck) {
           // if(refreshfeatured==true)
         //       swipeRefreshLayout1.setRefreshing(true);

                JsonArrayRequest featuredReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                pDialog.dismiss();
                                    if (response.length() > 0){
                                        int count=1;
                                        int cat_name=1;
                                    for (int i=0; i<response.length(); i++) {
                                        try {
                                            JSONObject obj = response.getJSONObject(i);
                                            if(count==1){
                                                FeathareConfig fconfig = new FeathareConfig();
                                                fconfig.setFirst_title(obj.getString("title"));
                                                fconfig.setFirst_date(obj.getString("date"));
                                                fconfig.setThumbnailUrl(obj.getString("image"));
                                                featuareList.add(fconfig);
                                            }
                                            if(count==2){
                                                FeathareConfig fconfig = new FeathareConfig();
                                                fconfig.setSecond_tilte(obj.getString("title"));
                                                fconfig.setSecond_date(obj.getString("date"));
                                                fconfig.setThumbnailUrl(obj.getString("image"));
                                                //fconfig.setLeft_thumb(obj.getString("image"));
                                                featuareList.add(fconfig);
                                            }
                                            if(count==3){
                                                FeathareConfig fconfig = new FeathareConfig();
                                                fconfig.setThird_title(obj.getString("title"));
                                                fconfig.setThird_date(obj.getString("date"));
                                                fconfig.setThumbnailUrl(obj.getString("image"));
                                                featuareList.add(fconfig);
                                            }
                                            if(count==4){
                                                FeathareConfig fconfig = new FeathareConfig();
                                                fconfig.setFourth_title(obj.getString("title"));
                                                fconfig.setFourth_date(obj.getString("date"));
                                                fconfig.setThumbnailUrl(obj.getString("image"));
                                                featuareList.add(fconfig);
                                            }
                                            if(count==6) {
                                                count=0;
                                            }
                                         //   count++;
                                          //  if(count==0){
                                           ////     first_title.setText(obj.getString("title"));

                                             //   final String url = "http://goo.gl/0rkaBz";
//                                                mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
//                                                        R.drawable.error, R.drawable
//                                                                .error));
                                             //   mNetworkImageView.setImageUrl(url, mImageLoader);
//                                                imageLoader1.get(fconfig.getThumbnailUrl(), ImageLoader.getImageListener(first_image,
//                                                        R.drawable.error, R.drawable.error));
//                                                first_image.setImageUrl(fconfig.getThumbnailUrl(), imageLoader1);
//                                                first_image.setDefaultImageResId(R.drawable.error);
//                                                first_image.setErrorImageResId(R.drawable.error);
                                             //   first_date.setText(obj.getString("date"));

                                        //    }

//                                               else if(count==1){
//                                                    data.setSecond_tilte(obj.getString("title"));
//                                                    data.setThumbnailUrl(obj.getString("image"));
//                                                    data.setSecond_date(obj.getString("date"));
//
//                                                }
//                                               else if(count==2){
//                                                    data.setThird_title(obj.getString("title"));
//                                                   data.setThumbnailUrl(obj.getString("image"));
//                                                    data.setThird_date(obj.getString("date"));
//                                                }


//                                           else if(count>2 && count<5){
//                                                    data.setFourth_title(obj.getString("title"));
//                                                    data.setFourth_date(obj.getString("date"));
//
//                                                if(count==5){
//
//                                                    data.setFirst_text(category[cat_name]);
//                                                    count=0;
//                                                    cat_name++;
//                                                }
//                                            }


                                                //featuareList.add(data);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        count++;
                                    }
                                }

                                fadapter.notifyDataSetChanged();
                                // Log.d(TAG, response.toString());
                                //    hidePDialog();

//                                if (response.length() > 0) {
//                                    // Parsing json
//                                    int count=0;
//                                    for (int i = 0; i < response.length(); i++) {
//                                        try {
//                                            JSONObject obj = response.getJSONObject(i);
//                                            if(count==1){
//                                                first_title.setText(obj.getString("title").toString());
//                                                first_date.setText(obj.getString("date").toString());
//
//                                            }
//                                            if(count>1 && count<=3){
//                                                if(count==2){
//                                                    second_tilte.setText(obj.getString("title").toString());
//                                                    second_date.setText(obj.getString("date").toString());
//
//                                                }
//                                                if(count==3){
//                                                    third_title.setText(obj.getString("title").toString());
//                                                    third_date.setText(obj.getString("date").toString());
//                                                }
//
//                                            }
//                                            if(count>3 && count<=6){
//                                                   // fourth_title.setText(obj.getString("title").toString());
//
//                                                if(count==6){
//                                                    count=0;
//                                                }
//                                            }
//                                            count++;
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        //Toast.makeText(getContext(), customerId, Toast.LENGTH_LONG).show();
//                                    }
//                                    refreshfeatured = true;
//                                }
                                // notifying list adapter about data changes
                                swipeRefreshLayout1.setRefreshing(false);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //   hidePDialog();
                        // stopping swipe refresh
                        // swipeRefreshLayout1.setRefreshing(false);
                    }
                });

                // Adding request to request queue
                AppController.getInstance(getContext()).addToRequestQueue(featuredReq);

        }
        else {

            Toast.makeText(getContext(),"Please check your internet connection.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}