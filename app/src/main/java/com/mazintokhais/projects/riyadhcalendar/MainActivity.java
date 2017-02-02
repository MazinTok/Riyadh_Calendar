package com.mazintokhais.projects.riyadhcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import static com.mazintokhais.projects.riyadhcalendar.AnalyticsApplication.languageToLoad;
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ListView theListView;
    FoldingCellListAdapter adapter;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private Tracker mTracker;
    AnalyticsApplication application;
     SwitchCompat actionView;
    SharedPreferences prefs;
    private MobileServiceClient mClient;
    private MobileServiceTable<News> mNewsTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load tasks from preference
        prefs = getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE);
        Localization();
        initalView();
        initalLisners();
        initalAure();

            // Obtain the shared Tracker instance.
            application = (AnalyticsApplication) getApplication();
            mTracker = application.getDefaultTracker();
            sendScreenImageName();


         ArrayList<News> items;
        items = News.getTestingList();

            try {

                items= (ArrayList<News>) ObjectSerializer.deserialize(prefs.getString("TASKS", ObjectSerializer.serialize(new ArrayList<News>())));

            } catch (IOException e) {
//                items = News.getTestingList();
                e.printStackTrace();
            }

            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
            adapter = new FoldingCellListAdapter(this, items);

            // set elements to adapter
            theListView.setAdapter(adapter);


//            // add default btn handler for each request btn on each item if custom handler not found
//            adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
//                }
//            });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Get the action view used in your toggleservice item
        final MenuItem toggleservice = menu.findItem(R.id.myswitch);
        toggleservice.setActionView(R.layout.switch_layout);
        actionView = (SwitchCompat) toggleservice.getActionView().findViewById(R.id.switchForActionBar);
        if (languageToLoad.equals("en"))
        {
            actionView.setChecked(true);
        }
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // change langouge

                if(isChecked) {
                     languageToLoad  = "en";
                } else {
                     languageToLoad  = "ar";
                }

//                Localization();

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("TASKS");
                editor.apply();

                editor.putString("LANG", languageToLoad);
                editor.commit();
//                editor.clear();
                Intent intent = new Intent( MainActivity.this, SplashActivity.class);
                finish();
                startActivity(intent);

            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void initalView(){
        setTitle(R.string.app_name);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        // refrech page-------------
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setWaveColor(Color.rgb(59, 46, 91));
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);

}
    private void initalLisners(){
        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });

        // refrech page-------------

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new LongOperation().execute("s");
            }
        });
    }
    private void Localization() {
        if(prefs.contains("LANG")) {
            languageToLoad = prefs.getString("LANG", "");
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
    private void sendScreenImageName() {

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + languageToLoad);
        mTracker.setScreenName("view~" + languageToLoad);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]
    }
    private class LongOperation extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> doInBackground(String... params) {

            String url ;
            HTMLRemoverParser ne = new HTMLRemoverParser();


            //  checking device language
            if(prefs.contains("LANG")) {
                languageToLoad = prefs.getString("LANG", "");
            }
            if  (languageToLoad.equals("ar"))
            {
                url = "  http://www.eyeofriyadh.com/ar/rss/events.php?lang=ar&cat=riyadh";
            }
            else
            {
                url = "http://www.eyeofriyadh.com/rss/events.php?cat=riyadh";
            }

            ArrayList<News> results= new ArrayList<News>();
            ArrayList<News> resultFromRS = ne.HTMLRemoverParser(url);

             //adding events from service
            try{
                results = mNewsTable.where().field("lang").
                        eq(val(languageToLoad)).execute().get();

            } catch (final Exception e){
                Log.d("News",e.toString());
            }

            //adding events from RSS
            if ( resultFromRS != null)
                results.addAll(resultFromRS);
            //sorting list
            Collections.sort(results, new Comparator<News>() {
                public int compare(News o1, News o2) {
                    if (o1.getdate() == null || o2.getdate() == null)
                        return 0;
                    return o1.getdate().compareTo(o2.getdate());
                }
            });
            return   results;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (listview == null )
//                Pbar2.setVisibility(ProgressBar.VISIBLE);

        }

        protected void onPostExecute(ArrayList<News> result) {
            super.onPostExecute(result);

            if (!result.isEmpty()) {

                adapter = new FoldingCellListAdapter(MainActivity.this, result);
                theListView.setAdapter(adapter);

                    // save the task list to preference
                     prefs = getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    try {
                        editor.putString("TASKS", ObjectSerializer.serialize(result));
                        editor.putString("LANG", languageToLoad);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();



            }
            else
            {
                Toast.makeText(MainActivity.this, "حدث خطأ بالاتصال",
                        Toast.LENGTH_SHORT).show();

            }
            mWaveSwipeRefreshLayout.setRefreshing(false);
        }
    }
    private void initalAure()
    {
        try{
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://riyadhcal.azurewebsites.net",
                    this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            // Get the Mobile Service Table instance to use
            mNewsTable = mClient.getTable(News.class);
        } catch (MalformedURLException e) {
//        createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
            Log.d("News initalAure",e.toString());
        } catch (Exception e){
            Log.d("News initalAure",e.toString());
//        createAndShowDialog(e, "Error");
        }
    }

}
