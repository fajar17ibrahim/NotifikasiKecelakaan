package com.ta.notifikasikecelakaan.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.adapter.ListGalleryAdapter;
import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.ui.gallery_details.GalleryDetailsActivity;
import com.ta.notifikasikecelakaan.utils.Constans;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements GalleryContract.View {

    private RecyclerView rvGallery;
    private List<Gallery> list;
    private ListGalleryAdapter listGaleryAdapter;
    private GalleryPresenter galleryPresenter;

    private ProgressBar pbLoading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        rvGallery = root.findViewById(R.id.rv_gallery);
        rvGallery.setHasFixedSize(true);

        showRecyclerList();

        galleryPresenter = new GalleryPresenter(this);
        galleryPresenter.requestDataFromServer();

        return root;
    }

    private void showRecyclerList() {
        list = new ArrayList<>();
        listGaleryAdapter = new ListGalleryAdapter(this, list);
        rvGallery.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGallery.setAdapter(listGaleryAdapter);

        listGaleryAdapter.setOnItemClickCallback(new ListGalleryAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Gallery gallery) {
                showSelectedGallery(gallery);
            }
        });
    }

    private void showSelectedGallery(Gallery data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getName(), Toast.LENGTH_SHORT).show();
        Intent iViewGambar = new Intent(getActivity(), GalleryDetailsActivity.class);
        iViewGambar.putExtra(Constans.TAG_GALLERY_ID, data.getGallery_id());
        startActivity(iViewGambar);
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
    public void setDataToRecyclerView(List<Gallery> galleryList) {
        list.addAll(galleryList);
        listGaleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}