package com.example.lab3.Database;

import com.example.lab3.Models.FeedListElement;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;

public class XmlSerializer {

    public static void writeXml(FeedListElement articlesList, File eventsFile) {
        Serializer serializer = new Persister();
        try {
            serializer.write(articlesList, eventsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FeedListElement readXml(File eventsFile) {
        Serializer serializer = new Persister();
        FeedListElement articlesList = null;
        try {
            articlesList = serializer.read(FeedListElement.class, eventsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (articlesList == null) {
            articlesList = new FeedListElement();
            articlesList.setFeedList(new ArrayList<>());
        }
        return articlesList;
    }
}
