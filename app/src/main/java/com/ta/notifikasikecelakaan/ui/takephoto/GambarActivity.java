package com.ta.notifikasikecelakaan.ui.takephoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

    public static final String KEY_User_Document1 = "doc1";
    private String Document_img1="";

    public static final int REQUEST_IMAGE = 100;
    private Uri uri;


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
                execute(REQUEST_IMAGE);
//                selectImage();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Open Gallery"), REQUEST_GALLERY);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = ProgressDialog.show(mContext, null, "Proses Upload...", true, false);
//                uploadImage();
//                Toast.makeText(GambarActivity.this, "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();

            }
        });

    }

    void execute(int requestCode){
        switch (requestCode){
            case REQUEST_IMAGE:
//                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
                    break;
//                }else{
//                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
//                }
        }
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
//                pDialog.dismiss();
                Toast.makeText(mContext, "Upload Berhasil" + response.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UploadImage> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            uri = data.getData();
            imgView.setImageURI(uri);
            uploadFile(uri);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//        if(requestCode == REQUEST_IMAGE){
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        if(requestCode == REQUEST_IMAGE){
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
//                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                    bitmap=getResizedBitmap(bitmap, 400);
//                    imgView.setImageBitmap(bitmap);
//                    BitMapToString(bitmap);
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                thumbnail=getResizedBitmap(thumbnail, 400);
//                Log.w("path of image from gallery......******************.........", picturePath+"");
//                imgView.setImageBitmap(thumbnail);
//                BitMapToString(thumbnail);
//            }
//        }
//    }
//    public String BitMapToString(Bitmap userImage1) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
//        byte[] b = baos.toByteArray();
//        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
//        return Document_img1;
//    }
//
//    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        float bitmapRatio = (float)width / (float) height;
//        if (bitmapRatio > 1) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }
//
//    private void SendDetail() {
//        final ProgressDialog loading = new ProgressDialog(Uplode_Reg_Photo.this);
//        loading.setMessage("Please Wait...");
//        loading.show();
//        loading.setCanceledOnTouchOutside(false);
//        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiURL.Registration_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            loading.dismiss();
//                            Log.d("JSON", response);
//
//                            JSONObject eventObject = new JSONObject(response);
//                            String error_status = eventObject.getString("error");
//                            if (error_status.equals("true")) {
//                                String error_msg = eventObject.getString("msg");
//                                ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                                alertDialogBuilder.setTitle("Vendor Detail");
//                                alertDialogBuilder.setCancelable(false);
//                                alertDialogBuilder.setMessage(error_msg);
//                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                    }
//                                });
//                                alertDialogBuilder.show();
//
//                            } else {
//                                String error_msg = eventObject.getString("msg");
//                                ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                                alertDialogBuilder.setTitle("Registration");
//                                alertDialogBuilder.setCancelable(false);
//                                alertDialogBuilder.setMessage(error_msg);
////                                alertDialogBuilder.setIcon(R.drawable.doubletick);
//                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        Intent intent=new Intent(Uplode_Reg_Photo.this,Log_In.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                                alertDialogBuilder.show();
//                            }
//                        }catch(Exception e){
//                            Log.d("Tag", e.getMessage());
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        loading.dismiss();
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("No connection");
//                            alertDialogBuilder.setMessage(" Connection time out error please try again ");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                        } else if (error instanceof AuthFailureError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage(" Authentication failure connection error please try again ");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof ServerError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage("Connection error please try again");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Connection Error");
//                            alertDialogBuilder.setMessage("Network connection error please try again");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                            //TODO
//                        } else if (error instanceof ParseError) {
//                            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//                            alertDialogBuilder.setTitle("Error");
//                            alertDialogBuilder.setMessage("Parse error");
//                            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            });
//                            alertDialogBuilder.show();
//                        }
////                        Toast.makeText(Login_Activity.this,error.toString(), Toast.LENGTH_LONG ).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<String,String>();
//                map.put(KEY_User_Document1,Document_img1);
//                return map;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(mRetryPolicy);
//        requestQueue.add(stringRequest);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (Document_img1.equals("") || Document_img1.equals(null)) {
//            ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
//            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//            alertDialogBuilder.setTitle("Id Prof Can't Empty ");
//            alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
//            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                }
//            });
//            alertDialogBuilder.show();
//            return;
//        }
//        else{
//
//            if (AppStatus.getInstance(this).isOnline()) {
//                SendDetail();
//
//
//                //           Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();
//
//            } else {
//
//                Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_LONG).show();
//                Log.v("Home", "############################You are not online!!!!");
//            }
//
//        }
//    }




//    private void openCamera(){
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        //camera intent
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);


//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //called when image was captured from camera
//        if(resultCode  == RESULT_OK) {
//            if (requestCode == REQUEST_GALLERY) {
//                Uri dataImage = data.getData();
//                String[] imageProjection = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(dataImage, imageProjection, null,null,null);
//
//                if(cursor != null) {
//                    cursor.moveToFirst();
//                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
//                    part_image = cursor.getString(indexImage);
//
//                    if (part_image != null ) {
//                        File image = new File(part_image);
//                        imgView.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//                    }
//                }
//
//            }
//        }
//    }
//
//    private void uploadImage(){
//        File file = new File(part_image);
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), file);
//        MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageupload", file.getName(), requestBody);
////        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//
//        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);
//        Call<UploadImage> call = apiInterface.uploadImage(partImage);
//        call.enqueue(new Callback<UploadImage>() {
//            @Override
//            public void onResponse(Call<UploadImage> call, Response<UploadImage> response) {
//                pDialog.dismiss();
//                Toast.makeText(mContext, "Upload Berhasil", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<UploadImage> call, Throwable t) {
//
//            }
//        });
//    }

}
