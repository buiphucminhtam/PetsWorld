package com.minhtam.petsworld.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Adapter.ExpandableListViewPetTypeAdapter;
import com.minhtam.petsworld.Class.PetInfo;
import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPetType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsertPetInfoActivity extends AppCompatActivity {
    private final String TAG = "InsertPetInfoActivity";

    private Toolbar              toolbar;
    private EditText            edtInsertPetInfoName;
    private TextView            tvInsertPetInfo_PetType;
    private CheckBox            cbInsertPetInfo_Vacines;
    private ExpandableListView  exlvInsertPetInfo;
    private Button              btnInsertPetInfoOK;

    private ArrayList<PetType> listChild;
    private ArrayList<String> listHeader;
    private HashMap<String,ArrayList<PetType>> hashMapPetType;
    private ExpandableListViewPetTypeAdapter adapterListPetType;

    private Toast toast;
    private boolean isPickPetType = false;
    private PetInfo petInfo;

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
        cbInsertPetInfo_Vacines = (CheckBox) findViewById(R.id.cbInsertPetInfo_Vacines);
        exlvInsertPetInfo       = (ExpandableListView) findViewById(R.id.exlvInsertPetInfo);
        btnInsertPetInfoOK      = (Button) findViewById(R.id.btnInsertPetInfoOK);

        petInfo = PlacePostActivity.petInfo;
        petInfo.setUserid(Integer.parseInt(MainActivity.userInfo.getId()));

        listHeader = new ArrayList<>();
        listChild = new ArrayList<>();
        hashMapPetType = new HashMap<>();
        adapterListPetType = new ExpandableListViewPetTypeAdapter(this,listHeader,hashMapPetType);
        exlvInsertPetInfo.setAdapter(adapterListPetType);
    }

    private void AddEvent() {
        new getListType().execute();

        exlvInsertPetInfo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String pettype = getString(R.string.pet_type) + ": " + hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename();
                tvInsertPetInfo_PetType.setText(pettype);
                petInfo.setTypeid(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getId());
                isPickPetType = true;
                return true;
            }
        });

        btnInsertPetInfoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInputField()) {
                    petInfo.setName(edtInsertPetInfoName.getText().toString());
                    if (cbInsertPetInfo_Vacines.isChecked()) {
                        petInfo.setVacine(1);
                    } else {
                        petInfo.setVacine(0);
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private boolean CheckInputField() {
        if (edtInsertPetInfoName.getText().toString().equals("")) {
            toast.cancel();
            toast = Toast.makeText(this,R.string.error_input,Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (!isPickPetType) {
            toast = Toast.makeText(this,R.string.error_empty_pettype,Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
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

    private class getListType extends AsyncTask<Void, Void, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InsertPetInfoActivity.this);
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
            List<PetType> posts = (List<PetType>) gson.fromJson(s, listType);
            Log.d(TAG,"List Pet Type: "+s);
            listChild.addAll(posts);
            //add to list header
            if (listChild.size() > 0) {
                //Add header to list header
                for (PetType petType : listChild) {
                    String headerTemp = petType.getTypename().substring(0, petType.getTypename().indexOf(" "));
                    if (listHeader.indexOf(headerTemp) == -1) {
                        listHeader.add(headerTemp);
                    }
                }
                //Add header to hashmap with list
                for (String header : listHeader) {
                    hashMapPetType.put(header,new ArrayList<PetType>());
                }

                //Add child to hash map
                for (PetType petType : listChild) {
                    String headerTemp = petType.getTypename().substring(0, petType.getTypename().indexOf(" "));
                    if (hashMapPetType.get(headerTemp) != null) {
                        hashMapPetType.get(headerTemp).add(petType);
                    }
                }
            }

            if (petInfo.getName() != null) {
                edtInsertPetInfoName.setText(petInfo.getName());
            }
            if (petInfo.getVacine() == 1) {
                cbInsertPetInfo_Vacines.setChecked(true);
            }
            if (petInfo.getTypeid() > 0) {
                for (PetType petType : listChild) {
                    if (petType.getId() == petInfo.getTypeid()) {
                        tvInsertPetInfo_PetType.setText(getString(R.string.pet_type)+": "+petType.getTypename());
                    }
                }

            }

            adapterListPetType.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}
