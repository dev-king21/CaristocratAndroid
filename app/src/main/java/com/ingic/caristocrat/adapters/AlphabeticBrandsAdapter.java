package com.ingic.caristocrat.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutBrandFilterSectionBinding;
import com.ingic.caristocrat.models.AlphabeticBrands;

import java.util.ArrayList;

public class AlphabeticBrandsAdapter extends RecyclerView.Adapter<AlphabeticBrandsAdapter.Holder>{
    private MainActivity mainActivityContext;
    private LayoutBrandFilterSectionBinding binding;
    private ArrayList<AlphabeticBrands> arrayList;
    private FilterBrandsAdapter adapter;

    public AlphabeticBrandsAdapter(MainActivity mainActivityContext){
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mainActivityContext), R.layout.layout_brand_filter_section, parent, false);

        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        /*
        if(arrayList.get(position).getBrands().size() > 0){
            binding.tvAlphabet.setText(arrayList.get(position).getAlphabet());

            adapter = new FilterBrandsAdapter(mainActivityContext, R.layout.layout_brands_filter);
            binding.gridView.setNumColumns(2);
            binding.gridView.setAdapter(adapter);
            binding.gridView.setExpanded(true);

            adapter.addAll(arrayList.get(position).getBrands());

        }
        */
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void add(AlphabeticBrands alphabeticBrands){
        this.arrayList.add(alphabeticBrands);
    }

    public void addAll(ArrayList<AlphabeticBrands> arrayList){
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        LayoutBrandFilterSectionBinding binding;

        Holder(LayoutBrandFilterSectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
