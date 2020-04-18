package com.ta.notifikasikecelakaan.ui.policeofficelocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.utils.Constans;

public class PoliceOfficeLocationActivity1 extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, PoliceOfficeLocationContract.View {

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private LatLng latLng;
    private MarkerOptions place1, place2;

    private PoliceOfficeLocationPresenter policeOfficeLocationPresenter;

    private TextView tvName, tvAddress;
    private String idPoliceOffice;

    private Double latitude1 = null;
    private Double longitude1 = null;

    private Polyline currentPolyline;

    private Button btnCariRute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_office_location);

        tvName = (TextView) findViewById(R.id.txt_name);
        tvAddress = (TextView) findViewById(R.id.txt_address);
        btnCariRute = (Button) findViewById(R.id.cari_lokasi);

        idPoliceOffice = getIntent().getStringExtra(Constans.TAG_POLICEOFFICE_ID);

        policeOfficeLocationPresenter =  new PoliceOfficeLocationPresenter(this);
        policeOfficeLocationPresenter.requestDataFromServer(idPoliceOffice);


        place1 = new MarkerOptions().position(new LatLng(-7.809385, 113.387259)).title("Lokasi Ku");
        place2 = new MarkerOptions().position(new LatLng(-7.761033, 113.416367)).title("Lokasi Kecelakaan");

        btnCariRute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PoliceOfficeLocationActivity1.this, "Mencari Rute...", Toast.LENGTH_SHORT).show();
                new FetchURL(PoliceOfficeLocationActivity1.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });


        mapView = (MapView) findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);



       // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-7.809385, 113.387259)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-7.761033, 113.416367)));
//        CameraPosition Surabaya = CameraPosition.builder().target(new LatLng(-7.761033, 113.416367)).zoom(16).bearing(0).tilt(0).build();
//        mGoogleMap.moveCamera((CameraUpdateFactory.newCameraPosition(Surabaya)));
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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.API_KEY);
        return url;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToView(PoliceOffice policeOffice) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(policeOffice.getName());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName.setText(policeOffice.getName());
        tvAddress.setText(policeOffice.getAddress());

        latitude1 = policeOffice.getLatitude();
        longitude1 = policeOffice.getLongitude();


    }

    @Override
    public void onResponseFailure(Throwable t) {
        Log.d("Error ", t.toString());
        Toast.makeText(this, "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
            currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
        }
    }
}
