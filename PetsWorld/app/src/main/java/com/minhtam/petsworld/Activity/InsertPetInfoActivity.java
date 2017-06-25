package com.minhtam.petsworld.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Adapter.ExpandableListViewPetTypeAdapter;
import com.minhtam.petsworld.Class.PetInfo;
import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPetType;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InsertPetInfoActivity extends AppCompatActivity {
    private final String TAG = "InsertPetInfoActivity";

    private Toolbar              toolbar;
    private EditText            edtInsertPetInfoName;
    private TextView            tvInsertPetInfo_PetType;
    private CheckBox            cbInsertPetInfo_Vacines;
    private ExpandableListView  exlvInsertPetInfo;
    private Button              btnInsertPetInfo_VaccineDate;
    private Button              btnInsertPetInfoOK;

    private ArrayList<PetType> listChild;
    private ArrayList<String> listHeader;
    private HashMap<String,ArrayList<PetType>> hashMapPetType;
    private ExpandableListViewPetTypeAdapter adapterListPetType;

    private Toast toast;
    private boolean isPickPetType = false;
    private PetInfo petInfo;

    private boolean isEdit = false;
    private FindOwnerPost findOwnerPost;
    private FindPetPost findPetPost;

    //Dialog Date Picker
    private Dialog datePickerDialog;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pet_info);
        InitToolBar();

        //Get data
        String type = getIntent().getStringExtra("type");
        if (type.equals("edit")) {
            isEdit = true;
        } else {
            isEdit = false;
        }

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        edtInsertPetInfoName         = (EditText) findViewById(R.id.edtInsertPetInfoName);
        tvInsertPetInfo_PetType      = (TextView) findViewById(R.id.tvInsertPetInfo_PetType);
        cbInsertPetInfo_Vacines      = (CheckBox) findViewById(R.id.cbInsertPetInfo_Vacines);
        exlvInsertPetInfo            = (ExpandableListView) findViewById(R.id.exlvInsertPetInfo);
        btnInsertPetInfo_VaccineDate = (Button) findViewById(R.id.btnInsertPetInfo_VaccineDate);
        btnInsertPetInfoOK           = (Button) findViewById(R.id.btnInsertPetInfoOK);

        cbInsertPetInfo_Vacines.setClickable(true);

        petInfo = getIntent().getParcelableExtra("petinfo");
        petInfo.setUserid(Integer.parseInt(MainActivity.userInfo.getId()));

        listHeader = new ArrayList<>();
        listChild = new ArrayList<>();
        hashMapPetType = new HashMap<>();
        adapterListPetType = new ExpandableListViewPetTypeAdapter(this,listHeader,hashMapPetType);
        exlvInsertPetInfo.setAdapter(adapterListPetType);

        //init dialog timepicker
        datePickerDialog = new Dialog(InsertPetInfoActivity.this);
        datePickerDialog.setContentView(R.layout.dialog_datepicker);
        datePickerDialog.setCancelable(true);
        datePickerDialog.setTitle("Chọn ngày tiêm chủng");
        datePicker = (DatePicker) datePickerDialog.findViewById(R.id.DatePicker);
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                btnInsertPetInfo_VaccineDate.setText(date);
                String dateVaccine = datePicker.getYear() + "/" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth();
                petInfo.setVaccinedate(dateVaccine);
            }
        });
    }

    private void AddEvent() {
        new getListType().execute();

        //Init data to view if is edit
        if (isEdit) {
            findOwnerPost = getIntent().getParcelableExtra("findownerpost");
            findPetPost = getIntent().getParcelableExtra("findpetpost");
            if (findOwnerPost != null) {
                edtInsertPetInfoName.setText(findOwnerPost.getPetname());

                if (findOwnerPost.getVaccine().equals("true")) {
                    cbInsertPetInfo_Vacines.setChecked(true);
                    btnInsertPetInfo_VaccineDate.setText(findOwnerPost.getVaccinedate());
                }
                tvInsertPetInfo_PetType.setText(findOwnerPost.getTypename());
            }
            else{
                edtInsertPetInfoName.setText(findPetPost.getPetname());

                if (findPetPost.getVaccine().equals("true")) {
                    cbInsertPetInfo_Vacines.setChecked(true);
                    btnInsertPetInfo_VaccineDate.setText(findPetPost.getVaccinedate());
                }
                tvInsertPetInfo_PetType.setText(findPetPost.getTypename());
            }
        }

        exlvInsertPetInfo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String pettype = getString(R.string.pet_type) + ": " + hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename();
                tvInsertPetInfo_PetType.setText(pettype);
                petInfo.setTypeid(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getId());
                if(findOwnerPost!=null)
                 findOwnerPost.setTypename(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                else if(findPetPost!=null)
                    findPetPost.setTypename(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                isPickPetType = true;
                return true;
            }
        });

        cbInsertPetInfo_Vacines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnInsertPetInfo_VaccineDate.setVisibility(View.VISIBLE);
                    datePickerDialog.show();
                } else {
                    btnInsertPetInfo_VaccineDate.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnInsertPetInfoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInputField()) {
                    petInfo.setName(edtInsertPetInfoName.getText().toString());
                    if(findOwnerPost!=null)
                        findOwnerPost.setPetname(edtInsertPetInfoName.getText().toString());
                    else if(findPetPost!=null)
                        findPetPost.setPetname(edtInsertPetInfoName.getText().toString());

                    if (cbInsertPetInfo_Vacines.isChecked()) {
                        petInfo.setVacine(1);
                        if(findOwnerPost!=null)
                            findOwnerPost.setVaccine("true");
                        else if(findPetPost!=null)
                            findPetPost.setVaccine("true");
                    } else {
                        petInfo.setVacine(0);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String dateString = sdf.format(new Date());
                        petInfo.setVaccinedate(dateString);
                        if(findOwnerPost!=null)
                            findOwnerPost.setVaccine("false");
                        else if(findPetPost!=null)
                            findPetPost.setVaccine("false");
                    }
                    Intent i = getIntent();
                    i.putExtra("petinfo",petInfo);
                    if(findOwnerPost!=null)
                        i.putExtra("findownerpost",findOwnerPost);
                    else if(findPetPost!=null)
                        i.putExtra("findpetpost",findPetPost);
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
        });

        btnInsertPetInfo_VaccineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private boolean CheckInputField() {
        if (edtInsertPetInfoName.getText().toString().equals("")) {
            if(toast!=null) toast.cancel();
            toast = Toast.makeText(this,R.string.error_input,Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (!isPickPetType) {
            if(toast!=null) toast.cancel();
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
                        isPickPetType = true;
                    }
                }

            }

            adapterListPetType.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}
