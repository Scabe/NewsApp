package com.example.newsapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by 晨阳大帅比 on 2018/3/13.
 */

public class ContentActivity extends Activity{
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
                String s = doGet(uri);
                webView.loadUrl(s);
                break;
            case 2:
                String url = getIntent().getStringExtra("uri");
                String str = doGet(url);
                webView.loadUrl(str);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private String doGet(final String url){
        //String str = null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String response = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(response);
            return  jsonObject.getString("share_url");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Error";
    }



}
