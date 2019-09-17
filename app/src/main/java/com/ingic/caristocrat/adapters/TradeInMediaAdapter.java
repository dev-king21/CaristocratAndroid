package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.Media;

import java.util.ArrayList;

/**
 * on 10/8/2018.
 */
public class TradeInMediaAdapter extends RecyclerView.Adapter<TradeInMediaAdapter.MyViewHolder> {
    ArrayList<Media> items, deletedItems;
    Context context;

    public TradeInMediaAdapter(Context context, ArrayList<Media> items) {
        this.context = context;
        this.items = items;
        this.deletedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trade_media, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if(holder.getAdapterPosition()==0){
            holder.image.setImageResource(R.drawable.camerabox);
            holder.cancel.setVisibility(View.INVISIBLE);
        }
        else {
            UIHelper.setImageWithGlide(context, holder.image, items.get(position).getFileUrl());
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletedItems.add(items.get(position));
                    items.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public ArrayList<Media> getAllList() {
        if (items != null && items.size() > 0) {
            return items;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clearAll() {
        items.clear();


    }
    public void addFirstItem() {
        items.add(new Media());
        notifyDataSetChanged();


    }
    public void addAll(ArrayList<Media> data) {
        items.addAll(data);
        notifyDataSetChanged();


    }

    public ArrayList<Media> getDeletedImages(){
        return deletedItems;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView cancel;


        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.ivMedia);
            cancel = (ImageView) itemView.findViewById(R.id.ivCancel);

        }
    }
}

