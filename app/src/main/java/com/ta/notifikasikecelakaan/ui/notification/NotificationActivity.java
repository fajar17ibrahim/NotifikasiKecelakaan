package com.ta.notifikasikecelakaan.ui.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.ui.home.HomeContract;
import com.ta.notifikasikecelakaan.ui.home.HomePresenter;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfileContract;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfilePresenter;
import com.ta.notifikasikecelakaan.utils.Constans;

public class NotificationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, HomeContract.View, ProfileContract.View {

    private GoogleMap mMap;

    private MapView mapView;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private HomePresenter homePresenter;
    private ProfilePresenter profilePresenter;

    private TextView tvName, tvAddress;
    private ProgressBar pbLoading;

    private int height = 100;
    private int width = 86;

    private Double latitude;
    private Double longitude;
    private Double latitude2;
    private Double longitude2;

    private SharedPreferences sharedpreferences;
    private String idRespondent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName = (TextView) findViewById(R.id.txt_name);
        tvAddress = (TextView) findViewById(R.id.txt_address);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        sharedpreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        idRespondent = sharedpreferences.getString(Constans.TAG_ID_RESPONDENT, "id");

        profilePresenter = new ProfilePresenter(this);
        profilePresenter.requestDataFromServer(idRespondent);

        homePresenter =  new HomePresenter(this);
        homePresenter.requestDataFromServer();

        mapView = (MapView) findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
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

    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void setDataToView(Respondent respondent) {

        latitude = respondent.getLatitude();
        longitude = respondent.getLongitude();
        Log.d("Response ", latitude.toString()+" "+longitude.toString());

        // GET CURRENT LOCATION
//        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//            if (location != null){
//                // Do it all with location
//                Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//
//                Current = CameraPosition.builder().target(latLng).zoom(13).bearing(0).tilt(0).build();
//
//                BitmapDrawable bitmapdrawcurrent = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_male);
//                Bitmap bcurrent = bitmapdrawcurrent.getBitmap();
//                Bitmap marker_current = Bitmap.createScaledBitmap(bcurrent, width, height, false);
//
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker_current));
//                mMap.addMarker(markerOptions);
//            }
//            }
//        });

    }

    @Override
    public void setDataToView(Accident accident) {

        //tvName.setText(policeOffice.getName());
        //tvAddress.setText(accident.getAddress());
        getSupportActionBar().setTitle(accident.getAddress());

        latitude2 = accident.getLatitude();
        longitude2 = accident.getLongitude();

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(this, "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}
