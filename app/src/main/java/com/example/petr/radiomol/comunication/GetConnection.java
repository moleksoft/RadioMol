package com.example.petr.radiomol.comunication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by petr on 1.8.17.
 */

public class GetConnection {


    public boolean getStateConnection(Context context, String info){

        // ziskani pristupu ke stavu o pripojeni
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        else if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
        {
            return true;
        }
        else
        {

            switch (info)
            {
                case "noConnection":
                    Toast.makeText(context, "Není dostupné připojení", Toast.LENGTH_SHORT).show();
                    break;
                case "lostConnection":
                    Toast.makeText(context, "Ztráta připojení", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            return false;
        }
    }
}
