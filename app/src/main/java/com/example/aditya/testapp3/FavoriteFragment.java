package com.example.aditya.testapp3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * Created by aditya on 16/11/16.
 */

public class FavoriteFragment extends Fragment implements View.OnClickListener{


    ListView l;
    Map<String, Integer> mapIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fav_main,container,false);
        l = (ListView) view.findViewById(R.id.fv);


        TabLayout tl=(TabLayout)view.findViewById(R.id.favtabs);
        Toolbar t=(Toolbar)getActivity().findViewById(R.id.toolbar);
        t.setNavigationIcon(R.drawable.ic_dehaze);
        ((MainActivity)getActivity()).setSupportActionBar(t);
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        t.setTitle("Favorites");

        if(MaintainBacks.favback==-1||MaintainBacks.favback==0)
        {
            tl.getTabAt(0).select();
            new GetLegList().execute(l);
        }
        else if(MaintainBacks.favback==1)
        {
            tl.getTabAt(1).select();
            new GetBillList().execute(l);
        }
        else if(MaintainBacks.favback==2)
        {
            tl.getTabAt(2).select();
            new GetCommList().execute(l);
        }

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0:MaintainBacks.favback=0;
                            new GetLegList().execute(l);
                            break;
                    case 1:MaintainBacks.favback=1;
                            new GetBillList().execute(l);
                            break;
                    case 2:MaintainBacks.favback=2;
                            new GetCommList().execute(l);
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

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        l.setSelection(mapIndex.get(selectedIndex.getText()));
    }

    public class GetLegList extends AsyncTask<ListView,String,String> {

        ListView lv;
        @Override
        protected String doInBackground(ListView... objects) {
            lv=objects[0];
            BufferedReader br;
            String result="{\"results\":[";
            String url="http://cs-server.usc.edu:19394/backend.php?option=l&id=";
            String ids[]=MaintainFavorites.legfavorite.toArray(new String[MaintainFavorites.legfavorite.size()]);
            for(int i=0;i<ids.length;i++)
            {
                try {
                    URL uri=new URL(url+ids[i]);
                    HttpURLConnection uc=(HttpURLConnection)uri.openConnection();
                    br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
                    String inputLine = "";
                    StringBuilder sb=new StringBuilder();
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    JSONObject temp= new JSONObject(sb.toString());

                    result+=temp.getJSONArray("results").getJSONObject(0).toString();
                    if(i!=ids.length-1)
                    {
                        result+=",";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result+="]}";
            //System.out.print(result);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_indexf);
            indexLayout.setVisibility(View.VISIBLE);
            lv.setFastScrollEnabled(false);

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
            Collections.sort(l,new LegComparatorName());
            JSONArray newja=new JSONArray();
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

    public class GetBillList extends AsyncTask<ListView,String,String> {

        ListView lv;
        @Override
        protected String doInBackground(ListView... objects) {
            lv=objects[0];
            BufferedReader br;
            String result="{\"results\":[";
            String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=ba&id=";
            String ids[]=MaintainFavorites.billfavorite.toArray(new String[MaintainFavorites.billfavorite.size()]);
            for(int i=0;i<ids.length;i++)
            {
                try {
                    URL uri=new URL(url+ids[i]);
                    HttpURLConnection uc=(HttpURLConnection)uri.openConnection();
                    br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
                    String inputLine = "";
                    StringBuilder sb=new StringBuilder();
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    JSONObject temp= new JSONObject(sb.toString());

                    result+=temp.getJSONArray("results").getJSONObject(0).toString();
                    if(i!=ids.length-1)
                    {
                        result+=",";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result+="]}";
            //System.out.print(result);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_indexf);
            indexLayout.setVisibility(View.GONE);
            lv.setFastScrollEnabled(true);

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

            Collections.sort(l,new BillComparator());
            JSONArray newja=new JSONArray();

            for(int i=0;i<ja.length();i++)
            {
                newja.put(l.get(i));
            }

            CustomAdapterbill bca = new CustomAdapterbill(getActivity().getApplicationContext(), newja);
            lv.setAdapter(bca);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                android.app.FragmentManager fm;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String bid = (String)view.getTag();
                    //System.out.println(msg);
                    Bundle bundle = new Bundle();
                    bundle.putString("bid", bid);
                    // set Fragmentclass Arguments
                    BillDetailFragment bdobject = new BillDetailFragment();
                    bdobject.setArguments(bundle);
                    fm=getFragmentManager();
                    fm.beginTransaction().replace(R.id.framename, bdobject).addToBackStack(null).commit();
                }

            });
        }


    }

    public class GetCommList extends AsyncTask<ListView,String,String> {

        ListView lv;
        @Override
        protected String doInBackground(ListView... objects) {
            lv=objects[0];
            BufferedReader br;
            String result="{\"results\":[";
            String url="http://resposivewebapplication.kiuscaq3am.us-west-2.elasticbeanstalk.com/index.php/backend.php?option=c&id=";
            String ids[]=MaintainFavorites.commfavorite.toArray(new String[MaintainFavorites.commfavorite.size()]);
            for(int i=0;i<ids.length;i++)
            {
                try {
                    URL uri=new URL(url+ids[i]);
                    HttpURLConnection uc=(HttpURLConnection)uri.openConnection();
                    br=new BufferedReader(new InputStreamReader(uc.getInputStream()));
                    String inputLine = "";
                    StringBuilder sb=new StringBuilder();
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    JSONObject temp= new JSONObject(sb.toString());

                    result+=temp.getJSONArray("results").getJSONObject(0).toString();
                    if(i!=ids.length-1)
                    {
                        result+=",";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            result+="]}";
            System.out.print(result);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_indexf);
            indexLayout.setVisibility(View.GONE);
            lv.setFastScrollEnabled(true);

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
            lv.setAdapter(cac);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void getIndexList(JSONArray jsonList) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < jsonList.length(); i++) {
            String legislator = null;
            try {

                legislator = jsonList.getJSONObject(i).getString("last_name");

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
        LinearLayout indexLayout = (LinearLayout) getView().findViewById(R.id.side_indexf);
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

}
