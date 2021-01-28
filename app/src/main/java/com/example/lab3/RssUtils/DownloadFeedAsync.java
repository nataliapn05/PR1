package com.example.lab3.RssUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Utils.ErrorTracker;
import com.example.lab3.Utils.RecyclerViewAdapter;
import com.example.lab3.WebUtils.Connector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class DownloadFeedAsync extends AsyncTask<Void, Void, Object> {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerAdapter;
    private List<String> urls;

    public DownloadFeedAsync(Context context, RecyclerView recyclerView, RecyclerViewAdapter recyclerAdapter, List<String> urlAddresses) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.urls = urlAddresses;
        this.recyclerAdapter = recyclerAdapter;
    }

    @Override
    protected Object doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if (data.toString().startsWith("Error")) {
            Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show();
        } else {
            List<InputStream> isList = (List<InputStream>) data;
            for (InputStream inputStream : isList) {
                new RssParser(context, inputStream, recyclerView, recyclerAdapter).execute();
            }
            Toast.makeText(context, "Incarcat cu succes", Toast.LENGTH_SHORT).show();
        }
    }

    private Object downloadData() {
        String errorReturn = "";
        List<InputStream> isList = new ArrayList<>();

        if (urls.size() > 0) {
            for (String url : urls) {
                Object connection = Connector.connect(url);
                if (connection.toString().startsWith("Error")) {
                    return connection.toString();
                }
                try {
                    HttpURLConnection con;
                    con = (HttpURLConnection) connection;
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = new BufferedInputStream(con.getInputStream());
                        isList.add(is);
                    }
                    errorReturn = ErrorTracker.RESPONSE_ERROR + con.getResponseMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    errorReturn = ErrorTracker.IO_ERROR;
                }
            }
            if (!isList.isEmpty()) {
                return isList;
            } else {
                return errorReturn;
            }
        }
        return ErrorTracker.NO_RESULTS;
    }
}