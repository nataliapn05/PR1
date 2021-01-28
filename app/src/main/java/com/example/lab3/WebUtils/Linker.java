package com.example.lab3.WebUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Linker {

    private Context context;

    public Linker(Context context) {
        this.context = context;
    }

    public void open(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
