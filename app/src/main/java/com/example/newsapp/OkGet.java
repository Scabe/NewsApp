package com.example.newsapp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 封装Okhttp，只需关注OnResponse即可
 * Created by 晨阳大帅比 on 2018/4/7.
 */

public class OkGet{

    private String url;//访问的目标地址
    public OnGetListener onGetListener;

    public OkGet(String url){
        this.url = url;
    }

    private void init(String url) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                onGetListener.get(str);
            }
        });
    }

    public void setOnGetListener(OnGetListener onGetListener){
        this.onGetListener = onGetListener;
        init(url);
    }

    public interface OnGetListener{
        void get(String s);
    }
}
