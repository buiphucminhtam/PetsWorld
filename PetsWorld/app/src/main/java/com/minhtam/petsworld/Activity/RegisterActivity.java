package com.minhtam.petsworld.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallRegister;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends Activity {
    private EditText edtUsername,edtPassword,edtPasswordConfirm;
    private Button btnRegister,btnQuit;

    private String username,password,passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        runFadeOutAnimation();

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        edtUsername = (EditText) findViewById(R.id.edtRegisterUsername);
        edtPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtPasswordConfirm = (EditText) findViewById(R.id.edtRegisterPasswordConfirm);
        btnRegister = (Button) findViewById(R.id.btnRegisterOK);
        btnQuit = (Button) findViewById(R.id.btnRegisterQuit);
    }

    private void AddEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                passwordConfirm = edtPasswordConfirm.getText().toString();

                if (CheckInput(username, password, passwordConfirm)) {
                    new AsynctaskRegister().execute();
                }
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void runFadeOutAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        a.reset();
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_register_layout);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

    //Call register
    private class AsynctaskRegister extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            UserInfo userinfo = new UserInfo();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar calobj = Calendar.getInstance();
            userinfo.setUsername(username);
            userinfo.setPassword(password);
            userinfo.setFullname("None");
            userinfo.setPhone("None");
            userinfo.setAddress("None");
            userinfo.setDatecreated(df.format(calobj.getTime()));
            userinfo.setUserimage("None");
            CallRegister register = new CallRegister();
            return register.Call(new Gson().toJson(userinfo));
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s.equals("1")) {
                Toast.makeText(RegisterActivity.this, R.string.register_successed, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    private boolean CheckInput(String username,String password,String passwordConfirm) {
        //Check length first (>6 char for username and password)
        if (username.length() > 6) {
            if (password.length() > 6) {
                if (passwordConfirm.equals(password)) {
                    return true;
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.error_password_notequal_register, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, R.string.error_length_input_register, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, R.string.error_length_input_register, Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
