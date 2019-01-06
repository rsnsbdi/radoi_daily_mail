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
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import com.orhanobut.hawk.Hawk;
import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.adapter.Adapter_EconomicNews;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.EconomicNewsReceiveParams;
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

public class Fragment_EconomicNews extends Fragment {

    View rootView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    Adapter_EconomicNews adapterEconomicNews;
    List<EconomicNewsReceiveParams.News23Bean> economic_list=new ArrayList<>();
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime = 0;
    List<EconomicNewsReceiveParams.News23Bean> local_list=new ArrayList<>();
    private static final String TAG = "Fragment_EconomicNews";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_economicnews,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        recyclerView=(RecyclerView) rootView.findViewById(R.id.economicnews_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);

        adapterEconomicNews=new Adapter_EconomicNews(recyclerView,economic_list,getContext());

        adapterEconomicNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                if(economic_list.size() > 0 && economic_list.get(economic_list.size()-1)!=null){

                    if(!adapterEconomicNews.isLoading()){
                        economic_list.add(null);
                        adapterEconomicNews.setLoading();
                        adapterEconomicNews.notifyItemInserted(economic_list.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       getEconomicnews(page);
                    }
                },200);
            }
        });

        recyclerView.setAdapter(adapterEconomicNews);
        recyclerView.setItemAnimator(null);

        if (!Hawk.contains("firstrun_economic")){
            getEconomicnews(page);

        }else {
            getNewsfromStorage();
            if (local_list.size() <= 0){
                getEconomicnews(page);
            }

        }

        cd=new ConnectionDetector(getContext());

        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
           // Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
                getEconomicnews(page);
            }
        }

        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
           /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
          //  getEconomicnews(page);
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cd.isNetworkAvailable() || cd.isDataAvailable()) {
                            getEconomicnews(1);
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

    public void getEconomicnews(int currentPage){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<EconomicNewsReceiveParams> call=networkClient.getEconomicNews(currentPage);

        call.enqueue(new Callback<EconomicNewsReceiveParams>() {
            @Override
            public void onResponse(Call<EconomicNewsReceiveParams> call, Response<EconomicNewsReceiveParams> response) {
                EconomicNewsReceiveParams economic=response.body();

                Hawk.put("firstrun_economic", false);
                newsUpdatedTime=MyApplication.getCurrentTime();

                List<EconomicNewsReceiveParams.News23Bean> tempList=new ArrayList<EconomicNewsReceiveParams.News23Bean>();

                if(page > 1) {

                    if (economic_list.size() > 0) {
                   /* if (tempList.get(tempList.size() - 1) == null)
                        tempList.remove(tempList.size() - 1);
*/
                        if (economic_list.get(economic_list.size() - 1) == null) {
                            economic_list.remove(economic_list.size() - 1);
                          //  adapterEconomicNews.setLoaded();
                            adapterEconomicNews.notifyItemRemoved(economic_list.size() - 1);
                        }
                    }

                    tempList = economic_list;
                }

                refreshLayout.setRefreshing(false);
                adapterEconomicNews.setLoaded();
                economic_list=economic.getNews23();
                int newsSize=economic_list.size();
                tempList.addAll(economic_list);
                Log.d(TAG, "onResponse: Economic List Data"+economic_list.toString());
                economic_list=tempList;

                if(newsSize > 0)
                    adapterEconomicNews.notifyItemInserted(economic_list.size()-newsSize);

                for(EconomicNewsReceiveParams.News23Bean n:economic_list){
                    n.setNews_time(newsUpdatedTime);
                }

                if(economic_list.size() > 0 && page==1) {

                    DbHelper.deleteTable(EconomicNewsReceiveParams.News23Bean.class);
                    ActiveAndroid.beginTransaction();
                    try {
                        for (EconomicNewsReceiveParams.News23Bean b : economic_list) {
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }
                    //adapterEconomicNews.add((List<EconomicNewsReceiveParams.News23Bean>) DbHelper.getRecordsDb(EconomicNewsReceiveParams.News23Bean.class));
                    adapterEconomicNews.add(economic_list);
                    adapterEconomicNews.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<EconomicNewsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Economic"+t.toString());

               /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();*/

                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
               // adapterEconomicNews.add((List<EconomicNewsReceiveParams.News23Bean>) DbHelper.getRecordsDb(EconomicNewsReceiveParams.News23Bean.class));
               // adapterEconomicNews.notifyDataSetChanged();
                if(economic_list.size() > 0) {
                    if (economic_list.get(economic_list.size() - 1) == null) {
                        economic_list.remove(economic_list.size() - 1);
                        adapterEconomicNews.setLoaded();
                        adapterEconomicNews.notifyItemInserted(economic_list.size() - 1);

                    }
                }

                adapterEconomicNews.setLoaded();
                if(refreshLayout.isRefreshing() && refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void getNewsfromStorage(){
        local_list=(List<EconomicNewsReceiveParams.News23Bean>) DbHelper.getRecordsDb(EconomicNewsReceiveParams.News23Bean.class);
        if(local_list.size() > 0){
            economic_list=local_list;
            adapterEconomicNews.add(economic_list);
            adapterEconomicNews.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getEconomicnews(page);
        }

    }
}
