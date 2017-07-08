package com.minhtam.petsworld.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.minhtam.petsworld.Adapter.AdapterListPhoto;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Class.Report;
import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPetInfo;
import com.minhtam.petsworld.Util.KSOAP.CallReport;
import com.minhtam.petsworld.Util.KSOAP.CallUserInfo;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FindOwnerItemDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private final String TAG = "FindOwnerItemDetailActivity";
    private final int REQUEST_EDIT = 1;
    private final int RESULT_DELETE = 3;
    private final int RESULT_EDIT = 4;
    private Activity activity;
    private TextView tvFindOwnerItemDetail_Username;
    private TextView tvFindOwnerItemDetail_Datetime;
    private TextView tvFindOwnerItemDetail_Description;
    private TextView tvFindOwnerItemDetail_Requirement;
    private TextView tvPetInfo_Petname;
    private TextView tvPetInfo_PetType;
    private Button btnPetInfo_VaccineDate;
    private CheckBox cbPetInfo_Vacine;
    private ImageView imvFindOwnerItemDetail_bigimage;
    private RecyclerView rvFindOwnerItem_ListImage;
    private AdapterListPhoto adaptetListPhoto;
    private LinearLayoutManager linearLayoutManager;
    private FindOwnerPost findOwnerPost;
    private ImageButton btnMenu;
    private UserInfo userPostInfo;

    //Dialog field
    private Dialog dialogMenu;
    private TextView tvDialogItemDetailReport;
    private TextView tvDialogItemDetailContact;
    private TextView tvDialogItemDetailEdit;
    private TextView tvDialogItemDetailDelete;

    private Intent intentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_owner_item_detail);
        intentFragment = getIntent();
        findOwnerPost = getIntent().getParcelableExtra("selecteditem");
        activity = this;
        AddControl();
        AddEvent();
    }

    private void AddControl() {


        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Thông tin chi tiết");

        tvFindOwnerItemDetail_Username      = (TextView) findViewById(R.id.tvFindOwnerItemDetail_Username);
        tvFindOwnerItemDetail_Datetime      = (TextView) findViewById(R.id.tvFindOwnerItemDetail_Datetime);
        tvFindOwnerItemDetail_Description   = (TextView) findViewById(R.id.tvFindOwnerItemDetail_Description);
        tvFindOwnerItemDetail_Requirement   = (TextView) findViewById(R.id.tvFindOwnerItemDetail_Requirement);
        tvPetInfo_Petname                   = (TextView) findViewById(R.id.tvPetInfo_Petname);
        tvPetInfo_PetType                   = (TextView) findViewById(R.id.tvPetInfo_PetType);
        cbPetInfo_Vacine                    = (CheckBox) findViewById(R.id.cbPetInfo_Vacine);
        btnPetInfo_VaccineDate              = (Button) findViewById(R.id.btnPetInfo_VaccineDate);
        imvFindOwnerItemDetail_bigimage     = (ImageView)findViewById(R.id.imvFindOwnerItemDetail_bigimage);
        rvFindOwnerItem_ListImage           = (RecyclerView) findViewById(R.id.rvFindOwnerItemDetail_ListImage);
        btnMenu                             = (ImageButton) findViewById(R.id.btnFindOwnerItemMenu);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        adaptetListPhoto = new AdapterListPhoto(this,findOwnerPost.getListPhoto());
        rvFindOwnerItem_ListImage.setLayoutManager(linearLayoutManager);
        rvFindOwnerItem_ListImage.setAdapter(adaptetListPhoto);
        dialogMenu = new Dialog(this);
        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenu.setContentView(R.layout.dialog_menu_postitem);
        tvDialogItemDetailReport = (TextView) dialogMenu.findViewById(R.id.tvDialogItemDetailReport);
        tvDialogItemDetailContact = (TextView) dialogMenu.findViewById(R.id.tvDialogItemDetailContact);
        tvDialogItemDetailEdit = (TextView) dialogMenu.findViewById(R.id.tvDialogItemDetailEditPost);
        tvDialogItemDetailDelete = (TextView) dialogMenu.findViewById(R.id.tvDialogItemDetailDeletePost);

        //get userpost info
        new getUserInfo().execute();

    }
    private void AddEvent() {
        if (findOwnerPost.getListPhoto() == null) {
            findOwnerPost.setListPhoto(new ArrayList<Photo>());
        }

        if(findOwnerPost.getFullname() != null) tvFindOwnerItemDetail_Username.setText(findOwnerPost.getFullname());
        if(findOwnerPost.getDatecreated() != null) tvFindOwnerItemDetail_Datetime.setText(findOwnerPost.getDatecreated());
        if(findOwnerPost.getDescription() != null) tvFindOwnerItemDetail_Description.setText(findOwnerPost.getDescription());
        if(findOwnerPost.getRequirement() != null) tvFindOwnerItemDetail_Requirement.setText(findOwnerPost.getRequirement());
        if(findOwnerPost.getPetname() != null) tvPetInfo_Petname.setText(findOwnerPost.getPetname());
        if(findOwnerPost.getTypename() != null) tvPetInfo_PetType.setText(findOwnerPost.getTypename());
        if(findOwnerPost.getVaccine() != null) {
            if (findOwnerPost.getVaccine().equals("false")) {
                cbPetInfo_Vacine.setChecked(false);
                btnPetInfo_VaccineDate.setVisibility(View.INVISIBLE);
            } else {
                cbPetInfo_Vacine.setChecked(true);
                btnPetInfo_VaccineDate.setVisibility(View.VISIBLE);
                btnPetInfo_VaccineDate.setText(findOwnerPost.getVaccinedate());
            }
        }

        if (findOwnerPost.getListPhoto().size() == 1) {
            rvFindOwnerItem_ListImage.setVisibility(View.GONE);
            Log.d("URL",findOwnerPost.getListPhoto().get(0).getUrl());
            Picasso.with(this).load(WebserviceAddress.WEB_ADDRESS+findOwnerPost.getListPhoto().get(0).getUrl()).into(imvFindOwnerItemDetail_bigimage);
        } else {
            imvFindOwnerItemDetail_bigimage.setVisibility(View.GONE);
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMenu.show();
                Window window = dialogMenu.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                window.setAttributes(wlp);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);

                if (userPostInfo.getId().equals(MainActivity.userInfo.getId())) {
                    tvDialogItemDetailEdit.setVisibility(View.VISIBLE);
                    tvDialogItemDetailDelete.setVisibility(View.VISIBLE);
                    tvDialogItemDetailContact.setVisibility(View.GONE);
                    tvDialogItemDetailReport.setVisibility(View.GONE);
                } else {
                    tvDialogItemDetailContact.setVisibility(View.VISIBLE);
                    tvDialogItemDetailReport.setVisibility(View.VISIBLE);
                    tvDialogItemDetailEdit.setVisibility(View.GONE);
                    tvDialogItemDetailDelete.setVisibility(View.GONE);
                }


                tvDialogItemDetailContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userPostInfo == null) {
                            new getUserInfo().execute();
                        } else {
                            showDialogUserInfo();
                        }
                    }
                });

                tvDialogItemDetailReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogReport = new Dialog(activity);
                        dialogReport.setContentView(R.layout.dialog_report_post);
                        dialogReport.setTitle("Gửi báo cáo");
                        dialogReport.show();
                        dialogMenu.dismiss();

                        final EditText edtMsg = (EditText) dialogReport.findViewById(R.id.edtDialogReportMsg);
                        Button btnSend = (Button) dialogReport.findViewById(R.id.btnDialogReportSend);

                        btnSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String msg = edtMsg.getText().toString();
                                Report report = new Report(0,Integer.parseInt(findOwnerPost.getUserId()),
                                        Integer.parseInt(findOwnerPost.getPetId()),1,
                                        msg,"");

                                new insertReport().execute(report.toJson());
                                dialogReport.dismiss();
                            }
                        });


                    }
                });

                tvDialogItemDetailEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //start activity place post with findowner post
                        Intent i = new Intent(activity, PlacePostFindOwnerActivity.class);
                        i.putExtra("findownerpost",findOwnerPost);
                        startActivityForResult(i,REQUEST_EDIT);
                    }
                });

                tvDialogItemDetailDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new deletePetInfo().execute();
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            FindOwnerPost findOwnerPost = data.getParcelableExtra("findownerpost");
            intentFragment.putExtra("findownerpost",findOwnerPost);
            setResult(RESULT_EDIT,intentFragment);
            finish();
        }
    }

    private class getUserInfo extends AsyncTask<Void,Void,String> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindOwnerItemDetailActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            String result = callUserInfo.CallGet(Integer.parseInt(findOwnerPost.getUserId()));

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                UserInfo userInfo = new Gson().fromJson(jsonArray.getString(0),UserInfo.class);
                userPostInfo = userInfo;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class deletePetInfo extends AsyncTask<Void,Void,String> {
        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindOwnerItemDetailActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallPetInfo callPetInfo = new CallPetInfo();
            int result = callPetInfo.Delete(Integer.parseInt(findOwnerPost.getPetId()));
            return String.valueOf(result);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1")) {
                Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();
                Intent i = getIntent().putExtra("deleted",findOwnerPost.getId());
                setResult(RESULT_DELETE,i);
                finish();
            } else {
                Toast.makeText(activity, "Xóa thất bại, kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    private class insertReport extends AsyncTask<String,Void,String> {
        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindOwnerItemDetailActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(String... params) {
            CallReport callReport = new CallReport();
            int result = callReport.Insert(params[0]);
            return String.valueOf(result);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1")) {
                Toast.makeText(activity, "Gửi báo cáo thành công", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "Gửi báo cáo thất bại, kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    private void showDialogUserInfo() {
        final Dialog dialog = new Dialog(FindOwnerItemDetailActivity.this);
        dialog.setContentView(R.layout.dialog_userinformation_detail);
        dialog.setTitle("Thông tin liên lạc");
        dialog.show();

        TextView tvName = (TextView) dialog.findViewById(R.id.tvUserName);
        TextView tvAddress = (TextView) dialog.findViewById(R.id.tvUserAddress);
        TextView tvPhoneNumber = (TextView) dialog.findViewById(R.id.tvPhoneNumbers);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgUserImage);

        Button btnLock = (Button) dialog.findViewById(R.id.btnUserInfomation_Detail_BlockUser);

        if (userPostInfo != null) {
            if (userPostInfo.getState().equals("2")) {
                btnLock.setText("Mở tài khoản");
            } else {
                btnLock.setText("Khóa tài khoản");
            }
        }

        if (MainActivity.isAdmin) {
            btnLock.setVisibility(View.VISIBLE);
        }

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPostInfo != null) {
                    if (userPostInfo.getState().equals("2")) {
                        new unlockUser().execute();
                        dialog.dismiss();
                    } else {
                        if (!userPostInfo.getState().equals("0")) {
                            new lockUser().execute();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });

        tvName.setText(userPostInfo.getFullname());
        tvAddress.setText(userPostInfo.getAddress());
        tvPhoneNumber.setText(userPostInfo.getPhone());

        Picasso.with(this).load(WebserviceAddress.WEB_ADDRESS + userPostInfo.getUserimage()).into(imageView);
    }

    private class lockUser extends AsyncTask<Void,Void,Integer> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindOwnerItemDetailActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            int result = 0;
            if(userPostInfo!=null)
                result = callUserInfo.LockUser(Integer.parseInt(userPostInfo.getId()));
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String text = "";
            if(s==1)
                text = "Khóa tài khoản thành công";
            else
                text = "Lỗi, Kiểm tra lại kết nối mạng";

            Toast.makeText(FindOwnerItemDetailActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private class unlockUser extends AsyncTask<Void,Void,Integer> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindOwnerItemDetailActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            int result = 0;
            if(userPostInfo!=null)
                result = callUserInfo.UnlockUser(Integer.parseInt(userPostInfo.getId()));
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String text = "";
            if(s==1)
                text = "Mở khóa tài khoản thành công";
            else
                text = "Lỗi, Kiểm tra lại kết nối mạng";

            Toast.makeText(FindOwnerItemDetailActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }
}
