package com.example.ravi1.bhaiya_auto;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AutoDriverSignUp extends AppCompatActivity {

    private EditText usernameField,nameField,emailField,passwordField,confirmField,mobileField,addressField,licField,plateField;
    private Button signUpButton;
    private String userName;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String address;
    private String lic;
    private String plateNumber;
    private String type;
    private JSONObject userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_driver_sign_up);
        addListenerOnButton();

    }
    public void  addListenerOnButton()
    {
        signUpButton =(Button)findViewById(R.id.autosubmitBtn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectData();
            }
        });
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_auto:
                if (checked)
                {
                    type="autodriver";
                }
                break;
            case R.id.checkbox_tempo:
                if (checked)
                {
                    type="tempodriver";
                }
                break;
        }
    }
    private void collectData()
    {
        usernameField=(EditText)findViewById(R.id.usernameField);
        nameField=(EditText)findViewById(R.id.nameField);
        emailField=(EditText)findViewById(R.id.emailField);
        passwordField=(EditText)findViewById(R.id.passwordField);
        confirmField=(EditText)findViewById(R.id.confirmPasswordField);
        mobileField=(EditText)findViewById(R.id.mobileField);
        addressField=(EditText)findViewById(R.id.addressField);


        userName =usernameField.getText().toString();
        name=nameField.getText().toString();
        email=emailField.getText().toString();
        password=passwordField.getText().toString();
        confirmPassword=confirmField.getText().toString();
       // address =addressField.getText().toString();
       // lic =licField.getText().toString();
        plateNumber=plateField.getText().toString();

        if(userName.equals("")||name.equals("")||email.equals("")||password.equals("")||confirmPassword.equals("")||address.equals(""))
        {
            Toast.makeText(this,"Please, Fill all fields",Toast.LENGTH_LONG);
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this,"Password do not match",Toast.LENGTH_LONG);
            passwordField.setText("");
            confirmField.setText("");
        }
        else
        {
            userData=new JSONObject();
            try {
                userData.put("username",userName);
                userData.put("name",name);
                userData.put("email",email);
                userData.put("password",password);
                userData.put("address",address);
                userData.put("licence",lic);
                userData.put("platenumber",plateNumber);
                Log.d("shubh",userData.toString());
                Toast.makeText(this,"SignUp successfully",Toast.LENGTH_LONG);
                senduserdata abc = new senduserdata(userName,name,email,password,address,type);
                abc.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private class senduserdata extends AsyncTask<Void,String,String> {

        private String userName,name,email,password,address,lic,plateNumber,type;
        String jsonStr;
        public senduserdata(String userName,String name,String email,String password,String address,String type)
        {
            this.userName=userName;
            this.name=name;
            this.email=email;
            this.password=password;
            this.address=address;
            this.lic=lic;
            this.plateNumber=plateNumber;
            this.type=type;

        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/auto_driver_signup/");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("username",userName));
            list.add(new BasicNameValuePair("name",name));
            list.add(new BasicNameValuePair("email",email));
            list.add(new BasicNameValuePair("password",password));
            list.add(new BasicNameValuePair("address",address));
            Log.d("shubh",type);
            list.add(new BasicNameValuePair("type",type));
            // Log.d("shubh",list);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = httpclient.execute(httppost);
                Log.d("shubh","kuch to huya");
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    jsonStr = EntityUtils.toString(r_entity);
                } else {
                    jsonStr = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonStr;
        }
    }

}