package me.shagor.ctnewsbd;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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
  Created by shagor on 6/19/2017.
 */

public class Popularnews extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String url = Config.popularPostURL;
    private List<Movie> popularList = new ArrayList<Movie>();
    private boolean refreshpopular=false;
    Boolean internetcheck;
    Functions func;
    private ListView listpopulr;
    private CustomListAdapter cmadapter;
    private static final String TAG = Category.class.getSimpleName();
    HashMap<String,String> Hash_file_maps1 ;
    SliderLayout sliderLayout;
    private SwipeRefreshLayout swipeRefreshLayout1;
    DatabaseHelper dbHelper;

    AdView adView;

    View view;
    long p=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.popular,container,false);

        //Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        func = new Functions();
        dbHelper = new DatabaseHelper(getContext());
        internetcheck = func.isNetworkAvailable(getContext());
        listpopulr = (ListView) view.findViewById(R.id.list);
        cmadapter = new CustomListAdapter(getContext(), popularList);
        listpopulr.setAdapter(cmadapter);
        swipeRefreshLayout1 = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_popular);
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
                        fetchCateData();
                    }
                },200);
            }
        });
        listpopulr.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listpopulr == null || listpopulr.getChildCount() == 0) ?
                                0 : listpopulr.getChildAt(0).getTop();
                swipeRefreshLayout1.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        if(internetcheck)
            fetchCateData();   //Toast.makeText(getContext(), "Hi.", Toast.LENGTH_LONG).show();
        else {
            Cursor c = dbHelper.getAllData("popular");

            int p=0;

            if(c.moveToFirst()) {
                if (c.getCount() > 0){
                do {
                    if (p < 3) {
                        Hash_file_maps1 = new HashMap<String, String>();

                        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
                        Hash_file_maps1.put(c.getString(2), c.getString(1) + "#" + c.getString(3));
                        String name1[] = {};
                        for (String name : Hash_file_maps1.keySet()) {

                            name1 = Hash_file_maps1.get(name).split("#");

                            // Toast.makeText(getApplication(),name1[1],Toast.LENGTH_LONG).show();
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView
                                    .description(name)
                                    .image(name1[1])
                                    //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(Popularnews.this);
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("post_Id", name1[0].trim());
                            sliderLayout.addSlider(textSliderView);

                        }
                    } else {
                        Movie movie = new Movie();
                        movie.setID(c.getString(1));
                        movie.setTitle(c.getString(2));
                        movie.setThumbnailUrl(c.getString(3));
                        movie.setDate(c.getString(4));

                        popularList.add(movie);
                    }

                    p++;


                } while (c.moveToNext());

            }
            }
        }
        return view;
    }

    public void fetchCateData() {
        internetcheck = func.isNetworkAvailable(getContext());
        if (internetcheck) {
            if(refreshpopular)
                swipeRefreshLayout1.setRefreshing(true);
            dbHelper.deleteallData("popular");
            popularList.clear();
            JsonArrayRequest populaurreq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            //  Hash_file_maps1.clear();
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject obj = response.getJSONObject(i);

                                        if (i < 3) {
                                            Hash_file_maps1 = new HashMap<String, String>();

                                            sliderLayout = (SliderLayout) view.findViewById(slider);
                                            Hash_file_maps1.put(obj.getString("title"), obj.getString("postId") + "#" + obj.getString("image"));
                                            String name1[] = {};
                                            for (String name : Hash_file_maps1.keySet()) {

                                                name1 = Hash_file_maps1.get(name).split("#");
                                                //tittle1=name;
                                                // Toast.makeText(getApplication(),name1[1],Toast.LENGTH_LONG).show();
                                                TextSliderView textSliderView = new TextSliderView(getContext());
                                                textSliderView
                                                        .description(name)
                                                        .image(name1[1])
                                                        //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                                        .setOnSliderClickListener(Popularnews.this);
                                                textSliderView.bundle(new Bundle());
                                                textSliderView.getBundle()
                                                        .putString("post_Id", name1[0].trim());
                                                sliderLayout.addSlider(textSliderView);
                                            }
                                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                            sliderLayout.setDuration(2000);
                                            sliderLayout.addOnPageChangeListener(Popularnews.this);
                                            dbHelper.insertData("popular",obj.getString("postId"),obj.getString("title"),obj.getString("image"),obj.getString("date"));
                                           // Toast.makeText(getContext(),""+p,Toast.LENGTH_LONG).show();
                                        }

                                        if (i > 3) {
                                            Movie movie = new Movie();
                                            movie.setID(obj.getString("postId"));
                                            movie.setTitle(obj.getString("title"));
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setDate(obj.getString("date"));

                                            // adding movie to movies array
                                            popularList.add(movie);
                                            dbHelper.insertData("popular",obj.getString("postId"),obj.getString("title"),obj.getString("image"),obj.getString("date"));

                                        }
                                      // Toast.makeText(getContext(),obj.getString("title"),Toast.LENGTH_SHORT).show();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                refreshpopular=true;
                            }

                            // so that it renders the list view with updated data
                            cmadapter.notifyDataSetChanged();
                            listpopulr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getContext(), Sinlgepage.class);
                                    String postid = popularList.get(position).getId();
                                    intent.putExtra("postid", postid);
                                    intent.putExtra("latestnews", "POPULAR");
                                    startActivity(intent);
                                   getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                                }
                            });
                            swipeRefreshLayout1.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                  //  swipeRefreshLayout.setRefreshing(false);
                    error.printStackTrace();
                }
            });

            // Adding request to request queue
            AppController.getInstance(getContext()).addToRequestQueue(populaurreq);
        } else {

            Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(getContext(), Sinlgepage.class);
        String postid = slider.getBundle().get("post_Id").toString();
        intent.putExtra("postid", postid);
        intent.putExtra("latestnews", "POPULAR");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_in_left);
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