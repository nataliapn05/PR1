package com.example.lab3;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Database.XmlSerializer;
import com.example.lab3.Models.FeedListElement;
import com.example.lab3.RssUtils.DownloadFeedAsync;
import com.example.lab3.Utils.PrimitiveArrays;
import com.example.lab3.Utils.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public TextView mFeedingSourcesText;
    public FloatingActionButton mDeleteButton;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private EditText mInputSourceText;

    private HashMap<String, Boolean> mFeedSources;
    private File mFeedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerAdapter = new RecyclerViewAdapter(this);

        mFeedSources = new HashMap<String, Boolean>() {{
            put(getString(R.string.default_feed_url), true);
        }};

        mFeedFile = new File(this.getFilesDir(), getString(R.string.default_storage_file));
        mFeedingSourcesText = findViewById(R.id.sourceFeedText);
        mRecyclerView = findViewById(R.id.recycler_view);
        mDeleteButton = findViewById(R.id.removeSavedBtn);
        mInputSourceText = findViewById(R.id.inputSourceText);

        mDeleteButton.setOnLongClickListener(view -> {
            mRecyclerAdapter.clearList();
            mRecyclerView.setAdapter(mRecyclerAdapter);
            Toast.makeText(MainActivity.this, "Sters", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    public void addFeedSource(View view) {
        String url = mInputSourceText.getText().toString();

        mFeedSources.put(url, false);
        mInputSourceText.setText("");
        Toast.makeText(MainActivity.this, "Sursa a fost adaugata", Toast.LENGTH_SHORT).show();
    }

    public void refreshFeedList(View view) {
        ArrayList<String> urls = new ArrayList<>();
        mRecyclerAdapter.clearList();
        mFeedSources.forEach((key, value) -> {
            if (value) urls.add(key);
        });
        if (urls.size() > 0) {
            mFeedingSourcesText.setText("" + urls.stream().collect(Collectors.joining(", ")));
            new DownloadFeedAsync(this, mRecyclerView, mRecyclerAdapter, urls).execute();
        }  else {
            mRecyclerView.setAdapter(mRecyclerAdapter);
        }
    }

    public void saveFeedList(View view) {
        XmlSerializer.writeXml(new FeedListElement(mRecyclerAdapter.getFeedList()), mFeedFile);
        Toast.makeText(this, "Date stocate", Toast.LENGTH_SHORT).show();
    }

    public void removeFeedList(View view) {
        XmlSerializer.writeXml(new FeedListElement(new ArrayList<>()), mFeedFile);
        Toast.makeText(this, "Datele stocate au fost sterse", Toast.LENGTH_SHORT).show();
    }

    public void loadSavedFeedList(View view) {
        FeedListElement feedList = XmlSerializer.readXml(mFeedFile);
        mRecyclerAdapter.clearList();
        mRecyclerAdapter.setFeedList(feedList.getFeedList());

        mRecyclerView.setAdapter(mRecyclerAdapter);
        Toast.makeText(this, "Datele stocate au fost incarcate", Toast.LENGTH_SHORT).show();
    }

    public void loadFeedSources(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alege sursa");

        ArrayList<String> values = new ArrayList<>();
        ArrayList<Boolean> checked = new ArrayList<>();
        mFeedSources.forEach((key, value) -> {
            values.add(key);
            checked.add(value);
        });

        builder.setMultiChoiceItems(values.toArray(new String[values.size()]),
                PrimitiveArrays.convertAsPrimitiveBoolArray(checked),
                (dialogInterface, which, isChecked) -> checked.set(which, isChecked));

        builder.setPositiveButton("DA", (dialog, which) -> {
            AtomicInteger i = new AtomicInteger();
            mFeedSources.forEach((key, value) -> {
                mFeedSources.put(key, checked.get(i.getAndIncrement()));
            });
        });

        builder.setNegativeButton("NU", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
