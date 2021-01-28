package com.example.lab3.Models;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class FeedListElement {

    @ElementList
    private List<FeedModel> feedList;

    public FeedListElement() {
    }

    public FeedListElement(List<FeedModel> list) {
        this.feedList = list;
    }

    public List<FeedModel> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<FeedModel> list) {
        this.feedList = list;
    }
}
