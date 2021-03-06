package com.example.newsapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newsapp.Banner.BannerView;
import com.example.newsapp.Activity.ContentActivity;
import com.example.newsapp.Json.Before;
import com.example.newsapp.Json.Recent;
import com.example.newsapp.Json.RecentItem;
import com.example.newsapp.Json.Stories;
import com.example.newsapp.Json.StoriesItemOne;
import com.example.newsapp.Json.Utility;
import com.example.newsapp.Custom.MyRecyclerViewAdapter;
import com.example.newsapp.Json.NewsList;
import com.example.newsapp.OkGet;
import com.example.newsapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/8.
 */

public class MessagesFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH)+1;
    private int day = calendar.get(Calendar.DAY_OF_MONTH)-1;
    //今日的date
    private int count = year * 10000 + month * 100 + day;

    private List<View> viewList;
    private BannerView bannerView;
    private Context context;

    //用来存放stories
    private List<StoriesItemOne> storiesList = new ArrayList<>();
    private List<RecentItem> recentitemList = new ArrayList<>();

    //true上拉加载，false下拉刷新
    private boolean up;
    //使BannerView不随onRefresh自动滑动
    public boolean isMove = true;

    public final static int NewsCase = 1;
    public final static int RecentCase = 2;
    public final static int BeforeCase = 3;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        requestNew(NewsCase);
        requestNew(RecentCase);
        viewList = new ArrayList<>();
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.bannerheader, null);
        bannerView = header.findViewById(R.id.banner);
        header.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        //bannerView.setTransformAnim(true);//设置轮播效果

        mRecyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyRecyclerViewAdapter();
        adapter.addDatas(storiesList);
        adapter.setHeaderView(header);
        mRecyclerView.setAdapter(adapter);


        swipeRefreshLayout = view.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                bannerView.once = true;
                isMove = false;
                up = false;
                requestNew(BeforeCase);
                swipeRefreshLayout.setRefreshing(false);
                isMove = true;
            }
        });

        final Intent intent = new Intent(getActivity(),ContentActivity.class);
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                intent.putExtra("flag",1);
                intent.putExtra("uri",id);
                startActivity(intent);
            }
        });
        adapter.setOnAutoChangeListener(new MyRecyclerViewAdapter.OnAutoChangeListener() {
            @Override
            public void onAutoChange() {
                up = true;
                requestNew(BeforeCase);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    //更新beforeList recentitemList storiesList
    public void requestNew(final int i){
        String address = response(i);
        OkGet okGet = new OkGet(address);
        okGet.setOnGetListener(new OkGet.OnGetListener() {
            @Override
            public void get(String s) {
                final List<StoriesItemOne> beforeList = new ArrayList<>();
                switch (i){
                    case NewsCase:
                        NewsList newlist = Utility.parseJsonWithGson(s);
                        for (Stories stories :newlist.stories){
                            StoriesItemOne mStories = new StoriesItemOne(stories.title,stories.ga_prefix,stories.images,stories.type,stories.id);
                            storiesList.add(mStories);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            };
                        });
                        break;
                    case RecentCase:
                        Recent recent = Utility.parseJsonwithGson(s);
                        for(RecentItem item:recent.recent){
                            recentitemList.add(item);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bannerView.setList(recentitemList);
                                adapter.notifyDataSetChanged();
                            };
                        });
                        break;
                    case BeforeCase:
                        Before before = Utility.ParseJsonWithGson(s);
                        count = Integer.parseInt(before.date);
                        for(Stories stories:before.stories){
                            StoriesItemOne mstories = new StoriesItemOne(stories.title,stories.ga_prefix,stories.images,stories.type,stories.id);
                            beforeList.add(mstories);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!up){
                                    storiesList.clear();
                                    storiesList.addAll(beforeList);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    int start = storiesList.size()+1;
                                    storiesList.addAll(beforeList);
                                    adapter.notifyItemRangeChanged(start,beforeList.size());
                                }
                            };
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private String response(int i){
        String address = "";
        final String LatestArticleUrl = "http://news-at.zhihu.com/api/4/news/latest";
        final String HotArticleUrl ="https://news-at.zhihu.com/api/3/news/hot";
        final String BeforeArticleUrl = "https://news-at.zhihu.com/api/4/news/before/";
        switch (i){
            case NewsCase:
                address = LatestArticleUrl;
                break;
            case RecentCase:
                address = HotArticleUrl;
                break;
            case BeforeCase:
                String str = count + "";
                address = BeforeArticleUrl + str;
            default:
                break;
        }
        return address;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
}
