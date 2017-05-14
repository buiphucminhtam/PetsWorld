package com.minhtam.petsworld.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    }

    private void AddEvent() {
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
                } else {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(LoginActivity.this,R.string.login_successed, Toast.LENGTH_SHORT);
                    toast.show();
                    //Get user info
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(jsonObject.getString("id"));
                    userInfo.setFullname(jsonObject.getString("fullname"));
                    //start activity
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("userInfo",userInfo);
                    startActivity(i);
                    edtUsername.setText("");
                    edtPasssword.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
