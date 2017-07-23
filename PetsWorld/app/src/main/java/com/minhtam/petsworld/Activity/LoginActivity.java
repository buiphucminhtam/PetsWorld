package com.minhtam.petsworld.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {
    private final String TAG = "LOG";

    //UI
    private EditText edtUsername,edtPasssword;
    private Button btnLogin,btnRegister;
    private CheckBox cbSaveLogin;

    //
    private String username = "";
    private String password = "";

    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        edtUsername = (EditText) findViewById(R.id.edtLoginUserName);
        edtPasssword = (EditText) findViewById(R.id.edtLoginUserPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        cbSaveLogin = (CheckBox) findViewById(R.id.cbSaveLogin);
    }

    private void AddEvent() {

        //Check save login
        SharedPreferences pre=getSharedPreferences ("userLogin",MODE_PRIVATE);

        if (pre.getBoolean("checked", false)) {
            Log.d("LoginActivity","isSaveLogin: true");
            username = pre.getString("username", "");
            password = pre.getString("pwd","");
            if (!username.equals("")) {
                if (!password.equals("")) {
                    new AsyncTaskLogin().execute();
                }
            }
        }else Log.d("LoginActivity","isSaveLogin: false");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check input field
                username = edtUsername.getText().toString();
                password = edtPasssword.getText().toString();
                if (!username.equals("")) {
                    if (!password.equals("")) {
                        new AsyncTaskLogin().execute();
                    }
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    //Call login
    private class AsyncTaskLogin extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                CallLogin login = new CallLogin();
                return login.Login(username, password);
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
            Log.d(TAG,s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                if (jsonObject.getString("RESULT").equals("0")) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (jsonObject.getString("RESULT").equals("-1")) {
                    toast = Toast.makeText(LoginActivity.this, jsonObject.getString("MSG"), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (jsonObject.getInt("state") == 2) {
                        toast = Toast.makeText(LoginActivity.this,"Tài khoản đang bị khóa", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(LoginActivity.this, R.string.login_successed, Toast.LENGTH_SHORT);
                        toast.show();
                        //Get user info
                        UserInfo userInfo = new UserInfo();
                        userInfo.setId(jsonObject.getString("id"));
                        userInfo.setFullname(jsonObject.getString("fullname"));
                        userInfo.setUsername(username);
                        userInfo.setPassword(password);
                        Log.d(TAG, "userInfoJSON: " + userInfo.toJSON());

                        //Check to save login
                        SharedPreferences pre = getSharedPreferences("userLogin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pre.edit();
                        if (cbSaveLogin.isChecked()) {

                            editor.putString("username", username);

                            editor.putString("pwd", password);

                            editor.putBoolean("checked", true);

                            editor.commit();
                        } else {
                            editor.clear();
                        }

                        //start activity
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("userInfo", userInfo);
                        startActivity(i);
                        edtUsername.setText("");
                        edtPasssword.setText("");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
