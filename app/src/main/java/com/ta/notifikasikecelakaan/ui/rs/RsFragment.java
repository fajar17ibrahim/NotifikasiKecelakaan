package com.ta.notifikasikecelakaan.ui.rs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;

import java.util.ArrayList;

public class RsFragment extends Fragment {


    private RecyclerView rvRs;
    private ArrayList<Rs> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rs, container, false);

        rvRs = root.findViewById(R.id.rv_rs);
        rvRs.setHasFixedSize(true);

        list.addAll(RsData.getListData());
        showRecyclerList();
        return root;
    }

    private void showRecyclerList() {
        rvRs.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListRsAdapter listRsAdapter = new ListRsAdapter(list);
        rvRs.setAdapter(listRsAdapter);

        listRsAdapter.setOnItemClickCallback(new ListRsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Rs data) {
                showSelectedPesan(data);
            }
        });
    }

    private void showSelectedPesan(Rs data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getNama(), Toast.LENGTH_SHORT).show();
    }
}