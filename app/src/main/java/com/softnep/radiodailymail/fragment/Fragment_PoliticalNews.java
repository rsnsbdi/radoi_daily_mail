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
import com.softnep.radiodailymail.adapter.Adapter_PoliticalNews;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.PoliticalNewsReceiveParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-07.
 */

public class Fragment_PoliticalNews extends Fragment{

    View rootView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    Adapter_PoliticalNews adapterPoliticalNews;
    List<PoliticalNewsReceiveParams.News26Bean> politics_news=new ArrayList<>();
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime = 0;
    List<PoliticalNewsReceiveParams.News26Bean> local_list=new ArrayList<>();
    private static final String TAG = "Fragment_PoliticalNews";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_politicsnews,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        recyclerView=(RecyclerView) rootView.findViewById(R.id.politicalnews_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);

        adapterPoliticalNews=new Adapter_PoliticalNews(recyclerView,politics_news,getContext());

        adapterPoliticalNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                if(politics_news.size() > 0 && politics_news.get(politics_news.size()-1)!=null){

                    if(!adapterPoliticalNews.isLoading()){
                        politics_news.add(null);
                        adapterPoliticalNews.setLoading();
                        adapterPoliticalNews.notifyItemInserted(politics_news.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       getPoliticalnews(page);
                    }
                },200);
            }
        });

        recyclerView.setAdapter(adapterPoliticalNews);
        recyclerView.setItemAnimator(null);

        if (!Hawk.contains("firstrun_political")){

            getPoliticalnews(page);
        }else {
            getNewsfromStorage();

            if (local_list.size() <= 0){
                getPoliticalnews(page);
            }

        }

        cd=new ConnectionDetector(getContext());

        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
          //  Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
               getPoliticalnews(page);
            }
        }

        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
           /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
          // getPoliticalnews(page);
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cd.isNetworkAvailable() || cd.isDataAvailable()) {
                            getPoliticalnews(1);
                            page=1;
                        }else {
                            Toasty.error(getContext(),"No Internet Connection!! Please Connect to WIFI or Mobile Data",300).show();
                            /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                                    .show();*/
                        }

                    }
                }, 3000);

            }

        });

        return rootView;
    }

    public void getPoliticalnews(int currentPage){
        refreshLayout.setRefreshing(true);
        Hawk.put("firstrun_political", true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<PoliticalNewsReceiveParams> call=networkClient.getPoliticalNews(currentPage);

        
        call.enqueue(new Callback<PoliticalNewsReceiveParams>() {
            @Override
            public void onResponse(Call<PoliticalNewsReceiveParams> call, Response<PoliticalNewsReceiveParams> response) {
                PoliticalNewsReceiveParams politics=response.body();
                Hawk.put("firstrun_political", false);

                newsUpdatedTime = MyApplication.getCurrentTime();

                List<PoliticalNewsReceiveParams.News26Bean> tempList=new ArrayList<PoliticalNewsReceiveParams.News26Bean>();

                if(page > 1) {

                    if (politics_news.size() > 0) {
                       /* if (tempList.get(tempList.size() - 1) == null)
                            tempList.remove(tempList.size() - 1);*/

                        if (politics_news.get(politics_news.size() - 1) == null) {
                            politics_news.remove(politics_news.size() - 1);
                           // adapterPoliticalNews.setLoaded();
                            adapterPoliticalNews.notifyItemRemoved(politics_news.size() - 1);
                        }
                    }

                    tempList = politics_news;
                }

                refreshLayout.setRefreshing(false);
                adapterPoliticalNews.setLoaded();
                politics_news=politics.getNews26();
                int newsSize=politics_news.size();
                tempList.addAll(politics_news);
                Log.d(TAG, "onResponse: Political Data "+politics_news.toString());
                politics_news=tempList;

                if(newsSize > 0)
                    adapterPoliticalNews.notifyItemInserted(politics_news.size()-newsSize);

                for(PoliticalNewsReceiveParams.News26Bean n : politics_news){
                    n.setNews_time(newsUpdatedTime);
                }

                if(politics_news.size() > 0 && page==1) {
                    DbHelper.deleteTable(PoliticalNewsReceiveParams.News26Bean.class);
                    ActiveAndroid.beginTransaction();
                    try{
                        for(PoliticalNewsReceiveParams.News26Bean b : politics_news){
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }

                    adapterPoliticalNews.add(politics_news);
                    adapterPoliticalNews.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PoliticalNewsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Political "+t.toString());
/*
                Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();*/
                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
               // adapterPoliticalNews.add((List<PoliticalNewsReceiveParams.News26Bean>) DbHelper.getRecordsDb(PoliticalNewsReceiveParams.News26Bean.class));
               // adapterPoliticalNews.notifyDataSetChanged();
                if(politics_news.size() > 0){
                if(politics_news.get(politics_news.size()-1)==null) {
                    politics_news.remove(politics_news.size() - 1);
                    adapterPoliticalNews.setLoaded();
                    adapterPoliticalNews.notifyItemInserted(politics_news.size() - 1);

                  }
                }

                adapterPoliticalNews.setLoaded();
                if(refreshLayout.isRefreshing() && refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void getNewsfromStorage(){
        local_list=(List<PoliticalNewsReceiveParams.News26Bean>) DbHelper.getRecordsDb(PoliticalNewsReceiveParams.News26Bean.class);
        if(local_list.size() > 0){
            politics_news=local_list;
            adapterPoliticalNews.add(politics_news);
            adapterPoliticalNews.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getPoliticalnews(page);
        }

    }
}
