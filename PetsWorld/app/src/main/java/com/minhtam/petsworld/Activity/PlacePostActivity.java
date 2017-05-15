package com.minhtam.petsworld.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.minhtam.petsworld.Adapter.ImageAdapter;
import com.minhtam.petsworld.Class.PetInfo;
import com.minhtam.petsworld.R;

import java.util.ArrayList;

public class PlacePostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvPlacePostPetInfo;
    private EditText edtPostWriting;
    private EditText edtPostRequirement;
    private Button btnPostImage;

    //Recylerview View Image
    private RecyclerView rvPetImages;
    public static ArrayList<String> listImage;
    private LinearLayoutManager linearLayoutManager;
    private ImageAdapter imageAdapter;

    private final String TAG = "PlacePostActivity";
    private final int REQUEST_PETINFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_post);
        InitToolBar();
        AddControl();
        AddEvent();
    }

    private void AddControl() {
        tvPlacePostPetInfo = (TextView) findViewById(R.id.tvPlacePostPetInfo);
        edtPostWriting = (EditText) findViewById(R.id.edtPostWriting);
        edtPostRequirement = (EditText) findViewById(R.id.edtPostRequirement);
        btnPostImage = (Button) findViewById(R.id.btnPostImage);

        //Init recyclerview
        rvPetImages = (RecyclerView) findViewById(R.id.rvPickedImage);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvPetImages.setLayoutManager(linearLayoutManager);
        listImage = new ArrayList<>();
        imageAdapter = new ImageAdapter(this,listImage);
        rvPetImages.setAdapter(imageAdapter);
    }

    private void AddEvent() {
        tvPlacePostPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacePostActivity.this,InsertPetInfoActivity.class);
                startActivityForResult(i,REQUEST_PETINFO);
            }
        });
        btnPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacePostActivity.this,ImageActivity.class);
                startActivity(i);
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
        if (requestCode == REQUEST_PETINFO && resultCode == RESULT_OK) {
            PetInfo petInfo = data.getParcelableExtra("petinfo");
        }
    }
}
