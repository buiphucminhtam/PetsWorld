package com.minhtam.petsworld.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.minhtam.petsworld.R;

public class InsertPetInfoActivity extends AppCompatActivity {

    private Toolbar              toolbar;
    private EditText            edtInsertPetInfoName;
    private TextView            tvInsertPetInfo_PetType;
    private TextView            tvInsertPetInfo_Vacines;
    private ExpandableListView  exlvInsertPetInfo;
    private Button              btnInsertPetInfoOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pet_info);
        InitToolBar();
        AddControl();
        AddEvent();
    }

    private void AddControl() {
        edtInsertPetInfoName    = (EditText) findViewById(R.id.edtInsertPetInfoName);
        tvInsertPetInfo_PetType = (TextView) findViewById(R.id.tvInsertPetInfo_PetType);
        tvInsertPetInfo_Vacines = (TextView) findViewById(R.id.tvInsertPetInfo_Vacines);
        exlvInsertPetInfo       = (ExpandableListView) findViewById(R.id.exlvInsertPetInfo);
        btnInsertPetInfoOK      = (Button) findViewById(R.id.btnInsertPetInfoOK);
    }

    private void AddEvent() {

    }

    private void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
