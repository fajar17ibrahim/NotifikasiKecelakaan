package com.ta.notifikasikecelakaan.ui.riwayat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ta.notifikasikecelakaan.R;

import java.util.ArrayList;

public class ListRiwayatAdapter extends RecyclerView.Adapter<ListRiwayatAdapter.ListViewHolder> {
    private ArrayList<Riwayat> listRiwayat;

    public ListRiwayatAdapter(ArrayList<Riwayat> list) {
        this.listRiwayat = list;
    }
    private ListRiwayatAdapter.OnItemClickCallback onItemClickCallback;

    @NonNull
    @Override
    public ListRiwayatAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_riwayat, viewGroup, false);
        return new ListRiwayatAdapter.ListViewHolder(view);
    }


    public void setOnItemClickCallback(ListRiwayatAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListRiwayatAdapter.ListViewHolder holder, int position) {
        Riwayat galery = listRiwayat.get(position);

        holder.tvKeterangan.setText(galery.getKeterangan());
        holder.tvLokasi.setText(galery.getLokasi());
        holder.tvWaktu.setText(galery.getWaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listRiwayat.get(holder.getAdapterPosition()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvKeterangan, tvLokasi, tvWaktu;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
        }
    }

    interface OnItemClickCallback {
        void onItemClicked(Riwayat riwayat);
    }
}
