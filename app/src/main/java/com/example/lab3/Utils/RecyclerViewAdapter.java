package com.example.lab3.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Models.FeedModel;
import com.example.lab3.R;
import com.example.lab3.WebUtils.Linker;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    public List<FeedModel> mFeedList;
    private List<FeedModel> mSavedFeedList;
    private Linker mLinker;

    public RecyclerViewAdapter(Context context) {
        mFeedList = new ArrayList<>();
        mSavedFeedList = new ArrayList<>();
        mLinker = new Linker(context);
    }

    public void clearList() {
        mFeedList.clear();
    }

    public void setFeedList(List<FeedModel> feedList) {
        mSavedFeedList.addAll(feedList);
        mFeedList = mSavedFeedList;
    }

    public List<FeedModel> getFeedList() {
        if (mFeedList.size() == 0)
            return new ArrayList<>();
        return mFeedList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_view_item, parent, false);

        RecyclerViewHolder item = new RecyclerViewHolder(v, parent.getContext());
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.setFeed(mFeedList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }
}
