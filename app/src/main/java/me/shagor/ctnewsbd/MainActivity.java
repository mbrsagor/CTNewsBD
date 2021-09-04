package me.shagor.ctnewsbd;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   // Expantable Menu
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    TextView expantable_listTitle,expandedListItem;

    //Bangla font
     Typeface externalFont;

    // Crete Object
    TabLayout my_tab;
    ViewPager my_view_pager;
    //calling Shared Preferecnce object
    SharedPreference SP = new SharedPreference();
    String version="বাংলা";
    String changeversion;

    // header dropdown menu spinner
    Spinner menu_spinner;
    private boolean isSpinnerTouched = false;
    ArrayAdapter<String> SpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Share-Preference
        version=SP.getValue(getApplicationContext());
        //Toast.makeText(getApplicationContext(),version,Toast.LENGTH_LONG).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Banlga font typecasting
        externalFont = Typeface.createFromAsset(getAssets(),"fonts/Siyamrupali.ttf");
        expantable_listTitle =(TextView) findViewById(R.id.listTitle) ;
        expandedListItem =(TextView) findViewById(R.id.expandedListItem) ;

//        expantable_listTitle.setTypeface(externalFont);
//        expandedListItem.setTypeface(externalFont);

        // Type casting
        my_tab = (TabLayout) findViewById(R.id.my_tab);
        my_view_pager = (ViewPager) findViewById(R.id.my_view_pager);
        addingToTab(my_view_pager);
        my_tab.setupWithViewPager(my_view_pager);

        // Expantable Menu
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                String menuname=expandableListTitle.get(groupPosition).toString().trim();
                if(menuname.equals("National")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("3",menuname);
                }else if(menuname.equals("POLITICS")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("4",menuname);
                }else if(menuname.equals("INTERNATIONAL")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("6",menuname);
                }else if(menuname.equals("SPORTS")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("7",menuname);
                }else   if(menuname.equals("ENTERTAINMENT")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("8",menuname);
                }else   if(menuname.equals("Opinion")){
                    Toast.makeText(getApplication(),expandableListTitle.get(groupPosition).toString(),Toast.LENGTH_SHORT).show();
                   IntentShow("42",menuname);
                }

            }
        });

        // Action bar spinner drop-down menu
        menu_spinner = (Spinner) findViewById(R.id.menu_spinner);
        SpinnerAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.menu_spinner,
                getResources().getStringArray(R.array.languages2));
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu_spinner.setAdapter(SpinnerAdapter);

        menu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerTouched) {
                    changeversion=parent.getItemAtPosition(position).toString();
                    changeversion=changeversion.trim().toLowerCase();
                    if (changeversion.equals("english")) {
                        SP.removeValue(getApplicationContext());
                        Toast.makeText(getApplicationContext(),"You have selected English",Toast.LENGTH_LONG).show();
                        SP.save(getApplicationContext(),changeversion);
                    }else {
                        Toast.makeText(getApplicationContext(),"You have selected বাংলা",Toast.LENGTH_LONG).show();
                        SP.save(getApplicationContext(),changeversion);
                    }Refresh();
                }else {
                    isSpinnerTouched=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Adding to tab
    public void addingToTab(ViewPager vp) {
        TabFragment tfm = new TabFragment(getSupportFragmentManager());

        //if(version.equals("english")){
//            tfm.addingFragment(new Selectednews(),"নির্বাচিত");
//            tfm.addingFragment(new Lastnews(),"সর্বশেষ");
//            tfm.addingFragment(new Popularnews(),"র্বাধিক পঠিত");
//            tfm.addingFragment(new Othernews(),"আলোচিত");
//        }else {
//            tfm.addingFragment(new Selectednews(), "Latest");
//            tfm.addingFragment(new Lastnews(), "Last News");
//            tfm.addingFragment(new Popularnews(), "Popular");
//            tfm.addingFragment(new Othernews(), "Others");
//        }
        tfm.addingFragment(new Featured(), "FEATURED");
        tfm.addingFragment(new Recent(), "RECENT");
        tfm.addingFragment(new Popularnews(), "POPULAR");

        vp.setAdapter(tfm);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This class for fragment Tab-view
    public class TabFragment extends FragmentPagerAdapter{

        private final List<Fragment> fragmentList = new ArrayList<Fragment>();
        private final List<String> fragmentTitle = new ArrayList<String>();

        public TabFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addingFragment(Fragment fragment_names,String fragment_titles){
            fragmentList.add(fragment_names);
            fragmentTitle.add(fragment_titles);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    // Refresh animation method
    public void Refresh(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        finish();
        startActivity(intent);
    }

public void IntentShow(String catId,String appName){
    Intent intent = new Intent(getApplication(), Category.class);
    intent.putExtra("catId", catId);
    intent.putExtra("app_name", appName);
    startActivity(intent);
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
}
}