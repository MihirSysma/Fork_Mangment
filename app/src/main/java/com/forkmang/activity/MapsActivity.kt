package com.forkmang.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.forkmang.R
import com.forkmang.helper.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

open class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private var longitude: Double = 0.0
    private var c_longitude: Double = 0.0
    private var c_latitude: Double = 0.0
    private var latitude: Double = 0.0

    //Session session;
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    var mLocationMarkerText: TextView? = null

    var text: TextView? = null
    var mapFragment: SupportMapFragment? = null
    private val storePrefrence by lazy { StorePrefrence(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_activity)
        val btn_updateloc: Button = findViewById(R.id.btn_updateloc)
        ApiConfig.getLocation(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mLocationMarkerText = findViewById<View>(R.id.locationMarkertext) as TextView?
        text = findViewById(R.id.tvLocation)
        val fabSatellite: FloatingActionButton = findViewById(R.id.fabSatellite)
        val fabStreet: FloatingActionButton = findViewById(R.id.fabStreet)
        mapFragment?.getMapAsync(this@MapsActivity)
        btn_updateloc.setOnClickListener { updateloc() }
        fabSatellite.setOnClickListener(OnClickListener {
            mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            mapFragment?.getMapAsync(this@MapsActivity)
        })
        fabStreet.setOnClickListener(OnClickListener {
            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            mapFragment?.getMapAsync(this@MapsActivity)
        })
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!Utils.isLocationEnabled(this)) {
                // notify user
                val dialog: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(this)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton(
                    "Open location settings"
                ) { paramDialogInterface, paramInt ->
                    val myIntent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.setNegativeButton("Cancel"
                ) { paramDialogInterface, paramInt ->
                    // TODO Auto-generated method stub
                }
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            showToastMessage("Location not supported in this device")
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "OnMapReady")
        mMap = googleMap
        mMap?.setOnCameraChangeListener { cameraPosition ->
            Log.d("Camera postion change" + "", cameraPosition.toString() + "")
            //latLng = cameraPosition.target;
            val latLng: LatLng = cameraPosition.target
            mMap?.clear()
            try {
                val mLocation: Location = Location("")
                mLocation.latitude = latLng.latitude
                mLocation.longitude = latLng.longitude
                Log.d("latlong==>", "" + latLng.longitude)
                c_latitude = latLng.latitude
                c_longitude = latLng.longitude
                latitude = latLng.latitude
                longitude = latLng.longitude
                Log.d("move", "" + latLng.latitude)
                Log.d("move", "" + latLng.longitude)
                //mLocationMarkerText.setText("Lat: " + latLng.latitude + "," + "Long: " + latLng.longitude);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if ((ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED))
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location_permission))
                    .setMessage(getString(R.string.location_permission_message))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this@MapsActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            0
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            }
        }
    }

    private fun updateloc() {
        //final LatLng latLng = new LatLng(c_latitude, c_longitude);
        Log.d("lat", "" + latitude)
        Log.d("longitude", "" + longitude)
        saveLocation(latitude, longitude)
        Log.d("clat==>", "" + c_latitude)
        val handler: Handler = Handler()
        handler.postDelayed({
            text?.text = getString(R.string.location_1) + ApiConfig.getAddress(
                latitude,
                longitude,
                this@MapsActivity
            )
            val view: View = findViewById(R.id.main_layout_id)
            val message: String = "Your location has been updated"
            val duration: Int = Snackbar.LENGTH_SHORT
            showSnackbar(view, message, duration)
        }, 1000)
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        storePrefrence.setData(Constant.KEY_LATITUDE, latitude.toString())
        storePrefrence.setData(Constant.KEY_LONGITUDE, longitude.toString())
        finish()
        //storePrefrence.setString(Constant.KEY_LATITUDE, String.valueOf(latitude));
        //storePrefrence.setString(Constant.KEY_LONGITUDE, String.valueOf(longitude));
    }

    fun showSnackbar(view: View?, message: String?, duration: Int) {
        Snackbar.make((view)!!, (message)!!, duration).show()
    }

    fun UpdateLocation_m(view: View?) {
        updateloc()
    }

    @SuppressLint("MissingPermission")
    public override fun onConnected(bundle: Bundle?) {
        if ((ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED))
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location_permission))
                    .setMessage(getString(R.string.location_permission_message))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this@MapsActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            0
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            }
        }
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation
            .addOnSuccessListener(this
            ) { location ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    c_longitude = location.longitude
                    c_latitude = location.latitude

                    //longitude = location.getLongitude();
                    //latitude = location.getLatitude();
                    if ((storePrefrence.getCoordinates(Constant.KEY_LATITUDE) == "0.0") || (storePrefrence.getCoordinates(
                            Constant.KEY_LONGITUDE
                        ) == "0.0")
                    ) {
                        longitude = location.longitude
                        latitude = location.latitude
                    } else {
                        longitude =
                            storePrefrence.getCoordinates(Constant.KEY_LONGITUDE)?.toDouble()?:0.0
                        latitude =
                            storePrefrence.getCoordinates(Constant.KEY_LATITUDE)?.toDouble()?:0.0
                    }
                    Log.d("c_latitude==>", "" + c_latitude)
                    Log.d("c_longitude==>", "" + c_longitude)
                    Log.d("latitude==>", "" + latitude)
                    Log.d("longitude==>", "" + longitude)
                    moveMap(location, true)
                }
            }
        try {
            val mLocationRequest: LocationRequest = LocationRequest()
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 5000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.FusedLocationApi.requestLocationUpdates(
                (mGoogleApiClient)!!, mLocationRequest, this
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection suspended")
        mGoogleApiClient?.connect()
    }

    override fun onLocationChanged(location: Location) {
        try {
            moveMap(location, false)
            LocationServices.FusedLocationApi.removeLocationUpdates(
                (mGoogleApiClient)!!, this
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    override fun onStart() {
        super.onStart()
        try {
            mGoogleApiClient?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveLocation(latitude, longitude)
        //saveLocation(c_latitude, c_longitude);
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.getMapAsync(this)
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
    override fun onStop() {
        super.onStop()
        try {
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        if (mGoogleApiClient != null && mGoogleApiClient?.isConnected == true) {
            mGoogleApiClient?.disconnect()
        }
    }

    private fun checkPlayServices(): Boolean {
        val resultCode: Int = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(
                    resultCode, this,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )?.show()
            } else {
                //finish();
            }
            return false
        }
        return true
    }

    private fun moveMap(location: Location, isfirst: Boolean) {
        Log.d(TAG, "Reaching map$mMap")
        if ((ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    || (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED))
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location_permission))
                    .setMessage(getString(R.string.location_permission_message))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this@MapsActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            0
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            }
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap?.uiSettings?.isZoomControlsEnabled = false
            val latLong: LatLng = LatLng(latitude, longitude)
            val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(15f).tilt(60f).build()
            mMap?.isMyLocationEnabled = true
            mMap?.uiSettings?.isMyLocationButtonEnabled = true
            if (isfirst) {
                mMap?.animateCamera(
                    CameraUpdateFactory
                        .newCameraPosition(cameraPosition)
                )
            }
            Log.d("lat%", "" + latitude)
            Log.d("long%", "" + longitude)
            text?.text = getString(R.string.location_1) + ApiConfig.getAddress(
                latitude,
                longitude,
                this@MapsActivity
            )
        } else {
            showToastMessage("Sorry! unable to create maps")
        }
    }

    companion object {
        private val PLAY_SERVICES_RESOLUTION_REQUEST: Int = 9000
        private val TAG: String = "MAP LOCATION"
        private val REQUEST_CODE_AUTOCOMPLETE: Int = 1
    }
}