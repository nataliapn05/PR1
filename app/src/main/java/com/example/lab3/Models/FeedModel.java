package com.example.lab3.Models;

public class FeedModel {
    public String title;
    public String content;
    public String date;
    public String url;

    public FeedModel() {
    }

    public FeedModel(String title, String content, String date, String url) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.url = url;
    }
}
