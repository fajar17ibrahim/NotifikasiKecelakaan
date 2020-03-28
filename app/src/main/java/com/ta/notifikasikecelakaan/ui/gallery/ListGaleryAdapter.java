package com.ta.notifikasikecelakaan.ui.gallery;

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

//public class ListGaleryAdapter extends RecyclerView.Adapter<ListGaleryAdapter.ListViewHolder> {
//    private ArrayList<Galery> listGalery;
//
//    public ListGaleryAdapter(ArrayList<Galery> list) {
//        this.listGalery = list;
//    }
//    private OnItemClickCallback onItemClickCallback;
//
//    @NonNull
//    @Override
//    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_galery, viewGroup, false);
//        return new ListViewHolder(view);
//    }
//
//    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
//       Galery galery = listGalery.get(position);
//
//        Glide.with(holder.itemView.getContext())
//                .load(galery.getGambar())
//                .apply(new RequestOptions().override(300, 200))
//                .into(holder.imgGambar);
//
//        holder.tvNama.setText(galery.getNama());
//        holder.tvJam.setText(galery.getJam());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickCallback.onItemClicked(listGalery.get(holder.getAdapterPosition()));
//
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return listGalery.size();
//    }
//
//    public class ListViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgGambar;
//        TextView tvNama, tvJam;
//
//        public ListViewHolder(View itemView) {
//            super(itemView);
//            imgGambar = itemView.findViewById(R.id.iv_gambar);
//            tvNama = itemView.findViewById(R.id.tv_nama);
//            tvJam = itemView.findViewById(R.id.tv_jam);
//        }
//    }
//
//    interface OnItemClickCallback {
//        void onItemClicked(Galery galery);
//    }
//
//}
