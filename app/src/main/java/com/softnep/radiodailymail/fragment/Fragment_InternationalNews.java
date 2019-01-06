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
import com.softnep.radiodailymail.adapter.Adapter_InternationalNews;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.InternationalNewsReceiveParams;
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

public class Fragment_InternationalNews extends Fragment {

    View rootView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    Adapter_InternationalNews adapterInternationalNews;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    List<InternationalNewsReceiveParams.News36Bean> international_news=new ArrayList<>();
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime = 0;
    List<InternationalNewsReceiveParams.News36Bean> local_list=new ArrayList<>();
    private static final String TAG = "Fragment_InternationaNe";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_internationalnews,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        recyclerView=(RecyclerView) rootView.findViewById(R.id.internationalnews_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapterInternationalNews=new Adapter_InternationalNews(recyclerView,international_news,getContext());

        adapterInternationalNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                if(international_news.size() > 0 && international_news.get(international_news.size()-1)!=null){

                    if(!adapterInternationalNews.isLoading()){
                        international_news.add(null);
                        adapterInternationalNews.setLoading();
                        adapterInternationalNews.notifyItemInserted(international_news.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       getInternationalNews(page);
                    }
                },200);
            }
        });

        recyclerView.setAdapter(adapterInternationalNews);
        recyclerView.setItemAnimator(null);

        if (!Hawk.contains("firstrun_international")){
            getInternationalNews(page);

        }else {
            getNewsfromStorage();
            if (local_list.size() <= 0){
                getInternationalNews(page);
            }

        }

        cd=new ConnectionDetector(getContext());

        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
           // Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
               getInternationalNews(page);
            }
        }

        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
           /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
            //getInternationalNews(page);
            refreshLayout.setRefreshing(false);

        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cd.isNetworkAvailable() || cd.isDataAvailable()) {
                            getInternationalNews(1);
                            page=1;
                        } else {
                            Toasty.error(getContext(),"No Internet Connection!! Please Connect to WIFI or Mobile Data",300).show();
                            /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                                    .show();*/
                        }

                    }
                }, 2000);

            }

        });

        return rootView;
    }

    public void getInternationalNews(int currentPage){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<InternationalNewsReceiveParams> call=networkClient.getInternationaNews(currentPage);

        call.enqueue(new Callback<InternationalNewsReceiveParams>() {
            @Override
            public void onResponse(Call<InternationalNewsReceiveParams> call, Response<InternationalNewsReceiveParams> response) {
              final InternationalNewsReceiveParams international=response.body();

                Hawk.put("firstrun_international", false);
                newsUpdatedTime=MyApplication.getCurrentTime();

                List<InternationalNewsReceiveParams.News36Bean> tempList=new ArrayList<InternationalNewsReceiveParams.News36Bean>();

                if(page > 1) {

                    if (international_news.size() > 0) {
                  /*  if (tempList.size() > 0 && tempList.get(tempList.size() - 1) == null)
                        tempList.remove(tempList.size() - 1);*/


                        if (international_news.get(international_news.size() - 1) == null) {
                            international_news.remove(international_news.size() - 1);
                           // adapterInternationalNews.setLoaded();
                            adapterInternationalNews.notifyItemRemoved(international_news.size() - 1);
                        }
                    }

                    tempList=international_news;
                }

                refreshLayout.setRefreshing(false);
                adapterInternationalNews.setLoaded();
                international_news = international.getNews36();
                int newsSize=international_news.size();
                tempList.addAll(international_news);
                Log.d(TAG, "onResponse: International"+international_news.toString());
                international_news=tempList;

                if(newsSize > 0)
                    adapterInternationalNews.notifyItemInserted(international_news.size()-newsSize);

                for(InternationalNewsReceiveParams.News36Bean n:international_news){
                    n.setNews_time(newsUpdatedTime);
                }

                if(international_news.size() > 0 && page==1) {
                    DbHelper.deleteTable(InternationalNewsReceiveParams.News36Bean.class);
                    ActiveAndroid.beginTransaction();
                    try {
                        for (InternationalNewsReceiveParams.News36Bean b : international_news) {
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }
                    //adapterInternationalNews.add((List<InternationalNewsReceiveParams.News36Bean>) DbHelper.getRecordsDb(InternationalNewsReceiveParams.News36Bean.class));
                    adapterInternationalNews.add(international_news);
                    adapterInternationalNews.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InternationalNewsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: International "+t.toString());

              /*  Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();*/
                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
               // adapterInternationalNews.add((List<InternationalNewsReceiveParams.News36Bean>) DbHelper.getRecordsDb(InternationalNewsReceiveParams.News36Bean.class));
               // adapterInternationalNews.notifyDataSetChanged();
                if(international_news.size() > 0 ){
                  if(international_news.get(international_news.size()-1)==null) {
                      international_news.remove(international_news.size() - 1);
                      adapterInternationalNews.setLoaded();
                      adapterInternationalNews.notifyItemInserted(international_news.size() - 1);

                  }
                }

                adapterInternationalNews.setLoaded();
                if(refreshLayout.isRefreshing() && refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }


    private void getNewsfromStorage(){
        local_list=(List<InternationalNewsReceiveParams.News36Bean>) DbHelper.getRecordsDb(InternationalNewsReceiveParams.News36Bean.class);
        if(local_list.size() > 0){
            international_news=local_list;
            adapterInternationalNews.add(international_news);
            adapterInternationalNews.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getInternationalNews(page);
        }

    }
}
