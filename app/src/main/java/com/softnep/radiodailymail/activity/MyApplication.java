package com.softnep.radiodailymail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import com.activeandroid.app.Application;
import com.facebook.stetho.Stetho;
import com.flurry.android.FlurryAgent;
import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.UIutils.FontsOverride;
import com.thefinestartist.finestwebview.FinestWebView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ADMIN on 2017-11-07.
 */

public class MyApplication extends MultiDexApplication {

    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this,"MONOSPACE","fonts/Ubuntu-R.ttf");

        app = getApplicationContext();
        Hawk.init(app).build();

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .withLogLevel(Log.VERBOSE)
                .build(this, getString(R.string.FLURRY_API_KEY));

       // Configuration config=new Configuration.Builder(this).setDatabaseName("radio.db").create();
        //ActiveAndroid.initialize(config);

        ActiveAndroid.initialize(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static void showWebView(String news_url){
        new FinestWebView.Builder(app)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.hold, R.anim.hold, R.anim.slide_right_out)
                .statusBarColorRes(R.color.colorPrimary)
                .toolbarColorRes(R.color.colorPrimary)
                .disableIconForward(true)
                .urlColorRes(R.color.white)
                .progressBarColorRes(R.color.red)
                .titleColorRes(R.color.white)
                .menuColorRes(R.color.white)
                .backPressToClose(false)
                .swipeRefreshColorRes(R.color.red)
                .updateTitleFromHtml(true)
                .iconDefaultColorRes(R.color.white)
                .show(news_url);
    }


    public static String getDiff(String givenDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

        Date postDate = null;

        try {
            postDate = formatter.parse(givenDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Date today = new Date();

        long diff = today.getTime() - postDate.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        int diffDays = (int) diffHours / 24;
        int diffMonths = diffDays / 30;
        int diffYears = diffMonths / 12;


        if (diffSeconds < 60) {
            return "1s";
        } else if (diffSeconds == 60) {
            return "1m";
        } else if (diffSeconds > 60) {
            if (diffMinutes < 60) {
                return diffMinutes + "m";
            } else if (diffMinutes == 1) {
                return "1m";
            } else if (diffMinutes == 60) {
                return "1h";
            } else if (diffMinutes > 60) {
                if (diffHours == 1) {
                    return "1h";
                } else if (diffHours < 24) {
                    return diffHours + "h";
                } else if (diffHours == 24) {
                    return "1d";
                } else if (diffHours > 24) {
                    if (diffDays == 1) {
                        return "1d";
                    } else if (diffDays < 30) {
                        return diffDays + "d";
                    } else if (diffDays == 30) {
                        return "1mo";
                    } else if (diffDays > 30) {
                        if (diffMonths == 1) {
                            return "1mo";
                        } else if (diffMonths < 12) {
                            return diffMonths + "mo";
                        } else if (diffMonths == 12) {
                            return "1y";
                        } else if (diffMonths > 12) {
                            if (diffYears == 1) {
                                return "1y";
                            } else if (diffYears < 10) {
                                return diffYears + "y";
                            } else if (diffYears == 10) {
                                return "1dec";
                            } else {
                                return "> 1dec";
                            }
                        }
                    }

                }

            }
        }
        return givenDate;
    }

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public static boolean shouldLoadNewNews(long currTime, long prevTime){
        return (currTime - prevTime) > 15*60*1000;
    }
}
