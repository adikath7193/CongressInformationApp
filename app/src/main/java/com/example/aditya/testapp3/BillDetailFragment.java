package com.example.aditya.testapp3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class BillDetailFragment extends Fragment {
    String bid="";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.billdetails,container,false);
        bid = getArguments().getString("bid");
        JSONObject bill=new JSONObject();
        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_arrow_back);

        t.setTitle("Bill Info");

        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        new GetBDetails().execute(view);

        CheckBox cb=(CheckBox)view.findViewById(R.id.billstar);
        if(MaintainFavorites.billfavorite.contains(bid))
            cb.setChecked(true);
        cb.setTag(bid);

        return view;
    }

    public class GetBDetails extends AsyncTask<View,String,String>
    {
        View v;
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            TextView billid= (TextView) v.findViewById(R.id.billdetailid);
            TextView title= (TextView) v.findViewById(R.id.billtitle);
            TextView type= (TextView) v.findViewById(R.id.billtype);
            TextView sponsor= (TextView) v.findViewById(R.id.billsponsor);
            TextView chamber= (TextView) v.findViewById(R.id.billchamber);
            TextView status= (TextView) v.findViewById(R.id.billstatus);
            TextView intro= (TextView) v.findViewById(R.id.billintro);
            TextView curl=(TextView)v.findViewById(R.id.billcongurl);
            TextView vstatus= (TextView) v.findViewById(R.id.billversion);
            TextView burl= (TextView) v.findViewById(R.id.billurl);


            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");
            String ipattern = "yyyy-MM-dd";
            SimpleDateFormat iformat = new SimpleDateFormat(ipattern);


            try {
                JSONObject j=new JSONObject(s);
                JSONObject resultbill= j.getJSONArray("results").getJSONObject(0);
                String name=resultbill.getJSONObject("sponsor").getString("title")+". ";
                name+=resultbill.getJSONObject("sponsor").getString("last_name")+",";
                name+=resultbill.getJSONObject("sponsor").getString("first_name");
                billid.setText(resultbill.getString("bill_id"));
                String btitle=resultbill.getString("short_title");
                if(btitle.equals("null"))
                    btitle=resultbill.getString("official_title");
                title.setText(btitle);
                chamber.setText(resultbill.getString("chamber"));
                String btype=(resultbill.getString("bill_type").equals("null"))?"N.A.":resultbill.getString("bill_type");
                type.setText(btype);
                sponsor.setText(name);

                Date introdate=null;
                String bintro=resultbill.getString("introduced_on");
                if(bintro.equals("null"))
                {
                    intro.setText("N.A.");
                }
                else
                {
                    try {
                        introdate=iformat.parse(resultbill.getString("introduced_on"));


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    intro.setText(formatter.format(introdate));
                }


                if(resultbill.getJSONObject("history").getString("active").equals("true"))
                {
                    status.setText("active");

                }
                else if(resultbill.getJSONObject("history").getString("active").equals("false"))
                {
                    status.setText("new");
                }
                else
                {
                    status.setText("N.A.");
                }
                if(resultbill.getJSONObject("urls").has("congress")&&!resultbill.getJSONObject("urls").getString("congress").equals("null"))
                {
                    curl.setText(resultbill.getJSONObject("urls").getString("congress"));
                }
                else
                {
                    curl.setText("N.A.");
                }

                if(resultbill.has("last_version")&&resultbill.getJSONObject("last_version").has("version_name")&&!resultbill.getJSONObject("last_version").getString("version_name").equals("null"))
                    vstatus.setText(resultbill.getJSONObject("last_version").getString("version_name"));
                else
                    vstatus.setText("N.A.");

                if(resultbill.has("last_version")&&resultbill.getJSONObject("last_version").has("urls")&&resultbill.getJSONObject("last_version").getJSONObject("urls").has("pdf")&&!resultbill.getJSONObject("last_version").getJSONObject("urls").getString("pdf").equals("null"))
                    burl.setText(resultbill.getJSONObject("last_version").getJSONObject("urls").getString("pdf"));
                else
                    burl.setText("N.A.");


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(View... views)
        {
            v=views[0];
            String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=ba&id="+bid;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }else
        {

        }

        return super.onOptionsItemSelected(item);
    }
}
