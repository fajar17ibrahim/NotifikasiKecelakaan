package com.ta.notifikasikecelakaan.ui.home;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
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
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.ui.gallery.GalleryFragment;
import com.ta.notifikasikecelakaan.ui.login.LoginActivity;
import com.ta.notifikasikecelakaan.ui.notification.NotificationActivity;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfileContract;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfilePresenter;
import com.ta.notifikasikecelakaan.ui.takephoto.GambarActivity;
import com.ta.notifikasikecelakaan.utils.Constans;



public class HomeFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener, ProfileContract.View {

    private SharedPreferences sharedPreferences;
    private int user_id = 0;
    private String idRespondent;

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private MapView mapView;
    private Context mContext;

    private MarkerOptions place1, place2;
    private Button btnCariRute, btnInLocation;
    private FloatingActionButton btnCurrentLocation;
    private FloatingActionButton btnCurrentLocation2;
    private FloatingActionButton vGambar;
    private FloatingActionButton phone;
    private FloatingActionButton kamera;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private HomePresenter homePresenter;
    private ProfilePresenter profilePresenter;

    private int height = 100;
    private int width = 86;

    private String telp = "112";
    private ProgressBar pbLoading;
    private TextView tvAddress, tvDistance;
    private RelativeLayout rlNotifInfo;

    private Double latitude;
    private Double longitude;
    private Double latitude2 = 0.0;
    private Double longitude2 = 0.0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        currentLocation();

        sharedPreferences = getActivity().getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        idRespondent = sharedPreferences.getString(Constans.TAG_RESPONDENT_ID, "0");
        user_id = sharedPreferences.getInt(Constans.TAG_USER_ID, 0);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        btnCariRute = (Button) root.findViewById(R.id.btnGetDirection);
        btnInLocation = (Button) root.findViewById(R.id.btn_in_location);

        vGambar = (FloatingActionButton) root.findViewById(R.id.btn_view_image);
        btnCurrentLocation = (FloatingActionButton) root.findViewById(R.id.btn_current_location);
        btnCurrentLocation2 = (FloatingActionButton) root.findViewById(R.id.btn_current_location2);
        phone = (FloatingActionButton) root.findViewById(R.id.phone);
        kamera = (FloatingActionButton) root.findViewById(R.id.kamera);
        rlNotifInfo = (RelativeLayout) root.findViewById(R.id.rl_notif_info);
        tvAddress = (TextView) root.findViewById(R.id.tv_address);
        tvDistance = (TextView) root.findViewById(R.id.tv_distance);

        profilePresenter = new ProfilePresenter(this);
        profilePresenter.requestDataFromServer(idRespondent);

        latitude = Double.parseDouble(sharedPreferences.getString(Constans.TAG_RESPONDENT_LAT, "0.0"));
        longitude = Double.parseDouble(sharedPreferences.getString(Constans.TAG_RESPONDENT_LONG, "0.0"));
        latitude2 = Double.parseDouble(sharedPreferences.getString(Constans.TAG_USER_LAT, "0.0"));
        longitude2 = Double.parseDouble(sharedPreferences.getString(Constans.TAG_USER_LONG, "0.0"));

        if(user_id != 0) {
            rlNotifInfo.setVisibility(View.VISIBLE);
            btnCariRute.setVisibility(View.VISIBLE);
            btnInLocation.setVisibility(View.VISIBLE);
            btnCurrentLocation2.hide();
            btnCurrentLocation.show();
            phone.show();
            kamera.show();
            vGambar.show();
            tvAddress.setText(latitude2 +", "+ longitude2);
        } else {
            rlNotifInfo.setVisibility(View.GONE);
            btnCariRute.setVisibility(View.GONE);
            btnInLocation.setVisibility(View.GONE);
            btnCurrentLocation2.show();
            phone.hide();
            kamera.hide();
            vGambar.hide();
            btnCurrentLocation.hide();
        }

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

        //klik tombol current location 2
        btnCurrentLocation2.setOnClickListener(this);

        //klik tombol in location
        btnInLocation.setOnClickListener(this);

        return root;
    }

    private void currentLocation() {

        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);

                    Current = CameraPosition.builder().target(latLng).zoom(16).bearing(0).tilt(0).build();
                    mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));

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
    public void setDataToView(Respondent respondent) {
        tvDistance.setText(respondent.getDistance() +" KM dari lokasi anda saat ini");

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Constans.TAG_RESPONDENT_DISTANCE, String.valueOf(respondent.getDistance()));
//        editor.putString(Constans.TAG_RESPONDENT_LAT, String.valueOf(respondent.getLatitude()));
//        editor.putString(Constans.TAG_RESPONDENT_LONG, String.valueOf(respondent.getLongitude()));
//        editor.apply();


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

        mMap.addMarker(place1);

        if (user_id != 0) {
            mMap.addMarker(place2);
            CameraPosition AccidentLocation = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(16).bearing(0).tilt(0).build();
            mMap.moveCamera((CameraUpdateFactory.newCameraPosition(AccidentLocation)));
        } else {
            currentLocation();

//            CameraPosition AccidentLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(0).build();
//            mMap.moveCamera((CameraUpdateFactory.newCameraPosition(AccidentLocation)));
        }
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
//                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
                CameraPosition Respondent = CameraPosition.builder().target(new LatLng(latitude2, longitude2)).zoom(13).bearing(0).tilt(0).build();
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Respondent)));
                break;

            case R.id.btn_current_location2:
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
//                CameraPosition Respondent1 = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(13).bearing(0).tilt(0).build();
//                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Respondent1)));
                break;

            case R.id.btn_in_location:
                    sharedPreferences.edit().remove(Constans.TAG_USER_ID).commit();
                    sharedPreferences.edit().remove(Constans.TAG_HISTORY_ID).commit();
                    Intent iLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(iLogin);
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
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }


}