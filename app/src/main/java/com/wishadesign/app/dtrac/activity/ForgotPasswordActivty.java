package com.wishadesign.app.dtrac.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivty extends AppCompatActivity {

    private EditText mEmail;
    private FloatingActionButton mForgotPassConBtn;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();

        mForgotPassConBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                if(email==null || email.equals("")) {
                    Toast.makeText(getBaseContext(), "Kindly enter an Email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.FORGOT_PASSWORD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        try {
                            JSONObject resp = new JSONObject(response);
                            String result = resp.getString("result");
                            String message = resp.getString("message");

                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mProgress.dismiss();
                                Log.d("ForgotPasswordActivty", error.getMessage());
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<String, String>();
                        param.put("email", email);
                        return param;
                    }
                };

                mProgress.show();
                APIRequest.getInstance(getBaseContext()).addToRequestQueue(strRequest);
            }
        });

    }

    public void initViews() {
        mEmail = (EditText) findViewById(R.id.input_email);
        mForgotPassConBtn = (FloatingActionButton) findViewById(R.id.forgot_pass_con_btn);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);
    }

}
