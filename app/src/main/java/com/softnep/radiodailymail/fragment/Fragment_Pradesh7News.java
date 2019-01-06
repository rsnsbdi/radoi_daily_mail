package com.softnep.radiodailymail.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.adapter.Adapter_Pradesh7News;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.Pradesh7NewsReceiveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-07.
 */

public class Fragment_Pradesh7News extends Fragment {

    View rootView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    RecyclerView recyclerView;
    Adapter_Pradesh7News adapterPradesh7News;
    List<Pradesh7NewsReceiveParams.News39Bean> pradesh7news_list=new ArrayList<>();
    List<Pradesh7NewsReceiveParams.News39Bean> local_list=new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime = 0;
    private static final String TAG = "Fragment_Pradesh7News";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_pradesh7news,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        recyclerView=(RecyclerView) rootView.findViewById(R.id.pradesh7news_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);

        adapterPradesh7News=new Adapter_Pradesh7News(recyclerView,pradesh7news_list,getContext());

        adapterPradesh7News.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
              //  Toast.makeText(getContext(), "asdf load", Toast.LENGTH_SHORT).show();
                page++;
                if(pradesh7news_list.size() > 0 && pradesh7news_list.get(pradesh7news_list.size()-1)!=null){

                    if(!adapterPradesh7News.isLoading()){
                        pradesh7news_list.add(null);
                        adapterPradesh7News.setLoading();
                        adapterPradesh7News.notifyItemInserted(pradesh7news_list.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPradesh7news(page);
                    }
                },20);
            }
        });

        recyclerView.setAdapter(adapterPradesh7News);
        recyclerView.setItemAnimator(null);

        //check if data is stored in local storage or not
        //if yes display that data otherwise get data from api

        cd=new ConnectionDetector(getContext());

        if (!Hawk.contains("firstrun_pradesh")){

            getPradesh7news(page);

        }else {
            getNewsfromStorage();

            if (local_list.size() <= 0){
                getPradesh7news(page);
            }

        }

        //check if last data stored time is greater that 15 min ago
            //if yes, make server request
            //else, do nothing
        //Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
            //  Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
                getPradesh7news(page);
            }
        }


        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
           /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
          //  getPradesh7news(page);
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(cd.isNetworkAvailable() || cd.isDataAvailable()){
                                getPradesh7news(1);
                                page=1;
                            }else{

                                Toasty.error(getContext(),"No Internet Connection!! Please Connect to WIFI or Mobile Data",300).show();
                                /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                                        .setDuration(2000)
                                        .show();*/
                            }

                        }
                    }, 2000);

            }

        });

        return rootView;
    }

    public void getPradesh7news(int currentPage){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<Pradesh7NewsReceiveParams> call=networkClient.getPradesh7News(currentPage);
        Log.d(TAG, "getPradesh7news: get url"+call.request().toString());

        call.enqueue(new Callback<Pradesh7NewsReceiveParams>() {
            @Override
            public void onResponse(Call<Pradesh7NewsReceiveParams> call, Response<Pradesh7NewsReceiveParams> response) {
                Log.d(TAG, "onResponse: Body "+response.body().toString());
                Pradesh7NewsReceiveParams pradesh_news=response.body();
                Hawk.put("firstrun_pradesh", false);
                //get current timestamp in a string
                //String updated = "current time"
                newsUpdatedTime = MyApplication.getCurrentTime();
                List<Pradesh7NewsReceiveParams.News39Bean> tempList=new ArrayList<Pradesh7NewsReceiveParams.News39Bean>();
                if(page > 1) {

                    if (pradesh7news_list.size() > 0) {
                      /*  if (tempList.get(tempList.size() - 1) == null)
                            tempList.remove(tempList.size() - 1);*/

                        if (pradesh7news_list.get(pradesh7news_list.size() - 1) == null) {
                            pradesh7news_list.remove(pradesh7news_list.size() - 1);
                          //  adapterPradesh7News.setLoaded();
                            adapterPradesh7News.notifyItemRemoved(pradesh7news_list.size() - 1);
                        }
                    }

                    tempList = pradesh7news_list;
                }

                    refreshLayout.setRefreshing(false);
                    adapterPradesh7News.setLoaded();
                    Log.d(TAG, "onResponse: Api Data" + pradesh_news.toString());
                    pradesh7news_list = pradesh_news.getNews39();
                    int newsSize=pradesh7news_list.size();
                    Log.d(TAG, "onResponse: List Data " + pradesh7news_list);
                    tempList.addAll(pradesh7news_list);
                    pradesh7news_list = tempList;

                    if(newsSize > 0 )
                        adapterPradesh7News.notifyItemInserted(pradesh7news_list.size()-newsSize);


                for (Pradesh7NewsReceiveParams.News39Bean n: pradesh7news_list) {
                    n.setNews_time(newsUpdatedTime);
                }

                if(pradesh7news_list.size() > 0 && page==1) {
                    DbHelper.deleteTable(Pradesh7NewsReceiveParams.News39Bean.class);
                    ActiveAndroid.beginTransaction();
                    try {
                        for (Pradesh7NewsReceiveParams.News39Bean b : pradesh7news_list) {
                            b.save();

                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }

                     adapterPradesh7News.add(pradesh7news_list);
                     adapterPradesh7News.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Pradesh7NewsReceiveParams> call, Throwable t) {
                    Log.d(TAG, "onFailure: Pradesh " + t.toString());
               /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();*/
                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();

               /* if(local_list.size() > 0){
                    adapterPradesh7News.add(local_list);
                }*/
               /* if(pradesh7news_list.size() > 0){
                    adapterPradesh7News.add(pradesh7news_list);
                }*/
                /* if (pradesh7news_list.size() > 0) {

                    adapterPradesh7News=new Adapter_Pradesh7News(recyclerView,(List<Pradesh7NewsReceiveParams.News39Bean>) DbHelper.getRecordsDb(Pradesh7NewsReceiveParams.News39Bean.class),getContext());
                    adapterPradesh7News.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterPradesh7News);
               }*/

                if(pradesh7news_list.size() > 0){
                 if(pradesh7news_list.get(pradesh7news_list.size()-1)==null) {
                     pradesh7news_list.remove(pradesh7news_list.size() - 1);
                     adapterPradesh7News.setLoaded();
                     adapterPradesh7News.notifyItemInserted(pradesh7news_list.size() - 1);

                 }
                }

                adapterPradesh7News.setLoaded();
                if (refreshLayout.isRefreshing() && refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }

            }
        });

    }

    private void getNewsfromStorage(){

        local_list=(List<Pradesh7NewsReceiveParams.News39Bean>) DbHelper.getRecordsDb(Pradesh7NewsReceiveParams.News39Bean.class);
        if(local_list.size() > 0){
            pradesh7news_list=local_list;
            adapterPradesh7News.add(pradesh7news_list);
            adapterPradesh7News.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getPradesh7news(page);
        }

    }

}
