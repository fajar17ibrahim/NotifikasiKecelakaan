package com.ta.notifikasikecelakaan.ui.home;

import android.content.Intent;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.ui.takephoto.GambarActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.ui.gallery.GalleryFragment;
import com.ta.notifikasikecelakaan.utils.Constans;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, TaskLoadedCallback {

    private HomeViewModel homeViewModel;
    private GoogleMap mGoogleMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private MapView mapView;

    private MarkerOptions place1, place2;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 100;
    private String telp = "112";

    private LatLng latLng;

    private Polyline currentPolyline;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        Button vGambar = (Button) root.findViewById(R.id.lihat_gambar);
        Button btnCariRute = (Button) root.findViewById(R.id.cari_lokasi);
        ImageView iconMarker = (ImageView) root.findViewById(R.id.icon_marker);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        FloatingActionButton kamera = root.findViewById(R.id.kamera);

        //klik tombol lihat gambar
        vGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        iconMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        btnCariRute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Mencari Rute...", Toast.LENGTH_SHORT).show();
                new FetchURL(getActivity()).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });

        //klik tombol call 112
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+ telp)));
            }
        });

        //klik tombol kamera
        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GambarActivity.class));
            }
        });
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);

            }
        });

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

        place1 = new MarkerOptions().position(new LatLng(-7.809385, 113.387259)).title("Lokasi Ku");
        place2 = new MarkerOptions().position(new LatLng(-7.761063, 113.416305)).title("Lokasi Kecelakaan");
/*
        String url = getUrl(place1.getPosition(), place2.getPosition(), "Driving");
        new FetchURL(MainActivity.this).execute(url, "driving");*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(place2);
        CameraPosition Surabaya = CameraPosition.builder().target(new LatLng(-7.761063, 113.416305)).zoom(14).bearing(0).tilt(45).build();
        googleMap.moveCamera((CameraUpdateFactory.newCameraPosition(Surabaya)));


    }

    @Override
    public void onClick(View v) {

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
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }
}