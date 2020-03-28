package com.ta.notifikasikecelakaan.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private String url = Server.URL + "galeri.php";
    String tag_json_obj = "json_obj_req";
    //private ListGaleryAdapter listGaleryAdapter;

    private RecyclerView rvGallery;
    //private ArrayList<Galery> list = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        rvGallery = root.findViewById(R.id.rv_gallery);
        rvGallery.setHasFixedSize(true);

        //list.addAll(GalerysData.getListData());
//        showRecyclerList();
//        getData();

        return root;
    }

//    private void showRecyclerList() {
//        rvGallery.setLayoutManager(new LinearLayoutManager(getActivity()));
//        listGaleryAdapter = new ListGaleryAdapter(list);
//        rvGallery.setAdapter(listGaleryAdapter);
//
//        listGaleryAdapter.setOnItemClickCallback(new ListGaleryAdapter.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(Galery data) {
//                showSelectedGallery(data);
//            }
//        });
//    }
//
//    private void showSelectedGallery(Galery data) {
//        Toast.makeText(getActivity(), "Kamu memilih " + data.getNama(), Toast.LENGTH_SHORT).show();
//        Intent iViewGambar = new Intent(getActivity(), ViewgambarActivity.class);
//        iViewGambar.putExtra("gambar", data.getGambar());
//        iViewGambar.putExtra("nama", data.getNama());
//        iViewGambar.putExtra("jam", data.getJam());
//        startActivity(iViewGambar);
//    }
//
//    private void getData() {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        JsonArrayRequest strReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//
//                        Galery galery = new Galery();
//                        galery.setNama(jsonObject.getString("nama_user"));
//                        galery.setJam(jsonObject.getString("jam"));
//                        galery.setGambar(jsonObject.getString("gambar"));
//
//                        list.add(galery);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                    }
//                }
//                listGaleryAdapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley", error.toString());
//                progressDialog.dismiss();
//            }
//        });
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
//    }
}