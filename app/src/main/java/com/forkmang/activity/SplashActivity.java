package com.forkmang.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.forkmang.R;
import com.forkmang.helper.ApiConfig;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;



public class SplashActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    StorePrefrence storePrefrence;
    Context ctx = SplashActivity.this;

    /*map var*/
    double longitude, c_longitude, c_latitude,latitude;
    GoogleMap mMap;
    String TAG = "MAP LOCATION";

    GoogleApiClient mGoogleApiClient;
    int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Activity activity = SplashActivity.this;
    TextView text,mLocationMarkerText;
    SupportMapFragment mapFragment;

    /*map var end*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        storePrefrence=new StorePrefrence(ctx);
        try {
            if (checkPlayServices()) {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                if (!Utils.isLocationEnabled(ctx)) {
                    // notify user
                    android.app.AlertDialog.Builder dialog_2 = new android.app.AlertDialog.Builder(ctx);
                    dialog_2.setMessage("Location not enabled!");
                    dialog_2.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    });
                    dialog_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog_2.show();
                }
                buildGoogleApiClient();
            } else {
                Toast.makeText(ctx, "Location not supported in this device", Toast.LENGTH_SHORT).show();
            }
            buildGoogleApiClient();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //loadLocale();
        GotoNextScreeen();
        //getToken();


    }

   /* private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SplashActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                if (!mToken.equals(session.getData(Session.KEY_FCM_ID))) {
                    //UpdateToken(mToken, MainActivity.this);
                }
                Log.e("Token",mToken);
            }
        });

    }*/



    private void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }


    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }



    private void GotoNextScreeen()
    {
        new Handler().postDelayed(() -> {

            /*if(storePrefrence.getString(Constant.NAME).length() == 0)
             {
                 final Intent register_intent = new Intent(SplashActivity.this, RegisterActivity.class);
                 startActivity(register_intent);
                 finish();
             }
             else if (storePrefrence.getBoolean("keeplogin")){
                 final Intent dash_board_intent = new Intent(SplashActivity.this, DashBoardActivity_2.class);
                 startActivity(dash_board_intent);
                 finish();
             }
             else if(!storePrefrence.getBoolean("keeplogin"))
             {
                 final Intent dash_board_intent = new Intent(SplashActivity.this, LoginFormActivity.class);
                 startActivity(dash_board_intent);
                 finish();
             }*/

            /*final Intent mainIntent = new Intent(SplashActivity.this, DashBoard_Activity.class);
            startActivity(mainIntent);
            finish();*/

            /*final Intent mainIntent = new Intent(SplashActivity.this, MapsActivity.class);
            startActivity(mainIntent);
            finish();*/

            showAlertView();

        }, 2000);
    }





    public  void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
        LayoutInflater inflater = (LayoutInflater) SplashActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.testmap, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();


        ApiConfig.getLocation(activity);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mLocationMarkerText = (TextView) dialog.findViewById(R.id.locationMarkertext);
        text = dialog.findViewById(R.id.tvLocation);
        mapFragment.getMapAsync(SplashActivity.this);

        dialog.show();
    }

    

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                //latLng = cameraPosition.target;
                LatLng latLng = cameraPosition.target;
                mMap.clear();
                try {
                    Location mLocation = new Location("");
                    mLocation.setLatitude(latLng.latitude);
                    mLocation.setLongitude(latLng.longitude);
                    Log.d("latlong==>",""+latLng.longitude);

                    c_latitude = latLng.latitude;
                    c_longitude = latLng.longitude;

                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    Log.d("move", ""+latLng.latitude);
                    Log.d("move", ""+latLng.longitude);
                    //mLocationMarkerText.setText("Lat: " + latLng.latitude + "," + "Long: " + latLng.longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(getString(R.string.location_permission))
                        .setMessage(getString(R.string.location_permission_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SplashActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }

    }

    private void updateloc() {
        //final LatLng latLng = new LatLng(c_latitude, c_longitude);
        Log.d("lat", ""+latitude);
        Log.d("longitude", ""+longitude);

        saveLocation(latitude, longitude);

        Log.d("clat==>",""+c_latitude);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setText(getString(R.string.location_1) + ApiConfig.getAddress(latitude, longitude, SplashActivity.this));
                View view = findViewById(R.id.main_layout_id);
                String message = "Your location has been updated";

                int duration = Snackbar.LENGTH_SHORT;
                showSnackbar(view, message, duration);
            }
        }, 1000);

    }

    public void saveLocation(double latitude, double longitude) {
        storePrefrence.setString(Constant.KEY_LATITUDE, String.valueOf(latitude));
        storePrefrence.setString(Constant.KEY_LONGITUDE, String.valueOf(longitude));

    }

    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }

    public void UpdateLocation_m(View view) {
        updateloc();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(getString(R.string.location_permission))
                        .setMessage(getString(R.string.location_permission_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SplashActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    c_longitude = location.getLongitude();
                    c_latitude = location.getLatitude();

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    /*if (storePrefrence.getString(Constant.KEY_LATITUDE).equals("0.0") || storePrefrence.getString(Constant.KEY_LONGITUDE).equals("0.0"))
                    {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    } else {
                        longitude = Double.parseDouble(storePrefrence.getString(Constant.KEY_LONGITUDE));
                        latitude = Double.parseDouble(storePrefrence.getString(Constant.KEY_LATITUDE));
                    }*/

                    Log.d("c_latitude==>", ""+c_latitude);
                    Log.d("c_longitude==>", ""+c_longitude);


                    Log.d("latitude==>", ""+latitude);
                    Log.d("longitude==>", ""+longitude);

                    moveMap(location,true);
                }
            }
        });
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                moveMap(location,false);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveLocation(latitude, longitude);
        //saveLocation(c_latitude, c_longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    private void moveMap(Location location, boolean isfirst) {
        Log.d(TAG, "Reaching map" + mMap);
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(getString(R.string.location_permission))
                        .setMessage(getString(R.string.location_permission_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SplashActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        0);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }

        // check if map is created successfully or not
        if (mMap != null)
        {
            mMap.getUiSettings().setZoomControlsEnabled(false);

            LatLng latLong;
            latLong = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(60).build();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            if (isfirst){
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }

            Log.d("lat%",""+ latitude);
            Log.d("long%",""+ longitude);

            text.setText(getString(R.string.location_1) + ApiConfig.getAddress(latitude, longitude, SplashActivity.this));
        } else {
            Toast.makeText(getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}