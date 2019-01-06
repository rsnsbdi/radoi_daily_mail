package com.softnep.radiodailymail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.fragment.Fragment_AboutUs;
import com.softnep.radiodailymail.fragment.Fragment_EconomicNews;
import com.softnep.radiodailymail.fragment.Fragment_EntertainmentNews;
import com.softnep.radiodailymail.fragment.Fragment_ForeignEmpForm;
import com.softnep.radiodailymail.fragment.Fragment_Home;
import com.softnep.radiodailymail.fragment.Fragment_InternationalNews;
import com.softnep.radiodailymail.fragment.Fragment_NationalNews;
import com.softnep.radiodailymail.fragment.Fragment_PoliticalNews;
import com.softnep.radiodailymail.fragment.Fragment_Pradesh7News;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.SoftKeyboardStateWatcher;
import com.softnep.radiodailymail.radioPlayer.Activity_Online_Radio;

import es.dmoral.toasty.Toasty;


public class Activity_NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    AdView adView;
    TextView titleText;
    private boolean doubleBackToExitPressedOnce = false;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    private static final String TAG = "Activity_NavigationDraw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        final FrameLayout frameLayout=(FrameLayout) findViewById(R.id.fragmentContainer);
        final ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();


        cd=new ConnectionDetector(getApplicationContext());
        titleText=(TextView) findViewById(R.id.toolbarText);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        adView=(AdView) findViewById(R.id.adView);
        final AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
              //  Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

              //  if(!cd.isDataAvailable() || !cd.isNetworkAvailable()){
                    adView.setVisibility(View.GONE);
               // }
                params.bottomMargin=0;
                frameLayout.setLayoutParams(params);
               // Toast.makeText(getApplicationContext(), "Ad failed to load!"+i, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onAdFailedToLoad: "+i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
              //  Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
               // Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

               // if(cd.isDataAvailable() || cd.isNetworkAvailable()){
                    adView.setVisibility(View.VISIBLE);
               // }
                params.bottomMargin=80;
                frameLayout.setLayoutParams(params);
              //  Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
            }
        });

        final SoftKeyboardStateWatcher softKeyboardStateWatcher=new SoftKeyboardStateWatcher(findViewById(R.id.fragmentContainer));
        softKeyboardStateWatcher.addSoftKeyboardStateListener(new SoftKeyboardStateWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardClosed() {
                adView.setVisibility(View.VISIBLE);
            }
        });


        if(cd.isDataAvailable() || cd.isNetworkAvailable()){
            adView.setVisibility(View.VISIBLE);
        }

        if(!cd.isDataAvailable() || !cd.isNetworkAvailable()){
            adView.setVisibility(View.GONE);
        }

        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Intent in=getIntent();
        int radioBack=in.getIntExtra("radio_back",20);
        int pradeshBack=in.getIntExtra("pradesh_back",21);
        int nationalBack=in.getIntExtra("national_back",22);
        int internationalBack=in.getIntExtra("international_back",23);
        int politicalBack=in.getIntExtra("political_back",24);
        int entertainmentBack=in.getIntExtra("entertainment_back",25);
        int economicBack=in.getIntExtra("economic_back",26);

        if(radioBack==8) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_Home()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            titleText.setText(getString(R.string.home_nepali));
        }

       if(pradeshBack==2){
           getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_Pradesh7News()).commit();
           navigationView.getMenu().getItem(0).setChecked(true);
           titleText.setText(getString(R.string.home_nepali));
       }

       if(nationalBack==3){
           getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_NationalNews()).commit();
           navigationView.getMenu().getItem(0).setChecked(true);
           titleText.setText(getString(R.string.home_nepali));
       }

        if(internationalBack==4){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_InternationalNews()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            titleText.setText(getString(R.string.home_nepali));
        }

       if(politicalBack==5){
           getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_PoliticalNews()).commit();
           navigationView.getMenu().getItem(0).setChecked(true);
           titleText.setText(getString(R.string.home_nepali));
       }

        if(entertainmentBack==6){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_EntertainmentNews()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            titleText.setText(getString(R.string.home_nepali));
        }

        if(economicBack==7){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_EconomicNews()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            titleText.setText(getString(R.string.home_nepali));
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_Home()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //drawer.setStatusBarBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        try{
            NavigationMenuView navigationMenuView=(NavigationMenuView) navigationView.getChildAt(0);
            navigationView.setItemIconTintList(null);
            navigationMenuView.addItemDecoration(new DividerItemDecoration(Activity_NavigationDrawer.this,DividerItemDecoration.VERTICAL));
        }catch (Exception e){
            e.printStackTrace();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    int count = getSupportFragmentManager().getBackStackEntryCount();
                    Log.d("OnBackChange", "onBackStackChanged: " + count);

                    if (count == 0) {
                        titleText.setText(getString(R.string.home_nepali));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: Count" +count);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count >= 1) {
            getSupportFragmentManager().popBackStack();
            try {
                navigationView.getMenu().getItem(0).setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;

            if(Activity_Online_Radio.playerStarted) {
                Snackbar.make(coordinatorLayout, "Cannot Exit App!! Radio is Playing!!", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();
            }
            else {
                Snackbar.make(coordinatorLayout, "Press BACK Again to Exit!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
              //  Activity_Online_Radio.closeActivity();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           Intent intent=new Intent(Activity_NavigationDrawer.this,Activity_Settings.class);
            startActivity(intent);
            finish();
        }

        if(id==R.id.action_online_radio){
            Intent intent=new Intent(Activity_NavigationDrawer.this, Activity_Online_Radio.class);
            startActivity(intent);
           // finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home: {
                getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Fragment_Home())
                        .addToBackStack("")
                        .commit();
                titleText.setText(getString(R.string.home_nepali));
                break;
            }

            case R.id.nav_pradesh7news: {
                getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Fragment_Pradesh7News())
                        .addToBackStack("")
                        .commit();
                titleText.setText(getString(R.string.pradesh7news_nepali));
                break;
            }

            case R.id.nav_foreignemp_form: {
                getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Fragment_ForeignEmpForm())
                        .addToBackStack("")
                        .commit();
                titleText.setText(getString(R.string.chaso_gunaso));
                break;
            }

            case R.id.nav_aboutus: {
                getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Fragment_AboutUs())
                        .addToBackStack("")
                        .commit();
                titleText.setText(getString(R.string.aboutus_nepali));
                break;
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        if(adView!=null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView!=null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(adView!=null){
            adView.destroy();
        }
        super.onDestroy();
    }

}
