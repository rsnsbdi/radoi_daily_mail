package com.softnep.radiodailymail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.model.PoliticalNewsReceiveParams;
import com.squareup.picasso.Picasso;

/**
 * Created by ADMIN on 2017-11-13.
 */

public class Activity_PoliticalNews_Description extends AppCompatActivity {

    TextView title, description,datetime;
    ImageView news_image;
    Button btnWeb,btnShare;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicalnews_description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        news_image = (ImageView) findViewById(R.id.politicalnews_image_desc);
        title = (TextView) findViewById(R.id.politicalnews_title_desc);
        description = (TextView) findViewById(R.id.politicalnews_long_description);
        datetime=(TextView) findViewById(R.id.politicalnews_datetime_desc);
        btnWeb = (Button) findViewById(R.id.btnViewWeb);
        btnShare=(Button) findViewById(R.id.btnSharePost);


        Intent intent = getIntent();
        final PoliticalNewsReceiveParams.News26Bean allnews = (PoliticalNewsReceiveParams.News26Bean) intent.getSerializableExtra("obj");

        title.setText(Html.fromHtml(allnews.getTitle().replace("\\n","")));
        description.setText(Html.fromHtml(allnews.getDescription().replace("\\n","<br />")));
        datetime.setText(Html.fromHtml(allnews.getDate().replace("\\n","")));
        Picasso.with(getApplicationContext())
                .load(allnews.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .into(news_image);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Activity_PoliticalNews_Description.this,Activity_NavigationDrawer.class);
        intent.putExtra("political_back",5);
        startActivity(intent);
        this.finish();
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Fragment_PoliticalNews()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            Intent intent=new Intent(Activity_PoliticalNews_Description.this,Activity_NavigationDrawer.class);
            intent.putExtra("political_back",5);
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

