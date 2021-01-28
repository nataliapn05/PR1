package com.example.lab3.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Models.FeedModel;
import com.example.lab3.R;
import com.example.lab3.WebUtils.Linker;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public FeedModel feedModel;

    private TextView title;
    private TextView time;
    private TextView content;
    private Context context;

    public RecyclerViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);

        title = itemView.findViewById(R.id.modelTitle);
        time = itemView.findViewById(R.id.modelTime);
        content = itemView.findViewById(R.id.modelContent);
        this.context = context;
    }

    public void setFeed(@NonNull FeedModel feedModel) {
        title.setText(feedModel.title);
        time.setText(feedModel.date);
        content.setText(feedModel.content);
        this.feedModel = feedModel;
    }

    @Override
    public void onClick(View view) {
        Linker l = new Linker(context);
        l.open(feedModel.url);
    }
}
