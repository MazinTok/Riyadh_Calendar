package com.mazintokhais.projects.riyadhcalendar;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mazoo_000 on 08/04/2015.
 */
public class News implements Serializable {
    @com.google.gson.annotations.SerializedName("id")
    private String id;
    private String Txt;
    private String Url;
    private String ImageURL;
    private String content;
    private String descraption;
    private String pubDate;
    private String Detials;
    private String Location;
    private View.OnClickListener requestBtnClickListener;
    public String getDescraption() {
        return descraption;
    }

    public void setDescraption(String descraption) {
        this.descraption = descraption;
    }
    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public News(String _txt, String url) {
        Txt = _txt;
        Url = url;
        setContent("");
    }
    public News() {
    }
    public String getTxt() {
        return Txt;
    }
    public String getUrl() {
        return Url;
    }
    public void setTxt(String _txt) {
        Txt = _txt;
    }
    public void setUrl(String url) {
        Url = url;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImageURL() {
        return ImageURL;
    }
    public void setImageURL(String mImageURL) {
        this.ImageURL = mImageURL;
    }
    public String getDetials() {
        return Detials;
    }
    public void setDetials(String mDetials) {
        this.Detials = mDetials;
    }
    public String getLocation() {
        return Location;
    }
    public void setLocation(String mLocation) {
        this.Location = mLocation;
    }
    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }
    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
    public static ArrayList<News> getTestingList() {
        ArrayList<News> items = new ArrayList<>();
        items.add(new News("",""));
        items.add(new News("",""));

        return items;
    }
    public static ArrayList<News> getFileList(String json) {
        HTMLRemoverParser ne = new HTMLRemoverParser();
        ArrayList<News> items =  ne.FileParser(json);
        return items;
    }
    public Date getdate() {
        int t = getPubDate().indexOf("-")-1;
         String startDate = getPubDate().substring(0, t);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);

        try {
            return dateFormat.parse(startDate);


        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
