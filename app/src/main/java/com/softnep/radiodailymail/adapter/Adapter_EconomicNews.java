package com.softnep.radiodailymail.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softnep.radiodailymail.MyApplication;
import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.activity.Activity_EconomicNews_Description;
import com.softnep.radiodailymail.helper.OnLoadMoreListener;
import com.softnep.radiodailymail.model.EconomicNewsReceiveParams;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 2017-11-09.
 */

public class Adapter_EconomicNews extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<EconomicNewsReceiveParams.News23Bean> economic_list=new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener listener;
    private boolean isLoading=false;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView recyclerView;
    private static final String TAG = "Adapter_EconomicNews";

    public Adapter_EconomicNews(RecyclerView recyclerView,List<EconomicNewsReceiveParams.News23Bean> economic,Context context){
        this.recyclerView=recyclerView;
        this.economic_list=economic;
        this.context=context;

        final LinearLayoutManager layoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount=layoutManager.getItemCount();
                Log.d(TAG, "totalItemCount: "+ totalItemCount);
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "lastvisible item "+ lastVisibleItem);

                if(!isLoading && totalItemCount<=(lastVisibleItem+visibleThreshold)){
                    if(listener!=null){
                        listener.onLoadMore();
                    }
                    isLoading=true;
                }
            }
        });
    }

    public void add(List<EconomicNewsReceiveParams.News23Bean> news_list){
        this.economic_list = news_list;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return economic_list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_economicnews_single_item, parent, false);
                return new Adapter_EconomicNews.NewsViewHolder(view);

            case VIEW_TYPE_LOADING:
                View view2=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_loading,parent,false);
                return new Adapter_EconomicNews.LoadingViewHolder(view2);

            default:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_economicnews_single_item, parent, false);
                return new Adapter_EconomicNews.NewsViewHolder(view3);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      final EconomicNewsReceiveParams.News23Bean pos=economic_list.get(position);

        if(holder instanceof NewsViewHolder){
            Adapter_EconomicNews.NewsViewHolder newsViewHolder=(Adapter_EconomicNews.NewsViewHolder) holder;
            newsViewHolder.news_title.setText(Html.fromHtml(pos.getTitle().replace("\\n","")));
            newsViewHolder.news_short_desc.setText(Html.fromHtml(pos.getDescription().replace("\\n","")));
            newsViewHolder.news_time.setText(Html.fromHtml(MyApplication.getDiff(pos.getDate().replace("\\n",""))));
            Picasso.with(context)
                    .load(pos.getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image)
                    .into(newsViewHolder.news_image);

        } else if(holder instanceof LoadingViewHolder){
            Adapter_EconomicNews.LoadingViewHolder loadingViewHolder=(Adapter_EconomicNews.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),Activity_EconomicNews_Description.class);
                intent.putExtra("obj",pos);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return economic_list==null? 0 : economic_list.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public boolean isLoading(){
        if(isLoading){
            return true;
        }
        return false;
    }

    public void setLoading(){
        isLoading=true;
    }

    private static class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView news_image;
        TextView news_title,news_short_desc,news_time;

        private NewsViewHolder(View itemView) {
            super(itemView);

            news_image=(ImageView) itemView.findViewById(R.id.economicnews_image);
            news_title=(TextView) itemView.findViewById(R.id.economicnews_title);
            news_short_desc=(TextView) itemView.findViewById(R.id.economicnews_short_description);
            news_time=(TextView) itemView.findViewById(R.id.economicnews_time);
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
