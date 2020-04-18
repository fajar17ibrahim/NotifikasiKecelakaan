package com.ta.notifikasikecelakaan.ui.hostipallocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.MapsInitializer;
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
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.ui.policeofficelocation.PoliceOfficeLocationPresenter;
import com.ta.notifikasikecelakaan.utils.Constans;

public class HospitalLocationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, HospitalLocationContract.View, View.OnClickListener {

    private GoogleMap mMap;

    private MapView mapView;
    private MarkerOptions place1, place2;
    private Button getDirection;
    private FloatingActionButton btnCurrentLocation;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private HospitalLocationPresenter hospitalLocationPresenter;

    private TextView tvName, tvAddress;
    private ProgressBar pbLoading;
    private String idHospital;

    private int height = 100;
    private int width = 88;

    private Double latitude, longitude, latitude2, longitude2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_location);

        tvName = (TextView) findViewById(R.id.txt_name);
        tvAddress = (TextView) findViewById(R.id.txt_address);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        idHospital = getIntent().getStringExtra(Constans.TAG_HOSPITAL_ID);

        hospitalLocationPresenter = new HospitalLocationPresenter(this);
        hospitalLocationPresenter.requestDataFromServer(idHospital);

        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(this);

        btnCurrentLocation = (FloatingActionButton) findViewById(R.id.btn_current_location);
        btnCurrentLocation.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        CameraPosition Surabaya = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(13).bearing(0).tilt(0).build();
        mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Surabaya)));
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

    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void setDataToView(Hospital hospital) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(hospital.getName());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName.setText(hospital.getName());
        tvAddress.setText(hospital.getAddress());

        mapView = (MapView) findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        // GET CURRENT LOCATION
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

                    Current = CameraPosition.builder().target(latLng).zoom(13).bearing(0).tilt(0).build();

                    BitmapDrawable bitmapdrawcurrent = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_male);
                    Bitmap bcurrent = bitmapdrawcurrent.getBitmap();
                    Bitmap marker_current = Bitmap.createScaledBitmap(bcurrent, width, height, false);

                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker_current));
                    mMap.addMarker(markerOptions);
                }
            }
        });

        latitude2 = hospital.getLatitude();
        longitude2 = hospital.getLongitude();

        BitmapDrawable bitmapdrawuser = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker);
        Bitmap buser = bitmapdrawuser.getBitmap();
        Bitmap marker_user = Bitmap.createScaledBitmap(buser, width, height, false);

        BitmapDrawable bitmapdrawhospital = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_hospital);
        Bitmap bhospital = bitmapdrawhospital.getBitmap();
        Bitmap marker_hospital = Bitmap.createScaledBitmap(bhospital, width, height, false);

        place1 = new MarkerOptions().position(new LatLng(-7.254542, 112.748604)).icon(BitmapDescriptorFactory.fromBitmap(marker_user));
        place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).icon(BitmapDescriptorFactory.fromBitmap(marker_hospital));

        new FetchURL(HospitalLocationActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(this, "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetDirection:

                break;

            case R.id.btn_current_location:
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
                break;
        }
    }
}
