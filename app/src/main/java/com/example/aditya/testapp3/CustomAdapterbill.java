package com.example.aditya.testapp3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aditya on 16/11/16.
 */

public class CustomAdapterbill extends BaseAdapter {

    String bid="";
    public Context c;
    public JSONArray j;

    public CustomAdapterbill(Context c, JSONArray j) {
        this.j = j;
        this.c = c;
    }

    @Override
    public int getCount() {
        return j.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return j.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(c, R.layout.layoutbill,null);
        TextView tid=(TextView)v.findViewById(R.id.billid);
        TextView ttitle=(TextView)v.findViewById(R.id.billshortitle);
        TextView date=(TextView)v.findViewById(R.id.billdate);
        try {
            tid.setText(j.getJSONObject(i).getString("bill_id"));
            String strbilltitle=j.getJSONObject(i).getString("short_title");
            if(strbilltitle.equals("null"))
            {
                strbilltitle=j.getJSONObject(i).getString("official_title");
            }
            ttitle.setText(strbilltitle);


            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");
            String ipattern = "yyyy-MM-dd";
            SimpleDateFormat iformat = new SimpleDateFormat(ipattern);
            Date introdate=null;
            String bintro=j.getJSONObject(i).getString("introduced_on");
            if(bintro.equals("null"))
            {
                date.setText("N.A.");
            }
            else
            {
                try {
                    introdate=iformat.parse(bintro);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date.setText(formatter.format(introdate));
            }

            bid=j.getJSONObject(i).getString("bill_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        v.setTag(bid);
        return v;
    }


}
