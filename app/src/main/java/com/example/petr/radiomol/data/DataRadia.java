package com.example.petr.radiomol.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by petr on 31.7.17.
 */

public class DataRadia {

    private String stanice;
    private String vysilac;
    private String www;
    private String facebook;
    private String sms;
    private String call;
    private String car;
    private String speed32;
    private String speed64;
    private String speed128;


    public String getStanice() {
        return stanice;
    }

    public void setStanice(String stanice) {
        this.stanice = stanice;
    }

    public String getVysilac() {
        return vysilac;
    }

    public void setVysilac(String vysilac) {
        this.vysilac = vysilac;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getSpeed32() {
        return speed32;
    }

    public void setSpeed32(String speed32) {
        this.speed32 = speed32;
    }

    public String getSpeed64() {
        return speed64;
    }

    public void setSpeed64(String speed64) {
        this.speed64 = speed64;
    }

    public String getSpeed128() {
        return speed128;
    }

    public void setSpeed128(String speed128) {
        this.speed128 = speed128;
    }

    @Override
    public String toString()
    {
        return stanice + "\nStream: " + vysilac;
    }

}
