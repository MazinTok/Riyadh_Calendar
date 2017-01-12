package com.mazintokhais.projects.riyadhcalendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<News> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context context;

    public FoldingCellListAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        News item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder

            viewHolder.txtLineOne = (TextView) cell.findViewById(R.id.txt_line1);
            viewHolder.txtLineTwo = (TextView) cell.findViewById(R.id.txt_line2);
//            vh.btnGo = (Button) convertView.findViewById(R.id.btn_go);
            viewHolder.mimgEvent = (ImageView) cell.findViewById(R.id.img_event);

//-----------------------------------
            viewHolder.txtLineOneContent = (TextView) cell.findViewById(R.id.dilg_txt_line1);
            viewHolder.txtLineTwoContent = (TextView) cell.findViewById(R.id.dilg_txt_line2);
            viewHolder.txtTital = (TextView) cell.findViewById(R.id.dilg_txt_tital);
//        DynamicHeightTextView txtTime = (DynamicHeightTextView) dialog.findViewById(R.id.dilg_txt_time);
            viewHolder.txtDetiales = (TextView) cell.findViewById(R.id.dilg_txt_detiales);
            viewHolder.txtLocation = (TextView) cell.findViewById(R.id.dilg_txt_location);
            viewHolder.mimgEventContent = (ImageView) cell.findViewById(R.id.dilg_img_event);
            viewHolder.btn_share = (ImageButton) cell.findViewById(R.id.btn_share);
            viewHolder.btn_add_calndar = (ImageButton) cell.findViewById(R.id.btn_add_calenar);
            viewHolder.btn_map = (ImageButton) cell.findViewById(R.id.btn_map);
            viewHolder.txtLink = (TextView)cell.findViewById(R.id.dilg_txt_link);
            //-----------------------------------
//            viewHolder.price = (TextView) cell.findViewById(R.id.title_price);
//            viewHolder.time = (TextView) cell.findViewById(R.id.title_time_label);
//            viewHolder.date = (TextView) cell.findViewById(R.id.title_date_label);
//            viewHolder.fromAddress = (TextView) cell.findViewById(R.id.title_from_address);
//            viewHolder.toAddress = (TextView) cell.findViewById(R.id.title_to_address);
//            viewHolder.requestsCount = (TextView) cell.findViewById(R.id.title_requests_count);
//            viewHolder.pledgePrice = (TextView) cell.findViewById(R.id.title_pledge);
//            viewHolder.contentRequestBtn = (TextView) cell.findViewById(R.id.content_request_btn);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (!getItem(position).getTxt().equals("") ) {
            //-------------------------------------------
            viewHolder.txtLineOne.setText(getItem(position).getTxt());
            String dates = getItem(position).getPubDate().replace("2016", "");
            dates = dates.replace("2017", "");
            viewHolder.txtLineTwo.setText(dates);

//        String URL ;
            int j = getItem(position).getContent().indexOf("http");
            int t = getItem(position).getContent().indexOf("width") - 2;
            //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));
//        URL = getItem(position).getContent().substring(j,t);
            String URL;
            URL = getItem(position).getImageURL();// mData.get(position).getContent().substring(j,t);

            Picasso.with(getContext())
                    .load(URL)
                    .error(R.drawable.ic_launcher)
                    .placeholder(R.drawable.ic_launcher)
//                .resize(Integer.valueOf(((int) positionHeight)).intValue() * 745, 745)
                    .fit()
//                .centerCrop()
                    .into(viewHolder.mimgEvent);

//-------------------------------------------

            dates = getItem(position).getPubDate().replace("2016", "");
            dates = dates.replace("2017", "");
            viewHolder.txtLineOneContent.setText(dates);
//        txtLineTwo.setText(dates);
            viewHolder.txtDetiales.setText(getItem(position).getDetials());
            viewHolder.txtLocation.setText(getItem(position).getLocation());
            viewHolder.txtTital.setText(getItem(position).getTxt());
            viewHolder.txtLink.setText(getItem(position).getUrl());
