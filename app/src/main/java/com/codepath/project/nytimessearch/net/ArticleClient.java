package com.codepath.project.nytimessearch.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;


/**
 * Created by GANESH on 9/23/17.
 */

public class ArticleClient {
    private static final String API_BASE_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private AsyncHttpClient client;
    Context mContext;

    public ArticleClient(Context context) {
        this.client = new AsyncHttpClient();
        this.mContext = context;
    }

    // Method for accessing the search API
    public void getArticles(RequestParams params, JsonHttpResponseHandler handler) {

       // try {
            //String url = getApiUrl("search.json?q=");
            //client.get(url + URLEncoder.encode(query, "utf-8"), handler);
            client.get(API_BASE_URL, params, handler);
        /*} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
