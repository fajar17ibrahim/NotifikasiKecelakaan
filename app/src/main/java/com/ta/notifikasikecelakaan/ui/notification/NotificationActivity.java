package com.ta.notifikasikecelakaan.ui.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.utils.Constans;

public class NotificationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener {

    private GoogleMap mMap;

    private MapView mapView;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private TextView tvDistance, tvAddress;
    private FloatingActionButton btnCurrentLocation;
    private ProgressBar pbLoading;

    private int height = 100;
    private int width = 86;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double latitude2 = 0.0;
    private Double longitude2 = 0.0;

    private float distance;

    private SharedPreferences sharedpreferences;
    private String idRespondent;
    private int user_id;

    private Button btnNavigation;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lokasi Kecelakaan");

        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCurrentLocation = (FloatingActionButton) findViewById(R.id.btn_current_location);
        btnCurrentLocation.setOnClickListener(this);

        btnNavigation = (Button) findViewById(R.id.btn_navigation);
        btnNavigation.setOnClickListener(this);

        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);

        sharedpreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        idRespondent = sharedpreferences.getString(Constans.TAG_RESPONDENT_ID, "0");
        user_id = sharedpreferences.getInt(Constans.TAG_USER_ID, 0);
        distance = Float.parseFloat(sharedpreferences.getString(Constans.TAG_RESPONDENT_DISTANCE, "0"));

        tvDistance.setText( distance + " KM dari lokasi anda saat ini");
        latitude = Double.parseDouble(sharedpreferences.getString(Constans.TAG_RESPONDENT_LAT, "0.0"));
        longitude = Double.parseDouble(sharedpreferences.getString(Constans.TAG_RESPONDENT_LONG, "0.0"));
        latitude2 = Double.parseDouble(sharedpreferences.getString(Constans.TAG_USER_LAT, "0.0"));
        longitude2 = Double.parseDouble(sharedpreferences.getString(Constans.TAG_USER_LONG, "0.0"));

        tvAddress.setText(latitude2 + ", " + longitude2);

        mapView = (MapView) findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        currentLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        BitmapDrawable bitmapdrawrespondent = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_male);
        Bitmap brespondent = bitmapdrawrespondent.getBitmap();
        Bitmap marker_respondent = Bitmap.createScaledBitmap(brespondent, width, height, false);

        BitmapDrawable bitmapdrawuser = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker);
        Bitmap buser = bitmapdrawuser.getBitmap();
        Bitmap marker_user = Bitmap.createScaledBitmap(buser, width, height, false);

        place1 = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromBitmap(marker_respondent));
        place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).icon(BitmapDescriptorFactory.fromBitmap(marker_user));

        new FetchURL(NotificationActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(13).bearing(0).tilt(0).build();
        mMap.moveCamera((CameraUpdateFactory.newCameraPosition(cameraPosition)));

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    public void currentLocation() {

//      GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
            if (location != null){
                // Do it all with location
                Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                Current = CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(0).build();

                BitmapDrawable bitmapdrawcurrent = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_male);
                Bitmap bcurrent = bitmapdrawcurrent.getBitmap();
                Bitmap marker_current = Bitmap.createScaledBitmap(bcurrent, width, height, false);

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker_current));
                mMap.addMarker(markerOptions);
            }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_current_location:
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
                break;

            case R.id.btn_navigation:
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude2 + ","+ longitude2);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(mContext, "Tujuan tidak valid", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
