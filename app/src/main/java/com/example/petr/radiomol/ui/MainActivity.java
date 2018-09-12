package com.example.petr.radiomol.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.petr.radiomol.brodcast_receiver.AlarmReceiver;
import com.example.petr.radiomol.comunication.GetConnection;
import com.example.petr.radiomol.R;
import com.example.petr.radiomol.data.SaveLoad;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NOTIF_ID = 1;
    private Intent notifIntent;
    private PendingIntent pendIntent;
    public static Handler handler;
    private Message message;
    private Bundle bundle;
    MediaPlayer player;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    SaveLoad saveLoad;
    Button buttonPlayer, buttonTimer, buttonAlarm;
    TextView textRadio, textTimer, textAlarm, textData;
    SeekBar seekVolume;
    MenuItem navRadioName, navRadioWeb, navRadioFacebook;
    String radioName, radioUrl, radioWeb, radioFacebook, radioCall, radioCar;
    int volume = 0;
    String minute = "00";
    String hour = "00";
    boolean isPlay = false;
    boolean alarm = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // zakazani zmeny orientace obrazovky
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();

        navRadioName = menu.findItem(R.id.nav_radio_name);
        navRadioWeb = menu.findItem(R.id.nav_radio_web);
        navRadioFacebook = menu.findItem(R.id.nav_radio_facebook);

        buttonPlayer = (Button) findViewById(R.id.button_player);
        buttonTimer = (Button) findViewById(R.id.button_timer);
        buttonAlarm = (Button) findViewById(R.id.button_alarm);
        textRadio = (TextView) findViewById(R.id.text_radio);
        seekVolume = (SeekBar) findViewById(R.id.seek_volume);
        textTimer = (TextView) findViewById(R.id.text_timer);
        textAlarm = (TextView) findViewById(R.id.text_alarm);
        textData = (TextView) findViewById(R.id.text_data);

        // prijem dat z AlarmReceiveru
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String command = extras.getString("alarm") + "";
            if (command.equals("start")) {
                alarm = true;
            }
        }

        // prijem data pro aktualizaci robrazeni
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = new Bundle();
                bundle = msg.getData();

                String data = bundle.getString("radioMol");

                if (data.substring(0, 1).equals("T")) {
                    hour = data.substring(1, 3);
                    minute = data.substring(4);
                    textTimer.setText(data.substring(1));
                    timer();
                } else if (data.substring(0, 1).equals("S")) {
                    textTimer.setText(data.substring(1));
                } else if (data.equals("start")) {
                    player();
                } else if (data.equals("konec")) {
                    textTimer.setText("--:--");
                    player();
                } else if (data.substring(0, 1).equals("A")) {
                    textAlarm.setText(data.substring(1, 6)); // A00:00;xxxx
                    saveLoad.saveAlarmTime(data.substring(1, 6));
                    saveLoad.saveAlarm(true);
                    buttonAlarm.setBackgroundResource(R.drawable.alarm_selector);
                    alarm(Integer.parseInt(data.substring(7)));
                } else if (data.substring(0, 1).equals("D"))
                {
                    textData.setText(data.substring(1));
                }
                else if(data.equals("fail"))
                {
                    stop();
                }
            }
        };


        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                volume = progress;
                setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        saveLoad = new SaveLoad(this);

        radioName = saveLoad.loadRadioName();
        radioUrl = saveLoad.loadRadioUrl();
        radioWeb = saveLoad.loadRadioWeb();
        radioFacebook = saveLoad.loadRadioFacebook();
        radioCar = saveLoad.loadRadioCar();
        radioCall = saveLoad.loadRadioCall();
        volume = saveLoad.loadVolume();

        navRadioName.setTitle(radioName);
        navRadioWeb.setTitle(radioWeb);
        navRadioFacebook.setTitle(radioFacebook);
        seekVolume.setProgress(volume);

        if (alarm) {
            player();
            saveLoad.saveAlarmTime("--:--");
            alarm = false;
            saveLoad.saveAlarm(alarm);
        }

        if (saveLoad.loadAlarm()){
            buttonAlarm.setBackgroundResource(R.drawable.alarm_selector);
        }
        else
        {
            buttonAlarm.setBackgroundResource(R.drawable.alarm_selector_off);
        }

        textAlarm.setText(saveLoad.loadAlarmTime());
    }


    @Override
    public void onPause(){
        super.onPause();

        //notification();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list_cz) {
            Intent intent = new Intent(this, RadioListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_o_app) {

        } else if (id == R.id.nav_radio_web) {
            openWeb(radioWeb);
        } else if (id == R.id.nav_radio_facebook) {
            openWeb(radioFacebook);
        } else if (id == R.id.nav_radio_sms) {

        } else if (id == R.id.nav_radio_call) {
            call(radioCall);
        } else if (id == R.id.nav_radio_car) {
            call(radioCar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
        mnou definované metody
    */
    public void playerAction(View v) {

        player();
    }


    public void player() {

        GetConnection getConnection = new GetConnection();

        if (getConnection.getStateConnection(this, "noConnection")) {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Připojuji: " + radioName);

            if (!isPlay) {
                progress.show();
                buttonPlayer.setBackgroundResource(R.drawable.stop_selector);
                isPlay = true;
                buttonPlayer.setEnabled(false);
                //command(commandSetNameRadio);
                textRadio.setText(radioName);
            } else {
                player.stop();
                cancelNotification();
                buttonPlayer.setBackgroundResource(R.drawable.play_selector);
                isPlay = false;
                resetTimer();
            }

            if (isPlay && !radioUrl.equals("")) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            player = MediaPlayer.create(getApplicationContext(), Uri.parse(radioUrl));
                            player.start();

                            if (player.isPlaying()) {
                                progress.cancel();
                                notification();
                                isPlay = true;
                                buttonPlayer.setEnabled(true);
                                //pocitadloData();
                            }
                        } catch (Exception e) {
                            progress.cancel();
                        }

                    }
                });
                thread.start();

                pocitadloData();

            } else {
                try {
                    player.stop();
                } catch (Exception e) {
                }

                if (radioUrl.equals("")) {
                    Toast.makeText(this, R.string.no_select, Toast.LENGTH_LONG).show();
                }

                progress.cancel();
                buttonPlayer.setBackgroundResource(R.drawable.play_selector);
                textRadio.setText("");
            }
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {

                    if (volume < 100) {
                        volume++;
                        setVolume(volume);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (volume > 0) {
                        volume--;
                        setVolume(volume);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    public void setVolume(int volume) {

        seekVolume.setProgress(volume);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume / 7, 0);

        saveLoad.saveVolume(volume);
    }


    public void openWeb(String url) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {

            Toast.makeText(this, "Webovou adresu se nepodařilo otevřít", Toast.LENGTH_SHORT).show();
        }
    }


    public void call(String number) {

        try {
            player.stop();
            buttonPlayer.setBackgroundResource(R.drawable.play_selector);
        } catch (Exception e) {

        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    public void setAlarm(View v) {

        if(saveLoad.loadAlarm())
        {
            saveLoad.saveAlarm(false);
            saveLoad.saveAlarmTime("--:--");
            textAlarm.setText(saveLoad.loadAlarmTime());
            buttonAlarm.setBackgroundResource(R.drawable.alarm_selector_off);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this,"Budík vypnut", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(this, SetTimeActivity.class);
            intent.putExtra("alarm", "setAlarm");
            startActivity(intent);
        }
    }


    public void alarm(int time){

        Toast.makeText(this, "Budík nastaven na " + textAlarm.getText(), Toast.LENGTH_LONG).show();
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
    }


    public void setTimer(View v) {

        if (isPlay) {
            player();
        }

        resetTimer();

        Intent intent = new Intent(this, SetTimeActivity.class);
        startActivity(intent);

    }


    // casovac
    public void timer() {

        final int[] h = {Integer.parseInt(minute)};
        final int[] m = {Integer.parseInt(hour)};

        if (m[0] > 0 || h[0] > 0) {
            command("start");
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (h[0] > 0 || m[0] > 0) {

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    m[0] = Integer.parseInt(minute);
                    h[0] = Integer.parseInt(hour);


                    command("S" + hour + ":" + minute);

                    m[0] = m[0] + h[0] * 60;

                    m[0]--;

                    h[0] = m[0] / 60;
                    m[0] = m[0] % 60;


                    if (m[0] < 10) {
                        minute = "0" + m[0];
                    } else {
                        minute = "" + m[0];
                    }

                    if (h[0] < 10) {
                        hour = "0" + h[0];
                    } else {
                        hour = "" + h[0];
                    }

                }

                if (isPlay) {
                    command("konec");
                }

                resetTimer();

            }
        });

        thread.start();
    }


    //reset textTimer
    public void resetTimer(){

        hour = "00";
        minute = "00";
        command("S--:--");
    }


    public void notification(){

        // notifikace v liste
        notifIntent = new Intent(this, MainActivity.class);
        pendIntent = PendingIntent.getActivity(this, 0,
                notifIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification.Builder notificationBuilder = new
                Notification.Builder(this).setTicker("RadioMol")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).setContentTitle("RadioMol")
                .setContentText("Právě hraje " + saveLoad.loadRadioName()).setContentIntent(pendIntent);
        NotificationManager notifManag = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManag.notify(NOTIF_ID, notificationBuilder.build());
    }


    public void cancelNotification() {
        String parametr = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(parametr);
        nMgr.cancel(NOTIF_ID);
    }


    public void pocitadloData(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                long d = 0;

                while (isPlay)
                {
                    d = d + (128 / 8);
                    command("D" + (d / 1000) + "," + ((d % 1000) / 100) + "MB");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }


    // v pripade fail nebo volani
    public void stop(){

        buttonPlayer.setBackgroundResource(R.drawable.play_selector);
        buttonPlayer.setEnabled(true);
        textRadio.setText("");
        isPlay = false;
        try{
            player.stop();
        }catch (Exception e){
        }
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




