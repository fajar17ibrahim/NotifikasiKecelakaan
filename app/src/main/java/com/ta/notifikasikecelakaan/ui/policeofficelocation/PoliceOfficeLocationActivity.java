package com.ta.notifikasikecelakaan.ui.policeofficelocation;

import androidx.annotation.Nullable;
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
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfileContract;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfilePresenter;
import com.ta.notifikasikecelakaan.utils.Constans;


public class PoliceOfficeLocationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private GoogleMap mMap;

    private MapView mapView;
    private MarkerOptions place1, place2;
    private Button getDirection;
    private FloatingActionButton btnCurrentLocation;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private TextView tvName, tvAddress;
    private ProgressBar pbLoading;
    private String idPoliceOffice;

    private int height = 100;
    private int width = 86;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double latitude2 = 0.0;
    private Double longitude2 = 0.0;

    private Button btnNavigation;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_office_location);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kantor polisi");
        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        tvName = (TextView) findViewById(R.id.txt_name);
        tvAddress = (TextView) findViewById(R.id.txt_address);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        btnCurrentLocation = (FloatingActionButton) findViewById(R.id.btn_current_location);
        btnCurrentLocation.setOnClickListener(this);

        btnNavigation = (Button) findViewById(R.id.btn_navigation);
        btnNavigation.setOnClickListener(this);

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

                    Current = CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(0).build();

                    BitmapDrawable bitmapdrawcurrent = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_male);
                    Bitmap bcurrent = bitmapdrawcurrent.getBitmap();
                    Bitmap marker_current = Bitmap.createScaledBitmap(bcurrent, width, height, false);

                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker_current));
                    mMap.addMarker(markerOptions);
                }
            }
        });

        latitude = Double.parseDouble(sharedPreferences.getString(Constans.TAG_USER_LAT, "0.0"));
        longitude = Double.parseDouble(sharedPreferences.getString(Constans.TAG_USER_LONG, "0.0"));

        Bundle data = getIntent().getExtras();

        tvName.setText(data.getString(Constans.TAG_POLICEOFFICE_NAME));
        tvAddress.setText(data.getString(Constans.TAG_POLICEOFFICE_ADDRESS));
        latitude2 = data.getDouble(Constans.TAG_POLICEOFFICE_LAT);
        longitude2 = data.getDouble(Constans.TAG_POLICEOFFICE_LONG);

        BitmapDrawable bitmapdrawuser = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker);
        Bitmap buser = bitmapdrawuser.getBitmap();
        Bitmap marker_user = Bitmap.createScaledBitmap(buser, width, height, false);

        BitmapDrawable bitmapdrawpolice = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_police);
        Bitmap bpolice = bitmapdrawpolice.getBitmap();
        Bitmap marker_police = Bitmap.createScaledBitmap(bpolice, width, height, false);

        place1 = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromBitmap(marker_user));
        place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).icon(BitmapDescriptorFactory.fromBitmap(marker_police));

        new FetchURL(PoliceOfficeLocationActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        CameraPosition cPoliceOffice = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(13).bearing(0).tilt(0).build();
        mMap.moveCamera((CameraUpdateFactory.newCameraPosition(cPoliceOffice)));
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
    public void onClick(View v) {
        switch (v.getId()) {
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

            case R.id.btn_current_location:
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
            break;
        }
    }
}