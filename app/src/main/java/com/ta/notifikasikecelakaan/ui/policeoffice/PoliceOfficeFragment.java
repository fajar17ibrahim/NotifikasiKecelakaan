package com.ta.notifikasikecelakaan.ui.policeoffice;

import android.content.Context;
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
import com.ta.notifikasikecelakaan.adapter.ListPoliceOfficeAdapter;
import com.ta.notifikasikecelakaan.model.PoliceOffice;

import java.util.ArrayList;
import java.util.List;

public class PoliceOfficeFragment extends Fragment implements PoliceOfficeContract.View {

    private ListPoliceOfficeAdapter listPoliceOfficeAdapter;
    private List<PoliceOffice> list;
    private PoliceOfficePresenter policeOfficePresenter;
    private RecyclerView rvPolisi;

    private ProgressBar pbLoading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_polisi, container, false);

        rvPolisi = root.findViewById(R.id.rv_polisi);
        rvPolisi.setHasFixedSize(true);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        showRecyclerList();

        policeOfficePresenter = new PoliceOfficePresenter(this);
        policeOfficePresenter.requestDataFromServer();
        return root;
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
    public void setDataToRecyclerView(List<PoliceOffice> policeOfficeList) {
        list.addAll(policeOfficeList);
        listPoliceOfficeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }

    private void showRecyclerList() {
        list = new ArrayList<>();
        listPoliceOfficeAdapter = new ListPoliceOfficeAdapter(this, list);
        rvPolisi.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPolisi.setAdapter(listPoliceOfficeAdapter);

        listPoliceOfficeAdapter.setOnItemClickCallback(new ListPoliceOfficeAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(PoliceOffice data) {
                showSelectedPesan(data);
            }
        });
    }

    private void showSelectedPesan(PoliceOffice data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getName(), Toast.LENGTH_SHORT).show();
    }
}