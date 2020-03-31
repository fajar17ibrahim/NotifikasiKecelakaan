package com.ta.notifikasikecelakaan.ui.hospital;

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
import com.ta.notifikasikecelakaan.adapter.ListHospitalsAdapter;
import com.ta.notifikasikecelakaan.model.Hospital;

import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends Fragment implements HospitalContract.View {

    private ListHospitalsAdapter listHospitalsAdapter;
    private HospitalPresenter hospitalPresenter;
    private RecyclerView rvHospital;
    private ProgressBar pbLoading;
    private List<Hospital> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rs, container, false);

        rvHospital = root.findViewById(R.id.rv_rs);
        rvHospital.setHasFixedSize(true);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        showRecyclerList();

        hospitalPresenter = new HospitalPresenter(this);
        hospitalPresenter.requestDataFromServer();

        return root;
    }

    private void showRecyclerList() {
        list = new ArrayList<>();
        listHospitalsAdapter = new ListHospitalsAdapter(this, list);
        rvHospital.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHospital.setAdapter(listHospitalsAdapter);

        listHospitalsAdapter.setOnItemClickCallback(new ListHospitalsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Hospital hospital) {
                showSelectedPesan(hospital);
            }
        });
    }

    private void showSelectedPesan(Hospital data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getName(), Toast.LENGTH_SHORT).show();
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
    public void setDataToRecyclerView(List<Hospital> hospitalList) {
        list.addAll(hospitalList);
        listHospitalsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}