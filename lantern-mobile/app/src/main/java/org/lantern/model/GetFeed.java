package org.lantern.model;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.lantern.activity.LanternMainActivity;
import org.lantern.R;

import java.util.ArrayList; 
import java.util.Locale;

import go.lantern.Lantern;

public class GetFeed extends AsyncTask<Boolean, Void, ArrayList<String>> {
    private static final String TAG = "GetFeed";

    private LanternMainActivity activity;
    private String allString;
    private ProgressBar progressBar;

    public GetFeed(LanternMainActivity activity) {
        this.activity = activity;
        this.allString = activity.getResources().getString(R.string.all_feeds);
        progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // show progress bar
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<String> doInBackground(Boolean... params) {

        if (!Utils.isNetworkAvailable(activity)) {
            // if there is no network connection, return right away
            return null;
        }

        boolean shouldProxy = params[0];
        String locale = Locale.getDefault().toString();
        Log.d(TAG, String.format("Fetching public feed: locale=%s", locale));
        final ArrayList<String> sources = new ArrayList<String>();

        Lantern.GetFeed(locale, allString, shouldProxy, new Lantern.FeedProvider() {
            public void AddSource(String source) {
                sources.add(source);
            }
        });
        return sources;
    }

    @Override
    protected void onPostExecute(ArrayList<String> sources) {
        super.onPostExecute(sources);

        progressBar.setVisibility(View.GONE);
        activity.setupFeed(sources);
    }
}   

