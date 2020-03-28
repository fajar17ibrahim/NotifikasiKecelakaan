package com.ta.notifikasikecelakaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KendaraanActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
    Button btn_register, btn_login;
    String id, telp, nopol, merk, tipe, tahun, pemilik;

    EditText eNopol, eTipe, eMerk, eTahun, ePemilik;
    Intent intent;

    int success, cek;
    ConnectivityManager conMgr;

    private String url = Server.URL + "kendaraan.php";

    private static final String TAG = KendaraanActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static final String TAG_TELP = "telp_user";
    public static final String TAG_ID = "id_user";
    public static final String TAG_CEK = "cek";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        // Cek status Kendaraan jika TRUE maka langsung buka MainActivity
//        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
//        cek = sharedpreferences.getInt(TAG_CEK, 0);
//        id = sharedpreferences.getString(TAG_ID, null);
//        telp = sharedpreferences.getString(TAG_TELP, null);
//
//        if (cek==1) {
//            Intent intent = new Intent(KendaraanActivity.this, MainActivity.class);
//            intent.putExtra(TAG_ID, id);
//            intent.putExtra(TAG_TELP, telp);
//            finish();
//            startActivity(intent);
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        TextView tvNama = (TextView) findViewById(R.id.tv_nama);

        eNopol = (EditText) findViewById(R.id.txt_nopol);
        eMerk = (EditText) findViewById(R.id.txt_merk);
        eTipe = (EditText) findViewById(R.id.txt_tipe);
        eTahun = (EditText) findViewById(R.id.txt_tahun);
        ePemilik = (EditText) findViewById(R.id.txt_pemilik);

        Button btnDaftar = (Button) findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.findViewById(R.id.btn_daftar).equals(v)) {
            nopol = eNopol.getText().toString();
            merk = eMerk.getText().toString();
            tipe = eTipe.getText().toString();
            tahun = eTahun.getText().toString();
            pemilik = ePemilik.getText().toString();

            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                checkRegister(nopol, merk, tipe, tahun, pemilik, id);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkRegister(final String nopol, final String merk, final String tipe, final String tahun, final String pemilik, final String id) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Registrasi Berhasil!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Intent iMain = new Intent(KendaraanActivity.this, MainActivity.class);
                        startActivity(iMain);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(KendaraanActivity.this, "Register Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nopol", nopol);
                params.put("id_user", id);
                params.put("merk", merk);
                params.put("tipe", tipe);
                params.put("tahun", tahun);
                params.put("pemilik", pemilik);
                return params;
            }
        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
