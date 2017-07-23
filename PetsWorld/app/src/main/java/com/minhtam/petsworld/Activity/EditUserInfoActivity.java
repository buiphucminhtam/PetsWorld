package com.minhtam.petsworld.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallUserInfo;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditUserInfoActivity extends AppCompatActivity {
    private final int REQUEST_PLACE_PICKER = 999;
    private final String TAG = "EditUserInfoActivity";
    private final int PICK_IMAGE = 1;

    //Dialog update information
    private ImageView imvUpdateImg;
    private EditText edtUpdateName,edtUpdateAddress,edtUpdatePhonenumbers;
    private Button btnOK;
    private CallUserInfo callUserInfo;
    private Toast alert;

    private Uri imagePicked;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        InitToolBar();
        AddControl();
        AddEvent();
    }

    private void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chỉnh sửa thông tin cá nhân");
    }

    private void AddControl() {
        imvUpdateImg = (ImageView) findViewById(R.id.imvUpdateImgUserInfo);
        edtUpdateName = (EditText) findViewById(R.id.edtUpdateUserName);
        edtUpdateAddress = (EditText) findViewById(R.id.edtUpdateAddress);
        edtUpdatePhonenumbers = (EditText) findViewById(R.id.edtUpdatePhonenumber);
        btnOK = (Button) findViewById(R.id.btnOK);
        
        initUI();

        callUserInfo = new CallUserInfo();
    }

    private void initUI() {
        UserInfo userInfo = MainActivity.userInfo;
        if (!userInfo.getUserimage().equals("None")) {
            Picasso.with(EditUserInfoActivity.this).load(WebserviceAddress.WEB_ADDRESS+userInfo.getUserimage()).fit().into(imvUpdateImg);
        }
        edtUpdateName.setText(userInfo.getFullname());
        edtUpdatePhonenumbers.setText(userInfo.getPhone());
        edtUpdateAddress.setText(userInfo.getAddress());
    }

    private void AddEvent() {

        imvUpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                final Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTextEmpty(edtUpdateName.getText().toString())) {
                    showAlert();
                } else if (!checkTextEmpty(edtUpdateAddress.getText().toString())) {
                    showAlert();
                } else if (!checkTextEmpty(edtUpdatePhonenumbers.getText().toString())) {
                    showAlert();
                } else {
                    UserInfo userInfo = MainActivity.userInfo;
                    userInfo.setPhone(edtUpdatePhonenumbers.getText().toString());
                    userInfo.setFullname(edtUpdateName.getText().toString());
                    userInfo.setAddress(edtUpdateAddress.getText().toString());
                    new UpdateInfo().execute();
                }
            }
        });

        edtUpdateAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new loadOpenMap().execute();
                }
            }
        });

        edtUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    //run update image in asynctask
    public String updateUserImage() {
        InputStream image_stream = null;
        try {
            image_stream = getContentResolver().openInputStream(imagePicked);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap= BitmapFactory.decodeStream(image_stream );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String result = callUserInfo.UpdateUserImage(Base64.encodeToString(byteArray,Base64.DEFAULT)
                ,Integer.parseInt(MainActivity.userInfo.getId()),MainActivity.userInfo.getUserimage());

        return result;
    }

    private boolean checkTextEmpty(String text) {
        if (text.equals("")) {
            return false;
        }
        return true;
    }

    private void showAlert() {
        if (alert != null) {
            alert.cancel();
        }
        alert = Toast.makeText(this,R.string.error_input,Toast.LENGTH_SHORT);
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Log.d(TAG,data.toString());
            if (data == null) return;
            imagePicked = data.getData();
            Log.d(TAG,imagePicked.toString());
            imvUpdateImg.setImageURI(imagePicked);
        }else
        if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
//                final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            edtUpdateAddress.setText(address);


        }
    }

    private class UpdateInfo extends AsyncTask<Void,Void,Integer> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditUserInfoActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            String userimage;
            if (imagePicked != null) {
                userimage = updateUserImage();
            } else {
                userimage = MainActivity.userInfo.getUserimage();
            }

            Log.d(TAG,"userimage: "+ userimage);
            if (!userimage.equals("0")) {
                MainActivity.userInfo.setUserimage(userimage);
                if (callUserInfo.Update(MainActivity.userInfo.toJSON()) == 1) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (integer == 1) {
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(EditUserInfoActivity.this, R.string.error_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**GOOGLE MAP**/
    private void OpenMap(){
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            Log.d("GOOGLEMAP", e.toString() + "");
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("GOOGLEMAP", e.toString() + "");
            // ...
        }
    }

    private class loadOpenMap extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditUserInfoActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

        }

        @Override
        protected Void doInBackground(Void... params) {
            OpenMap();
            return null;
        }

    }


}
