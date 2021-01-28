package com.example.lab3.RssUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Models.FeedModel;
import com.example.lab3.Models.FeedType;
import com.example.lab3.R;
import com.example.lab3.Utils.RecyclerViewAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class RssParser extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private InputStream inputStream;
    private ArrayList<FeedModel> articles = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerAdapter;

    public RssParser(Context context, InputStream inputStream, RecyclerView recyclerView, RecyclerViewAdapter recyclerAdapter) {
        this.context = context;
        this.inputStream = inputStream;
        this.recyclerView = recyclerView;
        this.recyclerAdapter = recyclerAdapter;
        articles.clear();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRss();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        if (isParsed) {
            recyclerAdapter.setFeedList(articles);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    private boolean parseRss() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int event = parser.getEventType();
            String tagValue = null;
            boolean isSiteMeta = true;

            FeedType feedType = null;

            FeedModel article = new FeedModel();

            do {
                String tagName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (feedType == null) {
                            feedType = (tagName.equalsIgnoreCase("rss")) ? FeedType.RSS :
                                    (tagName.equalsIgnoreCase("feed")) ? FeedType.ATOM : FeedType.UNKNOWN;
                        }

                        if (tagName.equalsIgnoreCase("rss")) {
                            feedType = FeedType.RSS;
                        } else if (tagName.equalsIgnoreCase("feed")) {
                            feedType = FeedType.ATOM;
                        }

                        if (tagName.equalsIgnoreCase("entry") || tagName.equalsIgnoreCase("item")) {
                            article = new FeedModel();
                            isSiteMeta = false;
                        } else if (tagName.equalsIgnoreCase("link")) {
                            String link = parser.getAttributeValue(null, "href");
                            if (link != null) {
                                article.url = link;
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        tagValue = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (!isSiteMeta) {
                            if (tagName.equalsIgnoreCase("title")) {
                                article.title = tagValue;
                            } else if (tagName.equalsIgnoreCase("summary")) {
                                String desc = tagValue;
                                article.content = desc.substring(desc.indexOf("/>") + 1);
                            } else if (tagName.equalsIgnoreCase("description")) {
                                article.content = tagValue;
                            } else if (tagName.equalsIgnoreCase("link")
                                    && feedType == FeedType.RSS) {
                                article.url = tagValue;
                            } else if (tagName.equalsIgnoreCase("pubDate") || tagName.equalsIgnoreCase("updated")) {
                                article.date = tagValue;
                            }
                        }
                        if (tagName.equalsIgnoreCase("entry") || tagName.equalsIgnoreCase("item")) {
                            articles.add(article);
                            isSiteMeta = true;
                        }
                        break;
                }
                event = parser.next();
            } while (event != XmlPullParser.END_DOCUMENT);
            Log.d("####PARSING_TYPE", "Parsing type: " + feedType.toString());

            return true;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
