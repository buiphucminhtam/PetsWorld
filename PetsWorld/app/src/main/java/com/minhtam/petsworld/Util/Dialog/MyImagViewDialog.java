package com.minhtam.petsworld.Util.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.ImageView;

import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

/**
 * Created by st on 2/19/2017.
 */

public class MyImagViewDialog {
    private Dialog dialog;
    private ImageView imvDialogViewImage;
    private Context context;
    private String url;

    public MyImagViewDialog(Context context, int contentview, String urlImage) {
        this.context = context;
        this.url = urlImage;
        dialog = new Dialog(context);
        dialog.setContentView(contentview);
    }


    public void ShowDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.show();
            imvDialogViewImage = (ImageView) dialog.findViewById(R.id.imvDialogViewImage);
            Picasso.with(context).load(WebserviceAddress.WEB_ADDRESS + url).fit().into(imvDialogViewImage);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
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
