package com.example.ravi1.bhaiya_auto;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


public class Notification extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    SharedPreferences preferences;
    ArrayList<icon> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
        preferences=getSharedPreferences("fff",MODE_PRIVATE);

        setContentView(R.layout.activity_notification);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        new GetDetails("shubham").execute();
    }
    private class GetDetails extends AsyncTask<Void, String, String> {
        ProgressDialog pDialog;
        String uid;
        String username;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }
        public  GetDetails(String username){
            this.username=username;
        }
        String jsonStr;
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/get_notifications/");
          // AndroidMultiPartEntity entity;
            List<NameValuePair> list=new ArrayList<NameValuePair>(2);
            SharedPreferences preferences=getSharedPreferences("userd",MODE_PRIVATE);
            list.add(new BasicNameValuePair("username",username));
            list.add(new BasicNameValuePair("type","driver"));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    jsonStr = EntityUtils.toString(r_entity);
                } else {
                    jsonStr = "faile";
                }
            } catch (ClientProtocolException e) {
                jsonStr = e.toString();
            } catch (IOException e) {
                jsonStr = e.toString();
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d("rfefef",result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));

            if(result.equals("org.apache.http.conn.HttpHostConnectException: Connection to http://172.31.81.238 refused"))
                showAlert("seems like you are not connected to wifi please connnect to collee internet");
            else if(result.equals("Sorry No data Available"))
                showAlert("No Notifications yet");
            else
            { try {
                    JSONObject jsonObj = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
                    //Toast.makeText(c, "jfjfijfw", Toast.LENGTH_SHORT).show();
                    JSONArray details = jsonObj.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject c = details.getJSONObject(i);
                        String status = c.getString("status");
                        String passenger = c.getString("passenger");
                        String p_lng = c.getString("p_lng");
                        String price=c.getString("price");
                        String dest=c.getString("dest");
                        String driver=c.getString("driver");
                        String init=c.getString("init");
                        String dr_lng=c.getString("dr_lng");
                        String p_lat=c.getString("p_lat");
                        String dr_lat=c.getString("dr_lat");
                        String id=c.getString("id");
                        String size=c.getString("size");
                        icon a = new icon();
                        a.setStatus(status);
                        a.setPassenger(passenger);
                        a.setP_lng(p_lng);
                        a.setP_lat(p_lat);
                        a.setId(id);
                        a.setPrice(price);
                        a.setDest(dest);
                        a.setDriver(driver);
                        a.setInit(init);
                        a.setDr_lat(dr_lat);
                        a.setSize(size);a.setDr_lng(dr_lng);
                        list.add(a);
                    }
                }
                catch (JSONException e){
                    Toast.makeText(Notification.this,e.toString(),Toast.LENGTH_LONG).show();
                }
                showAlert(result);
                Log.d("jjjjj",Integer.toString(list.size()));
               ncardadapter ncardadapter=new ncardadapter(Notification.this,list);
                recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));
                recyclerView.setAdapter(ncardadapter);}
            super.onPostExecute(result);
        }
        private void showAlert(String message) {
            new PromptDialog(Notification.this)
                    .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                    .setAnimationEnable(true)
                    .setTitleText("Inform")
                    .setContentText(message)
                    .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            SharedPreferences preferences=getSharedPreferences("userd",MODE_PRIVATE);
                            String name=preferences.getString("name","");
                            //preferences.edit().putString("name","Ravi Singh").commit();
                            //new item.senddetails("confirm",preferences.getString("name",""),name,price,path,title,id,uid,preferences.getString("uid",""),mobile).execute();
                            dialog.dismiss();
                        }
                    }).show();
        }

    }
}
