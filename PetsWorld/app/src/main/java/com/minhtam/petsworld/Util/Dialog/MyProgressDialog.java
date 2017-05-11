package com.minhtam.petsworld.Util.Dialog;


import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by st on 2/19/2017.
 */

public class MyProgressDialog {
    ProgressDialog dialog;

    public MyProgressDialog(Context context, String tittle, String message) {
        dialog = new ProgressDialog(context);
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setTitle(tittle);
        dialog.setMessage(message);
    }


    public void ShowProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.show();
        }
    }

    public void DissmissProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
