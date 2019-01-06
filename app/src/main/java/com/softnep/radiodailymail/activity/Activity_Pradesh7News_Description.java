package com.softnep.radiodailymail.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannedString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.model.Pradesh7NewsReceiveParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

/**
 * Created by ADMIN on 2017-11-13.
 */

public class Activity_Pradesh7News_Description extends AppCompatActivity{

    TextView title,description,datetime;
    ImageView news_image;
    Button btnWeb,btnShare;
    CoordinatorLayout coordinatorLayout;
    Context context;
    private static final String TAG = "Activity_Pradesh7News_D";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pradesh7news_description);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=getApplicationContext();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        news_image= (ImageView) findViewById(R.id.pradeshnews_image_desc);
        title=(TextView) findViewById(R.id.pradeshnews_title_desc);
        description=(TextView) findViewById(R.id.pradeshnews_long_description);
        datetime=(TextView) findViewById(R.id.pradeshnews_datetime_desc);
        btnWeb=(Button) findViewById(R.id.btnViewWeb);
        btnShare=(Button) findViewById(R.id.btnSharePost);


        Intent intent=getIntent();
        final Pradesh7NewsReceiveParams.News39Bean allnews=(Pradesh7NewsReceiveParams.News39Bean) intent.getSerializableExtra("obj");

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
        Intent intent=new Intent(Activity_Pradesh7News_Description.this,Activity_NavigationDrawer.class);
        intent.putExtra("pradesh_back",2);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            Intent intent=new Intent(Activity_Pradesh7News_Description.this,Activity_NavigationDrawer.class);
            intent.putExtra("pradesh_back",2);
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

