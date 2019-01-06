package com.softnep.radiodailymail.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import com.softnep.radiodailymail.adapter.Adapter_NationalNews;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.NationalNewsReceiveParams;
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

public class Fragment_NationalNews extends Fragment {

    View rootView;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    Adapter_NationalNews adapterNationalNews;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    SwipeRefreshLayout refreshLayout;
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime =0;
    List<NationalNewsReceiveParams.News37Bean> nationalnews_list=new ArrayList<>();
    List<NationalNewsReceiveParams.News37Bean> local_list=new ArrayList<>();
    private static final String TAG = "Fragment_NationalNews";
    private Animator spruceAnimator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_nationalnews,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        cd=new ConnectionDetector(getContext());

        recyclerView=(RecyclerView) rootView.findViewById(R.id.nationalnews_recycler_view);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);

        adapterNationalNews=new Adapter_NationalNews(recyclerView, nationalnews_list,getContext());

        adapterNationalNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if(nationalnews_list.size() > 0 && nationalnews_list.get(nationalnews_list.size()-1)!=null){

                    if(!adapterNationalNews.isLoading()){
                        nationalnews_list.add(null);
                        adapterNationalNews.setLoading();
                        adapterNationalNews.notifyItemInserted(nationalnews_list.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNationalNews(page);
                    }
                },200);
            }
        });

        recyclerView.setAdapter(adapterNationalNews);
       // recyclerView.setItemAnimator(null);

        if (!Hawk.contains("firstrun_national")){
            getNationalNews(page);
        }else {
            getNewsfromStorage();
            if (local_list.size() <= 0){
                getNationalNews(page);
            }

        }

        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
           // Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
               getNationalNews(page);
            }
        }

        if(!cd.isDataAvailable() || !cd.isNetworkAvailable()){
            /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
           // getNationalNews(page);
            refreshLayout.setRefreshing(false);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cd.isDataAvailable() || cd.isNetworkAvailable()) {
                            getNationalNews(1);
                            page=1;
                        } else {
                            Toasty.error(getContext(),"No Internet Connection!! Please Connect to WIFI or Mobile Data",300).show();
                           /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                                    .show();*/
                        }

                    }
                }, 2000);

            }

        });

        return rootView;
    }

    public void getNationalNews(int currentPage){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<NationalNewsReceiveParams> call=networkClient.getNationalNews(currentPage);

        call.enqueue(new Callback<NationalNewsReceiveParams>() {
            @Override
            public void onResponse(Call<NationalNewsReceiveParams> call, Response<NationalNewsReceiveParams> response) {

                Log.e(TAG, "onResponse: "+ response.body().toString() );

                final NationalNewsReceiveParams national_news=response.body();
                Hawk.put("firstrun_national", true);

                newsUpdatedTime=MyApplication.getCurrentTime();

                List<NationalNewsReceiveParams.News37Bean> tempList=new ArrayList<NationalNewsReceiveParams.News37Bean>();

                if(page > 1) {

                    if (nationalnews_list.size() > 0) {
                        /*if (tempList.get(tempList.size() - 1) == null)
                            tempList.remove(tempList.size() - 1);*/

                        if (nationalnews_list.get(nationalnews_list.size() - 1) == null) {
                            nationalnews_list.remove(nationalnews_list.size() - 1);
                           // adapterNationalNews.setLoaded();
                            adapterNationalNews.notifyItemRemoved(nationalnews_list.size() - 1);
                        }
                    }

                    tempList = nationalnews_list;
                }

                refreshLayout.setRefreshing(false);
                adapterNationalNews.setLoaded();
                nationalnews_list=national_news.getNews37();
                int newsSize=nationalnews_list.size();
                tempList.addAll(nationalnews_list);
                Log.d(TAG, "onResponse: List "+nationalnews_list.toString());
                nationalnews_list=tempList;

                if(newsSize > 0)
                    adapterNationalNews.notifyItemInserted(nationalnews_list.size()-newsSize);

                for (NationalNewsReceiveParams.News37Bean n:nationalnews_list) {
                    n.setNews_time(newsUpdatedTime);
                }

                if(nationalnews_list.size() > 0 && page==1) {
                    DbHelper.deleteTable(NationalNewsReceiveParams.News37Bean.class);
                    ActiveAndroid.beginTransaction();
                    try {
                        for (NationalNewsReceiveParams.News37Bean b : nationalnews_list) {
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }
                   // adapterNationalNews.add((List<NationalNewsReceiveParams.News37Bean>) DbHelper.getRecordsDb(NationalNewsReceiveParams.News37Bean.class));
                    adapterNationalNews.add(nationalnews_list);
                    adapterNationalNews.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NationalNewsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: National "+t.toString());

                /*Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();*/
                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
               // adapterNationalNews.add((List<NationalNewsReceiveParams.News37Bean>) DbHelper.getRecordsDb(NationalNewsReceiveParams.News37Bean.class));
              //  adapterNationalNews.notifyDataSetChanged();
                if(nationalnews_list.size() > 0){
                 if(nationalnews_list.get(nationalnews_list.size()-1)==null) {
                     nationalnews_list.remove(nationalnews_list.size() - 1);
                     adapterNationalNews.setLoaded();
                     adapterNationalNews.notifyItemInserted(nationalnews_list.size() - 1);

                 }
                }

                adapterNationalNews.setLoaded();
                if(refreshLayout.isRefreshing() && refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getNewsfromStorage(){
        local_list=(List<NationalNewsReceiveParams.News37Bean>) DbHelper.getRecordsDb(NationalNewsReceiveParams.News37Bean.class);
        if(local_list.size() > 0){
            nationalnews_list=local_list;
            adapterNationalNews.add(nationalnews_list);
            adapterNationalNews.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getNationalNews(page);
        }

    }
}
