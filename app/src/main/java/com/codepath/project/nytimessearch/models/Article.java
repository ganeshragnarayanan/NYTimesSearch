package com.codepath.project.nytimessearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by GANESH on 9/19/17.
 */

public class Article {

    String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    String headline;
    String thumbNail;

    public String getSnippet() {
        return snippet;
    }

    public String getNewdesk() {
        return newdesk;
    }

    String snippet;
    String newdesk;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            JSONArray newDeskObj = jsonObject.getJSONArray("keywords");

            if (newDeskObj.length() > 0) {
                Log.d("debug", "if block");
                JSONObject test2 = newDeskObj.getJSONObject(0);

                int size = newDeskObj.length();
                for (int i = 0; i < size; i++) {
                    Log.d("debug", newDeskObj.getJSONObject(i).toString());
                }

            } else {
                Log.d("debug", "else block");
            }


            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");

            } else {
                this.thumbNail = "";

            }

            /*if (newDeskObj.length() > 0) {
                this.newdesk =  newDeskObj.get.getJ("new_desk");

            }*/


        } catch (JSONException e){

        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int x=0;x<array.length();x++) {
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
