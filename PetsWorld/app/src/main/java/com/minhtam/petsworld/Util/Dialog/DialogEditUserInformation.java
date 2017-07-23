package com.minhtam.petsworld.Util.Dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.minhtam.petsworld.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by st on 3/19/2017.
 */

public class DialogEditUserInformation extends Activity {
    Context context;

    final int PICK_IMAGE = 1;

    //Dialog update information
    MyImagViewDialog dialogUpdateInfo;
    Dialog dialogUpdate;
    ImageView imvUpdateImg;
    EditText edtUpdateName,edtUpdateBirthDay,edtUpdateAddress,edtUpdatePhonenumbers;
    Button btnOK,btnCancel;


    public DialogEditUserInformation(final Context context) {
        this.context = context;
        //Init dialog
        dialogUpdateInfo = new MyImagViewDialog(context, R.layout.dialog_update_userinformation,"Chỉnh sửa thông tin");
        dialogUpdate = dialogUpdateInfo.getDialog();
        imvUpdateImg = (ImageView) dialogUpdate.findViewById(R.id.imvUpdateImgUserInfo);
        edtUpdateName = (EditText) dialogUpdate.findViewById(R.id.edtUpdateUserName);
        edtUpdateAddress = (EditText) dialogUpdate.findViewById(R.id.edtUpdateAddress);
        edtUpdateBirthDay = (EditText) dialogUpdate.findViewById(R.id.edtUpdateBirthDay);
        edtUpdatePhonenumbers = (EditText) dialogUpdate.findViewById(R.id.edtUpdatePhonenumber);
        btnOK = (Button) dialogUpdate.findViewById(R.id.btnOK);
        btnCancel = (Button) dialogUpdate.findViewById(R.id.btnCancel);

        //Init DatePicker
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //Update to edt
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = sdf.format(myCalendar.getTime());
                edtUpdateBirthDay.setText(dateString);
            }
        };

        //Set Onclick Edit Text Birth Day
        edtUpdateBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imvUpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                final Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(chooserIntent, PICK_IMAGE);
                    }
                });

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                if (edtUpdateName.getText().toString().equals("")) {
                    toast.cancel();
                    toast.setText("Không được để trống tên");
                    toast.show();
                }
                else
                 if (edtUpdateBirthDay.getText().toString().equals("")) {
                    toast.cancel();
                    toast.setText("Không dược để trống ngày sinh");
                    toast.show();
                }
                else{
                     if (edtUpdatePhonenumbers.getText().toString().equals("")) {
//                         toast.cancel();
//                         toast.setText("Không được để trống số điện thoại");
//                         toast.show();
                            edtUpdatePhonenumbers.setText("None");
                     }
                     if (edtUpdateAddress.getText().toString().equals("")) {
//                    toast.cancel();
//                    toast.setText("Không dược để trống địa chỉ");
//                    toast.show();
                         edtUpdateAddress.setText("None");
                     }
                    UpdateToDB();
                    dialogUpdateInfo.DissmissDialog();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdateInfo.DissmissDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == this.RESULT_OK) {
            if(data == null ) return;
            Uri uri = data.getData();
            imvUpdateImg.setImageURI(uri);
        }
    }

    public void ShowDialog() {
        dialogUpdateInfo.ShowDialog();
    }

    private void UpdateToDB() {


    }

}
