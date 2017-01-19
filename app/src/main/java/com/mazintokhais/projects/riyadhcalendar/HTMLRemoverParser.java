package com.mazintokhais.projects.riyadhcalendar;

/**
 * Created by mazoo_000 on 14/04/2015.
 */

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HTMLRemoverParser {

    HTMLRemoverBean objBean;
    Vector<HTMLRemoverBean> vectParse;

    int mediaThumbnailCount;
    boolean urlflag;
    int count = 0;

    // for https workarawnd
    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
    public ArrayList<News> HTMLRemoverParser(String Url) {
        try {

            ArrayList<News> LNews = new ArrayList();

            vectParse = new Vector<HTMLRemoverBean>();
            URL url = new URL(Url);
            trustEveryone();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

           // System.out.println("Connection is : " + con);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            //System.out.println("Reader :" + reader);

            String inputLine;
            String fullStr = "";
            while ((inputLine = reader.readLine()) != null)
                fullStr = fullStr.concat(inputLine + "\n");

            InputStream istream = url.openStream();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
//==============

            //=============================
           Document doc = builder.parse(istream);

            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("item");

            System.out.println();

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    News temp = new News();
                    Element eElement = (Element) nNode;

                    objBean = new HTMLRemoverBean();
                    vectParse.add(objBean);

                    objBean.title = getTagValue("title", eElement);

                    objBean.link = getTagValue("link", eElement);
                    objBean.pubdate = getTagValue("pubDate", eElement);
                    objBean.description =getTagValue("description", eElement);

                    Log.d("item",nNode.toString());

                    temp.setTxt(objBean.title);
                    temp.setUrl(objBean.link);
                   // temp.setPubDate(objBean.pubdate);

                    temp.setPubDate(objBean.pubdate);
//<img src="http://www.eyeofriyadh.com/events/events_images/10806732fbabf.jpg"
// width="100" hspace="5" vspace="5" align="left"/>
// Fri, 06 Jan 2017 - Fri, 06 Jan 2017<br><br>مدارس العليا<br><br>تقيم مدارسنا في قسم البنات كرنفالا احتفاليا تحت مسمى ( ك

                    int j = objBean.description.indexOf("<br>");
                    int t = objBean.description.indexOf("/>")+2;
                    //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));

                    temp.setDescraption(objBean.description);
                    temp.setDetials(objBean.description.substring(t));

                    temp.setContent(objBean.description.substring(0,t));
                    temp.setPubDate(objBean.description.substring(t,j));
                    temp.setDetials(temp.getDetials().replace(temp.getPubDate(),""));
//                    int a = temp.getDetials().indexOf("<br>");
//                    temp.setDetials(temp.getDetials().replace("<br><br>","1"));
                    temp.setDetials(temp.getDetials().substring(8));
                    temp.setLocation(temp.getDetials().substring(0,temp.getDetials().indexOf("<br>")));
                    temp.setDetials(temp.getDetials().replace(temp.getLocation(),""));
                    temp.setDetials(temp.getDetials().replace("<br>",""));




                    String URL ;
                    int a = temp.getContent().indexOf("http");
                    int b = temp.getContent().indexOf("width")-2;
                    //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));
                    temp.setImageURL( temp.getContent().substring(a,b));

                    LNews.add(temp);

                }
            }
            return LNews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
public ArrayList<News> FileParser(String json)
{
    try {

        ArrayList<News> LNews = new ArrayList();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(json);

        doc.getDocumentElement().normalize();


        NodeList nList = doc.getElementsByTagName("item");

        System.out.println();

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                News temp = new News();
                Element eElement = (Element) nNode;

                objBean = new HTMLRemoverBean();
                vectParse.add(objBean);

                objBean.title = getTagValue("title", eElement);
                objBean.link = getTagValue("link", eElement);
                objBean.pubdate = getTagValue("pubDate", eElement);
                objBean.description =getTagValue("description", eElement);

                Log.d("item",nNode.toString());

                temp.setTxt(objBean.title);
                temp.setUrl(objBean.link);
                // temp.setPubDate(objBean.pubdate);

                temp.setPubDate(objBean.pubdate);

                int j = objBean.description.indexOf("<br>");
                int t = objBean.description.indexOf("/>")+2;
                temp.setDescraption(objBean.description);
                temp.setDetials(objBean.description.substring(t));

                temp.setContent(objBean.description.substring(0,t));
                temp.setPubDate(objBean.description.substring(t,j));
                temp.setDetials(temp.getDetials().replace(temp.getPubDate(),""));
                temp.setDetials(temp.getDetials().substring(8));
                temp.setLocation(temp.getDetials().substring(0,temp.getDetials().indexOf("<br>")));
                temp.setDetials(temp.getDetials().replace(temp.getLocation(),""));
                temp.setDetials(temp.getDetials().replace("<br>",""));

                String URL ;
                int a = temp.getContent().indexOf("http");
                int b = temp.getContent().indexOf("width")-2;
                //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));
                temp.setImageURL( temp.getContent().substring(a,b));

                LNews.add(temp);

            }
        }
    return LNews;

    } catch (Exception e) {
            e.printStackTrace();
        return null;
    }
}
    public List<News> HTMLRemoverParserEvents(String Url) {
        try {

            ArrayList<News> LNews = new ArrayList<News>();

            vectParse = new Vector<HTMLRemoverBean>();
            URL url = new URL(Url);
            URLConnection con = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

            String inputLine;
            String fullStr = "";
            while ((inputLine = reader.readLine()) != null)
                fullStr = fullStr.concat(inputLine + "\n");

            InputStream istream = url.openStream();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();

            Document doc = builder.parse(istream);

            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("item");

            System.out.println();

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    News temp = new News();
                    Element eElement = (Element) nNode;

                    objBean = new HTMLRemoverBean();
                    vectParse.add(objBean);

                    objBean.title = getTagValue("description", eElement);
                    objBean.link = getTagValue("link", eElement);
                    objBean.pubdate = getTagValue("pubDate", eElement);

                    temp.setTxt( objBean.title);
                    // System.out.println("Description is : " + ObjNB.description);
                    temp.setUrl( objBean.link);
                    LNews.add(temp);

                }
            }

            return LNews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();

    }

    public static void main(String[] args) {
        new HTMLRemoverParser();
    }

}