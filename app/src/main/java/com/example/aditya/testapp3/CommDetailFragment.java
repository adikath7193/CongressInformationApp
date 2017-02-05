package com.example.aditya.testapp3;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aditya on 17/11/16.
 */

public class CommDetailFragment extends Fragment {
    String cid="";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.commdetails,container,false);
        cid = getArguments().getString("cid");
        JSONObject comm=new JSONObject();
        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_arrow_back);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        t.setTitle("Committee Info");
        new GetCDetails().execute(view);
        CheckBox cb=(CheckBox)view.findViewById(R.id.commstar);
        if(MaintainFavorites.commfavorite.contains(cid))
            cb.setChecked(true);
        cb.setTag(cid);
        return view;
    }

    public class GetCDetails extends AsyncTask<View,String,String>
    {
        View v;
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            TextView id= (TextView) v.findViewById(R.id.commdetailid);
            TextView name= (TextView) v.findViewById(R.id.commdetailname);
            TextView chamber= (TextView) v.findViewById(R.id.commchamber);
            TextView parent= (TextView) v.findViewById(R.id.commparent);
            TextView contact= (TextView) v.findViewById(R.id.commcontact);
            TextView office= (TextView) v.findViewById(R.id.commoffice);
            ImageView commimg=(ImageView)v.findViewById(R.id.commpartyimage);


            try {
                JSONObject j=new JSONObject(s);

                JSONObject resultcomm= j.getJSONArray("results").getJSONObject(0);

                id.setText(resultcomm.getString("committee_id"));
                if(resultcomm.has("name")&&!resultcomm.getString("name").equals("null"))
                    name.setText(resultcomm.getString("name"));
                else
                    name.setText("N.A.");
                if(resultcomm.getString("chamber").equals("house"))
                {
                    commimg.setImageDrawable(getResources().getDrawable(R.drawable.h));
                }
                else
                {
                    commimg.setImageDrawable(getResources().getDrawable(R.drawable.s));
                }
                chamber.setText(resultcomm.getString("chamber"));
                if(resultcomm.has("parent_committee_id")&&!resultcomm.getString("parent_committee_id").equals("null"))
                    parent.setText(resultcomm.getString("parent_committee_id"));
                else
                    parent.setText("N.A.");
                if(resultcomm.has("phone")&&!resultcomm.getString("phone").equals("null"))
                    contact.setText(resultcomm.getString("phone"));
                else
                    contact.setText("N.A.");
                if(resultcomm.has("office")&&!resultcomm.getString("office").equals("null"))
                    office.setText(resultcomm.getString("office"));
                else
                    office.setText("N.A.");

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(View... views)
        {
            v=views[0];
            String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&id="+cid;
            BufferedReader br;
            String result="";
            try {
                URL uri=new URL(url);
                HttpURLConnection uc=(HttpURLConnection)uri.openConnection();
                br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine = "";
                StringBuilder sb=new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result=sb.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
