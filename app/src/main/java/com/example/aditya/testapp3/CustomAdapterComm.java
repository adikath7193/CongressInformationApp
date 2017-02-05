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

/**
 * Created by aditya on 16/11/16.
 */

public class CustomAdapterComm extends BaseAdapter {

    String cid="";
    public Context c;
    public JSONArray j;

    public CustomAdapterComm(Context c, JSONArray j) {
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
        View v = View.inflate(c, R.layout.layoutcomm,null);
        TextView tid=(TextView)v.findViewById(R.id.commid);
        TextView tname=(TextView)v.findViewById(R.id.commname);
        TextView tchamber=(TextView)v.findViewById(R.id.cchamber);
        try {
            tid.setText(j.getJSONObject(i).getString("committee_id"));
            String strname=(j.getJSONObject(i).getString("name").equals("null"))?"N.A.":j.getJSONObject(i).getString("name");
            tname.setText(strname);
            cid=j.getJSONObject(i).getString("committee_id");
            String cc=j.getJSONObject(i).getString("chamber");
            cc=cc.substring(0,1).toUpperCase()+cc.substring(1);
            tchamber.setText(cc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        v.setTag(cid);
        return v;
    }


}
