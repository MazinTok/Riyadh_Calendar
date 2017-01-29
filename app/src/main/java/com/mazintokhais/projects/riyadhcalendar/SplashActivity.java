package com.mazintokhais.projects.riyadhcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static com.mazintokhais.projects.riyadhcalendar.AnalyticsApplication.languageToLoad;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
                url = "  http://www.eyeofriyadh.com/ar/rss/events.php?lang=ar&cat=riyadh";
            }
            else
            {
                url = "http://www.eyeofriyadh.com/rss/events.php?cat=riyadh";
            }
            return   ne.HTMLRemoverParser(url);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (listview == null )
//                Pbar2.setVisibility(ProgressBar.VISIBLE);

        }

        protected void onPostExecute(ArrayList<News> result) {
            super.onPostExecute(result);

            if (result != null) {

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
}
