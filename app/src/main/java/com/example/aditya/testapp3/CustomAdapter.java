package com.example.aditya.testapp3;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by aditya on 16/11/16.
 */

public class CustomAdapter extends BaseAdapter {

    String bid="";
    public Context c;
    public JSONArray j;

    public CustomAdapter(Context c, JSONArray j) {
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
        View v = View.inflate(c, R.layout.layoutleg,null);
        TextView t=(TextView)v.findViewById(R.id.fnameleg);
        TextView t2=(TextView)v.findViewById(R.id.details_leg);

        try {

            String state_name=(j.getJSONObject(i).getString("state_name").equals("null"))?"N.A.":j.getJSONObject(i).getString("state_name");
            String district=(j.getJSONObject(i).getString("district").equals("null"))?"N.A.":"District "+j.getJSONObject(i).getString("district");

            t.setText(j.getJSONObject(i).getString("last_name")+", "+j.getJSONObject(i).getString("first_name"));
            t2.setText("("+j.getJSONObject(i).getString("party")+")"+state_name+" - "+district);
            bid=j.getJSONObject(i).getString("bioguide_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView iv1=(ImageView)v.findViewById(R.id.imageleg);

        Picasso.with(c).load("https://theunitedstates.io/images/congress/original/"+bid+".jpg").into(iv1);
        v.setTag(bid);
        return v;
    }


}
