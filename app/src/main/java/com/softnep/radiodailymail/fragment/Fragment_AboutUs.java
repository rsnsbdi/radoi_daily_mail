package com.softnep.radiodailymail.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.model.AboutUsReceiveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-08.
 */

public class Fragment_AboutUs extends Fragment {

    View rootView;
    ConnectionDetector cd;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout refreshLayout;
    List<AboutUsReceiveParams.PageBean> about_us=new ArrayList<>();
    private static final String TAG = "Fragment_AboutUs";
    TextView aboutText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_aboutus,container,false);

        aboutText=(TextView) rootView.findViewById(R.id.txtAbout);
        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));
        cd=new ConnectionDetector(getContext());

        if(cd.isNetworkAvailable() || cd.isDataAvailable()){
            getAboutUsData();
        }

        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
          //  refreshLayout.setRefreshing(false);
          /*  Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(3000)
                    .show();*/
            Toasty.error(getContext(),"No Internet Connection!! Please Connect to WIFI or Mobile Data!", 400).show();
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAboutUsData();
                    }
                }, 3000);

            }

        });

        return rootView;
    }

    public void getAboutUsData(){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<AboutUsReceiveParams> call=networkClient.getAboutUs();

        call.enqueue(new Callback<AboutUsReceiveParams>() {
            @Override
            public void onResponse(Call<AboutUsReceiveParams> call, Response<AboutUsReceiveParams> response) {
                AboutUsReceiveParams aboutUsReceiveParams=response.body();
                about_us=new ArrayList<AboutUsReceiveParams.PageBean>(aboutUsReceiveParams.getPage());
                aboutText.setText(about_us.get(0).getDescription());
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<AboutUsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: About uS"+ t.toString());
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
