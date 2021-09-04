package me.shagor.ctnewsbd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sinlgepage extends AppCompatActivity {


    TextView news_tiltes,news_date,news_content,sharelink;
    ImageView ivBasicImage;
    String server_url = "";
    private Context ctx;
    Boolean check;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinlgepage);

        //Admob
        MobileAds.initialize(this, "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String appName = getIntent().getExtras().getString("latestnews");
        setTitle(appName);

        Functions fn = new Functions();
        check=fn.isNetworkAvailable(getApplication());
        if(check) {
            // Type casting element ID
            news_tiltes = (TextView) findViewById(R.id.the_title);
            news_date = (TextView) findViewById(R.id.the_date);
            news_content = (TextView) findViewById(R.id.the_content);
            sharelink = (TextView) findViewById(R.id.sharelink);
            ivBasicImage = (ImageView) findViewById(R.id.imageView);

            String postid = getIntent().getExtras().getString("postid");
            String singlepostURL = Config.englishSinglePostURL + postid;
            // news_tiltes.setText(set_titles);
            // news_date.setText(set_dates);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(singlepostURL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject obj = response.getJSONObject(i);
                            news_tiltes.setText(obj.getString("title"));
                            sharelink.setText(obj.getString("permalink"));
                            Picasso.with(getApplicationContext()).load(obj.getString("image")).fit().centerCrop()
                                    .placeholder(R.drawable.error)
                                    .error(R.drawable.error)
                                    .into(ivBasicImage);
                            news_content.setText(obj.getString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
        }
        else {
            Toast.makeText(getApplication(),"Please check your internet connection.",Toast.LENGTH_LONG).show();
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
            case R.id.sharelink:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = sharelink.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"CTNEWSBD");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Where are you want to share?"));
                return true;

        }
        return true;
    }

}
