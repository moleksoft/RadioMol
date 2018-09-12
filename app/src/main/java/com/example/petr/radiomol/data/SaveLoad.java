package com.example.petr.radiomol.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by petr on 28.7.17.
 */

public class SaveLoad {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String preferencesName = "radioMol";
    String preferencesRadioName = "radioName";
    String preferencesRadioUrl = "radioUrl";
    String preferencesVolume = "radioVolume";
    String preferencesWeb = "radioWeb";
    String preferencesFacebook = "radioFacebook";
    String preferencesSms = "radioSms";
    String preferencesCall = "radioCall";
    String preferencesCar = "radioCar";
    String preferencesAlarm = "radioAlarm";
    String preferencesAlarmTime = "radioAlarmTime";


    public SaveLoad(Context context){
        preferences = context.getSharedPreferences(preferencesName, 0);
        editor = preferences.edit();
    }


    public void saveRadioName(String name){

        editor.remove(preferencesRadioName);
        editor.putString(preferencesRadioName, name);
        editor.commit();
    }


    public void saveRadioWeb(String web){

        editor.remove(preferencesWeb);
        editor.putString(preferencesWeb, web);
        editor.commit();
    }


    public void saveRadioUrl(String url){

        editor.remove(preferencesRadioUrl);
        editor.putString(preferencesRadioUrl, url);
        editor.commit();
    }


    public void saveRadioFacebook(String facebook){

        editor.remove(preferencesFacebook);
        editor.putString(preferencesFacebook, facebook);
        editor.commit();
    }


    public void saveRadioCall(String call){

        editor.remove(preferencesCall);
        editor.putString(preferencesCall, call);
        editor.commit();
    }


    public void saveRadioCar(String car){

        editor.remove(preferencesCar);
        editor.putString(preferencesCar, car);
        editor.commit();
    }


    public void saveVolume(int volume){

        editor.remove(preferencesVolume);
        editor.putInt(preferencesVolume, volume );
        editor.commit();
    }


    public void saveAlarm(boolean alarm){

        editor.remove(preferencesAlarm);
        editor.putBoolean(preferencesAlarm, alarm);
        editor.commit();
    }


    public void saveAlarmTime(String time){

        editor.remove(preferencesAlarmTime);
        editor.putString(preferencesAlarmTime, time);
        editor.commit();
    }


    public String loadRadioName(){

        return preferences.getString(preferencesRadioName, "");
    }


    public String loadRadioWeb(){

        return preferences.getString(preferencesWeb, "");
    }


    public String loadRadioFacebook(){

        return preferences.getString(preferencesFacebook, "");
    }


    public String loadRadioCall(){

        return preferences.getString(preferencesCall, "");
    }


    public String loadRadioCar(){

        return preferences.getString(preferencesCar, "");
    }


    public String loadRadioUrl(){

        return preferences.getString(preferencesRadioUrl, "");
    }


    public int loadVolume(){

        return preferences.getInt(preferencesVolume, 0);
    }


    public boolean loadAlarm(){

        return  preferences.getBoolean(preferencesAlarm, false);
    }


    public String loadAlarmTime(){

        return  preferences.getString(preferencesAlarmTime, "");
    }
}
