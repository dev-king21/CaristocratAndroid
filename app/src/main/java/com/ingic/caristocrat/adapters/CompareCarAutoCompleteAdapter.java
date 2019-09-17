package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.AutoCompleteItemClickListener;
import com.ingic.caristocrat.models.Car;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.TradedCars;

import java.util.ArrayList;
import java.util.List;

/**
 * on 1/10/2019.
 */

public class CompareCarAutoCompleteAdapter extends ArrayAdapter<TradeCar> {
    private ArrayList<TradeCar> dataList;
    private MainActivity mContext;
    private int itemLayout = R.layout.item_auto_complete;
    AutoCompleteItemClickListener autoCompleteItemClickListener;

    public CompareCarAutoCompleteAdapter(MainActivity context, int resource, ArrayList<TradeCar> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TradeCar getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.tvCarName);
        ImageView ivCar = (ImageView) view.findViewById(R.id.ivCar);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llMainLayout);
        strName.setText(getItem(position).getName());
        UIHelper.setImageWithGlide(mContext,ivCar,getItem(position).getMedia().get(0).getFileUrl());
        linearLayout.setOnClickListener((view1 -> {
            autoCompleteItemClickListener.onItemClick(getItem(position),position,view1);
        }));
        return view;
    }

    public void addAllCars(ArrayList<TradeCar> data){
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void setAutoCompleteItemClickListener(AutoCompleteItemClickListener autoCompleteItemClickListener) {
        this.autoCompleteItemClickListener = autoCompleteItemClickListener;
    }
}

