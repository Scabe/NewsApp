package com.example.newsapp.Json;

import com.google.gson.Gson;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/11.
 */

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }

    public static Before ParseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText,Before.class);
    }

    public static Recent parseJsonwithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, Recent.class);
    }

}
