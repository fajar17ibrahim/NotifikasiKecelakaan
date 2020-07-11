
package com.ta.notifikasikecelakaan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ta.notifikasikecelakaan.directionhelpers.TaskLoadedCallback;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfileContract;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.ProfilePresenter;
import com.ta.notifikasikecelakaan.utils.Constans;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ProfileContract.View {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView tvNama, tvTelp;
    private SharedPreferences sharedpreferences;

    private ProfilePresenter profilePresenter;

    private NavigationView navigationView;
    private View hView;

    private String idRespondent;
    private GoogleMap mMap;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("sendMessage");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        idRespondent = sharedpreferences.getString(Constans.TAG_RESPONDENT_ID, "0");

        profilePresenter = new ProfilePresenter(this);
        profilePresenter.requestDataFromServer(idRespondent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery,
                R.id.nav_riwayat, R.id.nav_pesan, R.id.nav_share, R.id.nav_setting, R.id.nav_polisi, R.id.nav_rs)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToView(Respondent respondent) {
        hView =  navigationView.getHeaderView(0);
        tvNama = (TextView) hView.findViewById(R.id.tv_name);
        tvTelp = (TextView) hView.findViewById(R.id.tv_telp);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constans.TAG_RESPONDENT_DISTANCE, String.valueOf(respondent.getDistance()));
        editor.putString(Constans.TAG_RESPONDENT_PHONE, respondent.getPhone());
        editor.putString(Constans.TAG_RESPONDENT_LAT, String.valueOf(respondent.getLatitude()));
        editor.putString(Constans.TAG_RESPONDENT_LONG, String.valueOf(respondent.getLongitude()));
        editor.apply();

        tvNama.setText(respondent.getName());
        tvTelp.setText(respondent.getPhone());
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(this, "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Konfirmasi")
                .setMessage("Anda hanya akan menerima notifikasi pada notification bar dan tidak bisa melihat informasi mengenai lokasi korban. Anda yakin ingin menutup aplikasi ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                }).setNegativeButton("Tidak", null).show();
    }
}