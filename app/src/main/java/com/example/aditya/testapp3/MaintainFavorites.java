package com.example.aditya.testapp3;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by aditya on 18/11/16.
 */

public class MaintainFavorites{
    static Set<String> legfavorite=new HashSet<String>();
    static Set<String> billfavorite=new HashSet<String>();
    static Set<String> commfavorite=new HashSet<String>();


    public static void addleg(String id)
    {

        if(legfavorite.contains(id))
            legfavorite.remove(id);
        else
            legfavorite.add(id);
    }
    public static void removeleg(String id)
    {
        if(legfavorite.contains(id))
            legfavorite.remove(id);
    }
    public static void addbill(String id)
    {

        if(billfavorite.contains(id))
            billfavorite.remove(id);
        else
            billfavorite.add(id);
    }
    public static void removebill(String id)
    {
        if(billfavorite.contains(id))
            billfavorite.remove(id);
    }
    public static void addcomm(String id)
    {

        if(commfavorite.contains(id))
            commfavorite.remove(id);
        else
            commfavorite.add(id);
    }
    public static void removecomm(String id)
    {
        if(commfavorite.contains(id))
            commfavorite.remove(id);
    }
}
