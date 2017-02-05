package com.example.aditya.testapp3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class LegDetailFragment extends Fragment{
    String bid="";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.legdetails,container,false);
        bid = getArguments().getString("bid");
        JSONObject leg=new JSONObject();

        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_arrow_back);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        t.setTitle("Legislator Info");

        new GetLDetails().execute(view);



        CheckBox cb=(CheckBox)view.findViewById(R.id.legstar);
        if(MaintainFavorites.legfavorite.contains(bid))
            cb.setChecked(true);
        cb.setTag(bid);

        return view;
    }

    public class GetLDetails extends AsyncTask<View,String,String>
    {
        View v;
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            TextView party= (TextView) v.findViewById(R.id.legparty);
            TextView name= (TextView) v.findViewById(R.id.legname);
            TextView email= (TextView) v.findViewById(R.id.legemail);
            TextView chamber= (TextView) v.findViewById(R.id.legchamber);
            TextView contact= (TextView) v.findViewById(R.id.legcontact);
            TextView start= (TextView) v.findViewById(R.id.legstart);
            TextView end= (TextView) v.findViewById(R.id.legend);
            ProgressBar term= (ProgressBar) v.findViewById(R.id.legterm);
            term.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            TextView termvalue=(TextView)v.findViewById(R.id.legtermvalue);
            TextView office= (TextView) v.findViewById(R.id.legoffice);
            TextView state= (TextView) v.findViewById(R.id.legstate);
            TextView fax= (TextView) v.findViewById(R.id.legfax);
            TextView bday= (TextView) v.findViewById(R.id.legbirthday);
            ImageView image=(ImageView)v.findViewById(R.id.legimage);
            ImageView imagefb=(ImageView)v.findViewById(R.id.legfb);
            ImageView imagetw=(ImageView)v.findViewById(R.id.legt);
            ImageView imageweb=(ImageView)v.findViewById(R.id.legw);
            ImageView imageparty=(ImageView)v.findViewById(R.id.legpartyimage);


            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");
            String ipattern = "yyyy-mm-dd";
            SimpleDateFormat iformat = new SimpleDateFormat(ipattern);

            String emailstr;
            String contactstr;
            String officestr;
            String faxstr;
            final String twstr;
            final String fbstr;
            final String webstr;


            //String date = formatter.format(Date.parse("Your date string"));

            try {
                JSONObject j=new JSONObject(s);
                JSONObject resultleg= j.getJSONArray("results").getJSONObject(0);

                if(resultleg.getString("party").equals("R"))
                {
                    party.setText("Republican");
                    imageparty.setImageDrawable(getResources().getDrawable(R.drawable.r));
                }
                else
                {
                    party.setText("Democrat");
                    imageparty.setImageDrawable(getResources().getDrawable(R.drawable.d));

                }
                name.setText(resultleg.getString("title")+". "+resultleg.getString("last_name")+","+resultleg.getString("first_name"));
                emailstr=(!resultleg.getString("oc_email").equals("null"))?resultleg.getString("oc_email"):"N.A.";
                contactstr=(!resultleg.getString("phone").equals("null"))?resultleg.getString("phone"):"N.A.";
                email.setText(emailstr);
                chamber.setText(resultleg.getString("chamber"));
                contact.setText(contactstr);
                //System.out.println(resultleg.getString("term_start"));
                Date startdate=null;
                Date enddate=null;
                Date bdaydate=null;
                Date now= new Date();
                try {
                    startdate=iformat.parse(resultleg.getString("term_start"));
                    enddate=iformat.parse(resultleg.getString("term_end"));
                    bdaydate=iformat.parse(resultleg.getString("birthday"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                    //System.out.println(startdate.toString());
                start.setText(formatter.format(startdate));
                end.setText(formatter.format(enddate));
                float progressfactor=((float)(now.getTime()-startdate.getTime())/(float)(enddate.getTime()-startdate.getTime()))*100;
                term.setProgress((int)progressfactor);
                termvalue.setText((int)progressfactor+"%");
                officestr=(!resultleg.getString("office").equals("null"))?resultleg.getString("office"):"N.A.";
                office.setText(officestr);
                state.setText(resultleg.getString("state"));
                faxstr=(!resultleg.getString("fax").equals("null"))?resultleg.getString("fax"):"N.A.";
                fax.setText(faxstr);
                bday.setText(formatter.format(bdaydate));
                Picasso.with(v.getContext()).load("https://theunitedstates.io/images/congress/original/"+bid+".jpg").into(image);


                if(resultleg.has("facebook_id")&&!resultleg.getString("facebook_id").equals("null"))
                {
                    fbstr=resultleg.getString("facebook_id");
                    imagefb.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.facebook.com/"+fbstr+"/"));
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    imagefb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Facebook Page not available",Toast.LENGTH_SHORT).show();
                }


                if(resultleg.has("twitter_id")&&!resultleg.getString("twitter_id").equals("null"))
                {
                    twstr=resultleg.getString("twitter_id");
                    imagetw.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://twitter.com/"+twstr+"/"));
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    imagetw.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Twitter Page not available",Toast.LENGTH_SHORT).show();
                }

                if(resultleg.has("website")&&!resultleg.getString("website").equals("null"))
                {
                    webstr=resultleg.getString("website");
                    imageweb.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(webstr));
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    imageweb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Website not available",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(View... views)
        {
            v=views[0];
            //System.out.print("ajhsbdjhasbd");
            String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=l&id="+bid;
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
