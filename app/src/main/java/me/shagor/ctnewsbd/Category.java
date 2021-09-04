package me.shagor.ctnewsbd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Category extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    // Movies json url
    private static final String url = Config.englishcategory;
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView list;
    private CustomListAdapter adapter;
    private String cat_url;
    private SwipeRefreshLayout swipeRefreshLayout;
    SliderLayout sliderLayout;
    HashMap<String,List<String>> Hash_file_maps1 ;
    Boolean check;
    Functions fn;
    private long p;
    private boolean refreshcheck=false;
    String catId;
    String appName;

    AdView adView;
    private static final String TAG = Category.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Admob
        MobileAds.initialize(this, "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fn = new Functions();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cutomlayout);
        catId = getIntent().getExtras().getString("catId");
        appName = getIntent().getExtras().getString("app_name");
        setTitle(appName);
        cat_url =url+"&cat="+catId;
        list = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(getApplicationContext(), movieList);
        list.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        movieList.clear();
        fetchCateData();
        swipeRefreshLayout.setColorSchemeResources(R.color.logo_color,
                android.R.color.holo_green_light,
                R.color.logo_color,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        fetchCateData();
                    }
                },200);
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (list == null || list.getChildCount() == 0) ?
                                0 : list.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
    }

    public void fetchCateData() {

        check = fn.isNetworkAvailable(getApplicationContext());

        if (check) {
            if(refreshcheck)
            swipeRefreshLayout.setRefreshing(true);
            JsonArrayRequest movieReq = new JsonArrayRequest(cat_url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            pDialog.dismiss();
                            // Parsing json

                            movieList.clear();
                            //  Hash_file_maps1.clear();
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject obj = response.getJSONObject(i);

                                        if (i < 3) {
                                            Hash_file_maps1 = new HashMap<String, List<String>>();
                                            List<String> fieldData= new ArrayList<>();
                                            fieldData.add(obj.getString("postId"));
                                            fieldData.add(obj.getString("image"));
                                            sliderLayout = (SliderLayout) findViewById(slider);
                                            Hash_file_maps1.put(obj.getString("title"), fieldData);
                                            for (String name : Hash_file_maps1.keySet()) {

                                               List<String> listdata = Hash_file_maps1.get(name);
                                                String post_id=listdata.get(0);
                                                String image=listdata.get(1);
                                                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                                textSliderView
                                                        .description(name)
                                                        .image(image)
                                                        //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                                        .setOnSliderClickListener(Category.this);
                                                textSliderView.bundle(new Bundle());
                                                textSliderView.getBundle()
                                                        .putString("post_Id", post_id.trim());
                                                sliderLayout.addSlider(textSliderView);
                                            }
                                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                            sliderLayout.setDuration(2000);
                                            sliderLayout.addOnPageChangeListener(Category.this);

                                        }

                                        if (i >= 3) {
                                            Movie movie = new Movie();
                                            movie.setID(obj.getString("postId"));
                                            movie.setTitle(obj.getString("title").toString());
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setDate(obj.getString("date"));

                                            // adding movie to movies array
                                            movieList.add(movie);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                refreshcheck=true;
                            }

                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), Sinlgepage.class);
                                    String postid = movieList.get(position).getId();
                                    intent.putExtra("postid", postid);
                                    intent.putExtra("latestnews", appName);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                                }
                            });
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                    error.printStackTrace();
                }
            });

            // Adding request to request queue
            AppController.getInstance(getApplicationContext()).addToRequestQueue(movieReq);
        } else {

            Toast.makeText(getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(getApplicationContext(), Sinlgepage.class);
        String postid = slider.getBundle().get("post_Id").toString();
        intent.putExtra("postid", postid);
        intent.putExtra("latestnews", appName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
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