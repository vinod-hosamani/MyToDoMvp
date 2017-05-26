package com.bridgelabz.mytodomvp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bridgeit on 7/5/17.
 */
public class Connectivity
{
    public static boolean isNetworkConnected(Context context)
    {
        ConnectivityManager conn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=conn.getActiveNetworkInfo();
        return info !=null && info.isConnected();
    }
}
