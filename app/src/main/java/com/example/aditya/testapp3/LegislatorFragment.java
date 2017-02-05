package com.example.aditya.testapp3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by aditya on 16/11/16.
 */

public class LegislatorFragment extends Fragment implements View.OnClickListener {

    String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=l";
    ListView l;
    Map<String, Integer> mapIndex;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.leg_main,container,false);
        l = (ListView) view.findViewById(R.id.lv);
        new GetList().execute(l);
        TabLayout tl=(TabLayout)view.findViewById(R.id.legtabs);

        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_dehaze);
        ((MainActivity)getActivity()).setSupportActionBar(t);
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        t.setTitle("Legislators");

        if(MaintainBacks.legback==-1||MaintainBacks.legback==0)
        {
            tl.getTabAt(0).select();
        }
        else if(MaintainBacks.legback==1)
        {
            tl.getTabAt(1).select();
        }
        else if(MaintainBacks.legback==2)
        {
            tl.getTabAt(2).select();
        }

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=l";
                            MaintainBacks.legback=0;
                            new GetList().execute(l);
                            //System.out.println(url);
                            break;
                    case 1: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=l&chamber=house";
                            //System.out.println(url);
                            MaintainBacks.legback=1;
                            new GetList().execute(l);
                            break;
                    case 2: url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=l&chamber=senate";
                            //System.out.println(url);
                            MaintainBacks.legback=2;
                            new GetList().execute(l);
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

        ListView lv;
        @Override
        protected String doInBackground(ListView... objects) {
            lv=objects[0];
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
            if(MaintainBacks.legback==-1||MaintainBacks.legback==0)
            {
                Collections.sort(l,new LegComparator());
            }
            else
            {
                Collections.sort(l,new LegComparatorName());
            }
            final JSONArray newja=new JSONArray();
            for(int i=0;i<ja.length();i++)
            {
                newja.put(l.get(i));
            }

            CustomAdapter ca = new CustomAdapter(getActivity().getApplicationContext(), newja);
            lv.setAdapter(ca);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                android.app.FragmentManager fm;
                LegDetailFragment ldobject;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String bid = (String)view.getTag();
                    //System.out.println(msg);
                    Bundle bundle = new Bundle();
                    bundle.putString("bid", bid);
                    // set Fragmentclass Arguments
                    ldobject = new LegDetailFragment();
                    ldobject.setArguments(bundle);
                    fm=getFragmentManager();

                    fm.beginTransaction().replace(R.id.framename, ldobject).addToBackStack(null).commit();



                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }


            });
            getIndexList(newja);
            displayIndex();
        }


    }

    private void getIndexList(JSONArray jsonList) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < jsonList.length(); i++) {
            String legislator = null;
            try {
                if(MaintainBacks.legback==-1||MaintainBacks.legback==0)
                {
                    legislator = jsonList.getJSONObject(i).getString("state_name");
                }
                else
                {
                    legislator = jsonList.getJSONObject(i).getString("last_name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String index = legislator.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);


        }

        //System.out.println(mapIndex.keySet());
    }

    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_index);
        indexLayout.removeAllViews();
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());

        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        l.setSelection(mapIndex.get(selectedIndex.getText()));
    }

}
