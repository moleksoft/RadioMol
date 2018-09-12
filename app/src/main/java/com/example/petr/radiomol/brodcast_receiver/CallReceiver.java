package com.example.petr.radiomol.brodcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.example.petr.radiomol.ui.MainActivity;

public class CallReceiver extends BroadcastReceiver {

    Message message;
    Bundle bundle;


    @Override
    public void onReceive(Context context, Intent intent) {

        command("call");
    }


    // posilani dat do hlavni aktivity
    public void command(String command) {

        message = new Message();
        bundle = new Bundle();
        bundle.putString("radioMol", command);
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);
    }
}
