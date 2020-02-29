package com.ta.notifikasikecelakaan.ui.riwayat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;

import java.util.ArrayList;

public class RiwayatFragment extends Fragment {

    private RiwayatViewModel riwayatViewModel;
    private RecyclerView rvRiwayat;
    private ArrayList<Riwayat> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        riwayatViewModel =
                ViewModelProviders.of(this).get(RiwayatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        rvRiwayat = root.findViewById(R.id.rv_gallery);
        rvRiwayat.setHasFixedSize(true);

        list.addAll(RiwayatData.getListData());
        showRecyclerList();

        return root;
    }

    private void showRecyclerList() {
        rvRiwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListRiwayatAdapter listMallAdapter = new ListRiwayatAdapter(list);
        rvRiwayat.setAdapter(listMallAdapter);

        listMallAdapter.setOnItemClickCallback(new ListRiwayatAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Riwayat data) {
                showSelectedGallery(data);
            }
        });
    }

    private void showSelectedGallery(Riwayat data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getKeterangan(), Toast.LENGTH_SHORT).show();
//        Intent iViewGambar = new Intent(getActivity(), ViewgambarActivity.class);
//        iViewGambar.putExtra("gambar", data.getGambar());
//        iViewGambar.putExtra("nama", data.getNama());
//        iViewGambar.putExtra("jam", data.getJam());
//        startActivity(iViewGambar);
    }
}