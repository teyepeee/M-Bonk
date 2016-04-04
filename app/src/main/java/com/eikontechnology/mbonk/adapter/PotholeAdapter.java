package com.eikontechnology.mbonk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.model.ListPothole;

import java.util.List;

public class PotholeAdapter extends RecyclerView.Adapter<PotholeAdapter.ViewHolder> {

    public List<ListPothole> pthItems;

    public PotholeAdapter(List<ListPothole> pthItems){
        this.pthItems = pthItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.pth_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pothole,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(pthItems.get(position).title);
    }

    @Override
    public int getItemCount() {
        return pthItems.size();
    }
}
