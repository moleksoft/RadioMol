package com.example.petr.radiomol.brodcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.petr.radiomol.comunication.GetConnection;


/**
 * Created by petr on 31.10.16.
 */

public class ReceiverStateConnection extends BroadcastReceiver {

    GetConnection getConnection = new GetConnection();


    @Override
    public void onReceive(Context context, Intent intent) {

        // zavolani metody getStavwifi z tridy StavWifi
        getConnection.getStateConnection(context, "lostConnection");
    }


}
