package com.example.danielwinther.androidroomreservations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class HelperClass {
    public static final String URL = "http://anboroomreservation.cloudapp.net/Service1.svc/";
    public static final String ERROR = "error";
    private static final String DEBUG = "debug";

    public void ErrorDialog(Context context, String title, String message) {
        title = title != null ? title : "An error occurred!";
        message = message != null ? message : "An error occurred!";

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            }
        }).show();
    }
}