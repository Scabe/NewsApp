package com.example.newsapp.Json;

/**
 * Created by 晨阳大帅比 on 2018/3/15.
 */

public class TopStoriesItemOne {
    private String title;
    private String ga_prefix;
    private String image;
    public int type;
    public int id;


    public TopStoriesItemOne(String title, String ga_prefix, String imgs, int type, int id) {
        this.title = title;
        this.ga_prefix = ga_prefix;
        this.image = imgs;
        this.type = type;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public String getImg() {
        return image;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
