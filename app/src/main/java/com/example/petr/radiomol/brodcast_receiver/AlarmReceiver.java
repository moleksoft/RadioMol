package com.example.petr.radiomol.brodcast_receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.petr.radiomol.R;
import com.example.petr.radiomol.data.SaveLoad;
import com.example.petr.radiomol.ui.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIF_ID = 1;
    private Intent notifIntent;
    private PendingIntent pendIntent;
    SaveLoad saveLoad;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        saveLoad = new SaveLoad(context);

        // notifikace v liste
        notifIntent = new Intent(context, MainActivity.class);
        pendIntent = PendingIntent.getActivity(context, 0,
                notifIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification.Builder notificationBuilder = new
                Notification.Builder(context).setTicker("RadioMol")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).setContentTitle("RadioMol")
                .setContentText("Vztávate s " + saveLoad.loadRadioName()).setContentIntent(pendIntent);
        NotificationManager notifManag = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManag.notify(NOTIF_ID, notificationBuilder.build());

        /*Intent alarm =new Intent(context,MainActivity.class);
        alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarm.putExtra("alarm", "start");
        context.startActivity(alarm);*/
    }
}





