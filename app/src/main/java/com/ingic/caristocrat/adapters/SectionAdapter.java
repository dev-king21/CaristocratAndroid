package com.ingic.caristocrat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.FilterBrand;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class SectionAdapter extends StatelessSection implements SectionIndexer {
    private final MainActivity mainActivity;
    private final char title;
    private final ArrayList<FilterBrand> brandList;
    private ArrayList<Integer> mSectionPositions;

    public SectionAdapter(MainActivity mainActivity, char title, ArrayList<FilterBrand> brandList) {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_brands_filter) //Section Item
                .headerResourceId(R.layout.layout_brand_filter_section)
                .build());
        this.mainActivity = mainActivity;
        this.title = title;
        this.brandList = brandList;
    }

    @Override
    public int getContentItemsTotal() {
        return brandList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.tvAlphabet.setText(title + "");
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        if (brandList.get(position).getMedia().size() > 0) {
            if (brandList.get(position).getMedia().get(0) != null && brandList.get(position).getMedia().get(0).getFileUrl() != null) {
                UIHelper.setImageWithGlide(mainActivity, itemHolder.imgItem, brandList.get(position).getMedia().get(0).getFileUrl());
            }
        }

        for(int pos = 0; pos < LuxuryMarketSearchFilter.getInstance().getBrandsList().size(); pos++){
            if(LuxuryMarketSearchFilter.getInstance().getBrandsList().get(pos).getId() == brandList.get(position).getId()){
                itemHolder.llBrandsLogo.setBackground(mainActivity.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands_selected));
                break;
            }
        }

        itemHolder.llBrandsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                int foundPos = -1;
                for(int pos = 0; pos < LuxuryMarketSearchFilter.getInstance().getBrandsList().size(); pos++){
                    if(LuxuryMarketSearchFilter.getInstance().getBrandsList().get(pos).getId() == brandList.get(position).getId()){
                        found = true;
                        foundPos = pos;
                        break;
                    }
                }
                if(found){
                    LuxuryMarketSearchFilter.getInstance().removeBrand(foundPos);
                    itemHolder.llBrandsLogo.setBackground(mainActivity.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands));
                }else{
                    LuxuryMarketSearchFilter.getInstance().addBrand(brandList.get(position));
                    itemHolder.llBrandsLogo.setBackground(mainActivity.getResources().getDrawable(R.drawable.bordered_item_rounded_filtered_brands_selected));

                }
            }
        });
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = brandList.size(); i < size; i++) {
            String section = String.valueOf(brandList.get(i).getName().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    public void clear() {
        this.brandList.clear();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAlphabet;

        HeaderViewHolder(View view) {
            super(view);

            tvAlphabet = view.findViewById(R.id.tvAlphabet);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        LinearLayout llBrandsLogo;
        ImageView imgItem;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = view.findViewById(R.id.ivBrandLogo);
            llBrandsLogo = view.findViewById(R.id.llBrandsLogo);
        }
    }
}

