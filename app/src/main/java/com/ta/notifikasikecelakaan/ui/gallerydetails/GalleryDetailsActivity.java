package com.ta.notifikasikecelakaan.ui.gallerydetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.utils.Constans;

public class GalleryDetailsActivity extends AppCompatActivity implements GalleryDetailsContract.View {

    private GalleryDetailsPresenter galleryDetailsPresenter;
    private ProgressBar pbLoading;

    private String galleryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewgambar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        galleryId = getIntent().getStringExtra(Constans.TAG_GALLERY_ID);

        galleryDetailsPresenter = new GalleryDetailsPresenter(this);
        galleryDetailsPresenter.requestDataFromServer(galleryId);
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
    public void setDataToView(Gallery gallery) {
        ImageView imgGambar = (ImageView) findViewById(R.id.imageView);
        Glide.with(GalleryDetailsActivity.this)
                .load(gallery.getImage())
                .apply(new RequestOptions().override(300, 200))
                .into(imgGambar);
        TextView tvKeterangan = (TextView) findViewById(R.id.tv_keterangan);
        tvKeterangan.setText("Gambar ini diambil oleh "+ gallery.getName() +" di lokasi kecelakaan tanggal "+ gallery.getCreated_at() +" WIB.");
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(this, "Data gagal dimuat." +throwable.toString(), Toast.LENGTH_LONG).show();
    }
}
