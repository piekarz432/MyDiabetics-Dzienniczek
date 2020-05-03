package com.karol.piekarski.mydiabetics_dzienniczek.Java.Class;

import android.app.Activity;
import android.app.AlertDialog;

public class Authentication {

    private Activity activity;
    private AlertDialog alertDialog;

    public Authentication(Activity activity)
    {
        this.activity=activity;
    }

    public void loadingDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
    }
}
