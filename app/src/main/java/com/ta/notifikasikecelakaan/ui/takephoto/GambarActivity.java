package com.ta.notifikasikecelakaan.ui.takephoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.UploadImage;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GambarActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private Uri image_uri;
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    Bitmap bitmap;

    ImageView imgView;
    Button btnAmbil, btnUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambar);
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

        imgView = findViewById(R.id.imageView);
        btnAmbil = findViewById(R.id.ambil_gambar);
        btnUpload = findViewById(R.id.upload_gambar);

        btnAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                }else {
                    openCamera();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                Toast.makeText(GambarActivity.this, "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called when image was captured from camera
        if(resultCode == RESULT_OK) {
            imgView.setImageURI(image_uri);
            image_uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(image_uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(GambarActivity.this, "o", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        String mediaPath = null;
        File file = new File(mediaPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiInterface getResponse = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);
        Call call = getResponse.upload(fileToUpload, filename);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                UploadImage serverResponse = (UploadImage) response.body();
                if ( serverResponse != null ) {
                    if ( serverResponse.isSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response ", serverResponse.toString());
                }
                hideDialog();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private String imagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }

    @Override
    public void onClick(View v) {

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
