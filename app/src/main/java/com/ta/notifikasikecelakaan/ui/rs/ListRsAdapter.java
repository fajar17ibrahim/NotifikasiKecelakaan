package com.ta.notifikasikecelakaan.ui.rs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;

import java.util.ArrayList;

public class ListRsAdapter extends RecyclerView.Adapter<ListRsAdapter.ListViewHolder> {
    private ArrayList<Rs> listRs;

    public ListRsAdapter(ArrayList<Rs> list) {
        this.listRs = list;
    }
    private ListRsAdapter.OnItemClickCallback onItemClickCallback;

    @NonNull
    @Override
    public ListRsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_rs, viewGroup, false);
        return new ListRsAdapter.ListViewHolder(view);
    }


    public void setOnItemClickCallback(ListRsAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListRsAdapter.ListViewHolder holder, int position) {
        Rs rs = listRs.get(position);

        holder.tvNama.setText(rs.getNama());
        holder.tvAlamat.setText(rs.getAlamat());
        holder.tvTelp.setText(rs.getTelp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listRs.get(holder.getAdapterPosition()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return listRs.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvTelp;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvTelp = itemView.findViewById(R.id.tv_telp);
        }
    }

    interface OnItemClickCallback {
        void onItemClicked(Rs rs);
    }
}
