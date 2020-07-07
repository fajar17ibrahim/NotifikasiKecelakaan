package com.ta.notifikasikecelakaan.ui.policeoffice;

import android.content.Intent;
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
import com.ta.notifikasikecelakaan.adapter.ListPoliceOfficeAdapter;
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.ui.policeofficelocation.PoliceOfficeLocationActivity;
import com.ta.notifikasikecelakaan.utils.Constans;

import java.util.ArrayList;
import java.util.List;

public class PoliceOfficeFragment extends Fragment implements PoliceOfficeContract.View {

    private ListPoliceOfficeAdapter listPoliceOfficeAdapter;
    private List<PoliceOffice> list;
    private PoliceOfficePresenter policeOfficePresenter;
    private RecyclerView rvPolisi;

    private ProgressBar pbLoading;
    private TextView tvEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_polisi, container, false);

        rvPolisi = root.findViewById(R.id.rv_polisi);
        rvPolisi.setHasFixedSize(true);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);
        tvEmpty = (TextView) root.findViewById(R.id.tv_empty);

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

        if (listPoliceOfficeAdapter.getItemCount() > 0 ) {
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

    private void showEmpty() {
        rvPolisi.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        rvPolisi.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }

    private void showSelectedPesan(PoliceOffice policeOffice) {
        Intent iPoliceOfficeLocation = new Intent(getActivity(), PoliceOfficeLocationActivity.class);
        Bundle data = new Bundle();
        data.putString(Constans.TAG_POLICEOFFICE_NAME, policeOffice.getName());
        data.putString(Constans.TAG_POLICEOFFICE_ADDRESS, policeOffice.getAddress());
        data.putDouble(Constans.TAG_POLICEOFFICE_LAT, policeOffice.getLatitude());
        data.putDouble(Constans.TAG_POLICEOFFICE_LONG, policeOffice.getLongitude());
        iPoliceOfficeLocation.putExtras(data);
        startActivity(iPoliceOfficeLocation);
    }
}