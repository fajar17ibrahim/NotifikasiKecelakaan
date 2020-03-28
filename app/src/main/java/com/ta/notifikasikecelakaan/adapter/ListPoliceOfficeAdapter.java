package com.ta.notifikasikecelakaan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.ui.policeoffice.PoliceOfficeFragment;

import java.util.List;

public class ListPoliceOfficeAdapter extends RecyclerView.Adapter<ListPoliceOfficeAdapter.ListViewHolder> {

    private PoliceOfficeFragment policeOfficeFragment;
    private List<PoliceOffice> list;
    private OnItemClickCallback onItemClickCallback;

    public ListPoliceOfficeAdapter(PoliceOfficeFragment policeOfficeFragment, List<PoliceOffice> list) {
        this.policeOfficeFragment = policeOfficeFragment;
        this.list = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_polisi, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        PoliceOffice policeOffice = list.get(position);

        holder.tvName.setText(policeOffice.getName());
        holder.tvAddress.setText(policeOffice.getAddress());
        holder.tvPhone.setText(policeOffice.getPhone());
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
        TextView tvName, tvAddress, tvPhone;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPhone = itemView.findViewById(R.id.tv_phone);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(PoliceOffice policeOffice);
    }
}
