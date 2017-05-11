package com.minhtam.petsworld.Util.Dialog;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by st on 2/19/2017.
 */

public class MyAlertDialog {

    AlertDialog.Builder builder;


    public MyAlertDialog(Context context, String tittle, String message) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(tittle);
        builder.setMessage(message);

    }


    public AlertDialog.Builder getBuilder() {
        return builder;
    }
}
