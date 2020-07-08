package com.ta.notifikasikecelakaan.ui.takephoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ta.notifikasikecelakaan.MainActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.UploadImage;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GambarActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String respondent_id;
    private String history_id;

    private Context mContext;
    private ImageView imgView;
    private Button btnAmbil;
    private Button btnUpload;
    private ProgressDialog pDialog;

    private Uri uri;
    private Bitmap thumbnail;


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

        mContext = this;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        respondent_id = sharedPreferences.getString(Constans.TAG_RESPONDENT_ID, "0");
        history_id = sharedPreferences.getString(Constans.TAG_HISTORY_ID, "0");

        imgView = (ImageView) findViewById(R.id.img_view);
        btnAmbil = findViewById(R.id.ambil_gambar);
        btnUpload = findViewById(R.id.upload_gambar);

        btnAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(GambarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(GambarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                        openGalleryIntent.setType("image/*");
                        startActivityForResult(openGalleryIntent, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Ambil Foto", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){

                uri = data.getData();
                Log.d("Uri ", uri.toString());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(uri, filePath, null, null, null);
                c.moveToFirst();
                if (c.moveToFirst()) {

                    if (Build.VERSION.SDK_INT >= 29) {
                        // now that you have the media URI, you can decode it to a bitmap
                        try (ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "r")) {
                            if (pfd != null) {
                                thumbnail = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                            }
                        } catch (IOException ex) {

                        }
                    } else {
                        // Repeat the code you already are using
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        thumbnail = (BitmapFactory.decodeFile(picturePath));
                    }
                }

                thumbnail = getResizedBitmap(thumbnail, 1000);
//                Log.w("path of image from gallery......******************.........", picturePath + "");
                imgView.setImageBitmap(thumbnail);

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pDialog = ProgressDialog.show(mContext, null, "Upload gambar...", true, false);
                    uploadFile(uri);

                }
            });

        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    void uploadFile(Uri contentURI){

        String filePath = getRealPathFromURIPath(contentURI,GambarActivity.this);
        File file = new File(filePath);
        Log.d("File",""+file.getName());

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"),file);
        RequestBody respondentId = RequestBody.create(MediaType.parse("text/plaint"), respondent_id);
        RequestBody historyId = RequestBody.create(MediaType.parse("text/plaint"), history_id);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),mFile);

        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);
        Call<UploadImage> call = apiInterface.uploadImage(body, historyId, respondentId);
        call.enqueue(new Callback<UploadImage>() {
            @Override
            public void onResponse(Call<UploadImage> call, Response<UploadImage> response) {
                pDialog.dismiss();
                Log.d("Response Upload ", response.toString());
                if (response.code() == 200) {
                    Toast.makeText(mContext, "Upload Berhasil", Toast.LENGTH_SHORT).show();
                    Intent iHome = new Intent(mContext, MainActivity.class);
                    startActivity(iHome);
                } else {
                    Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadImage> call, Throwable t) {
                pDialog.dismiss();
                Log.d("Response Upload ", t.toString());
                Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
