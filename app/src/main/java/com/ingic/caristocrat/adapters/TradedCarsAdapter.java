package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ItemTradeInBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.webhelpers.models.TradedCars;

import java.util.ArrayList;

public class TradedCarsAdapter extends RecyclerView.Adapter<TradedCarsAdapter.Holder> {
    private ItemTradeInBinding binding;
    private MainActivity activityContext;
    private ArrayList<TradedCars> arrayList;
    String screenType;

    public TradedCarsAdapter(MainActivity activityContext, String screenType) {
        this.activityContext = activityContext;
        this.arrayList = new ArrayList<>();
        this.screenType = screenType;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activityContext), R.layout.item_trade_in, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (screenType != null) {
            if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                if (arrayList.get(position).getMyCar() != null) {
                    if (arrayList.get(position).getMyCar().getMedia() != null && arrayList.get(position).getMyCar().getMedia().size() > 0) {
                        UIHelper.setImageWithGlide(activityContext, holder.binding.ivTradeIn, arrayList.get(position).getMyCar().getMedia().get(0).getFileUrl());
                    }
                    holder.binding.tvTradeIn.setText(Utils.getCarNameByBrand(arrayList.get(position).getMyCar(), false));
                    holder.binding.tvModel.setText(activityContext.getResources().getString(R.string.year) + " " + arrayList.get(position).getMyCar().getYear());
                }
            } else if (screenType.equals(AppConstants.MyTradeInScreenTypes.EVALUATION)) {
                if (arrayList.get(position).getTradeAgainstCar() != null) {
                    if (arrayList.get(position).getTradeAgainstCar().getMedia() != null && arrayList.get(position).getTradeAgainstCar().getMedia().size() > 0) {
                        UIHelper.setImageWithGlide(activityContext, holder.binding.ivTradeIn, arrayList.get(position).getTradeAgainstCar().getMedia().get(0).getFileUrl());
                    }
                    holder.binding.tvTradeIn.setText(Utils.getCarNameByBrand(arrayList.get(position).getTradeAgainstCar(), false));
                    holder.binding.tvModel.setText(activityContext.getResources().getString(R.string.year) + " " + arrayList.get(position).getTradeAgainstCar().getYear());
                }
            }
        }
//        holder.binding.tvChasis.setText(arrayList.get(position).getTradeAgainstCar().getChassis());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<TradedCars> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemTradeInBinding binding;

        Holder(ItemTradeInBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
