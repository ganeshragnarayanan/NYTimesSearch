package com.codepath.project.nytimessearch.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by GANESH on 9/23/17.
 */

public class ArticleClient {
    private static final String API_BASE_URL = "http://openlibrary.org/";
    private AsyncHttpClient client;

    public ArticleClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getArticles(final String query, RequestParams params, JsonHttpResponseHandler handler) {

       // try {
            //String url = getApiUrl("search.json?q=");
            //client.get(url + URLEncoder.encode(query, "utf-8"), handler);
            client.get(query, params, handler);
        /*} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }
}