//        txtTime.setText(dates);
//        viewHolder.btn_share.setMaxHeight(txtLineOne.getHeight());
//
//            String URL ;
//            int j =  mData.get(position).getContent().indexOf("http");
//            int t =  mData.get(position).getContent().indexOf("width")-2;
            //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));
//        String URL ;
//        URL = getItem(position).getImageURL();// mData.get(position).getContent().substring(j,t);

            Picasso.with(getContext())
                    .load(URL)
                    .error(R.drawable.ic_launcher)
                    .placeholder(R.drawable.ic_launcher)
//                .resize(Integer.valueOf(((int) positionHeight)).intValue() * 745, 745)
                    .fit()
//                .centerCrop()
                    .into(viewHolder.mimgEventContent);

            final String tital = getItem(position).getTxt();
            final String time = getItem(position).getPubDate();
            t = getItem(position).getPubDate().indexOf("-");
            final String startDate = getItem(position).getPubDate().substring(0, t);
            final String endDate = getItem(position).getPubDate().substring(t + 2);
            final String location = getItem(position).getLocation();
            final String link = getItem(position).getUrl();

            //------------------------------------------------
            viewHolder.btn_add_calndar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //temp.setContent("<p  align=\"center\">" + objBean.title + "</p>" + objBean.description.substring(0,j));


                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
                    try {
                        Date date = dateFormat.parse(startDate);
                        Date date2 = dateFormat.parse(endDate);

                        Calendar cal = Calendar.getInstance();
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", date.getTime());
                        intent.putExtra("allDay", true);
                        intent.putExtra("rrule", "FREQ=YEARLY");
                        intent.putExtra("endTime", date2.getTime());
                        intent.putExtra("title", tital);
                        context.startActivity(intent);

                        String out = dateFormat2.format(date);
                        Log.e("Time", out);
                    }
                    catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                }
            });

            viewHolder.btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Your body here";
                    String shareSub = "Your subject here";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tital);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, time);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
            });

            viewHolder.btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });
            viewHolder.txtLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    context.startActivity(browserIntent);

                }
            });
            //---------------------------------

            // bind data from selected element to view through view holder
//        viewHolder.price.setText(item.getPrice());
//        viewHolder.time.setText(item.getTime());
//        viewHolder.date.setText(item.getDate());
//        viewHolder.fromAddress.setText(item.getFromAddress());
//        viewHolder.toAddress.setText(item.getToAddress());
//        viewHolder.requestsCount.setText(String.valueOf(item.getRequestsCount()));
//        viewHolder.pledgePrice.setText(item.getPledgePrice());

            // set custom btn handler for list item from that item
//        if (item.getRequestBtnClickListener() != null) {
//            viewHolder.btn_map.setOnClickListener(item.getRequestBtnClickListener());
//        } else {
//            // (optionally) add "default" handler if no handler found in item
//            viewHolder.btn_map.setOnClickListener(defaultRequestBtnClickListener);
//        }

        }



        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
//    private static class ViewHolder {
//        TextView price;
//        TextView contentRequestBtn;
//        TextView pledgePrice;
//        TextView fromAddress;
//        TextView toAddress;
//        TextView requestsCount;
//        TextView date;
//        TextView time;
//    }
    static class ViewHolder {
        TextView txtLineOne;
        TextView txtLineTwo;
        Button btnGo;
        ImageView mimgEvent;

        TextView txtLineOneContent ;
        TextView txtLineTwoContent ;
        TextView txtTital ;
        //        DynamicHeightTextView txtTime = (DynamicHeightTextView) dialog.findViewById(R.id.dilg_txt_time);
        TextView txtDetiales;
        TextView txtLocation ;
        ImageView mimgEventContent ;
        ImageButton btn_share ;
        ImageButton btn_add_calndar;
        ImageButton btn_map ;
        TextView txtLink;
    }
}
