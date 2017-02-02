package com.mazintokhais.projects.riyadhcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mazintokhais.projects.riyadhcalendar.AnalyticsApplication.languageToLoad;
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private MobileServiceClient mClient;
    private MobileServiceTable<News> mNewsTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initalAure();


        prefs = getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if(!prefs.contains("TASKS")){

            new LongOperation().execute("s");
        }
        else
        {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private class LongOperation extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> doInBackground(String... params) {
            String url ;
            HTMLRemoverParser ne = new HTMLRemoverParser();
            if(prefs.contains("LANG")) {
                languageToLoad = prefs.getString("LANG", "");
            }
            if  (languageToLoad.equals("ar"))
            {
                url = "http://www.eyeofriyadh.com/ar/rss/events.php?lang=ar&cat=riyadh";
            }
            else
            {
                url = "http://www.eyeofriyadh.com/rss/events.php?cat=riyadh";
            }
            ArrayList<News> results= new ArrayList<News>();
            ArrayList<News> resultFromRS = ne.HTMLRemoverParser(url);

            try{
                results  = mNewsTable.where().field("lang").
                         eq(val(languageToLoad)).execute().get();


            } catch (final Exception e){
//                createAndShowDialogFromTask(e, "Error");
                Log.d("News",e.toString());
            }


            if ( resultFromRS != null)
            results.addAll(resultFromRS);
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

                // save the task list to preference

                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putString("TASKS", ObjectSerializer.serialize(result));
                    editor.putString("LANG", languageToLoad);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor.commit();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(SplashActivity.this, "حدث خطأ بالاتصال",
                        Toast.LENGTH_SHORT).show();
//                finish();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
//            mWaveSwipeRefreshLayout.setRefreshing(false);
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
            Log.d("News initalAure",e.toString());
    } catch (Exception e){
            Log.d("News initalAure",e.toString());

    }
    }
}
