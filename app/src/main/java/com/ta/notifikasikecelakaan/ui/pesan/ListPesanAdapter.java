package com.ta.notifikasikecelakaan.ui.pesan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;

import java.util.ArrayList;

public class ListPesanAdapter extends RecyclerView.Adapter<ListPesanAdapter.ListViewHolder> {
    private ArrayList<Pesan> listPesan;

    public ListPesanAdapter(ArrayList<Pesan> list) {
        this.listPesan = list;
    }
    private ListPesanAdapter.OnItemClickCallback onItemClickCallback;

    @NonNull
    @Override
    public ListPesanAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_pesan, viewGroup, false);
        return new ListPesanAdapter.ListViewHolder(view);
    }


    public void setOnItemClickCallback(ListPesanAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListPesanAdapter.ListViewHolder holder, int position) {
        Pesan pesan = listPesan.get(position);

        holder.tvJudul.setText(pesan.getJudul());
        holder.tvIsi.setText(pesan.getIsi());
        holder.tvWaktu.setText(pesan.getWaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listPesan.get(holder.getAdapterPosition()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPesan.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvIsi, tvWaktu;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvIsi = itemView.findViewById(R.id.tv_isi);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
        }
    }

    interface OnItemClickCallback {
        void onItemClicked(Pesan pesan);
    }
}
