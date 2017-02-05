package com.example.aditya.testapp3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aditya on 16/11/16.
 */

public class CommitteeFragment extends Fragment{

    String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&chamber=house";
    ListView c;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.comm_main,container,false);
        c = (ListView) view.findViewById(R.id.cv);
        new GetList().execute(c);

        TabLayout tl=(TabLayout)view.findViewById(R.id.commtabs);

        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_dehaze);
        ((MainActivity)getActivity()).setSupportActionBar(t);
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        t.setTitle("Committees");

        if(MaintainBacks.commback==-1||MaintainBacks.commback==0)
        {
            tl.getTabAt(0).select();
        }
        else if(MaintainBacks.commback==1)
        {
            tl.getTabAt(1).select();
        }
        else if(MaintainBacks.commback==2)
        {
            tl.getTabAt(2).select();
        }

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&chamber=house";
                        MaintainBacks.commback=0;
                        new GetList().execute(c);
                        //System.out.println(url);
                        break;
                    case 1: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&chamber=senate";
                        MaintainBacks.commback=1;
                        //System.out.println(url);
                        new GetList().execute(c);
                        break;
                    case 2: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&chamber=joint";
                        MaintainBacks.commback=2;
                        //System.out.println(url);
                        new GetList().execute(c);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }




        });
        return view;
    }
    public class GetList extends AsyncTask<ListView,String,String> {

        ListView cv;
        @Override
        protected String doInBackground(ListView... objects) {
            cv=objects[0];
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
                // System.out.print("String is : "+result);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            JSONArray ja=null;
            try {
                JSONObject jo=new JSONObject(s);
                ja = jo.getJSONArray("results");
                //System.out.print(jo);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            List<JSONObject> l=new ArrayList<JSONObject>();
            for(int i=0;i<ja.length();i++)
            {
                try {
                    l.add(ja.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(l,new CommComparator());
            JSONArray newja=new JSONArray();

            for(int i=0;i<ja.length();i++)
            {
                newja.put(l.get(i));
            }

            CustomAdapterComm cac = new CustomAdapterComm(getActivity().getApplicationContext(), newja);
            cv.setAdapter(cac);
            cv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                android.app.FragmentManager fm;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String cid = (String)view.getTag();
                    //System.out.println(msg);
                    Bundle bundle = new Bundle();
                    bundle.putString("cid", cid);
                    // set Fragmentclass Arguments
                    CommDetailFragment cdobject = new CommDetailFragment();
                    cdobject.setArguments(bundle);
                    fm=getFragmentManager();
                    fm.beginTransaction().replace(R.id.framename, cdobject).addToBackStack(null).commit();
                }

            });
        }


    }
}
