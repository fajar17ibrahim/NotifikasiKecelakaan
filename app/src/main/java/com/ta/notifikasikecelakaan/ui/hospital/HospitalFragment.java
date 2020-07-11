package com.ta.notifikasikecelakaan.ui.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.adapter.ListHospitalsAdapter;
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.ui.hostipallocation.HospitalLocationActivity;
import com.ta.notifikasikecelakaan.utils.Constans;

import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends Fragment implements HospitalContract.View {

    private SharedPreferences sharedPreferences;
    private String historyId;

    private ListHospitalsAdapter listHospitalsAdapter;
    private HospitalPresenter hospitalPresenter;
    private RecyclerView rvHospital;
    private ProgressBar pbLoading;
    private List<Hospital> list;
    private TextView tvEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rs, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        historyId = sharedPreferences.getString(Constans.TAG_HISTORY_ID, "0");

        rvHospital = root.findViewById(R.id.rv_rs);
        rvHospital.setHasFixedSize(true);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);
        tvEmpty = (TextView) root.findViewById(R.id.tv_empty);

        showRecyclerList();

        hospitalPresenter = new HospitalPresenter(this);
        hospitalPresenter.requestDataFromServer(historyId);

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

    public void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
        rvHospital.setVisibility(View.GONE);
    }

    private void hideEmpty() {
        tvEmpty.setVisibility(View.GONE);
        rvHospital.setVisibility(View.VISIBLE);
    }

    private void showSelectedPesan(Hospital hospital) {
        Intent iHospitalLocation = new Intent(getActivity(), HospitalLocationActivity.class);
        Bundle data = new Bundle();
        data.putString(Constans.TAG_HOSPITAL_ID, hospital.getHospital_id());
        data.putString(Constans.TAG_HOSPITAL_NAME, hospital.getName());
        data.putString(Constans.TAG_HOSPITAL_ADDRESS, hospital.getAddress());
        data.putDouble(Constans.TAG_RESPONDENT_LAT, hospital.getLatitude());
        data.putDouble(Constans.TAG_RESPONDENT_LONG, hospital.getLongitude());
        iHospitalLocation.putExtras(data);
        startActivity(iHospitalLocation);
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

        if (listHospitalsAdapter.getItemCount() > 0 ) {
            hideEmpty();
        } else {
            showEmpty();
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}