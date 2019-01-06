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
import com.softnep.radiodailymail.adapter.Adapter_EntertainmentNews;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.EntertainmentNewsReceiveParams;
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

public class Fragment_EntertainmentNews extends Fragment{

    View rootView;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    Adapter_EntertainmentNews adapterEntertainmentNews;
    List<EntertainmentNewsReceiveParams.News28Bean> entertainment_list=new ArrayList<>();
    private int page=1;
    private long newsUpdatedTime= 0;
    private long lastUpdatedTime = 0;
    List<EntertainmentNewsReceiveParams.News28Bean> local_list=new ArrayList<>();
    private static final String TAG = "Fragment_EntertainmentN";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_entertainmentnews,container,false);

        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        refreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.Swiperefreshlayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.purple),getResources().getColor(R.color.cyan));

        recyclerView=(RecyclerView) rootView.findViewById(R.id.entertainmentnews_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutmanager);

        adapterEntertainmentNews=new Adapter_EntertainmentNews(recyclerView,entertainment_list,getContext());

        adapterEntertainmentNews.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                if(entertainment_list.size() > 0 && entertainment_list.get(entertainment_list.size()-1)!=null){

                    if(!adapterEntertainmentNews.isLoading()){
                        entertainment_list.add(null);
                        adapterEntertainmentNews.setLoading();
                        adapterEntertainmentNews.notifyItemInserted(entertainment_list.size()-1);
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       getEntertainmentnews(page);
                    }
                },200);
            }
        });

        recyclerView.setAdapter(adapterEntertainmentNews);
        recyclerView.setItemAnimator(null);

        cd=new ConnectionDetector(getContext());

        if (!Hawk.contains("firstrun_entertainment")){
            getEntertainmentnews(page);

        }else {
            getNewsfromStorage();
            if (local_list.size() <= 0){
                getEntertainmentnews(page);
            }

        }

        if(MyApplication.shouldLoadNewNews(MyApplication.getCurrentTime(),lastUpdatedTime)) {
         //   Toast.makeText(getContext(), "news time elapsed "+ (MyApplication.getCurrentTime()-lastUpdatedTime), Toast.LENGTH_SHORT).show();
            if (cd.isNetworkAvailable() || cd.isDataAvailable()) {
               getEntertainmentnews(page);
            }
        }

        if(!cd.isNetworkAvailable() || !cd.isDataAvailable()){
           /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();*/
          //  getEntertainmentnews(page);
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(cd.isDataAvailable() || cd.isNetworkAvailable()) {
                            getEntertainmentnews(1);
                            page=1;
                        }else {
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

    public void getEntertainmentnews(int currentPage){
        refreshLayout.setRefreshing(true);
        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        Call<EntertainmentNewsReceiveParams> call=networkClient.getEntertainmentNews(currentPage);

        call.enqueue(new Callback<EntertainmentNewsReceiveParams>() {
            @Override
            public void onResponse(Call<EntertainmentNewsReceiveParams> call, Response<EntertainmentNewsReceiveParams> response) {
                EntertainmentNewsReceiveParams entertainment = response.body();

                Hawk.put("firstrun_entertainment", false);
                newsUpdatedTime=MyApplication.getCurrentTime();

                List<EntertainmentNewsReceiveParams.News28Bean> tempList = new ArrayList<EntertainmentNewsReceiveParams.News28Bean>();

                if (page > 1) {

                    if (entertainment_list.size() > 0) {
                   /* if (tempList.get(tempList.size() - 1) == null)
                        tempList.remove(tempList.size() - 1);*/

                        if (entertainment_list.get(entertainment_list.size() - 1) == null) {
                            entertainment_list.remove(entertainment_list.size() - 1);
                           // adapterEntertainmentNews.setLoaded();
                            adapterEntertainmentNews.notifyItemRemoved(entertainment_list.size() - 1);
                        }
                    }

                    tempList = entertainment_list;
                }

                refreshLayout.setRefreshing(false);
                adapterEntertainmentNews.setLoaded();
                entertainment_list = entertainment.getNews28();
                int newsSize=entertainment_list.size();
                tempList.addAll(entertainment_list);
                Log.d(TAG, "onResponse: Entertainment "+entertainment_list.toString());
                entertainment_list = tempList;

                if(newsSize > 0)
                    adapterEntertainmentNews.notifyItemInserted(entertainment_list.size()-newsSize);

                for(EntertainmentNewsReceiveParams.News28Bean n:entertainment_list){
                    n.setNews_time(newsUpdatedTime);
                }

                if (entertainment_list.size() > 0 && page==1) {
                DbHelper.deleteTable(EntertainmentNewsReceiveParams.News28Bean.class);
                ActiveAndroid.beginTransaction();
                try {
                    for (EntertainmentNewsReceiveParams.News28Bean b : entertainment_list) {
                        b.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                //adapterEntertainmentNews.add((List<EntertainmentNewsReceiveParams.News28Bean>) DbHelper.getRecordsDb(EntertainmentNewsReceiveParams.News28Bean.class));
                    adapterEntertainmentNews.add(entertainment_list);
                    adapterEntertainmentNews.notifyDataSetChanged();
               }
            }

            @Override
            public void onFailure(Call<EntertainmentNewsReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: Entertainment "+t.toString());

               /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .setDuration(2000)
                        .show();
*/
                page--;
                Toasty.error(getContext(),"Couldnot Load Data!! No Internet Connection!",300).show();
              //  adapterEntertainmentNews.add((List<EntertainmentNewsReceiveParams.News28Bean>) DbHelper.getRecordsDb(EntertainmentNewsReceiveParams.News28Bean.class));
              //  adapterEntertainmentNews.notifyDataSetChanged();
                if(entertainment_list.size() > 0){
                 if(entertainment_list.get(entertainment_list.size()-1)==null) {
                     entertainment_list.remove(entertainment_list.size() - 1);
                     adapterEntertainmentNews.setLoaded();
                     adapterEntertainmentNews.notifyItemInserted(entertainment_list.size() - 1);
                 }
                }

                adapterEntertainmentNews.setLoaded();
                if(refreshLayout.isRefreshing() && refreshLayout!=null){
                    refreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void getNewsfromStorage(){
        local_list=(List<EntertainmentNewsReceiveParams.News28Bean>) DbHelper.getRecordsDb(EntertainmentNewsReceiveParams.News28Bean.class);
        if(local_list.size() > 0){
            entertainment_list=local_list;
            adapterEntertainmentNews.add(entertainment_list);
            adapterEntertainmentNews.notifyDataSetChanged();

            lastUpdatedTime = local_list.get(0).getNews_time();
        } else {
            getEntertainmentnews(page);
        }

    }
}
