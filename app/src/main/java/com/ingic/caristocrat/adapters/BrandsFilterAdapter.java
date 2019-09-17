package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutBrandsFilterBinding;
import com.ingic.caristocrat.models.FilterBrand;

import java.util.ArrayList;
import java.util.List;

public class BrandsFilterAdapter extends RecyclerView.Adapter<BrandsFilterAdapter.Holder> {
    private MainActivity mainActivityContext;
    private String headerTitle;
    private ArrayList<FilterBrand> arrayList;

    private List<String> mDataArray;
    private ArrayList<Integer> mSectionPositions;

    public BrandsFilterAdapter() {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public BrandsFilterAdapter(MainActivity mainActivityContext, List<String> dataset) {
        this.mainActivityContext = mainActivityContext;
        this.mDataArray = dataset;
    }




    public static class Holder extends RecyclerView.ViewHolder {
        LayoutBrandsFilterBinding binding;

        Holder(LayoutBrandsFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
