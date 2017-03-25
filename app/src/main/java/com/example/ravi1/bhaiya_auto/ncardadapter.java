package com.example.ravi1.bhaiya_auto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;

public class ncardadapter extends RecyclerView.Adapter<ncardadapter.MyViewHolder> {
    private Context context;
    private List<icon> list;

    public class MyViewHolder extends RecyclerView.ViewHolder{
      //  private ImageView image;
        private TextView text;
        public MyViewHolder(View view){
            super(view);
            text=(TextView)view.findViewById(R.id.text);
            //delete=(ImageView) view.findViewById(R.id.delete);
        }
    }
    int width;
    ViewGroup.LayoutParams params;
    public ncardadapter(Context context, List<icon> List ){
        this.list=List;
        this.context=context;
    }
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return  new MyViewHolder(itemView);
    }
    public  void onBindViewHolder(final  MyViewHolder holder,int position) {
        final icon a = list.get(position);
        holder.text.setText("User want to book your vehicle for destination "+a.getDest()+" with number of members "+a.getSize()+" for price Rs."+a.getPrice()+" click here to confirm");
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PromptDialog(context)
                        .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                        .setAnimationEnable(true)
                        .setTitleText("Inform")
                        .setContentText("Do you want to confirm the booking???")
                        .setPositiveListener("Accept", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                SharedPreferences preferences=context.getSharedPreferences("userd",context.MODE_PRIVATE);
                                String name=preferences.getString("name","");
                                //preferences.edit().putString("name","Ravi Singh").commit();
                                //new new item.senddetails("confirm",preferences.getString("name",""),name,price,path,title,id,uid,preferences.getString("uid",""),mobile).execute();
                                new senddetails(a.getDriver(),a.getPassenger(),"2").execute();
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
    }
    private void showAlert(String message) {

    }
    public int getItemCount(){
        return list.size();
    }
    private class senddetails extends AsyncTask<Void, String, String> {

        ProgressDialog pDialog;
        String driver,passenger,status;
        TextView userdetials;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }
        public  senddetails(String driver,String passenger,String status ){
            this.driver=driver;
            this.passenger=passenger;
            this.status=status;

        }
        String jsonStr;
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/driver_reply/");
         //   AndroidMultiPartEntity entity;
            Log.d("card adapter","sending to serveer");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("driver",driver));
            list.add(new BasicNameValuePair("passenger",passenger));
            list.add(new BasicNameValuePair("status",status));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    jsonStr = EntityUtils.toString(r_entity);
                } else {
                    jsonStr = "Error occurred! Http Status Code: "
                            + statusCode;
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
           // pDialog.dismiss();
            showAlert(result);
            super.onPostExecute(result);
        }
        private void showAlert(String message) {
            new PromptDialog(context)
                    .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                    .setAnimationEnable(true)
                    .setTitleText("Response from server")
                    .setContentText(message)
                    .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }

    }
}
