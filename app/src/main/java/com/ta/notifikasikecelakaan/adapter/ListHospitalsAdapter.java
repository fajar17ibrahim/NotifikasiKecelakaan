package com.ta.notifikasikecelakaan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.ui.hospital.HospitalFragment;

import java.util.List;


public class ListHospitalsAdapter extends RecyclerView.Adapter<ListHospitalsAdapter.ListViewHolder> {

    private HospitalFragment hospitalFragment;
    private List<Hospital> list;
    private OnItemClickCallback onItemClickCallback;

    public ListHospitalsAdapter(HospitalFragment hospitalFragment, List<Hospital> list) {
        this.hospitalFragment = hospitalFragment;
        this.list = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_rs, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Hospital hospital = list.get(position);

        holder.tvName.setText(hospital.getName());
        holder.tvAddress.setText(hospital.getAddress());
        holder.tvPhone.setText(hospital.getPhone());
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

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvAddress, tvPhone;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPhone = itemView.findViewById(R.id.tv_phone);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Hospital hospital);
    }

}
