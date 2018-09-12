package com.example.petr.radiomol.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.petr.radiomol.R;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SetTimeActivity extends Activity {

    TextView hourText, minuteText;
    String hour = "00";
    String minute = "00";
    private Message message;
    private Bundle bundle;
    boolean alarm = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        // zakazani zmeny orientace obrazovky
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        hourText = (TextView) findViewById(R.id.text_hour);
        minuteText = (TextView) findViewById(R.id.text_minute);

        // prijem informaci z MainActivity zdali se jedna o nastavovani casu pro budik
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String command = bundle.getString("alarm") + "";
            if (command.equals("setAlarm")) {
                alarm = true;
            }
        }
    }


    @Override
    public void onStart(){
        super.onStart();
    }


    // metoda hdina +
    public void upHour(View v){

        int h = Integer.parseInt(hour);
        h++;

        if(h >= 0 &&  10 > h)
        {

            hour = "0" + h;
            hourText.setText(hour);
        }
        else if(h >= 9 && 24 > h)
        {
            hour = "" + h;
            hourText.setText(hour);
        }
        else if(h == 24)
        {
            hour = "00";
            hourText.setText(hour);
        }


    }


    // metoda minuta +
    public void upMinute(View v){

        int m = Integer.parseInt(minute);
        m++;

        if(m >= 0 &&  10 > m)
        {

            minute = "0" + m;
            minuteText.setText(minute);
        }
        else if(m >= 9 && 60 > m)
        {
            minute = "" + m;
            minuteText.setText(minute);
        }
        else if(m == 60)
        {
            minute = "00";
            minuteText.setText(minute);
        }
    }


    // metoda hdina -
    public void downHour(View v){

        int h = Integer.parseInt(hour);
        h--;

        if(h >= 0 &&  10 > h)
        {

            hour = "0" + h;
            hourText.setText(hour);
        }
        else if(h >= 9 && 24 > h)
        {
            hour = "" + h;
            hourText.setText(hour);
        }
        else if(h < 0)
        {
            hour = "23";
            hourText.setText(hour);
        }
    }


    // metoda minuta -
    public void downMinute(View v){

        int m = Integer.parseInt(minute);
        m--;

        if(m >= 0 &&  10 > m)
        {

            minute = "0" + m;
            minuteText.setText(minute);
        }
        else if(m >= 9 && 60 > m)
        {
            minute = "" + m;
            minuteText.setText(minute);
        }
        else if(m < 0)
        {
            minute = "59";
            minuteText.setText(minute);
        }
    }


    public void setTimer(View v){

        if(alarm)
        {
            setAlarm();
            finish();
        }
        else
        {
            if(Integer.parseInt(minute) >= 1 || Integer.parseInt(hour) > 0)
            {
                sendData("T" + hour + ":" + minute);
                finish();
            }
            else
            {
                Toast.makeText(this, "Interval musí být nejméně 1 minuta!", Toast.LENGTH_LONG).show();
            }

        }

    }




    public void setAlarm(){

        // pomocne promenne
        String time = "00:00";
        int timeM = 0;
        long alarmTime = 0;

        // ziskani aktualniho casu
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(date);

        // prevod aktualniho casu na minuty
        timeM = Integer.parseInt(time.substring(3)) + Integer.parseInt(time.substring(0,2)) * 60;

        // odecteni aktualniho casu od casu alarmu
        timeM = Integer.parseInt(minute) + Integer.parseInt(hour) * 60 - timeM;

        if(timeM < 0)
        {
            timeM = timeM + 1440;
        }

        // prevod na milisekundy
        alarmTime = timeM * 1000 * 60;

        sendData("A" + hour + ":" + minute + ";" + alarmTime);


    }


    public void sendData(String data){

        message = new Message();
        bundle = new Bundle();
        bundle.putString("radioMol", data);
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);
    }
}
