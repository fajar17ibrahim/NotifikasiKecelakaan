package com.ta.notifikasikecelakaan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.History;
import com.ta.notifikasikecelakaan.ui.history.HistoriesFragment;

import java.util.List;

public class ListHistoryAdapter extends RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder> {

    private HistoriesFragment historiesFragment;
    private List<History> list;

    public ListHistoryAdapter(HistoriesFragment historiesFragment, List<History> list) {
        this.historiesFragment = historiesFragment;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_riwayat, parent, false);
        return new ListHistoryAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        History history = list.get(position);

        holder.tvKeterangan.setText(history.getAddress());
        holder.tvLokasi.setText(String.valueOf(history.getLatitude())+", "+String.valueOf(history.getLongitude()));
        holder.tvWaktu.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvKeterangan, tvLokasi, tvWaktu;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
        }
    }

}
