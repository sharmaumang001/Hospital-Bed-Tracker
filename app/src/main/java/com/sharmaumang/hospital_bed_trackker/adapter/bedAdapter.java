package com.sharmaumang.hospital_bed_trackker.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sharmaumang.hospital_bed_trackker.R;
import com.sharmaumang.hospital_bed_trackker.model.bedModel;

import java.util.ArrayList;
import java.util.List;

public class bedAdapter extends RecyclerView.Adapter<bedAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<bedModel> details;
    ArrayList<bedModel> detailsArrayList;

    public bedAdapter(Context c , ArrayList<bedModel> d)
    {
        context = c;
        details = d;
        this.detailsArrayList=new ArrayList<>(details);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.details_row,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvHospital.setText(details.get(position).getHosName());
        holder.tvAvailable.setText(details.get(position).getAvailable());
        holder.tvTotal.setText(details.get(position).getTotal());

        boolean isExpanded = details.get(position).isExpanded();
        holder.materialCardView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<bedModel> filteredList=new ArrayList<>();
            if(charSequence.toString().isEmpty())
            {
                filteredList.addAll(detailsArrayList);
            }
            else
            {
                for(bedModel bed:detailsArrayList)
                {
                    if(bed.toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        filteredList.add(bed);
                    }
                }
            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            details.clear();
            details=(ArrayList<bedModel>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvHospital,tvAvailable,tvTotal;
        MaterialCardView layout;
        CardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHospital=itemView.findViewById(R.id.tvHospital);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            tvAvailable=itemView.findViewById(R.id.tvAvailable);
            layout=itemView.findViewById(R.id.layout);
            materialCardView=itemView.findViewById(R.id.expandableLayout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bedModel d = details.get(getAdapterPosition());
                    d.setExpanded(!d.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }


}
