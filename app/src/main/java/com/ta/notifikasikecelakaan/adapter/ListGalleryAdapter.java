package com.ta.notifikasikecelakaan.adapter;

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
import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.ui.gallery.GalleryFragment;

import java.util.List;

public class ListGalleryAdapter extends RecyclerView.Adapter<ListGalleryAdapter.ListViewHolder> {

    private GalleryFragment galleryFragment;
    private List<Gallery> list;
    private OnItemClickCallback onItemClickCallback;

    public ListGalleryAdapter(GalleryFragment galleryFragment, List<Gallery> list) {
        this.galleryFragment = galleryFragment;
        this.list = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Gallery gallery);
    }

    @NonNull
    @Override
    public ListGalleryAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_gallery, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListGalleryAdapter.ListViewHolder holder, int position) {
        Gallery gallery = list.get(position);

        holder.tvName.setText(gallery.getName());
        holder.tvTime.setText(gallery.getCreated_at());
        Glide.with(holder.itemView.getContext())
                .load(gallery.getImage())
                .apply(new RequestOptions().override(500, 300))
                .into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvTime;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }

}
