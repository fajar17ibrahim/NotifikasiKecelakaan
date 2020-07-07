package com.ta.notifikasikecelakaan.ui.history;

import android.content.Context;
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
import com.ta.notifikasikecelakaan.adapter.ListHistoryAdapter;
import com.ta.notifikasikecelakaan.model.History;
import com.ta.notifikasikecelakaan.utils.Constans;

import java.util.ArrayList;
import java.util.List;

public class HistoriesFragment extends Fragment implements HistoriesContract.View {
    private SharedPreferences sharedPreferences;
    private int respondent_id = 0;

    private RecyclerView rvRiwayat;
    private List<History> list;
    private ListHistoryAdapter listHistoryAdapter;
    private HistoriesPresenter historiesPresenter;

    private ProgressBar pbLoading;
    private TextView tvEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        rvRiwayat = root.findViewById(R.id.rv_gallery);
        rvRiwayat.setHasFixedSize(true);

        sharedPreferences = getActivity().getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        respondent_id = Integer.parseInt(sharedPreferences.getString(Constans.TAG_RESPONDENT_ID, "0"));

        pbLoading = (ProgressBar) root.findViewById(R.id.pb_loading);
        tvEmpty = (TextView) root.findViewById(R.id.tv_empty);

        showRecyclerList();

        historiesPresenter = new HistoriesPresenter(this);
        historiesPresenter.requestDataFromServer(respondent_id);

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

        if (listHistoryAdapter.getItemCount() > 0 ) {
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

    private void showEmpty() {
        rvRiwayat.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmpty() {
        rvRiwayat.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }
}