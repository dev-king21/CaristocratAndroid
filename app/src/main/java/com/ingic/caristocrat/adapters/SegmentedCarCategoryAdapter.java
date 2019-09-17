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
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.Car;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;

import java.util.ArrayList;

/**
 */
public class SegmentedCarCategoryAdapter extends RecyclerView.Adapter<SegmentedCarCategoryAdapter.VH> {
    private final MainActivity activity;
    ArrayList<CarBodyStyle> arrayList;

    public SegmentedCarCategoryAdapter(MainActivity activity, ArrayList<CarBodyStyle> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_segmented_car, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        UIHelper.setImageWithGlide(activity,holder.ivSegmentedCar,arrayList.get(position).getUn_selected_icon());
        holder.tvSegmentName.setText(arrayList.get(position).getName()!=null?arrayList.get(position).getName():"");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<CarBodyStyle> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivSegmentedCar;
        TextView tvSegmentName;


        public VH(View itemView) {
            super(itemView);

            ivSegmentedCar = itemView.findViewById(R.id.ivSegmentedCar);
            tvSegmentName = itemView.findViewById(R.id.tvSegmentName);


        }
    }


}

