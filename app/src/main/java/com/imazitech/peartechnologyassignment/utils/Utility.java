package com.imazitech.peartechnologyassignment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utility {

    /* Check Internet Connectivity */
    public static boolean internetCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(context, "Internet Connection Not Available !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
}
