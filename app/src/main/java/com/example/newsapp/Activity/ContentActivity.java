package com.example.newsapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.newsapp.OkGet;
import com.example.newsapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 点击Item或轮播图后打开的网页具体内容
 * Created by 晨阳大帅比 on 2018/3/13.
 */

public class ContentActivity extends Activity {
    private WebView webView;
    private WebSettings webSettings ;
    final String ArticleContentUrl = "http://news-at.zhihu.com/api/4/news/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.web_view);
        webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.setWebViewClient(new WebViewClient());
        int flag = getIntent().getIntExtra("flag",0);
        switch (flag){
            case 1:
                int id = getIntent().getIntExtra("uri",0);
                String ids = id+"";
                String uri = ArticleContentUrl + ids;
                showweb(uri);
                break;
            case 2:
                String url = getIntent().getStringExtra("uri");
                showweb(url);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
/*
    private void showweb(final String url){
        try{
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //webview设置需要在同一个线程中进行，所以这里runOnUiThread
                                    webView.loadUrl(jsonObject.getString("share_url"));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void showweb(final String url){
        OkGet okGet = new OkGet(url);
        okGet.setOnGetListener(new OkGet.OnGetListener() {
            @Override
            public void get(String s) {
                try{
                    final JSONObject jsonObject = new JSONObject(s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //webview设置需要在同一个线程中进行，所以这里runOnUiThread
                                webView.loadUrl(jsonObject.getString("share_url"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
