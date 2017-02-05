package com.example.aditya.testapp3;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by aditya on 19/11/16.
 */

public class CommComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject jsonObject, JSONObject t1) {
        try {
            return jsonObject.getString("name").compareTo(t1.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
