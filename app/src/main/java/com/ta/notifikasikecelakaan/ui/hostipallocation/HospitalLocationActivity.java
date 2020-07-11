package com.ta.notifikasikecelakaan.ui.hostipallocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ta.notifikasikecelakaan.MainActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.directionhelpers.FetchURL;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.login.LoginActivity;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfileContract;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfilePresenter;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalLocationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String respondentId;
    private String historyId;
    private String helperId = "0";
    private String userStatus;
    private String hospitalId;
    private GoogleMap mMap;

    private MapView mapView;
    private MarkerOptions place1, place2;
    private Button getDirection;
    private FloatingActionButton btnCurrentLocation;
    private Polyline currentPolyline;
    private CameraPosition Current;

    private ProgressDialog loading;

    private TextView tvName, tvAddress;

    private int height = 100;
    private int width = 86;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double latitude2 = 0.0;
    private Double longitude2 = 0.0;

    private Button btnUpdateStatus;
    private Button btnArriveRs;
    private Button btnNavigation;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_location);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rumah Sakit");

        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        respondentId = sharedPreferences.getString(Constans.TAG_RESPONDENT_ID, "0");
        historyId = sharedPreferences.getString(Constans.TAG_HISTORY_ID, "0");
        helperId = sharedPreferences.getString(Constans.TAG_HELPER_ID, "0");
        userStatus = sharedPreferences.getString(Constans.TAG_USER_STATUS, "0");

        tvName = (TextView) findViewById(R.id.txt_name);
        tvAddress = (TextView) findViewById(R.id.txt_address);

        btnUpdateStatus = (Button) findViewById(R.id.btn_go_rs);
        btnUpdateStatus.setOnClickListener(this);

        btnArriveRs = (Button) findViewById(R.id.btn_arrive_rs);
        btnArriveRs.setOnClickListener(this);

        btnNavigation = findViewById(R.id.btn_navigation);
        btnNavigation.setOnClickListener(this);

        if (!helperId.equals("0")) {
            if (helperId == respondentId && userStatus.equals("Dibawa ke Rumah Sakit")) {
                btnArriveRs.setVisibility(View.VISIBLE);
                btnUpdateStatus.setVisibility(View.GONE);
            }
        } else {
            btnArriveRs.setVisibility(View.GONE);
            btnUpdateStatus.setVisibility(View.VISIBLE);
        }

        btnCurrentLocation = (FloatingActionButton) findViewById(R.id.btn_current_location);
        btnCurrentLocation.setOnClickListener(this);

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

        tvName.setText(data.getString(Constans.TAG_HOSPITAL_NAME));
        hospitalId = data.getString(Constans.TAG_HOSPITAL_ID);
        tvAddress.setText(data.getString(Constans.TAG_HOSPITAL_ADDRESS));
        latitude2 = data.getDouble(Constans.TAG_RESPONDENT_LAT);
        longitude2 = data.getDouble(Constans.TAG_RESPONDENT_LONG);

        BitmapDrawable bitmapdrawuser = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker);
        Bitmap buser = bitmapdrawuser.getBitmap();
        Bitmap marker_user = Bitmap.createScaledBitmap(buser, width, height, false);

        BitmapDrawable bitmapdrawhospital = (BitmapDrawable)getResources().getDrawable(R.drawable.icon_marker_hospital);
        Bitmap bhospital = bitmapdrawhospital.getBitmap();
        Bitmap marker_hospital = Bitmap.createScaledBitmap(bhospital, width, height, false);

        place1 = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromBitmap(marker_user));
        place2 = new MarkerOptions().position(new LatLng(latitude2, longitude2)).icon(BitmapDescriptorFactory.fromBitmap(marker_hospital));

        new FetchURL(HospitalLocationActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

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

            case R.id.btn_go_rs:
                if (userStatus.equals("Dibawa ke Rumah Sakit")) {
                    Toast.makeText(mContext, "Mohon maaf, korban sedang dibawa ke Rumah Sakit", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(mContext).setTitle("Konfirmasi")
                            .setMessage("Anda akan berbagi lokasi dan nomor telepon kepada keluarga korban. Anda yakin ingin membawa korban ke Rumah Sakit ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                    updateStatus();
                                }
                            }).setNegativeButton("Tidak", null).show();
                }
                break;

            case R.id.btn_current_location:
                mMap.moveCamera((CameraUpdateFactory.newCameraPosition(Current)));
                break;

            case R.id.btn_arrive_rs:
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                updateArriveRS();
        }
    }

    private void updateArriveRS() {
        ApiInterface apiClient = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<ResponseBody> call = apiClient.requestUpdateStatusNormal(historyId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("success").equals("1")) {
                            String message = jsonRESULTS.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                            sharedPreferences.edit().remove(Constans.TAG_HISTORY_ID).commit();
                            sharedPreferences.edit().remove(Constans.TAG_USER_ID).commit();
                            sharedPreferences.edit().remove(Constans.TAG_USER_LAT).commit();
                            sharedPreferences.edit().remove(Constans.TAG_USER_LONG).commit();
                            sharedPreferences.edit().remove(Constans.TAG_USER_STATUS).commit();
                            sharedPreferences.edit().remove(Constans.TAG_HELPER_ID).commit();
                            sharedPreferences.edit().remove(Constans.TAG_HELPER_NAME).commit();
                            sharedPreferences.edit().remove(Constans.TAG_HELPER_PHONE).commit();
                            sharedPreferences.edit().remove(Constans.TAG_HELPER_LAT).commit();
                            sharedPreferences.edit().remove(Constans.TAG_HELPER_LONG).commit();

                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                        } else {
                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(mContext, error_message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Update Gagal! Coba lagi", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HospitalLocationActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void updateStatus() {

        ApiInterface apiClient = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<ResponseBody> call = apiClient.requestUpdateStatus(historyId, "Dibawa ke Rumah Sakit", respondentId, hospitalId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("success").equals("1")) {
                            String message = jsonRESULTS.getString("message");
                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        } else {
                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(mContext, error_message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Update Gagal! Coba lagi", Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HospitalLocationActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
