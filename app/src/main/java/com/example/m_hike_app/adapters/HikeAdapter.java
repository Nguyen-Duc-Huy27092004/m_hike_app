package com.example.m_hike_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m_hike_app.R;
import com.example.m_hike_app.data.Hike;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.VH> {

    public interface OnItemClickListener { void onItemClick(Hike hike); }

    private List<Hike> list;
    private OnItemClickListener listener;

    public HikeAdapter(List<Hike> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Hike h = list.get(position);
        holder.tvName.setText(h.getName());
        holder.tvLocation.setText(h.getLocation());
        holder.tvDate.setText(h.getDate());
        holder.tvDifficulty.setText(h.getDifficulty());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(h));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvDate, tvDifficulty;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
        }
    }
}
