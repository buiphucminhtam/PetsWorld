package com.minhtam.petsworld.Util.Dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by st on 2/19/2017.
 */

public class MyCustomDialog {
    Dialog dialog;

    public MyCustomDialog(Context context, int contentview, String tittle) {
        dialog = new Dialog(context);
        dialog.setContentView(contentview);
        dialog.setTitle(tittle);
        dialog.setCancelable(false);
    }


    public void ShowDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.show();
        }
    }

    public void DissmissDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public Dialog getDialog() {
        return dialog;
    }
}
