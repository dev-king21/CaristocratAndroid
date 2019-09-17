package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;

/**
 *  on 8/7/2018.
 */
public class MyCarsAdapter extends RecyclerView.Adapter<MyCarsAdapter.Holder> {
    MainActivity mainActivityContext;
    ArrayList<TradeCar> cars;

    public MyCarsAdapter(MainActivity mainActivityContext, ArrayList<TradeCar> cars) {
        this.mainActivityContext = mainActivityContext;
        this.cars = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.layout_my_car, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.ivCarThumbnail.setImageResource(R.drawable.image_placeholder);
        holder.tvBrand.setText("");
        holder.tvModel.setText("");
//        holder.ivCarThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (holder.getAdapterPosition() == 0) {
            holder.ivCarThumbnail.setImageResource(R.drawable.add_own_car);
            holder.ivCarThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.tvBrand.setAlpha((float) 0.2);
            holder.tvModel.setAlpha((float) 0.2);
            holder.tvBrand.setText(R.string.add_cars_you_own);
            holder.tvModel.setText(R.string.please_select);
        } else {
            holder.tvBrand.setAlpha((float) 1.0);
            holder.tvModel.setAlpha((float) 1.0);
            if (cars.get(position).getMedia() != null && cars.get(position).getMedia().size() > 0) {
                UIHelper.setImageWithGlide(mainActivityContext, holder.ivCarThumbnail, cars.get(position).getMedia().get(0).getFileUrl());
            }
            if (cars.get(position).getModel() != null && cars.get(position).getModel().getBrand() != null)
                holder.tvBrand.setText(!UIHelper.isEmptyOrNull(cars.get(position).getModel().getBrand().getName()) ? cars.get(position).getModel().getBrand().getName() : "");
            if (cars.get(position).getModel() != null)
                holder.tvModel.setText(!UIHelper.isEmptyOrNull(cars.get(position).getModel().getName()) ? cars.get(position).getModel().getName() : "");
        }
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void addAll(ArrayList<TradeCar> cars) {
        this.cars.clear();
        this.cars.add(new TradeCar());
        this.cars.addAll(cars);
        notifyDataSetChanged();
    }

    public Object getItem(int position){
        return cars.get(position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView ivCarThumbnail;
        TextView tvBrand;
        TextView tvModel;

        public Holder(View itemView) {
            super(itemView);
            ivCarThumbnail = (ImageView) itemView.findViewById(R.id.ivCarThumbnail);
            tvBrand = (TextView) itemView.findViewById(R.id.tvBrand);
            tvModel = (TextView) itemView.findViewById(R.id.tvModel);

        }

    }
}




