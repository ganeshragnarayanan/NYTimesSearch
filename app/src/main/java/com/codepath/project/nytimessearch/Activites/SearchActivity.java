package com.codepath.project.nytimessearch.Activites;

import android.app.SearchManager;
import android.content.Context;
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

import com.codepath.project.nytimessearch.R;
import com.codepath.project.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.project.nytimessearch.decorators.SpacesItemDecoration;
import com.codepath.project.nytimessearch.fragments.EditNameDialogFragment;
import com.codepath.project.nytimessearch.models.Article;
import com.codepath.project.nytimessearch.net.ArticleClient;
import com.codepath.project.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import com.codepath.project.nytimessearch.utils.RecyclerItemClickListener;
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
    private EndlessRecyclerViewScrollListener scrollListener;
    private static final String API_KEY = "c232ab9374584104b91cc354d49784d4";

    // filters
    String searchDate1 = "";
    String searchOrder1 = "";
    boolean searchArts;
    boolean searchFashion;
    boolean searchSports;
    private ArticleClient client;

    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    /* callback for the filters dialog */
    public void getResult(String searchDate, String searchOrder, boolean arts, boolean fashion, boolean sports) {
        searchDate1 = searchDate;
        searchOrder1 = searchOrder;
        searchArts = arts;
        searchFashion = fashion;
        searchSports = sports;
        Log.d("debug", "getResult");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            articles.clear();
            adapter.notifyDataSetChanged();
            scrollListener.resetState();
            fetchArticlesFromWeb(searchQuery, 0);
        }

    }

    /* invoke the filters dialog */
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

        RecyclerView rvResults = (RecyclerView) findViewById(R.id.rvResults);

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        rvResults.setAdapter(adapter);

        StaggeredGridLayoutManager gridLayoutManger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManger);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvResults.addItemDecoration(itemDecoration);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvResults.addItemDecoration(decoration);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManger) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("debug", "onLoadMore");
                loadNextDataFromApi(page);
            }
        };
        rvResults.addOnScrollListener(scrollListener);

        rvResults.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvResults, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Article article = articles.get(position);

                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
                        builder.addDefaultShareMenuItem();
                        CustomTabsIntent customTabsIntent = builder.build();

                        customTabsIntent.launchUrl(view.getContext(), Uri.parse(article.getWebUrl()));

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        fetchArticlesFromWeb(searchQuery, offset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        /*SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }*/
        final SearchView searchView = (SearchView) searchItem.getActionView();



        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("debug", "onQueryTextSubmit");
                searchQuery = query;
                onArticlesSearch(null, query);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
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
        Log.d("debug", "first");
        showEditDialog();
        Log.d("debug", "second");
    }

    public void onArticlesSearch(View view, String query) {

        articles.clear();
        adapter.notifyDataSetChanged();
        scrollListener.resetState();

        fetchArticlesFromWeb(query, 0);
    }

    public void fetchArticlesFromWeb(String query, int page) {
        String newsDesk = "news_desk:(";
        String newsCategories = "";
        client = new ArticleClient(this);

        if (client.isNetworkAvailable() == false || client.isOnline() == false) {
            CharSequence text = "Check your internet connection";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }

        /* construct parameters */
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
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

        client = new ArticleClient(this);
        client.getArticles(params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articlesResults = null;
                try {
                    articlesResults = response.getJSONObject("response").getJSONArray("docs");
                    ArrayList<Article> results = Article.fromJsonArray(articlesResults);
                    articles.addAll(results);
                    //adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeInserted(adapter.getItemCount(), articles.size() - 1);

                    /*final int curSize = adapter.getItemCount();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                        }
                    });*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Context context = getApplicationContext();
                CharSequence text = "Error loading page";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Context context = getApplicationContext();
                CharSequence text = "Error loading page" +
                        "!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

}
