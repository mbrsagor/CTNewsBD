package me.shagor.ctnewsbd;

import android.app.ProgressDialog;
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

/**
 * Created by shagor on 6/19/2017.
 */

public class Selectednews extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = Config.englishSelecetdURL;
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int offSet = 0;
    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;
    Boolean check;
    Functions fn;
    DatabaseHelper dbHealper;
    private long p;

    AdView adView;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.selected_layout,container,false);

        //Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        NetworkImageView thumbNail = (NetworkImageView)view.
                findViewById(R.id.thumbnail);
        TextView postid = (TextView)view. findViewById(R.id.postid);
        TextView title = (TextView)view. findViewById(R.id.title);
        TextView date = (TextView)view. findViewById(R.id.date);

        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);
        Hash_file_maps = new HashMap<String, String>();
        //Objects
        dbHealper = new DatabaseHelper(getContext());
        fn = new Functions();
        fetchData();
        Cursor c = dbHealper.getAllData("latest");

        int p=0;

        if(c.moveToFirst())
        {

            do
            {
                if (p < 3) {
                    Hash_file_maps = new HashMap<String, String>();

                    sliderLayout = (SliderLayout)view.findViewById(R.id.slider);
                    Hash_file_maps.put(c.getString(2),c.getString(1) + "#" + c.getString(3));
                    String name1[] = {};
                    for (String name : Hash_file_maps.keySet()) {

                        name1 = Hash_file_maps.get(name).split("#");

                        // Toast.makeText(getApplication(),name1[1],Toast.LENGTH_LONG).show();
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        textSliderView
                                .description(name)
                                .image(name1[1])
                                //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(Selectednews.this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("post_Id", name1[0].trim());
                        sliderLayout.addSlider(textSliderView);
                    }
                }
                else{
                    Movie movie = new Movie();
                    movie.setID(c.getString(1));
                    movie.setTitle(c.getString(2));
                    movie.setThumbnailUrl( c.getString(3));
                    movie.setDate( c.getString(4));

                    movieList.add(movie);
                }

                p++;


            }while(c.moveToNext());
        }

        listView = (ListView)view.findViewById(R.id.list);
        adapter = new CustomListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), Sinlgepage.class);
                String postid = movieList.get(position).getId();
                intent.putExtra("postid", postid);
                intent.putExtra("latestnews", "Recent");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
            }
        });

        c.close();
        // fetchData();
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
                        fetchData();
                    }
                },200);
            }
        });

listView.setOnScrollListener(new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition =
                (listView == null || listView.getChildCount() == 0) ?
                        0 : listView.getChildAt(0).getTop();
        swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
    }
});

//        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
//            @Override
//            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                return listView.getFirstVisiblePosition() > 0 ||
//                        listView.getChildAt(0) == null ||
//                        listView.getChildAt(0).getTop() < 0;
//            }
//
//        });
        return view;
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void fetchData() {

        check=fn.isNetworkAvailable(getContext());

        if(check) {
            swipeRefreshLayout.setRefreshing(true);
            String url = Config.englishSinglePostURL + offSet;
            dbHealper.deleteallData("latest");
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            movieList.clear();
                            Hash_file_maps.clear();

                            // Log.d(TAG, response.toString());
                        //    hidePDialog();
                            if (response.length() > 0) {
                                String tittle1;
                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        if (i < 3) {
                                            Hash_file_maps = new HashMap<String, String>();

                                            sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
                                            Hash_file_maps.put(obj.getString("title"), obj.getString("postId") + "#" + obj.getString("image"));
                                            String name1[]= {};
                                            for (String name : Hash_file_maps.keySet()) {

                                                name1 = Hash_file_maps.get(name).split("#");
                                                tittle1=name;
                                                // Toast.makeText(getApplication(),name1[1],Toast.LENGTH_LONG).show();
                                                TextSliderView textSliderView = new TextSliderView(getContext());
                                                textSliderView
                                                        .description(name)
                                                        .image(name1[1])
                                                        //  .setScaleType(BaseSliderView.ScaleType.Fit)
                                                        .setOnSliderClickListener(Selectednews.this);
                                                textSliderView.bundle(new Bundle());
                                                textSliderView.getBundle()
                                                        .putString("post_Id", name1[0].trim());
                                                sliderLayout.addSlider(textSliderView);
                                            }
                                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                                            sliderLayout.setDuration(2000);
                                            sliderLayout.addOnPageChangeListener(Selectednews.this);
                                            p= dbHealper.insertData("latest",obj.getString("postId"),obj.getString("title"),obj.getString("image"),obj.getString("date"));

                                        }
                                        if (i >= 3) {
                                            Movie movie = new Movie();
                                            movie.setID(obj.getString("postId"));
                                            movie.setTitle(obj.getString("title"));
                                            movie.setThumbnailUrl(obj.getString("image"));
                                            movie.setDate(obj.getString("date"));

                                            movieList.add(movie);
                                            p= dbHealper.insertData("latest",obj.getString("postId"),obj.getString("title"),obj.getString("image"),obj.getString("date"));
                                            offSet = 0;
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                String url = Config.englishSelecetdURL + offSet;
                            }
                            // notifying list adapter about data changes
                            adapter.notifyDataSetChanged();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getContext(), Sinlgepage.class);
                                    String postid = movieList.get(position).getId();
                                    intent.putExtra("postid", postid);
                                    intent.putExtra("latestnews", "Recent");
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                                }
                            });
                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //   hidePDialog();
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            // Adding request to request queue
            AppController.getInstance(getContext()).addToRequestQueue(movieReq);
        }
        else {

            Toast.makeText(getContext(),"Please check your internet connection.",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

        Intent intent = new Intent(getContext(), Sinlgepage.class);
        String postid = slider.getBundle().get("post_Id").toString();
        intent.putExtra("postid", postid);
        intent.putExtra("latestnews", "Recent");
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
