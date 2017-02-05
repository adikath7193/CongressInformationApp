package com.example.aditya.testapp3;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by aditya on 19/11/16.
 */

public class BillComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject jsonObject, JSONObject t1) {
        try {
            String ipattern = "yyyy-MM-dd";
            SimpleDateFormat iformat = new SimpleDateFormat(ipattern);

            try {
                Date d1=iformat.parse(jsonObject.getString("introduced_on"));
                Date d2=iformat.parse(t1.getString("introduced_on"));
                Long time1=d1.getTime();
                Long time2=d2.getTime();
                if((time2-time1)<0)
                    return -1;
                else if((time2-time1)==0)
                    return 0;
                else
                    return 1;

                //System.out.println((int)(time2-time1));
                //return (int)(time2-time1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
                return 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
