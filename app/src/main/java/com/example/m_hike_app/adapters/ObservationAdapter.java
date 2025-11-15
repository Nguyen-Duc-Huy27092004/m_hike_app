package com.example.m_hike_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m_hike_app.R;
import com.example.m_hike_app.data.Observation;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.VH> {

    public interface OnItemClickListener { void onItemClick(Observation observation); }

    private List<Observation> list;
    private OnItemClickListener listener;

    public ObservationAdapter(List<Observation> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Observation o = list.get(position);
        holder.tvObservation.setText(o.getObservation());
        holder.tvTime.setText(o.getTime());
        holder.tvComment.setText(o.getComment());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(o));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvObservation, tvTime, tvComment;
        VH(@NonNull View itemView) {
            super(itemView);
            tvObservation = itemView.findViewById(R.id.tvObservation);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }
}
