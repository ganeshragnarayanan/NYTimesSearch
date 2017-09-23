package com.codepath.project.nytimessearch.Activites;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.nytimessearch.Article;
import com.codepath.project.nytimessearch.ArticleArrayAdapter;
import com.codepath.project.nytimessearch.Contact;
import com.codepath.project.nytimessearch.EditNameDialogFragment;
import com.codepath.project.nytimessearch.EndlessRecyclerViewScrollListener;
import com.codepath.project.nytimessearch.R;
import com.codepath.project.nytimessearch.SpacesItemDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329

public class SearchActivity extends AppCompatActivity {

    TextView etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    ArrayList<Contact> contacts;
    private EndlessRecyclerViewScrollListener scrollListener;

    // filters
    String searchDate1 = "";
    String searchOrder1 = "";
    String searchCategory1 = "";
    boolean searchArts;
    boolean searchFashion;
    boolean searchSports;

    String searchQuery;

    // https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:("Arts" "Sports" "Fashion" "Style")&api-key=227c750bb7714fc39ef1559ef1bd8329
    //https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:("Arts" "Sports" "Fashion" "Style")&page=2&api-key=227c750bb7714fc39ef1559ef1bd8329

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void getResult(String searchDate, String searchOrder, boolean arts, boolean fashion, boolean sports) {
        searchDate1 = searchDate;
        searchOrder1 = searchOrder;
        searchArts = arts;
        searchFashion = fashion;
        searchSports = sports;
    }

    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Filters");


        Bundle args = new Bundle();
        args.putString("date", searchDate1);
        args.putString("sort", searchOrder1);
        args.putBoolean("arts", searchArts);
        args.putBoolean("fashion", searchFashion);
        args.putBoolean("sports", searchSports);

        editNameDialogFragment.setArguments(args);
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }

    public void setupViews() {

        //etQuery = (TextView) findViewById(R.id.tvText);
        //GridView gvResults = (GridView) findViewById(R.id.gvResults);
        RecyclerView rvResults = (RecyclerView) findViewById(R.id.rvResults);


        //Button btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        rvResults.setAdapter(adapter);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //rvResults.setLayoutManager(linearLayoutManager);

        //GridLayoutManager gridLayoutManger = new GridLayoutManager(this, 2);
        //rvResults.setLayoutManager(gridLayoutManger);
        StaggeredGridLayoutManager gridLayoutManger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManger);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvResults.addItemDecoration(itemDecoration);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvResults.addItemDecoration(decoration);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManger) {
        //scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("debug", "onLoadMore");
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
                /*ArrayList<Article> moreContacts = Article.fromJsonArray(10, page);
                int curSize = adapter.getItemCount();
                allContacts.addAll(moreContacts);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, allContacts.size() - 1);
                    }
                });*/
            }
        };
        rvResults.addOnScrollListener(scrollListener);

        rvResults.addOnItemTouchListener(
                new com.codepath.project.nytimessearch.RecyclerItemClickListener(this, rvResults, new com.codepath.project.nytimessearch.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("debug", "onclick");
                        // do whatever
                        // chrome (start)
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
                        builder.addDefaultShareMenuItem();
                        CustomTabsIntent customTabsIntent = builder.build();
                        Article article = articles.get(position);
                        customTabsIntent.launchUrl(view.getContext(), Uri.parse(article.getWebUrl()));
                        // chrome (end)

                        /*Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);
                        i.putExtra("url", article.getWebUrl());
                        startActivity(i);*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        //fetchArticles(searchQuery, offset);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "c232ab9374584104b91cc354d49784d4");
        params.put("page", offset);
        params.put("q", searchQuery);

        String newsDesk = "news_desk:(";
        String newsCategories = "";

        if (!searchDate1.isEmpty()) {
            params.put("begin_date", searchDate1);
        }

        if (!searchOrder1.isEmpty()) {
            params.put("sort", searchOrder1);
        }

        if (searchArts) {
            newsCategories += "\"Arts\"";
        }

        if (searchSports) {
            newsCategories += " \"Sports\"";
        }

        if (searchFashion) {
            newsCategories += " \"Fashion\"";
        }

        if (searchArts || searchFashion || searchSports) {
            params.put("fq", newsDesk + newsCategories + ")");
        }
        //params.put("fq", "news_desk:(\"Arts\" \"Sports\" \"Fashion\" \"Style\")");

        Log.d("debug", "Query Parameters");
        Log.d("debug", params.toString());

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("debug", "on success");
                JSONArray articlesResults = null;
                try {
                    articlesResults = response.getJSONObject("response").getJSONArray("docs");
                    //adapter.addAll(Article.fromJsonArray(articlesResults));
                    ArrayList<Article> results = Article.fromJsonArray(articlesResults);
                    articles.addAll(results);
                    adapter.notifyDataSetChanged();

                    //adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d("debug", "got exception $$$$$$$$$$$$$$$$$$$$$$$$$");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Context context = getApplicationContext();
                CharSequence text = "No Internet Connection 2" +
                        "!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                onArticlesSearch(null, query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onComposeAction(MenuItem mi) {
        showEditDialog();
    }

    public void onArticlesSearch(View view, String query) {
        fetchArticles(query, 0);
    }

    public void fetchArticles(String query, int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        String newsDesk = "news_desk:(";
        String newsCategories = "";

////https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112
/// &sort=oldest&fq=news_desk:("Arts" "Sports" "Fashion" "Style")&page=2&api-key=227c750bb7714fc39ef1559ef1bd8329

        articles.clear();
        adapter.notifyDataSetChanged();
        scrollListener.resetState();

        if (isNetworkAvailable() == false || isOnline() == false) {
            CharSequence text = "Check your internet connection";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }

        RequestParams params = new RequestParams();
        params.put("api-key", "c232ab9374584104b91cc354d49784d4");
        params.put("page", page);
        params.put("q", query);
        if (!searchDate1.isEmpty()) {
            params.put("begin_date", searchDate1);
        }

        if (!searchOrder1.isEmpty()) {
            params.put("sort", searchOrder1);
        }

        if (searchArts) {
            newsCategories += "\"Arts\"";
        }

        if (searchSports) {
            newsCategories += " \"Sports\"";
        }

        if (searchFashion) {
            newsCategories += " \"Fashion\"";
        }

        if (searchArts || searchFashion || searchSports) {
            params.put("fq", newsDesk + newsCategories + ")");
        }
        //params.put("fq", "news_desk:(\"Arts\" \"Sports\" \"Fashion\" \"Style\")");

        Log.d("debug", "Query Parameters");
        Log.d("debug", params.toString());

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articlesResults = null;
                try {
                    articlesResults = response.getJSONObject("response").getJSONArray("docs");
                    ArrayList<Article> results = Article.fromJsonArray(articlesResults);
                    articles.addAll(results);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Context context = getApplicationContext();
                CharSequence text = "No Internet Connection 1!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Context context = getApplicationContext();
                CharSequence text = "No Internet Connection 2" +
                        "!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
