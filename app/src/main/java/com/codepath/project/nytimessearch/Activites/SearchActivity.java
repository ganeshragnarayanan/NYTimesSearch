package com.codepath.project.nytimessearch.Activites;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.codepath.project.nytimessearch.Article;
import com.codepath.project.nytimessearch.ArticleArrayAdapter;
import com.codepath.project.nytimessearch.Contact;
import com.codepath.project.nytimessearch.EditNameDialogFragment;
import com.codepath.project.nytimessearch.EndlessRecyclerViewScrollListener;
import com.codepath.project.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;




public class SearchActivity extends AppCompatActivity {

    TextView etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    ArrayList<Contact> contacts;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //getSupportActionBar().setCustomView(R.layout.actionbar_title);

        setupViews();




//        adapter.notifyDataSetChanged();


        /*RecyclerView rvResults = (RecyclerView) findViewById(R.id.rvResults);
        contacts = Contact.createContactsList(20);
        adapter = new ArticleArrayAdapter(this, contacts);
        rvResults.setAdapter(adapter);

        rvResults.setLayoutManager(new LinearLayoutManager(this));*/


    }

    public void getResult(String searchText, String b, String c) {
        Log.d("debug", "reachedBack");
        Log.d("debug", searchText);
    }

    private void showEditDialog() {

        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }

    public void setupViews() {

        etQuery = (TextView) findViewById(R.id.tvText);
        //GridView gvResults = (GridView) findViewById(R.id.gvResults);
        RecyclerView rvResults = (RecyclerView) findViewById(R.id.rvResults);


        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        /*
        JSONArray articlesResults = null;
        ArrayList<Article> results = Article.fromJsonArray(articlesResults);
        articles.addAll(results);*/


        rvResults.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvResults.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi(page);
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

        //articles.clear();
        /*rvResults.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("url", article.getWebUrl());
                startActivity(i);

            }
        });*/
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
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
                Log.d("debug", "onQuerytextsubmit");
                // Toast like print

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("debug", "onQueryTextChange");
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void onComposeAction(MenuItem mi) {
        Log.d("debug", "onmenuclick");
        showEditDialog();
    }

    public void onArticlesSearch(View view) {
        Log.d("debug", "onArticlesSearch");
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "c232ab9374584104b91cc354d49784d4");
        params.put("page", 0);
        params.put("q", query);



        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.d("debug", "on success");
                JSONArray articlesResults = null;
                try {
                    articlesResults = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("debug","got results");
                    //adapter.addAll(Article.fromJsonArray(articlesResults));
                    ArrayList<Article> results = Article.fromJsonArray(articlesResults);
                    articles.addAll(results);
                    Log.d("debug", "notify adapter");
                    adapter.notifyDataSetChanged();

                    Log.d("debug", articles.toString());
                    //adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }



}
