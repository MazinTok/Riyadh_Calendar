package com.mazintokhais.projects.riyadhcalendar;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mazoo_000 on 08/04/2015.
 */
public class News implements Serializable {
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
//        items.add(new News());
//        items.add(new Item("$23", "$116", "W 36th St, NY, 10015", "W 114th St, NY, 10037", 10, "TODAY", "11:10 AM"));
//        items.add(new Item("$63", "$350", "W 36th St, NY, 10029", "56th Ave, NY, 10041", 0, "TODAY", "07:11 PM"));
//        items.add(new Item("$19", "$150", "12th Ave, NY, 10012", "W 57th St, NY, 10048", 8, "TODAY", "4:15 AM"));
//        items.add(new Item("$5", "$300", "56th Ave, NY, 10041", "W 36th St, NY, 10029", 0, "TODAY", "06:15 PM"));
        return items;
    }
    public static ArrayList<News> getFileList(String json) {
        HTMLRemoverParser ne = new HTMLRemoverParser();
        ArrayList<News> items =  ne.FileParser(json);
        return items;
    }
}
