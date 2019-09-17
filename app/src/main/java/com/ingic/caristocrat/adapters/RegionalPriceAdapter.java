package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutRegionalPriceBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.RegionalPrice;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RegionalPriceAdapter extends RecyclerView.Adapter<RegionalPriceAdapter.Holder> {
    private LayoutRegionalPriceBinding binding;
    private MainActivity activityContext;
    private ArrayList<RegionalPrice> arrayList;

    public RegionalPriceAdapter(MainActivity activityContext) {
        this.activityContext = activityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activityContext), R.layout.layout_regional_price, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (arrayList.get(position).getRegion() != null) {
            if (arrayList.get(position).getRegion().getCurrency() != null) {
                holder.binding.tvPrice.setText(arrayList.get(position).getRegion().getCurrency() + " " + NumberFormat.getNumberInstance(Locale.US).format(arrayList.get(position).getPrice()));
            } else {
                holder.binding.tvPrice.setText(activityContext.getCurrency() + " " + NumberFormat.getNumberInstance(Locale.US).format(arrayList.get(position).getPrice()));
            }
            holder.binding.tvCountryName.setText(arrayList.get(position).getRegion().getName());
            holder.binding.tvPrice.setSelected(true);
            holder.binding.tvCountryName.setSelected(true);
            UIHelper.setImageWithGlide(activityContext, holder.binding.ivFlag, arrayList.get(position).getRegion().getFlag());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addAll(ArrayList<RegionalPrice> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    class Holder extends RecyclerView.ViewHolder {
        LayoutRegionalPriceBinding binding;

        Holder(LayoutRegionalPriceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
