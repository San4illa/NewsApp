package com.hfad.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    private static final String URL = "https://newsapi.org/v2/everything";
    private static final String API_KEY = "YOUR_API_KEY";

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter adapter;
    private ImageView emptyView;
    private ArrayList<News> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewsAdapter(news);
        recyclerView.setAdapter(adapter);

        emptyView = findViewById(R.id.empty_view);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            emptyView.setImageResource(R.drawable.no_internet_connection);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String query = sharedPreferences.getString(
                getString(R.string.settings_query_key),
                getString(R.string.settings_query_default));

        String sortBy = sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        Uri baseUri = Uri.parse(URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q", query);
        builder.appendQueryParameter("sortBy", sortBy);
        builder.appendQueryParameter("apiKey", API_KEY);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        if (news.size() > 0) {
            this.news.addAll(news);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setImageResource(R.drawable.no_result_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        adapter.notifyDataSetChanged();
    }
}
