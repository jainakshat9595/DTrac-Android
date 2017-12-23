package com.wishadesign.app.dtrac.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton mLoginButton;
    private EditText mEditUsername;
    private EditText mEditPassword;
    private TextView mForgotPasswordButton;
    private TextView mSignupButton;

    ProgressDialog mProgress;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        mSessionManager = SessionManager.getInstance(getBaseContext());
        if(mSessionManager.isLoggedIn()) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String username = mEditUsername.getText().toString();
                final String password = mEditPassword.getText().toString();

                if(username == null || password == null || username.equals("") || password.equals("")) {
                    Snackbar.make(view, "Username or Password is empty!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        try {
                            JSONObject resp = new JSONObject(response);
                            Log.d("LoginActivity", resp.getString("cp_userId"));
                            if(resp.getString("result").equals("fail")) {
                                Snackbar.make(view, resp.getString("message"), Snackbar.LENGTH_LONG).show();
                            } else {
                                mSessionManager.createLoginSession(resp.getString("cp_userId"), username);

                                Intent i = new Intent(getBaseContext(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                   }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Log.d("LoginActivity", error.getMessage());
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("username", username);
                        param.put("password", password);
                        return param;
                    }
                };

                mProgress.show();
                APIRequest.getInstance(getBaseContext()).addToRequestQueue(strRequest);
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ForgotPasswordActivty.class);
                startActivity(i);
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), SignupActivity.class);
                startActivity(i);
            }
        });
    }

    private void initViews() {
        mLoginButton = (FloatingActionButton) findViewById(R.id.login_btn);
        mEditUsername = (EditText) findViewById(R.id.input_email);
        mEditPassword = (EditText) findViewById(R.id.input_password);
        mForgotPasswordButton = (TextView) findViewById(R.id.forgot_pass_btn);
        mSignupButton = (TextView) findViewById(R.id.signup_btn);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

    }

}
