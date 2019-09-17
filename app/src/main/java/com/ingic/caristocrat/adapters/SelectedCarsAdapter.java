package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.DataNotFoundListener;
import com.ingic.caristocrat.models.Car;
import com.ingic.caristocrat.models.TradeCar;

import java.util.ArrayList;

/**
 * on 1/10/2019.
 */
public class SelectedCarsAdapter extends RecyclerView.Adapter<SelectedCarsAdapter.VH> {
    private final MainActivity activity;
    ArrayList<TradeCar> arrayList;
    DataNotFoundListener mlistener;


    public SelectedCarsAdapter(MainActivity activity,ArrayList<TradeCar> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_selected_car_to_compare, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        UIHelper.setImageWithGlide(activity,holder.ivCar,arrayList.get(position).getMedia().get(0).getFileUrl());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public ArrayList<TradeCar> getArrayList() {
        return arrayList;
    }

    public void addAll(ArrayList<TradeCar> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }



    public void add(TradeCar data) {
        this.arrayList.add(data);
        mlistener.isDataFound(true);
        notifyDataSetChanged();

    }
    public void remove(TradeCar data) {
        this.arrayList.remove(data);
        notifyDataSetChanged();
        if(this.arrayList.size()==0)
            mlistener.isDataFound(false);
    }
    class VH extends RecyclerView.ViewHolder {
        ImageView ivCar;


        public VH(View itemView) {
            super(itemView);
            ivCar = itemView.findViewById(R.id.ivSelectedCar);


        }
    }

    public void setMlistener(DataNotFoundListener mlistener) {
        this.mlistener = mlistener;
    }
}

