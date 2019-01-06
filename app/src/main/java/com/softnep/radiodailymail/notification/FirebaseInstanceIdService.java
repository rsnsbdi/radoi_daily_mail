package com.softnep.radiodailymail.notification;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.google.firebase.iid.FirebaseInstanceId;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.model.NotificationReceiveParams;
import com.softnep.radiodailymail.model.NotificationSendParams;
import com.softnep.radiodailymail.model.TokenSaveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-14.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService{

    private static final String TAG = "FirebaseInstanceIdServi";

    @Override
    public void onTokenRefresh() {
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
        Log.d(TAG, "onTokenRefresh: Device ID "+refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

        //saving device id to local database
        List<TokenSaveParams.DeviceTokenList> tokenLists = new ArrayList<>();

            TokenSaveParams.DeviceTokenList saveToken = new TokenSaveParams.DeviceTokenList();
            saveToken.setDevice_token(token);
            tokenLists.add(saveToken);
            Log.d(TAG, "sendRegistrationToServer: Token List" +tokenLists.toString());

        if (tokenLists.size() > 0) {
        DbHelper.deleteTable(TokenSaveParams.DeviceTokenList.class);
        ActiveAndroid.beginTransaction();
        try {
            for (TokenSaveParams.DeviceTokenList b : tokenLists) {
                b.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }
        

        //call subscribe api url without category
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        NotificationSendParams notificationSendParams=new NotificationSendParams();
        Call<NotificationReceiveParams> call=networkClient.sendNotification(notificationSendParams);

        notificationSendParams.setDevice_id(token);
        notificationSendParams.setCategory_id("0");

        call.enqueue(new Callback<NotificationReceiveParams>() {
            @Override
            public void onResponse(Call<NotificationReceiveParams> call, Response<NotificationReceiveParams> response) {
                final NotificationReceiveParams notificationReceiveParams=response.body();

                List<NotificationReceiveParams.MessageBean> list=new ArrayList<NotificationReceiveParams.MessageBean>(notificationReceiveParams.getMessage());
               // Toast.makeText(FirebaseInstanceIdService.this, ""+notificationReceiveParams.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Firebase onResponse: "+list.toString());
                if(list.size() > 0){
                    DbHelper.deleteTable(NotificationReceiveParams.MessageBean.class);
                    ActiveAndroid.beginTransaction();
                    try{
                        for(NotificationReceiveParams.MessageBean b:list){
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    }finally {
                        ActiveAndroid.endTransaction();
                    }

                  //  Toast.makeText(FirebaseInstanceIdService.this, ""+notificationReceiveParams.getMessage().get(0).getResult(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NotificationReceiveParams> call, Throwable t) {
                Log.d(TAG, "Firebase onFailure: "+t.toString());
            }
        });

    }


}
