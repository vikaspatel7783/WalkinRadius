package com.walkinradius.beacon.ui;

import android.app.AlertDialog;
import android.content.Context;

public class UiUtils {

    public static AlertDialog getAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }

}
