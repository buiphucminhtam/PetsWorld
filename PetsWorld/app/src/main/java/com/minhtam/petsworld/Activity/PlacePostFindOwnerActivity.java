package com.minhtam.petsworld.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Adapter.ImageAdapter;
import com.minhtam.petsworld.Class.FindOwner;
import com.minhtam.petsworld.Class.PetInfo;
import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPetInfo;
import com.minhtam.petsworld.Util.KSOAP.CallPetType;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlacePostFindOwnerActivity extends AppCompatActivity {
    private final int REQUEST_PLACE_PICKER = 999;
    private Toolbar toolbar;
    private TextView tvPlacePostPetInfo;
    private EditText edtPostWriting;
    private EditText edtPostRequirement;
    private Button btnPostImage;
    private Button btnLocation;

    //Recylerview View Image
    private RecyclerView rvPetImages;
    private ArrayList<String> listImage;
    private LinearLayoutManager linearLayoutManager;
    private ImageAdapter imageAdapter;

    private final String    TAG = "PlacePostFindOwner";
    private final int       REQUEST_PETINFO = 1;
    private final int       REQUEST_PICKIMAGE = 2;
    public static           PetInfo petInfo;
    private Boolean isInsertPetInfo = false;
    public FindOwner findOwner;
    public FindOwnerPost findOwnerPost;
    private Toast toast;
    private ArrayList<String> listUrlImage;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_post_findowner);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                //do the things} else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
        }
        AddControl();
        AddEvent();
    }

    private void AddControl() {
        tvPlacePostPetInfo = (TextView) findViewById(R.id.tvPlacePostPetInfo);
        edtPostWriting = (EditText) findViewById(R.id.edtPostWriting);
        edtPostRequirement = (EditText) findViewById(R.id.edtPostRequirement);
        btnPostImage = (Button) findViewById(R.id.btnPostImage);
        btnLocation = (Button) findViewById(R.id.btnFindOwnerLocation);

        //Init recyclerview
        rvPetImages = (RecyclerView) findViewById(R.id.rvPickedImage);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPetImages.setLayoutManager(linearLayoutManager);
        listImage = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, listImage);
        rvPetImages.setAdapter(imageAdapter);

        findOwnerPost = getIntent().getParcelableExtra("findownerpost");
        petInfo = new PetInfo();
        if (findOwnerPost != null){
            isEdit = true;
            new getListType().execute();
            findOwner = new FindOwner(Integer.parseInt(findOwnerPost.getId()),
                    Integer.parseInt(findOwnerPost.getUserId()),
                    Integer.parseInt(findOwnerPost.getPetId()),
                    findOwnerPost.getDescription(),
                    findOwnerPost.getRequirement(),
                    findOwnerPost.getDatecreated(),"",1,1);

            //disable image button
            btnPostImage.setEnabled(false);
        }
        else {
            isEdit = false;
            findOwner = new FindOwner();
            findOwnerPost = new FindOwnerPost();
        }
        InitToolBar();

    }

    private void AddEvent() {

        if (isEdit) {
            edtPostRequirement.setText(findOwnerPost.getRequirement());
            edtPostWriting.setText(findOwnerPost.getDescription());
            for (Photo photo : findOwnerPost.getListPhoto()) {
                listImage.add(0,photo.getUrl());
                imageAdapter.notifyItemInserted(0);
            }
            imageAdapter.TurnOffClickToDelItem();
            tvPlacePostPetInfo.setBackgroundResource(R.color.body_background);
            isInsertPetInfo = true;

            petInfo.setName(findOwnerPost.getPetname());
            if (findOwnerPost.getVaccine().equals("true")) {
                petInfo.setVacine(1);
            } else {
                petInfo.setVacine(0);
            }
            petInfo.setVaccinedate(findOwnerPost.getVaccinedate());
            petInfo.setId(Integer.parseInt(findOwnerPost.getPetId()));
            petInfo.setUserid(Integer.parseInt(findOwnerPost.getUserId()));

            btnLocation.setText(findOwnerPost.getAddress());
            btnLocation.setBackgroundResource(R.color.transparent);
        }

        tvPlacePostPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacePostFindOwnerActivity.this,InsertPetInfoActivity.class);
                if(isEdit)
                i.putExtra("type","edit");
                else
                i.putExtra("type","new");
                i.putExtra("petinfo",petInfo);
                i.putExtra("findownerpost",findOwnerPost);
                startActivityForResult(i,REQUEST_PETINFO);
            }
        });

        btnPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacePostFindOwnerActivity.this,ImageActivity.class);
                i.putExtra("listImage",listImage);
                startActivityForResult(i,REQUEST_PICKIMAGE);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMap();
            }
        });
    }

    private void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            MenuItem menuItem = menu.add(1,1,1,R.string.update_post);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            MenuItem menuItem = menu.add(1,1,1,R.string.upload_post);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == 1) {
            if (checkInput()) {
                findOwner.setDescription(edtPostWriting.getText().toString());
                findOwner.setRequirement(edtPostRequirement.getText().toString());
                findOwner.setUserid(Integer.parseInt(MainActivity.userInfo.getId()));
                findOwnerPost.setDescription(edtPostWriting.getText().toString());
                findOwnerPost.setRequirement(edtPostRequirement.getText().toString());
                findOwnerPost.setUserId(MainActivity.userInfo.getId());
                if (isEdit) {
                    findOwner.setPetid(Integer.parseInt(findOwnerPost.getPetId()));
                    new updatePost().execute();
                } else {
                    new uploadPost().execute();
                }
            }
        }
        return true;
    }

    private boolean checkInput() {
        if (toast != null) {
            toast.cancel();
        }
        if (edtPostWriting.getText().toString().equals("")) {
            toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_input, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (edtPostRequirement.getText().toString().equals("")) {
            toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_input, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (listImage.size() == 0) {
            toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_photo_numbers, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (!isInsertPetInfo) {
            toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_not_insert_petinfo, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (findOwner.getAddress()==null) {
            toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_not_insert_address, Toast.LENGTH_SHORT);
            toast.show();
            return false;
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
        if (requestCode == REQUEST_PETINFO && resultCode == RESULT_OK) {
            tvPlacePostPetInfo.setBackgroundResource(R.color.body_background);
            petInfo = data.getParcelableExtra("petinfo");
            findOwnerPost = data.getParcelableExtra("findownerpost");
            isInsertPetInfo = true;
        }
        else
            if (requestCode == REQUEST_PICKIMAGE && resultCode == RESULT_OK) {
                listImage = data.getStringArrayListExtra("listImage");
                imageAdapter = new ImageAdapter(PlacePostFindOwnerActivity.this,listImage);
                rvPetImages.setAdapter(imageAdapter);
            }
        else
            if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {

                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(data, this);
//                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                btnLocation.setText(address);
                btnLocation.setBackgroundResource(R.color.transparent);

                //add to post
                LatLng latLng = place.getLatLng();

                findOwner.setAddress(String.valueOf(address));
                findOwner.setLatitute(latLng.latitude);
                findOwner.setLongitude(latLng.longitude);

                findOwnerPost.setAddress(address.toString());
                findOwnerPost.setLatitute(latLng.latitude);
                findOwnerPost.setLongitude(latLng.longitude);

//                String attributions = PlacePicker.getAttributions(data);
//                if (attributions == null) {
//                    attributions = "";
//                }
            }
    }

    private boolean uploadPhoto(int userid, int petid) {
        try {
            listUrlImage = new ArrayList<>();
            CallPhoto callPhoto = new CallPhoto();
            for (String photo : listImage) {
                InputStream image_stream = null;

                image_stream = getContentResolver().openInputStream(Uri.parse(new File("file://"+photo).toString()));

                Bitmap bitmap= BitmapFactory.decodeStream(image_stream );

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String url = callPhoto.InsertPhoto(Base64.encodeToString(byteArray,Base64.DEFAULT),userid,petid);
                listUrlImage.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class uploadPost extends AsyncTask<Void, Void, String> {
        //Upload petinfo first then get id and upload post
        //Upload petinfo
        Dialog progressDialog;
        Toast toast;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlacePostFindOwnerActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallPetInfo callPetInfo = new CallPetInfo();
            int idPetInfo = callPetInfo.Insert(petInfo.toJSON());
            if (idPetInfo == 0) {
                Log.d(TAG,"Error Insert Petinfo");
                return "";
            }
            findOwner.setPetid(idPetInfo);
            CallPostFindOwner callPostFindOwner = new CallPostFindOwner();
            int idPost = callPostFindOwner.InsertPostFindOwner(findOwner.toJSON());
            if (idPost == 0) {
                Log.d(TAG,"Error Insert PostOwner");
                return "";
            }
            findOwner.setId(idPost);

            if (!uploadPhoto(Integer.parseInt(MainActivity.userInfo.getId()), idPetInfo)) {
                Log.d(TAG,"Error Insert Photo");
                return "";
            }

            //Add to list photo
            ArrayList<Photo> listPhotoTemp = new ArrayList<>();

            for (String urlPhoto : listUrlImage) {
                Photo photo = new Photo();
                photo.setUrl(urlPhoto);
                listPhotoTemp.add(photo);
            }

            findOwnerPost.setListPhoto(listPhotoTemp);

            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (!s.equals("OK")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_connection, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent i = getIntent();
                i.putExtra("findownerpost",findOwnerPost);
                setResult(RESULT_OK,i);
                finish();
            }
        }
    }

    private class updatePost extends AsyncTask<Void, Void, String> {
        //Upload petinfo first then get id and upload post
        //Upload petinfo
        Dialog progressDialog;
        Toast toast;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlacePostFindOwnerActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            try {
//                Date reformatDateTime = sdf.parse(petInfo.getVaccinedate());
//                SimpleDateFormat fd = new SimpleDateFormat("yyyy/MM/dd");
//                petInfo.setVaccinedate(fd.format(reformatDateTime));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            CallPetInfo callPetInfo = new CallPetInfo();
            int resultUpdatePetinfo = callPetInfo.Update(petInfo.toJSON());
            if (resultUpdatePetinfo == 1) {
                CallPostFindOwner callPostFindOwner = new CallPostFindOwner();
                if (callPostFindOwner.UpdatePostFindOwner(findOwner.toJSON()) == 1) {
                    return "OK";
                }
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (!s.equals("OK")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(PlacePostFindOwnerActivity.this, R.string.error_connection, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                toast = Toast.makeText(PlacePostFindOwnerActivity.this, "Chỉnh sửa bài đăng thành công", Toast.LENGTH_SHORT);
                toast.show();
                Intent i = getIntent();
                i.putExtra("findownerpost",findOwnerPost);
                setResult(RESULT_OK,i);
                finish();
            }
        }
    }

    private class getListType extends AsyncTask<Void, Void, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlacePostFindOwnerActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallPetType callPetType = new CallPetType();
            String result = callPetType.Get();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PetType>>(){}.getType();
            List<PetType> listPetType = (List<PetType>) gson.fromJson(s, listType);
            Log.d(TAG,"List Pet Type: "+s);
            //get type id
            for (PetType petType : listPetType) {
                if (petType.getTypename().equals(findOwnerPost.getTypename())) {
                    petInfo.setTypeid(petType.getId());
                }
            }
            progressDialog.dismiss();
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


}
