package com.softnep.radiodailymail.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.model.NotificationReceiveParams;
import com.softnep.radiodailymail.model.NotificationSendParams;
import com.softnep.radiodailymail.model.TokenSaveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-15.
 */

public class Activity_Settings extends AppCompatPreferenceActivity {

    Context context;
    String LocalDeviceID="";
    ConnectionDetector cd;
    private CheckBoxPreference checkBoxPreference;
    private SwitchPreference switchPreference;
    private MultiSelectListPreference newsList;
    SharedPreferences sharedPreferences;

    private static final String TAG = "Activity_Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        Hawk.init(context).build();

        cd=new ConnectionDetector(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        switchPreference = new SwitchPreference(this);
        switchPreference = (SwitchPreference) getPreferenceManager().findPreference("breaking_news");
        switchPreference.setDefaultValue(false);

        newsList=new MultiSelectListPreference(this);
        newsList=(MultiSelectListPreference) getPreferenceManager().findPreference("news_category");

        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Boolean check=(Boolean) o;
                
                if(check){
                    Hawk.put("news_key",9);
                   // Snackbar.make(getWindow().getDecorView(),"Breaking News Notification ON",Snackbar.LENGTH_LONG).show();
                    Toasty.info(getApplicationContext(),"Breaking News Notification ON",Toast.LENGTH_LONG).show();
                  //  Toast.makeText(context, "Switch is "+o.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Hawk.delete("news_key");
                    Toasty.info(getApplicationContext(),"Breaking News Notification OFF",Toast.LENGTH_LONG).show();
                   //Snackbar.make(getWindow().getDecorView(),"Breaking News Notification OFF",Snackbar.LENGTH_LONG).show();
                   // Toast.makeText(context, "Switch is "+o.toString(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


            newsList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                   // String key=o.toString();
                   // Toast.makeText(context, "Key "+key, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

    }


    public void callRemoteApiToSetNewsPref(){
        Set<String> selections=sharedPreferences.getStringSet("news_category",null);
        String[] selected=selections.toArray(new String[] {});
        String ids = "";
        for(int i=0;i<selected.length;i++) {
            ids += selected[i] + ", ";
            Log.d(TAG, "onPreferenceChange: "+ ids);
        }

        //obtain device id from local storage
        List<TokenSaveParams.DeviceTokenList> list=new ArrayList<>();

        list=((List<TokenSaveParams.DeviceTokenList>) DbHelper.getRecordsDb(TokenSaveParams.DeviceTokenList.class));
        if(list.size() > 0) {
            LocalDeviceID = list.get(0).getDevice_token();
        }
        Log.d(TAG, "callRemoteApiToSetNewsPref: "+LocalDeviceID);
       // Toast.makeText(context,"Device ID"+LocalDeviceID.toString(),Toast.LENGTH_LONG).show();

        //call to api with both deviceId and category ids

        if(cd.isDataAvailable() || cd.isNetworkAvailable()) {
            NetworkClient networkClient = ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
            NotificationSendParams sendParams = new NotificationSendParams();
            Call<NotificationReceiveParams> call = networkClient.sendNotification(sendParams);

            sendParams.setCategory_id(ids);
            sendParams.setDevice_id(LocalDeviceID);

            call.enqueue(new Callback<NotificationReceiveParams>() {
                @Override
                public void onResponse(Call<NotificationReceiveParams> call, Response<NotificationReceiveParams> response) {
                    NotificationReceiveParams receiveParams = response.body();
                    List<NotificationReceiveParams.MessageBean> list = new ArrayList<NotificationReceiveParams.MessageBean>(receiveParams.getMessage());

                    // if(list.size() > 0){
                    // Toast.makeText(Activity_Settings.this, "Device ID and News Category ID Sent Successfully!!!!", Toast.LENGTH_SHORT).show();
                  //  Toasty.success(Activity_Settings.this, "Device ID and News Category ID Sent Successfully!!!!", Toast.LENGTH_LONG, true).show();
                   /* Snackbar.make(getWindow().getDecorView(),"Device ID and News Category ID Sent Successfully!!!!", Snackbar.LENGTH_LONG)
                            .setDuration(2000)
                            .show();*/
                    //  }
                }

                @Override
                public void onFailure(Call<NotificationReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: Settings"+t.toString());
                }
            });
        }
        if(!cd.isDataAvailable() || !cd.isNetworkAvailable()){
            Log.d(TAG, "callRemoteApiToSetNewsPref: No Internet");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callRemoteApiToSetNewsPref();
        Intent intent=new Intent(Activity_Settings.this,Activity_NavigationDrawer.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            callRemoteApiToSetNewsPref();
            Intent intent=new Intent(Activity_Settings.this,Activity_NavigationDrawer.class);
            startActivity(intent);
            this.finish();

        }
        return true;
    }
}
