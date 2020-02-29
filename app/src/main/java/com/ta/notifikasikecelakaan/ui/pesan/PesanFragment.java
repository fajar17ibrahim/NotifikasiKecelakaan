package com.ta.notifikasikecelakaan.ui.pesan;

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

public class PesanFragment extends Fragment {

    private PesanViewModel pesanViewModel;
    private RecyclerView rvPesan;
    private ArrayList<Pesan> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        pesanViewModel =
//                ViewModelProviders.of(this).get(PesanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pesan, container, false);
//        final TextView textView = root.findViewById(R.id.text_tools);
//        pesanViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        rvPesan = root.findViewById(R.id.rv_pesan);
        rvPesan.setHasFixedSize(true);

        list.addAll(PesanData.getListData());
        showRecyclerList();
        return root;
    }

    private void showRecyclerList() {
        rvPesan.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListPesanAdapter listMallAdapter = new ListPesanAdapter(list);
        rvPesan.setAdapter(listMallAdapter);

        listMallAdapter.setOnItemClickCallback(new ListPesanAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Pesan data) {
                showSelectedPesan(data);
            }
        });
    }

    private void showSelectedPesan(Pesan data) {
        Toast.makeText(getActivity(), "Kamu memilih " + data.getJudul(), Toast.LENGTH_SHORT).show();
//        Intent iViewGambar = new Intent(getActivity(), ViewgambarActivity.class);
//        iViewGambar.putExtra("gambar", data.getGambar());
//        iViewGambar.putExtra("nama", data.getNama());
//        iViewGambar.putExtra("jam", data.getJam());
//        startActivity(iViewGambar);
    }
}