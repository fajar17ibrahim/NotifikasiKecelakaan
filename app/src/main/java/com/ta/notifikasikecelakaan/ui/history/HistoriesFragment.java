package com.ta.notifikasikecelakaan.ui.history;

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
import com.ta.notifikasikecelakaan.adapter.ListHistoryAdapter;
import com.ta.notifikasikecelakaan.model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoriesFragment extends Fragment implements HistoriesContract.View {

    private RecyclerView rvRiwayat;
    private List<History> list;
    private ListHistoryAdapter listHistoryAdapter;
    private HistoriesPresenter historiesPresenter;

    private ProgressBar pbLoading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        rvRiwayat = root.findViewById(R.id.rv_gallery);
        rvRiwayat.setHasFixedSize(true);

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);

        showRecyclerList();

        historiesPresenter = new HistoriesPresenter(this);
        historiesPresenter.requestDataFromServer();

        return root;
    }

    private void showRecyclerList() {
        list = new ArrayList<>();
        listHistoryAdapter = new ListHistoryAdapter(this, list);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRiwayat.setAdapter(listHistoryAdapter);
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
    public void setDataToRecyclerView(List<History> historyList) {
        list.addAll(historyList);
        listHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(getActivity(), "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }
}