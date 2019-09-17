package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.LoadMoreListener;
import com.ingic.caristocrat.interfaces.OnCarSelectedForTradeListener;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;

/**
 * on 10/4/2018.
 */
public class MyTradeInAdapter extends RecyclerView.Adapter<MyTradeInAdapter.MyViewHolder> {
    ArrayList<TradeCar> tradeIn;
    Context context;

    boolean dialogAdapter, viewTypeAddNew;
    OnCarSelectedForTradeListener onCarSelectedForTradeListener;
    LoadMoreListener loadMoreListener;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public MyTradeInAdapter(Context context, ArrayList<TradeCar> tradeIn) {
        this.context = context;
        this.tradeIn = tradeIn;
    }

    public MyTradeInAdapter(Context context, ArrayList<TradeCar> tradeIn, boolean dialogAdapter, OnCarSelectedForTradeListener onCarSelectedForTradeListener) {
        this.context = context;
        this.tradeIn = tradeIn;
        this.dialogAdapter = dialogAdapter;
        this.onCarSelectedForTradeListener = onCarSelectedForTradeListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (dialogAdapter) {
            if (viewType == 0) {
                view = LayoutInflater.from(context).inflate(R.layout.layout_add_new_car_option, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.layout_horizontal_my_car_item, parent, false);
            }
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_trade_in, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (dialogAdapter) {
            if(position == getItemCount() - 1){
                if(loadMoreListener != null){
                    loadMoreListener.onLoadMore();
                }
            }
            if (position == 0) {
                holder.llAddNewCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCarSelectedForTradeListener.onNewCarAdd();
                    }
                });
            }
            if (position > 0) {
                if (tradeIn.get(position).isNewlyAdded()) {
                    holder.llNewCar.setBackgroundColor(context.getResources().getColor(R.color.colorNewAddedCarBackground));
                }
                if (tradeIn.get(position).getMedia() != null && tradeIn.get(position).getMedia().size() > 0) {
                    UIHelper.setImageWithGlide(context, holder.ivModelPicture, tradeIn.get(position).getMedia().get(0).getFileUrl());
                }

                holder.tvCarName.setText(Utils.getCarNameByBrand(tradeIn.get(position), false));
                String chassis = context.getResources().getString(R.string.chassis) + " " + (!UIHelper.isEmptyOrNull(tradeIn.get(position).getChassis()) ? tradeIn.get(position).getChassis() : "");
                holder.tvBrandName.setText(chassis);
                holder.llNewCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCarSelectedForTradeListener.onCarSelected(tradeIn.get(position));
                    }
                });
            }
        } else {
            holder.tradeIn.setText(Utils.getCarName(tradeIn.get(position), false));
            holder.model.setText(context.getResources().getString(R.string.model) + " " + (!UIHelper.isEmptyOrNull(tradeIn.get(position).getYear() + "") ? tradeIn.get(position).getYear() + "" : ""));
            String chassis = context.getResources().getString(R.string.chassis) + " " + (!UIHelper.isEmptyOrNull(tradeIn.get(position).getChassis()) ? tradeIn.get(position).getChassis() : "");
            holder.chasis.setText(chassis);
            if (tradeIn.get(position).getMedia() != null && tradeIn.get(position).getMedia().size() > 0)
                UIHelper.setImageWithGlide(context, holder.image, tradeIn.get(position).getMedia().get(0).getFileUrl());
        }
    }

    @Override
    public int getItemCount() {
        return tradeIn.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void addAll(ArrayList<TradeCar> data) {
        tradeIn.clear();
        tradeIn.addAll(data);
        notifyDataSetChanged();
    }

    public void addAllCars(ArrayList<TradeCar> data) {
        tradeIn.clear();
        tradeIn.addAll(data);
    }

    public void add(int position, TradeCar tradeCar) {
        for (int i = 0; i < tradeIn.size(); i++) {
            this.tradeIn.get(i).setNewlyAdded(false);
        }
        this.tradeIn.add(position, tradeCar);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tradeIn;
        ImageView image;
        TextView model;
        TextView chasis;

        //For Selection List
        LinearLayout llAddNewCar, llNewCar;
        ImageView ivModelPicture;
        TextView tvCarName, tvBrandName;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (dialogAdapter) {
                llAddNewCar = itemView.findViewById(R.id.llAddNewCar);
                llNewCar = itemView.findViewById(R.id.llNewCar);
                ivModelPicture = itemView.findViewById(R.id.ivModelPicture);
                tvCarName = itemView.findViewById(R.id.tvCarName);
                tvBrandName = itemView.findViewById(R.id.tvBrandName);
            } else {
                tradeIn = (TextView) itemView.findViewById(R.id.tvTradeIn);
                model = (TextView) itemView.findViewById(R.id.tvModel);
                chasis = (TextView) itemView.findViewById(R.id.tvChasis);
                image = (ImageView) itemView.findViewById(R.id.ivTradeIn);
            }
        }
    }
}

