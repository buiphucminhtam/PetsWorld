package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtam.petsworld.Activity.EditUserInfoActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.LayoutManager.FindOwnerPostLayoutManager;
import com.minhtam.petsworld.LayoutManager.FindPetPostLayoutManager;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallUserInfo;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {


    private ImageView imvUserImage;
    private TextView tvUserName,tvUserAddress,tvUserPhonenumbers;
    private Button btnUserInfor_ChangePassword;
    private View v;

    private ViewGroup mPostLayout;
    //Find Owner User Post
    private View mFindOwnerPostLayout;
    private FindOwnerPostLayoutManager findOwnerPostLayoutManager;
    //Find Pet User Post
    private View mFindPetPostLayout;
    private FindPetPostLayoutManager findPetPostLayoutManager;

    private TextView tvUserInformation_FindOwnerPost,tvUserInformation_FindPetPost;

    private final int EDIT_USERINFO = 1;

    private Dialog dialogChangePassword;

    public UserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_information, container, false);
        setHasOptionsMenu(true);
        AddControl();
        AddEvent();

        return v;
    }

    private void AddControl() {
        imvUserImage = (ImageView) v.findViewById(R.id.imgUserImage);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvUserAddress = (TextView) v.findViewById(R.id.tvUserAddress);
        tvUserPhonenumbers = (TextView) v.findViewById(R.id.tvPhoneNumbers);
        btnUserInfor_ChangePassword = (Button) v.findViewById(R.id.btnUserInfor_ChangePassword);
        mPostLayout = (ViewGroup) v.findViewById(R.id.layoutListPost);

        tvUserInformation_FindOwnerPost = (TextView) v.findViewById(R.id.tvUserInformation_FindOwnerPost);
        tvUserInformation_FindPetPost = (TextView) v.findViewById(R.id.tvUserInformation_FindPetPost);

        updateUserInfo();
    }

    private void AddEvent() {
        btnUserInfor_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });

        //Event for texview post
        tvUserInformation_FindOwnerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findOwnerPostLayoutManager == null) {
                    initFindOwnerPostLayout();
                }
                changeViewPost(mFindOwnerPostLayout);
                tvUserInformation_FindOwnerPost.setBackgroundResource(R.color.selector);
                tvUserInformation_FindPetPost.setBackgroundResource(R.color.colorPrimary);
            }
        });

        tvUserInformation_FindPetPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findPetPostLayoutManager == null) {
                    initFindPetPostLayout();
                }
                changeViewPost(mFindPetPostLayout);
                tvUserInformation_FindOwnerPost.setBackgroundResource(R.color.colorPrimary);
                tvUserInformation_FindPetPost.setBackgroundResource(R.color.selector);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(1, 1, 1, R.string.edit);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent i = new Intent(getActivity(), EditUserInfoActivity.class);
            startActivityForResult(i, EDIT_USERINFO);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_USERINFO && resultCode == getActivity().RESULT_OK) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        if (MainActivity.userInfo.getUserimage() != null)
            if (!MainActivity.userInfo.getUserimage().equals("None")) {
                Picasso.with(getContext()).load(WebserviceAddress.WEB_ADDRESS + MainActivity.userInfo.getUserimage()).fit().into(imvUserImage);
            }
        tvUserName.setText(MainActivity.userInfo.getFullname());
        tvUserAddress.setText(MainActivity.userInfo.getAddress());
        tvUserPhonenumbers.setText(MainActivity.userInfo.getPhone());
    }

    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtNewPasswordConfirm;

    private void initDialog() {
        dialogChangePassword = new Dialog(getContext());
        dialogChangePassword.setContentView(R.layout.layout_change_password);
        dialogChangePassword.setTitle(R.string.change_password);
        edtOldPassword = (EditText) dialogChangePassword.findViewById(R.id.edtChangePassword_OldPassworld);
        edtNewPassword = (EditText) dialogChangePassword.findViewById(R.id.edtChangePassword_NewPassworld);
        edtNewPasswordConfirm = (EditText) dialogChangePassword.findViewById(R.id.edtChangePassword_NewPassworld_Confirm);
        Button btnOK = (Button) dialogChangePassword.findViewById(R.id.btnChangePassworldOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInputPassword(edtOldPassword.getText().toString(), edtNewPassword.getText().toString(), edtNewPasswordConfirm.getText().toString())) {
                    new AsynctaskChangePassword().execute();
                }
            }
        });

        dialogChangePassword.show();
    }

    private void initFindOwnerPostLayout() {
        mFindOwnerPostLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_userinformation_findowner_post,null);
        findOwnerPostLayoutManager = new FindOwnerPostLayoutManager(getContext(), mFindOwnerPostLayout);
        mPostLayout.addView(mFindOwnerPostLayout);
        mFindOwnerPostLayout.setVisibility(View.GONE);
    }

    private void initFindPetPostLayout() {
        mFindPetPostLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_userinformation_findpet_post,null);
        findPetPostLayoutManager = new FindPetPostLayoutManager(getContext(), mFindPetPostLayout);
        mPostLayout.addView(mFindPetPostLayout);
        mFindPetPostLayout.setVisibility(View.GONE);
    }

    private void changeViewPost(View v) {
//        mPostLayout.removeAllViews();
//        if(((ViewGroup)v.getParent())!=null)
//            ((ViewGroup)v.getParent()).removeAllViews();
//        mPostLayout.addView(v);
        for (int i=0; i< mPostLayout.getChildCount(); i++) {
            if (v == mPostLayout.getChildAt(i)) {
                v.setVisibility(View.VISIBLE);
            }else mPostLayout.getChildAt(i).setVisibility(View.GONE);

        }
    }


    private boolean CheckInputPassword(String oldpassword, String password, String passwordConfirm) {
        if (!oldpassword.equals(MainActivity.userInfo.getPassword())) {
            Toast.makeText(getActivity(), R.string.error_input_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() > 6) {
            if (passwordConfirm.equals(password)) {
                return true;
            } else {
                Toast.makeText(getActivity(), R.string.error_password_notequal_register, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_length_input_register, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    //Call register
    private class AsynctaskChangePassword extends AsyncTask<Void, Void, Integer> {
        Dialog progressDialog;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            password = edtNewPassword.getText().toString();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            return callUserInfo.ChangePassword(password, Integer.parseInt(MainActivity.userInfo.getId()));
        }

        @Override
        protected void onPostExecute(Integer s) {
            if (s == 1) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.changepassword_successed, Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }



}
