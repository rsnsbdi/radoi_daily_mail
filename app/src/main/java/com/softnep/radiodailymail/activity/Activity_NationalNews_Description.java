package com.softnep.radiodailymail.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;


import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.model.NationalNewsReceiveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-13.
 */

public class Activity_NationalNews_Description extends AppCompatActivity {

    TextView title, description, datetime, txtTitle;
    ImageView news_image;
    Button btnWeb, btnShare;
    CoordinatorLayout coordinatorLayout;
    private int news_id, category_id;
    NationalNewsReceiveParams.News37Bean allnews;
    ProgressDialog progress;
    private static final String TAG = "Activity_NationalNews_D";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationalnews_description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        txtTitle = (TextView) findViewById(R.id.titleText);
        news_image = (ImageView) findViewById(R.id.nationalnews_image_desc);
        title = (TextView) findViewById(R.id.nationalnews_title_desc);
        description = (TextView) findViewById(R.id.nationalnews_long_description);
        datetime = (TextView) findViewById(R.id.nationalnews_datetime_desc);
        btnWeb = (Button) findViewById(R.id.btnViewWeb);
        btnShare = (Button) findViewById(R.id.btnSharePost);

        showViews(false);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isNotification", false) == true) {

            //get news id and category id from intent
            news_id = intent.getIntExtra("news_id", 0);
            category_id = intent.getIntExtra("category_id", 0);

            //create category name string array
            //create category id iint array from resource
            String[] categoryNames = getResources().getStringArray(R.array.news_category_items);
            int[] categoryIds = getResources().getIntArray(R.array.all_news_category_id);

            int index = 0;
            for (int i=0; i<categoryIds.length; i++){
                if (categoryIds[i] == category_id){
                    index = i;
                    break;
                }
            }

            String category_name = categoryNames[index];

            txtTitle.setText(category_name);

            //new network call
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            Call<NationalNewsReceiveParams> call = networkClient.getNotificationNews(news_id);

            progress=new ProgressDialog(Activity_NationalNews_Description.this);
            progress.setMessage("Loading.....");
            progress.setCancelable(true);
            progress.show();

            Log.d(TAG, "getNationalNews: " + call.request().url());


            call.enqueue(new Callback<NationalNewsReceiveParams>() {
                @Override
                public void onResponse(Call<NationalNewsReceiveParams> call, Response<NationalNewsReceiveParams> response) {
                    NationalNewsReceiveParams national = response.body();
                    List<NationalNewsReceiveParams.News37Bean> newsList = new ArrayList<NationalNewsReceiveParams.News37Bean>(national.getNews37());
                    Log.d(TAG, "onResponse: " + newsList.toString());
                    allnews = newsList.get(0);
                    updateUI();
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<NationalNewsReceiveParams> call, Throwable t) {
                    Snackbar.make(coordinatorLayout, "Error getting news.", 5000).show();
                    Log.d(TAG, "onFailure: " + t.toString());
                    progress.dismiss();
                }
            });

        } else {
            allnews = (NationalNewsReceiveParams.News37Bean) intent.getSerializableExtra("obj");
            updateUI();
        }

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String news_url = allnews.getWeb_link();
                MyApplication.showWebView(news_url);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String WebLink=allnews.getWeb_link();
                sharePost(WebLink);
            }
        });
    }

    private void showViews(boolean isVisible){
        if(isVisible){
            title.setVisibility(View.VISIBLE);
            datetime.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            news_image.setVisibility(View.VISIBLE);
            btnWeb.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);
        }else{
            title.setVisibility(View.GONE);
            datetime.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            news_image.setVisibility(View.GONE);
            btnWeb.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
        }


    }

    private void updateUI() {
        title.setText(Html.fromHtml(allnews.getTitle().replace("\\n", "")));
        description.setText(Html.fromHtml(allnews.getDescription().replace("\\n", "<br />")));
        datetime.setText(Html.fromHtml(allnews.getDate().replace("\\n", "")));
        Picasso.with(getApplicationContext())
                .load(allnews.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .into(news_image);

        showViews(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Activity_NationalNews_Description.this,Activity_NavigationDrawer.class);
        intent.putExtra("national_back",3);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent intent=new Intent(Activity_NationalNews_Description.this,Activity_NavigationDrawer.class);
            intent.putExtra("national_back",3);
            startActivity(intent);
            this.finish();
        }
        return true;
    }

    public void sharePost(String postUrl){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postUrl);
        startActivity(Intent.createChooser(shareIntent, "Share Using"));

    }
}
