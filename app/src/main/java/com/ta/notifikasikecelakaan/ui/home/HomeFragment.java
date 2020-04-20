package com.ta.notifikasikecelakaan.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.navigation.NavigationView;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.ui.gallery.GalleryFragment;
import com.ta.notifikasikecelakaan.ui.notification.NotificationActivity;
import com.ta.notifikasikecelakaan.ui.takephoto.GambarActivity;

public class HomeFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener, HomeContract.View{

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private MapView mapView;
    private Context mContext;

    private MarkerOptions place1, place2;
    private Button btnCariRute, btnInLocation;
    private FloatingActionButton btnCurrentLocation, vGambar, phone, kamera;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private HomePresenter homePresenter;

    private int height = 100;
    private int width = 86;

    private String telp = "112";
    private ProgressBar pbLoading;
    private TextView tvAddress;

    private LatLng latLng;

    private Double latitude, longitude, latitude2, longitude2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        btnCariRute = (Button) root.findViewById(R.id.btnGetDirection);
        btnInLocation = (Button) root.findViewById(R.id.btn_in_location);

        vGambar = (FloatingActionButton) root.findViewById(R.id.btn_view_image);
        btnCurrentLocation = (FloatingActionButton) root.findViewById(R.id.btn_current_location);
        phone = (FloatingActionButton) root.findViewById(R.id.phone);
        kamera = (FloatingActionButton) root.findViewById(R.id.kamera);
        tvAddress = (TextView) root.findViewById(R.id.tv_address);

        //klik tombol lihat gambar
        vGambar.setOnClickListener(this);

        //klik tombol cari rute
        btnCariRute.setOnClickListener(this);

        //klik tombol call 112
        phone.setOnClickListener(this);

        //klik tombol kamera
        kamera.setOnClickListener(this);

        //klik tombol current location
        btnCurrentLocation.setOnClickListener(this);

        //klik tombol in location
        btnInLocation.setOnClickListener(this);

        homePresenter = new HomePresenter(this);
        homePresenter.requestDataFromServer();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
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

        mMap.addMarker(place2);

        CameraPosition AccidentLocation = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(13).bearing(0).tilt(0).build();
        mMap.moveCamera((CameraUpdateFactory.newCameraPosition(AccidentLocation)));

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
            case R.id.btn_view_image:
                try {
                    GalleryFragment galleryFragment = new GalleryFragment();

                    FragmentManager mFragmentManager = getFragmentManager();
                    FragmentTransaction mFragmentTransaction = mFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, galleryFragment, GalleryFragment.class.getSimpleName());
                    mFragmentTransaction.addToBackStack(null).commit();
                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_gallery);
                    navigationView.setTag("Galery");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnGetDirection:
                Intent iAccident = new Intent(getActivity(), NotificationActivity.class);
                startActivity(iAccident);
                break;

            case R.id.phone:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+ telp)));
                break;

            case R.id.kamera:
                startActivity(new Intent(getActivity(), GambarActivity.class));
                break;

            case R.id.btn_current_location:
                CameraPosition AccidentLocation = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(13).bearing(0).tilt(0).build();
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(AccidentLocation)));
                break;

            case R.id.btn_in_location:
                    btnInLocation.setText("Bawa ke Rumah Sakit");
                break;
        }
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
    public void setDataToView(Accident accident) {
        tvAddress.setText(accident.getAddress());

        latitude2 = accident.getLatitude();
        longitude2 = accident.getLongitude();

        BitmapDrawable bitmapdrawuser = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker);
        Bitmap buser = bitmapdrawuser.getBitmap();
        Bitmap marker_user = Bitmap.createScaledBitmap(buser, width, height, false);

        place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).icon(BitmapDescriptorFactory.fromBitmap(marker_user));
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}