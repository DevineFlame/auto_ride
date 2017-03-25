package com.example.ravi1.bhaiya_auto;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import cn.refactor.lib.colordialog.PromptDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private View navHeader;
    ImageView imgNavHeaderBg,imgProfile;

    private ActionBarDrawerToggle mDrawerToggle;
    public GoogleMap mMap;

    TextView txtName,txtWebsite;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
  //  AppLocationService appLocationService;
    ArrayList<icon> list;


    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        SharedPreferences preferences=getSharedPreferences("fff",MODE_PRIVATE);
        preferences.edit().putString("check","false").commit();
        setContentView(R.layout.activity_maps);

        toolbar=(Toolbar)findViewById(R.id.toolbar);

        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(getSupportActionBar()!=null)
        {getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);
        }*/
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        setUpNavigationView();
        list=new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            fm.getMapAsync(this);
            // setContentView(R.layout.activity_main);

        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getSupportActionBar()ActionBar().setTitle("");
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void setUpNavigationView() {
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        Glide.with(this).load(R.drawable.signup2)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        SharedPreferences preferences=getSharedPreferences("userd",MODE_PRIVATE);
        String str=preferences.getString("imagepath","");
        if(!str.equals("")&&!preferences.getString("uid","").equals("")){
            Glide.with(this).load(str)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
        else
        {  Glide.with(this).load(R.drawable.user)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);}
        if(!preferences.getString("name","").equals("")&&!preferences.getString("uid","").equals(""))
        {txtName.setText(preferences.getString("name",""));
            txtWebsite.setText(preferences.getString("mobile",""));}
        else
        {
            txtName.setText("XYZ");
            txtWebsite.setText("999999");
        }
        // navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.notification:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Intent i = new Intent(MapsActivity.this, Notification.class);
                        startActivity(i);
                        break;
                    case R.id.my_account:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        //Intent a=new Intent(MapsActivity.this,MyAccount.class);
                       // startActivity(a);
                        break;
                    case R.id.my_uploads:

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                       // Intent b=new Intent(MapsActivity.this,MyUploads.class);
                       // startActivity(b);
                        break;
                    default:
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });

    }
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lat,lng;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                GPSTracker gps = new GPSTracker(this);
                    SharedPreferences preferences=getSharedPreferences("fff",MODE_PRIVATE);
                Log.d("oh",preferences.getString("check","dfdf"));
                    if(true)
                    { mMap.setMyLocationEnabled(true);
                        googleMap = mMap;
                        // googleMap.setMyLocationEnabled(true);

                        // check if GPS enabled
                        if(gps.canGetLocation()){
                            lat = gps.getLatitude();
                            lng = gps.getLongitude();
                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
                            drawMarker(new LatLng(lat, lng),googleMap,1);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                        callAsynchronousTask(lat,lng,googleMap);
                    }
                    else
                    {
                       /* Double p_lat,p_lng,d_lat,d_lng;
                        p_lat=Double.parseDouble(preferences.getString("p_lat","31.848484"));
                        p_lng=Double.parseDouble(preferences.getString("p_lng","30.654156165"));
                        d_lng=Double.parseDouble(preferences.getString("p_lng","34.561561561"));
                        d_lat=Double.parseDouble(preferences.getString("p_lng","35.5165515164"));
                        preferences.edit().putString("chek","false").commit();
                        drawMarker(new LatLng(p_lat,p_lng),googleMap,2);
                        drawMarker(new LatLng(d_lat,d_lng),googleMap,3);*/

                    }
                    Calendar cal = Calendar.getInstance();
                    /*Intent intent = new Intent(MapsActivity.this, AlarmReceiver.class);
                    PendingIntent pintent = PendingIntent.getService(MapsActivity.this, 0, intent, 0);
                    AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10*1000, pintent);*/
                    buildGoogleApiClient();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }


                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final int position = (int)(marker.getTag());
                       int i;
                        for( i=0;i<list.size();i++){
                        if(position==(i+2)){
                          final  AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                            // Get the layout inflater

                            final AlertDialog alertDialog=builder.create();
                            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();

                            // Inflate and set the layout for the dialog
                            // Pass null as the parent view because its going in the dialog layout
                            builder.setView(inflater.inflate(R.layout.dialogbox, null))
                                    // Add action buttons
                                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            Dialog f=(Dialog) dialog;
                                            final EditText price,dest,size;
                                            price=(EditText)f.findViewById(R.id.price);
                                            dest=(EditText)f.findViewById(R.id.destination);
                                            size=(EditText)f.findViewById(R.id.members);

                                            String Size=size.getText().toString();
                                            String Price=price.getText().toString();
                                            String Dest=dest.getText().toString();
                                            GPSTracker gps = new GPSTracker(MapsActivity.this);

                                            double lat=31.084155,lng=32.151584561;
                                            if(gps.canGetLocation()){
                                               lat = gps.getLatitude();
                                                lng = gps.getLongitude();}
                                            new senddetails(Price,Dest,Size,Double.toString(lat),Double.toString(lng),list.get(position).getLat(),list.get(position).getLang()).execute();
                                        }
                                    })
                                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            alertDialog.cancel();
                                        }
                                    });
                             builder.create().show();
                           // new senddetails("yguygs","udshusd","dshcius").execute();
                            Toast.makeText(MapsActivity.this,"hello world",Toast.LENGTH_SHORT).show();
                            break;
                        }}
                        return false;
                    }
                });
            }
        }
        else {
            //buildGoogleApiClient();

            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));
        }
    }


    public void callAsynchronousTask(final Double lat, final Double lng, final GoogleMap googlemap) {
        final Handler handler = new Handler();

        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
           // @Override
            double latt,lngg;
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("osdijfid","run has been called");
                        try {
                            GPSTracker gps = new GPSTracker(MapsActivity.this);

                            // check if GPS enabled
                            if(gps.canGetLocation()){

                                latt = gps.getLatitude();
                                 lngg = gps.getLongitude();

                                // \n is for new line
                                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lng, Toast.LENGTH_LONG).show();
                                drawMarker(new LatLng(latt, lngg),googlemap,1);
                                //googlemap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latt, lngg)));
                               // callAsynchronousTask(lat,lng,googlemap);
                                Calendar cal = Calendar.getInstance();
                    /*Intent intent = new Intent(MapsActivity.this, AlarmReceiver.class);
                    PendingIntent pintent = PendingIntent.getService(MapsActivity.this, 0, intent, 0);
                    AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10*1000, pintent);*/
                              //  buildGoogleApiClient();
                            }else{
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }

                            new GetDetails(Double.toString(latt),Double.toString(lngg),googlemap).execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000000); //execute in every 5000 ms
    }




    private Marker drawMarker(LatLng point,GoogleMap googleMap,int position){
        // Creating an instance of MarkerOptions
        Log.d("adding mark",Integer.toString(position));
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        Marker marker=googleMap.addMarker(markerOptions);
        marker.setTag(position);
        return marker;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    private class senddetails extends AsyncTask<Void, String, String> {

        ProgressDialog pDialog;
        String price,dest,members;
        TextView userdetials;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }
        public  senddetails(String price,String dest,String members,String p_lat,String p_lng,String dr_lat,String dr_lng){
         this.price=price;
            this.dest=dest;
            this.members=members;
            this.
        }
        String jsonStr;
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/accept_data/");
          //  httppost.setHeader("x-csrf-token", X_CSRF_TOKEN);
            //  AndroidMultiPartEntity entity;
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("userData",price));
         //   list.add(new BasicNameValuePair("dest",dest));
          //  list.add(new BasicNameValuePair("p",price));
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

            showAlert(result);
            super.onPostExecute(result);
        }
        private void showAlert(String message) {
            new PromptDialog(MapsActivity.this)
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
    private class GetDetails extends AsyncTask<Void, String, String> {

        ProgressDialog pDialog;
        String uid;
        GoogleMap googleMap;
        String lat,lng;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }
        public  GetDetails(String lat,String lng,GoogleMap googleMap){
            this.uid=uid;
            this.lat=lat;
            this.lng=lng;
            this.googleMap=googleMap;
        }
        String jsonStr;
        protected String doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://172.20.53.21:8000/user_side/auto_in_range/");

            List<NameValuePair> list=new ArrayList<NameValuePair>(1);
            SharedPreferences preferences=getSharedPreferences("userd",MODE_PRIVATE);
            list.add(new BasicNameValuePair("lat",lat));
            list.add(new BasicNameValuePair("lng",lng));
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
            //Log.d("rfefef",result.substring(result.indexOf("{"), result.lastIndexOf("}")+1))
            if(list.size()!=0){
                for(int i=0;i<list.size();i++){
                    list.get(i).getMarker().remove();
                    list.remove(i);
                }
            }
            if(result.equals("org.apache.http.conn.HttpHostConnectException: Connection to http://172.31.81.238 refused"))
                showAlert("seems like you are not connected to wifi please connnect to collee internet");
            else if(result.equals("Sorry No data Available"))
                showAlert("No Notifications yet");
            else
           {  try {
                    JSONObject jsonObj = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
                    //Toast.makeText(c, "jfjfijfw", Toast.LENGTH_SHORT).show();
                    JSONArray details = jsonObj.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject c = details.getJSONObject(i);
                        String lat = c.getString("lat");
                        String lng = c.getString("lng");
                        String type = c.getString("type");
                        String id=c.getString("id");
                       // String name=c.getString("name");
                        icon a = new icon();
                        a.setId(id);
                        a.setLang(lng);
                        a.setLat(lat);
                        a.setType(type);
                       // a.setName(name);
                        list.add(a);
                        Log.d("sdsd","storing the string value");
                    }
                }
                catch (JSONException e){
                    Toast.makeText(MapsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }
              // showAlert(result);
                Log.d("jjjjj",Integer.toString(list.size()));
              }
            for(int i=0;i<list.size();i++){
                icon a=list.get(i);
                Log.d("lat and alng",a.getLat()+"    "+a.getLang());
                a.setMarker(drawMarker(new LatLng(Double.parseDouble(a.getLat()), Double.parseDouble(a.getLang())),googleMap,i+2));
            }
            super.onPostExecute(result);
        }
        private void showAlert(String message) {
            new PromptDialog(MapsActivity.this)
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
