package com.mazintokhais.projects.riyadhcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        setContentView(R.layout.activity_splash);
        animation();

          prefs = getSharedPreferences("SHARED_PREFS_FILE", Context.MODE_PRIVATE);

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
                         eq(val(languageToLoad)).and().field("live").
                        eq(val(true)).execute().get();



            } catch (final Exception e){
             runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(SplashActivity.this,getString(R.string.internet_error),Toast.LENGTH_SHORT).show();
                }
            });

            }


            for (News item : results) {
                if(new Date().after(item.getEndDate()))
                    results.remove(item);
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
            initalAure();
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
                Toast.makeText(SplashActivity.this,getString(R.string.internet_error),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
//            mWaveSwipeRefreshLayout.setRefreshing(false);
        }
    }
    private void initalAure(){
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
            Toast.makeText(SplashActivity.this,getString(R.string.internet_error),Toast.LENGTH_SHORT).show();
    } catch (Exception e){
            Toast.makeText(SplashActivity.this,getString(R.string.internet_error),Toast.LENGTH_SHORT).show();

    }
    }
    private void animation(){

        ImageView img_logo = (ImageView) findViewById(R.id.img_logo);
        final ImageView img_light1 = (ImageView) findViewById(R.id.img_light1);
        final ImageView img_light2 = (ImageView) findViewById(R.id.img_light2);

        Picasso.with(this)
                .load(R.drawable.ic_splash)
                .error(R.mipmap.ic_app)
                .placeholder(R.mipmap.ic_app)
                .fit()
                .into(img_logo);

        final Animation RotationLeft ;
        RotationLeft = AnimationUtils.loadAnimation(this, R.anim.rotation_left);
        final Animation RotationLeft2 ;
        RotationLeft2 = AnimationUtils.loadAnimation(this, R.anim.rotation_left2);

        final  Animation RotationRight;
        RotationRight = AnimationUtils.loadAnimation(this, R.anim.rotation_right);
        final Animation RotationRight2;
        RotationRight2 = AnimationUtils.loadAnimation(this, R.anim.rotation_right2);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int height = size.y /5;

//        LogoAnimation = new TranslateAnimation(0, 0, 0, -height);
//        LogoAnimation.setDuration(700);
//        LogoAnimation.setFillAfter(true);

//        RotationLeft.setFillAfter(true);
//        RotationRight.setFillAfter(true);

        img_light1.setDrawingCacheEnabled(true);
        img_light2.setDrawingCacheEnabled(true);
//        img_logo.startAnimation(LogoAnimation);
        img_light1.startAnimation(RotationLeft);
        img_light2.startAnimation(RotationRight);

//        img_logo.setVisibility(View.VISIBLE);

        Animation.AnimationListener leftStartAnimationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_light1.startAnimation(RotationLeft2);
            }
        };
        RotationLeft.setAnimationListener(leftStartAnimationListener);

        Animation.AnimationListener leftEndAnimationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_light1.startAnimation(RotationLeft);
            }
        };
        RotationLeft2.setAnimationListener(leftEndAnimationListener);

        Animation.AnimationListener RightStartAnimationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_light2.startAnimation(RotationRight2);
            }
        };
        RotationRight.setAnimationListener(RightStartAnimationListener);

        Animation.AnimationListener RightEndAnimationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_light2.startAnimation(RotationRight);
            }
        };
        RotationRight2.setAnimationListener(RightEndAnimationListener);
    }
}
