package com.example.ravi1.bhaiya_auto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;

public class Login extends AppCompatActivity {

    private String email, password;
    private EditText emailField, passwordField;
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInBtn = (Button) findViewById(R.id.logInButton);
        emailField = (EditText) findViewById(R.id.logInEmailField);
        passwordField = (EditText) findViewById(R.id.logInPasswordField);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentication();
            }
        });
    }

    private void authentication() {
        email = emailField.getText().toString();
        password = passwordField.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Fill both Field", Toast.LENGTH_SHORT).show();
        } else if (!validate(email, password)) {
            Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();
        } else {
           // logInBtn.setEnabled(false);
            new AsyncTaskRunner(email,password).execute();

        }

    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String email, password;

        public AsyncTaskRunner(String email, String password) {
            super();
            this.email = email;
            this.password = password;
            //Toast.makeText(getApplicationContext(),this.password,Toast.LENGTH_SHORT).show();
        }

        protected String doInBackground(String... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            Log.d("shubh", email + " " + password);
            String res = postData(email, password);
            //Log.d("shubham",res);
            return res;
        }

        protected void onPostExecute(String result) {
            int a = 10000000;
            String status = null;
            Log.d("shubham", "onPostexecution");
            try {
                int len = result.length();
                StringBuffer bufstr = new StringBuffer(result);
                bufstr.insert(16, '[');
                bufstr.insert(len, ']');
                Log.d("shubh", bufstr.toString());
                JSONObject jsonObj = new JSONObject(bufstr.toString());
                Log.d("shubh", jsonObj.toString());
                JSONArray details = jsonObj.getJSONArray("loginStatus");

                for (int i = 0; i < details.length(); i++) {
                    JSONObject c = details.getJSONObject(i);
                    status = c.getString("error");
                    Log.d("shubh", status);
                }


                //a = Integer.parseInt(result.trim());
                //Log.d("shubh", status);


                if (status.equals("success"))
            {
                    SharedPreferences preferences = getSharedPreferences("userd", MODE_PRIVATE);
                    preferences.edit().putString("username", "shubhamgupta501").commit();
                    //Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();
                    Log.d("shubham", "chalo ho gya");
                    showAlert("Successfully Login");
                    Intent intent=new Intent(Login.this,MapsActivity.class);
                    startActivity(intent);
                } else {
                    showAlert("Some error occured please retry");

                }
            } catch (NumberFormatException nfe) {
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }

        private void showAlert(String message) {
            new PromptDialog(Login.this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                    .setAnimationEnable(true)
                    .setTitleText("Response from server")
                    .setContentText(message)
                    .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            dialog.dismiss();
                            //Intent i = new Intent(Login.this, Login.class);
                            //startActivity(i);
                        }
                    }).show();
        }

        @Override
        protected void onPreExecute() {
            //ProgressDialog progressDialog=new ProgressDialog(Login.this);
            //progressDialog.setMessage("Logging in....");
            //progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

        public String postData(String email, String password) {
            // Create a new HttpClient and Post Header
            String jsonstr = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/login/");
            //HttpResponse response=null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            final String bb;
            try {
                // Add your data

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                Log.d("shubham", "kuch to ho jaa");
                HttpResponse response = httpclient.execute(httppost);
                Log.d("shubham", "done login");
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    jsonstr = EntityUtils.toString(r_entity);
                } else {
                    jsonstr = "Error occurred! Http Status Code: "
                            + statusCode;
                }
                //  response.toString();
                //return response.toString();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            //Log.d("shubh",response.toString());
            Log.d("shubham", jsonstr);
            return jsonstr;
        }
    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            //_passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            //_passwordText.setError(null);
        }

        return valid;
    }
}